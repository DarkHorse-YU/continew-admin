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

package top.continew.admin.coupon.service.safety.impl;

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
import top.continew.admin.coupon.config.CouponProperties;
import top.continew.admin.coupon.service.safety.CouponImageContentCheckService;
import top.continew.starter.core.exception.BusinessException;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TencentCouponImageContentCheckServiceImpl implements CouponImageContentCheckService {

    private final CouponProperties properties;

    @Override
    public void checkImage(String imageUrl) {
        CouponProperties.Safety safety = properties.getSafety();
        if (!safety.isEnabled()) {
            return;
        }
        if (StrUtil.isBlank(imageUrl)) {
            throw new BusinessException("图片地址不能为空");
        }
        if (StrUtil.hasBlank(safety.getSecretId(), safety.getSecretKey(), safety.getRegion())) {
            throw new BusinessException("未配置腾讯云图片安全检测参数，请检查 coupon.writeoff.safety.secret-id/secret-key/region");
        }

        try {
            Credential credential = new Credential(safety.getSecretId(), safety.getSecretKey());
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(StrUtil.blankToDefault(safety.getEndpoint(), "ims.tencentcloudapi.com"));
            httpProfile.setConnTimeout(Math.max(1000, safety.getTimeoutMs()));

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            ImsClient client = new ImsClient(credential, safety.getRegion(), clientProfile);

            ImageModerationRequest request = new ImageModerationRequest();
            request.setBizType(StrUtil.blankToDefault(safety.getBizType(), "default"));
            request.setDataId(UUID.randomUUID().toString());
            request.setFileUrl(imageUrl);

            ImageModerationResponse response = client.ImageModeration(request);
            String suggestion = response.getSuggestion();
            if ("Pass".equalsIgnoreCase(suggestion)) {
                return;
            }

            log.warn("Image moderation rejected: suggestion={}, bizType={}, requestId={}, imageUrl={}",
                suggestion, request.getBizType(), response.getRequestId(), imageUrl);
            if ("Review".equalsIgnoreCase(suggestion)) {
                throw new BusinessException("图片需要人工复审，暂不允许提交");
            }
            throw new BusinessException("图片未通过内容安全检测");
        } catch (TencentCloudSDKException e) {
            log.error("Tencent image moderation failed: code={}, requestId={}, message={}",
                e.getErrorCode(), e.getRequestId(), e.getMessage(), e);
            throw new BusinessException("图片安全检测失败: " + e.getMessage());
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Image moderation failed: {}", e.getMessage(), e);
            throw new BusinessException("图片安全检测失败");
        }
    }
}
