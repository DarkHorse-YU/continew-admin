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

package top.continew.admin.activity.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 补贴活动配置项。
 *
 * @author OpenCode
 * @since 2026/2/28 15:00
 */
@Data
@Component
@ConfigurationProperties(prefix = "activity.subsidy")
public class ActivityProperties {

    /** 文件上传父目录。 */
    private String uploadParentPath = "/activity/subsidy/";

    /** OCR 配置。 */
    private Ocr ocr = new Ocr();

    /** 图片安全检测配置。 */
    private Safety safety = new Safety();

    @Data
    public static class Ocr {

        private boolean enabled;

        private String apiKey;

        private String secretKey;

        /** key: ocr_mapping_key，value: 百度 OCR 接口地址。 */
        private Map<String, String> endpoints = new HashMap<>();
    }

    @Data
    public static class Safety {

        private boolean enabled;

        /** 腾讯天御检测接口地址。 */
        private String endpoint;

        private Integer timeoutMs = 3000;
    }
}
