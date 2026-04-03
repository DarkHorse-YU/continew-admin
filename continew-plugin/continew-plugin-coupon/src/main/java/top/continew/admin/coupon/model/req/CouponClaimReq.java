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

/**
 * 用户抢券请求参数
 */
@Data
@Schema(description = "用户抢券请求")
public class CouponClaimReq {

    /** 券模板 ID */
    @NotNull(message = "券模板 ID 不能为空")
    @Schema(description = "券模板 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long templateId;

    //    /** 用户 ID */
    //    @Schema(description = "用户 ID（仅压测未登录场景使用，正常业务可不传）")
    //    private Long userId;

    @Schema(description = "滑块验证 token", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "滑块验证 token 不能为空")
    private String captchaToken;
}
