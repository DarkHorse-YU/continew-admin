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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动列表响应
 */
@Data
@Schema(description = "活动列表响应")
public class CouponActivityListResp {

    @Schema(description = "活动ID")
    private Long id;

    @Schema(description = "活动编码")
    private String activityCode;

    @Schema(description = "活动名称")
    private String activityName;

    @Schema(description = "活动描述")
    private String description;

    @Schema(description = "抢券开始时间")
    private LocalDateTime claimStartTime;

    @Schema(description = "抢券结束时间")
    private LocalDateTime claimEndTime;

    @Schema(description = "核销开始时间")
    private LocalDateTime verifyStartTime;

    @Schema(description = "核销结束时间")
    private LocalDateTime verifyEndTime;

    @Schema(description = "状态：ENABLED=启用，DISABLED=禁用")
    private String status;

    @Schema(description = "活动状态描述（进行中/未开始/已结束）")
    private String statusDesc;
}
