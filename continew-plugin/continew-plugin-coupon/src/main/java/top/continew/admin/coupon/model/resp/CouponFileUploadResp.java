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

package top.continew.admin.coupon.model.resp;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 文件上传响应参数
 */
@Data
@Builder
public class CouponFileUploadResp {

    /** 文件 ID */
    private Long fileId;
    /** 文件访问地址 */
    private String url;
    /** 缩略图地址 */
    private String thUrl;
    /** 存储文件名 */
    private String fileName;
    /** 原始文件名 */
    private String originalName;
    /** 文件大小 */
    private Long size;
    /** OCR 识别结果 */
    private Map<String, String> ocrResult;
}
