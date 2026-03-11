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
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 抢券请求
 */
@Data
@Schema(description = "抢券请求")
public class CouponClaimReq {

    @Schema(description = "券模板ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "券模板ID不能为空")
    private Long templateId;

    @Schema(description = "滑块验证token", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "验证token不能为空")
    private String captchaToken;
}
