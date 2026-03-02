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

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.continew.admin.activity.config.ActivityProperties;
import top.continew.admin.activity.service.safety.SubsidyImageContentCheckService;
import top.continew.starter.core.exception.BusinessException;

import java.util.Map;

/**
 * 腾讯天御图片安全检测实现。
 *
 * @author OpenCode
 * @since 2026/2/28 15:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TencentSubsidyImageContentCheckServiceImpl implements SubsidyImageContentCheckService {

    private final ActivityProperties properties;

    @Override
    public void checkImage(String imageUrl) {
        ActivityProperties.Safety safety = properties.getSafety();
        if (!safety.isEnabled()) {
            return;
        }
        if (StrUtil.isBlank(safety.getEndpoint())) {
            throw new BusinessException("未配置图片安全检测地址，请检查 activity.subsidy.safety.endpoint");
        }
        try {
            String body = JSONUtil.toJsonStr(Map.of("imageUrl", imageUrl));
            String response = HttpRequest.post(safety.getEndpoint())
                .body(body)
                .timeout(safety.getTimeoutMs())
                .execute()
                .body();
            if (!JSONUtil.isTypeJSON(response)) {
                throw new BusinessException("图片安全检测返回异常");
            }
            boolean pass = JSONUtil.parseObj(response).getBool("pass", false);
            if (!pass) {
                throw new BusinessException("图片未通过内容安全检测");
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("图片安全检测失败: {}", e.getMessage(), e);
            throw new BusinessException("图片安全检测失败");
        }
    }
}
