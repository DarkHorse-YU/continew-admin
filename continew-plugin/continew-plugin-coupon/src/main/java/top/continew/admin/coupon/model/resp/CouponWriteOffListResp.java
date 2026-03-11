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
 * 核销列表响应
 */
@Data
@Schema(description = "核销列表响应")
public class CouponWriteOffListResp {

    @Schema(description = "核销ID")
    private Long id;

    @Schema(description = "券码")
    private String couponNo;

    @Schema(description = "活动ID")
    private Long activityId;

    @Schema(description = "活动名称")
    private String activityName;

    @Schema(description = "券模板ID")
    private Long templateId;

    @Schema(description = "券名称")
    private String templateName;

    @Schema(description = "券类型")
    private String couponType;

    @Schema(description = "折扣率")
    private BigDecimal discountRate;

    @Schema(description = "减免金额")
    private BigDecimal discountAmount;

    @Schema(description = "领券用户ID")
    private Long userId;

    @Schema(description = "领券用户名")
    private String userName;

    @Schema(description = "核销员ID")
    private Long verifierUserId;

    @Schema(description = "核销员名称")
    private String verifierName;

    @Schema(description = "核销方式")
    private String writeOffMode;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "状态描述")
    private String statusDesc;

    @Schema(description = "核销时间")
    private LocalDateTime writeOffTime;

    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "当前提交版本号")
    private Integer currentSubmissionNo;

    @Schema(description = "提交次数")
    private Integer submissionCount;
}
