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

package top.continew.admin.activity.service.safety.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.continew.admin.activity.config.ActivityProperties;
import top.continew.admin.activity.service.safety.SubsidyOcrService;
import top.continew.starter.core.exception.BusinessException;

import java.io.IOException;
import java.util.Map;

/**
 * 百度 OCR 实现。
 *
 * @author OpenCode
 * @since 2026/2/28 15:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BaiduSubsidyOcrServiceImpl implements SubsidyOcrService {

    private static final String TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";
    private final ActivityProperties properties;

    @Override
    public String recognize(MultipartFile file, String mappingKey) {
        ActivityProperties.Ocr ocr = properties.getOcr();
        if (!ocr.isEnabled() || StrUtil.isBlank(mappingKey)) {
            return null;
        }
        String endpoint = ocr.getEndpoints().get(mappingKey);
        if (StrUtil.isBlank(endpoint)) {
            return null;
        }
        String accessToken = this.getAccessToken(ocr);
        try {
            String imageBase64 = Base64.encode(file.getBytes());
            String body = HttpUtil.toParams(Map.of("image", imageBase64));
            String response = HttpRequest.post(endpoint + "?access_token=" + accessToken)
                .contentType(ContentType.FORM_URLENCODED.toString())
                .body(body)
                .timeout(5000)
                .execute()
                .body();
            if (!JSONUtil.isTypeJSON(response)) {
                return null;
            }
            return JSONUtil.parseObj(response)
                .getJSONArray("words_result")
                .stream()
                .map(item -> JSONUtil.parseObj(item).getStr("words"))
                .filter(StrUtil::isNotBlank)
                .reduce((a, b) -> a + "\n" + b)
                .orElse(null);
        } catch (IOException e) {
            throw new BusinessException("OCR 识别失败");
        } catch (Exception e) {
            log.warn("OCR 调用失败: {}", e.getMessage());
            return null;
        }
    }

    private String getAccessToken(ActivityProperties.Ocr ocr) {
        String response = HttpRequest.get(TOKEN_URL)
            .form("grant_type", "client_credentials")
            .form("client_id", ocr.getApiKey())
            .form("client_secret", ocr.getSecretKey())
            .timeout(5000)
            .execute()
            .body();
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
