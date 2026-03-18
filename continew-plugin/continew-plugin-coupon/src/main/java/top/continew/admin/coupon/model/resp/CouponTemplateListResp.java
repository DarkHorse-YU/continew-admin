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
 * 券模板列表响应参数
 */
@Data
public class CouponTemplateListResp {

    /** 模板 ID */
    private Long id;
    /** 活动 ID */
    private Long activityId;
    /** 模板编码 */
    private String templateCode;
    /** 模板名称 */
    private String templateName;
    /** 模板描述 */
    private String description;

    /** 券类型 */
    private String couponType;
    /** 折扣比例 */
    private BigDecimal discountRate;
    /** 优惠金额 */
    private BigDecimal discountAmount;
    /** 使用门槛金额 */
    private BigDecimal thresholdAmount;

    /** 总库存 */
    private Integer totalStock;
    /** 已领取库存 */
    private Integer claimedStock;
    /** 剩余库存 */
    private Integer remainingStock;

    /** 每人限领数量 */
    private Integer perUserLimit;
    /** 用户已领取数量 */
    private Integer userClaimedCount;
    /** 表单模板 ID */
    private Long formTemplateId;

    /** 是否可领取 */
    private Boolean canClaim;
    /** 不可领取原因 */
    private String cannotClaimReason;

    /** 创建时间 */
    private LocalDateTime createdAt;
}
