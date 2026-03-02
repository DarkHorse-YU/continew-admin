/*
 * Copyright (c) 2022-present Charles7c Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.continew.admin.activity.service.safety.impl;

import cn.hutool.core.util.StrUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ims.v20201229.ImsClient;
import com.tencentcloudapi.ims.v20201229.models.ImageModerationRequest;
import com.tencentcloudapi.ims.v20201229.models.ImageModerationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.continew.admin.activity.config.ActivityProperties;
import top.continew.admin.activity.service.safety.SubsidyImageContentCheckService;
import top.continew.starter.core.exception.BusinessException;

import java.util.UUID;

/**
 * 腾讯云图片安全检测实现。
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
        if (StrUtil.isBlank(imageUrl)) {
            throw new BusinessException("图片地址不能为空");
        }
        if (StrUtil.hasBlank(safety.getSecretId(), safety.getSecretKey(), safety.getRegion())) {
            throw new BusinessException("未配置腾讯云图片安全检测参数，请检查 activity.subsidy.safety.secret-id/secret-key/region");
        }

        try {
            // 1. 实例化认证对象
            Credential credential = new Credential(safety.getSecretId(), safety.getSecretKey());

            // 2. 配置请求域名和超时
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(StrUtil.blankToDefault(safety.getEndpoint(), "ims.tencentcloudapi.com"));
            int timeoutMs = Math.max(1000, safety.getTimeoutMs());
            httpProfile.setConnTimeout(timeoutMs);

            // 3. 组装客户端
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            ImsClient client = new ImsClient(credential, safety.getRegion(), clientProfile);

            // 4. 组装请求参数（对齐官方示例）
            ImageModerationRequest request = new ImageModerationRequest();
            request.setBizType(StrUtil.blankToDefault(safety.getBizType(), "default"));
            request.setDataId(UUID.randomUUID().toString());
            request.setFileUrl(imageUrl);

            // 5. 发起检测
            ImageModerationResponse response = client.ImageModeration(request);
            String suggestion = response.getSuggestion();
            if ("Pass".equalsIgnoreCase(suggestion)) {
                return;
            }

            log.warn("图片未通过腾讯云内容安全检测: suggestion={}, bizType={}, requestId={}, imageUrl={}", suggestion, request
                    .getBizType(), response.getRequestId(), imageUrl);
            if ("Review".equalsIgnoreCase(suggestion)) {
                throw new BusinessException("图片需人工复审，暂不允许提交");
            }
            throw new BusinessException("图片未通过内容安全检测");
        } catch (TencentCloudSDKException e) {
            log.error("腾讯云图片安全检测失败: code={}, requestId={}, message={}", e.getErrorCode(), e.getRequestId(), e.getMessage(), e);
            throw new BusinessException("图片安全检测失败: " + e.getMessage());
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("图片安全检测失败: {}", e.getMessage(), e);
            throw new BusinessException("图片安全检测失败");
        }
    }
}
