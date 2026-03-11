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

package top.continew.admin.coupon.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 核销请求
 */
@Data
@Schema(description = "核销请求")
public class CouponWriteOffReq {

    @Schema(description = "券码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "券码不能为空")
    private String couponNo;

    @Schema(description = "核销方式：QR_SCAN=扫码，CODE_INPUT=输码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "核销方式不能为空")
    private String writeOffMode;

    @Schema(description = "幂等请求号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "请求号不能为空")
    private String requestNo;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "表单字段值列表（需要凭证时必填）")
    private List<FieldValueReq> fieldValues;

    @Data
    @Schema(description = "表单字段值")
    public static class FieldValueReq {

        @Schema(description = "字段编码", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "字段编码不能为空")
        private String fieldCode;

        @Schema(description = "值序号（多值场景）")
        private Integer valueSeq;

        @Schema(description = "字段值")
        private String value;

        @Schema(description = "关联文件ID")
        private Long fileId;

        @Schema(description = "是否OCR自动填充：0=否，1=是")
        private Integer ocrAutofill;
    }
}
