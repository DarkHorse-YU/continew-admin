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

package top.continew.admin.activity.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户提交申报请求。
 *
 * @author OpenCode
 * @since 2026/2/28 15:00
 */
@Data
@Schema(description = "用户提交申报请求")
public class SubsidySubmitReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "活动 ID 不能为空")
    @Schema(description = "活动 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long activityId;

    @NotEmpty(message = "字段值列表不能为空")
    @Valid
    @Schema(description = "字段值列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<FieldValueReq> fieldValues = new ArrayList<>();

    @Data
    @Schema(description = "字段值")
    public static class FieldValueReq implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        @NotNull(message = "字段编码不能为空")
        @Schema(description = "字段编码", requiredMode = Schema.RequiredMode.REQUIRED)
        private String fieldCode;

        @Schema(description = "字段值")
        private String value;

        @Schema(description = "上传文件 ID")
        private Long fileId;

        @Schema(description = "同字段多值序号", example = "1")
        private Integer valueSeq = 1;

        @Schema(description = "是否为 OCR 自动填充（0 否 1 是）", example = "0")
        private Integer ocrAutofill = 0;

        @Schema(description = "OCR 识别结果（上传时返回的 ocrResult）")
        private Map<String, String> ocrResult;
    }
}
