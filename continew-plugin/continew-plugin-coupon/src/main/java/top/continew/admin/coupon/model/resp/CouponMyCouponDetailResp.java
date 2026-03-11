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

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 我的券详情响应
 */
@Data
@Schema(description = "我的券详情响应")
public class CouponMyCouponDetailResp {

    @Schema(description = "券实例ID")
    private Long id;

    @Schema(description = "券码")
    private String couponNo;

    @Schema(description = "二维码Token")
    private String qrToken;

    @Schema(description = "活动名称")
    private String activityName;

    @Schema(description = "券名称")
    private String templateName;

    @Schema(description = "券描述")
    private String description;

    @Schema(description = "券类型")
    private String couponType;

    @Schema(description = "折扣率")
    private BigDecimal discountRate;

    @Schema(description = "减免金额")
    private BigDecimal discountAmount;

    @Schema(description = "门槛金额")
    private BigDecimal thresholdAmount;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "状态描述")
    private String statusDesc;

    @Schema(description = "生效时间")
    private LocalDateTime validStartTime;

    @Schema(description = "失效时间")
    private LocalDateTime validEndTime;

    @Schema(description = "领券时间")
    private LocalDateTime claimTime;

    @Schema(description = "核销时间")
    private LocalDateTime writeOffTime;

    @Schema(description = "核销记录ID")
    private Long writeOffId;

    @Schema(description = "核销详情（已核销时返回）")
    private WriteOffResp writeOffDetail;

    @Data
    @Schema(description = "核销详情")
    public static class WriteOffResp {

        @Schema(description = "核销状态")
        private String status;

        @Schema(description = "核销员ID")
        private Long verifierUserId;

        @Schema(description = "核销时间")
        private LocalDateTime writeOffTime;

        @Schema(description = "审核意见")
        private String auditComment;
    }
}
