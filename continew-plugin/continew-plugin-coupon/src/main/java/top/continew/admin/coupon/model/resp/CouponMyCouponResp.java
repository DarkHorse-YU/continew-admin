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

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 我的优惠券响应参数
 */
@Data
public class CouponMyCouponResp {

    /** 用户券 ID */
    private Long id;
    /** 活动 ID */
    private Long activityId;
    /** 活动名称 */
    private String activityName;
    /** 模板 ID */
    private Long templateId;
    /** 模板名称 */
    private String templateName;
    /** 券码 */
    private String couponNo;
    /** 二维码令牌 */
    private String qrToken;

    /** 券类型 */
    private String couponType;
    /** 折扣比例 */
    private BigDecimal discountRate;
    /** 优惠金额 */
    private BigDecimal discountAmount;
    /** 使用门槛金额 */
    private BigDecimal thresholdAmount;

    /** 状态 */
    private String status;
    /** 状态描述 */
    private String statusDesc;

    /** 领券时间 */
    private LocalDateTime claimTime;
    /** 生效开始时间 */
    private LocalDateTime validStartTime;
    /** 生效结束时间 */
    private LocalDateTime validEndTime;
    /** 核销记录 ID */
    private Long writeOffId;
    /** 核销时间 */
    private LocalDateTime writeOffTime;
}
