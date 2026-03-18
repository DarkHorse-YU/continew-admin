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

/**
 * 核销准备信息响应参数
 */
@Data
public class CouponWriteOffPrepareResp {

    /** 是否可核销 */
    private Boolean canWriteOff;
    /** 不可核销原因 */
    private String cannotReason;

    /** 用户券 ID */
    private Long claimId;
    /** 券码 */
    private String couponNo;

    /** 活动 ID */
    private Long activityId;
    /** 活动名称 */
    private String activityName;
    /** 模板 ID */
    private Long templateId;
    /** 模板名称 */
    private String templateName;

    /** 券类型 */
    private String couponType;
    /** 折扣比例 */
    private BigDecimal discountRate;
    /** 优惠金额 */
    private BigDecimal discountAmount;
    /** 使用门槛金额 */
    private BigDecimal thresholdAmount;
}
