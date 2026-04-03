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

package top.continew.admin.coupon.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 优惠券核销配置项
 */
@Data
@Component
@ConfigurationProperties(prefix = "coupon.writeoff")
public class CouponProperties {

    /** 上传文件默认父目录 */
    private String uploadParentPath = "/coupon/writeoff/";

    /** 文件上传使用的存储配置编码 */
    private String storageCode = "dev-minio";

    /** OCR 配置 */
    private Ocr ocr = new Ocr();

    /** 图片安全检查配置 */
    private Safety safety = new Safety();

    @Data
    public static class Ocr {

        private boolean enabled;
        private String apiKey;
        private String secretKey;
        private Map<String, Endpoint> endpoints = new HashMap<>();
    }

    @Data
    public static class Endpoint {

        private String url;
        private Map<String, String> params = new HashMap<>();
    }

    @Data
    public static class Safety {

        private boolean enabled;
        private String secretId;
        private String secretKey;
        private String region;
        private String endpoint;
        private String bizType;
        private Integer timeoutMs = 3000;
    }
}
