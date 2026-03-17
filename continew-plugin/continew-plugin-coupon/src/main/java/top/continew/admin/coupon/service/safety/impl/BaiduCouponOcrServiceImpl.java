/*
 * Copyright (c) 2022-present Charles7c Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.continew.admin.coupon.service.safety.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.continew.admin.coupon.config.CouponProperties;
import top.continew.admin.coupon.service.safety.CouponOcrService;
import top.continew.starter.core.exception.BusinessException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaiduCouponOcrServiceImpl implements CouponOcrService {

    private static final String TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";

    private final CouponProperties properties;

    @Override
    public Map<String, String> recognize(MultipartFile file, String mappingKey) {
        CouponProperties.Ocr ocr = properties.getOcr();
        if (!ocr.isEnabled() || StrUtil.isBlank(mappingKey)) {
            return Collections.emptyMap();
        }
        CouponProperties.Endpoint endpointConfig = ocr.getEndpoints().get(mappingKey);
        if (endpointConfig == null || StrUtil.isBlank(endpointConfig.getUrl())) {
            return Collections.emptyMap();
        }
        String accessToken = this.getAccessToken(ocr);
        try {
            String imageBase64 = Base64.encode(file.getBytes());
            String imageParam = URLEncoder.encode(imageBase64, StandardCharsets.UTF_8);

            StringBuilder paramBuilder = new StringBuilder("image=").append(imageParam);
            Map<String, String> extraParams = endpointConfig.getParams();
            if (extraParams != null) {
                extraParams.forEach((key, value) -> paramBuilder.append("&").append(key).append("=").append(value));
            }

            String response;
            try (HttpResponse httpResponse = HttpRequest.post(endpointConfig.getUrl() + "?access_token=" + accessToken)
                .contentType(ContentType.FORM_URLENCODED.toString())
                .body(paramBuilder.toString())
                .timeout(5000)
                .execute()) {
                response = httpResponse.body();
            }

            if (!JSONUtil.isTypeJSON(response)) {
                log.warn("OCR returned non-JSON response: {}", response);
                return Collections.emptyMap();
            }

            JSONObject responseObj = JSONUtil.parseObj(response);
            String errorCode = responseObj.getStr("error_code");
            if (StrUtil.isNotBlank(errorCode)) {
                log.warn("OCR returned error: error_code={}, error_msg={}", errorCode, responseObj.getStr("error_msg"));
                return Collections.emptyMap();
            }

            Object wordsResult = responseObj.get("words_result");
            if (wordsResult == null) {
                log.warn("OCR response missing words_result: {}", response);
                return Collections.emptyMap();
            }

            Map<String, String> result = new LinkedHashMap<>();
            if (wordsResult instanceof JSONArray jsonArray) {
                String text = jsonArray.stream()
                    .map(item -> JSONUtil.parseObj(item).getStr("words"))
                    .filter(StrUtil::isNotBlank)
                    .collect(Collectors.joining("\n"));
                if (StrUtil.isNotBlank(text)) {
                    result.put("text", text);
                }
            } else if (wordsResult instanceof JSONObject wordsObj) {
                wordsObj.forEach((key, value) -> {
                    if (value instanceof JSONObject valueObj) {
                        String words = valueObj.getStr("words");
                        if (StrUtil.isNotBlank(words)) {
                            result.put(key, words);
                        }
                    }
                });
            }
            return result;
        } catch (IOException e) {
            throw new BusinessException("OCR 识别失败: " + e.getMessage());
        } catch (Exception e) {
            log.warn("OCR invocation failed: {}", e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    private String getAccessToken(CouponProperties.Ocr ocr) {
        String response;
        try (HttpResponse httpResponse = HttpRequest.get(TOKEN_URL)
            .form("grant_type", "client_credentials")
            .form("client_id", ocr.getApiKey())
            .form("client_secret", ocr.getSecretKey())
            .timeout(5000)
            .execute()) {
            response = httpResponse.body();
        }
        if (!JSONUtil.isTypeJSON(response)) {
            throw new BusinessException("获取 OCR token 失败");
        }
        String token = JSONUtil.parseObj(response).getStr("access_token");
        if (StrUtil.isBlank(token)) {
            throw new BusinessException("获取 OCR token 失败");
        }
        return token;
    }
}
