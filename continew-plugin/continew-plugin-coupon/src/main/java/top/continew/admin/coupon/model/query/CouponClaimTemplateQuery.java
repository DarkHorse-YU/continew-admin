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

    private Long templateId;

    private Long activityId;

    private String templateCode;

    private String templateName;

    private String description;

    private String couponType;

    private BigDecimal discountRate;

    private BigDecimal discountAmount;

    private BigDecimal thresholdAmount;

    private Integer totalStock;

    private Integer claimedStock;

    private Integer writeOffStock;

    private Integer perUserLimit;

    private Integer dailyClaimLimit;

    private String validType;

    private Integer validDays;

    private LocalDateTime fixedValidStartTime;

    private LocalDateTime fixedValidEndTime;

    private Long formTemplateId;

    private String templateStatus;

    private Integer sortNo;

    private Integer version;

    private String templateRuleJson;

    private LocalDateTime claimStartTime;

    private LocalDateTime claimEndTime;

    private LocalDateTime verifyStartTime;

    private LocalDateTime verifyEndTime;

    private String auditMode;

    private String activityStatus;

    private String activityRuleJson;
}
