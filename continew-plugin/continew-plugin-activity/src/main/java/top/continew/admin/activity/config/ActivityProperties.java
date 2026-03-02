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

package top.continew.admin.activity.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 补贴活动配置项。
 */
@Data
@Component
@ConfigurationProperties(prefix = "activity.subsidy")
public class ActivityProperties {

    /** 上传文件默认父目录。 */
    private String uploadParentPath = "/activity/subsidy/";

    /** 文件上传使用的存储配置编码。 */
    private String storageCode = "dev-minio";

    /** OCR 配置。 */
    private Ocr ocr = new Ocr();

    /** 图片安全检测配置。 */
    private Safety safety = new Safety();

    @Data
    public static class Ocr {

        /** 是否开启 OCR。 */
        private boolean enabled;

        /** 百度 OCR API Key。 */
        private String apiKey;

        /** 百度 OCR Secret Key。 */
        private String secretKey;

        /** key: ocr_mapping_key，value: OCR 接口地址。 */
        private Map<String, String> endpoints = new HashMap<>();
    }

    @Data
    public static class Safety {

        /** 是否开启图片安全检测。 */
        private boolean enabled;

        /** 腾讯云 SecretId。 */
        private String secretId;

        /** 腾讯云 SecretKey。 */
        private String secretKey;

        /** 地域，例如 ap-guangzhou。 */
        private String region;

        /** 接口域名，默认 ims.tencentcloudapi.com。 */
        private String endpoint;

        /** 业务类型（BizType），例如 TencentCloudDefault。 */
        private String bizType;

        /** 接口超时（毫秒）。 */
        private Integer timeoutMs = 3000;
    }
}
