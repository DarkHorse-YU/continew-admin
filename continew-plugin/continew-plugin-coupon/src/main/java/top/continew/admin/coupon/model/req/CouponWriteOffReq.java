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
import lombok.Data;

/**
 * 核销请求参数
 */
@Data
@Schema(description = "核销请求")
public class CouponWriteOffReq {

    /** 券码 */
    @NotBlank(message = "couponNo can not be blank")
    private String couponNo;

    /** 核销方式 */
    @NotBlank(message = "writeOffMode can not be blank")
    private String writeOffMode;

    /** 幂等请求号 */
    @NotBlank(message = "requestNo can not be blank")
    private String requestNo;

    /** 备注 */
    private String remark;
}
