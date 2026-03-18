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

package top.continew.admin.coupon.model.query;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 抢券模板与活动聚合查询结果
 */
@Data
public class CouponClaimTemplateQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 模板 ID */
    private Long templateId;
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
    /** 已核销库存 */
    private Integer writeOffStock;
    /** 每人限领数量 */
    private Integer perUserLimit;
    /** 每日限领数量 */
    private Integer dailyClaimLimit;
    /** 有效期类型 */
    private String validType;
    /** 有效天数 */
    private Integer validDays;
    /** 固定有效期开始时间 */
    private LocalDateTime fixedValidStartTime;
    /** 固定有效期结束时间 */
    private LocalDateTime fixedValidEndTime;
    /** 表单模板 ID */
    private Long formTemplateId;
    /** 模板状态 */
    private String templateStatus;
    /** 排序号 */
    private Integer sortNo;
    /** 版本号 */
    private Integer version;
    /** 模板规则 JSON */
    private String templateRuleJson;
    /** 抢券开始时间 */
    private LocalDateTime claimStartTime;
    /** 抢券结束时间 */
    private LocalDateTime claimEndTime;
    /** 核销开始时间 */
    private LocalDateTime verifyStartTime;
    /** 核销结束时间 */
    private LocalDateTime verifyEndTime;
    /** 审核模式 */
    private String auditMode;
    /** 活动状态 */
    private String activityStatus;
    /** 活动规则 JSON */
    private String activityRuleJson;
}
