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

package top.continew.admin.coupon.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 券模板实体
 */
@Data
@TableName("coupon_template")
public class CouponTemplateDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 活动ID */
    private Long activityId;

    /** 券模板编码 */
    private String templateCode;

    /** 券名称 */
    private String templateName;

    /** 券描述 */
    private String description;

    /** 券类型：DISCOUNT=折扣券， CASH=满减券 */
    private String couponType;

    /** 折扣率（如0.90表示九折） */
    private BigDecimal discountRate;

    /** 减免金额 */
    private BigDecimal discountAmount;

    /** 门槛金额（满X元可用) */
    private BigDecimal thresholdAmount;

    /** 总库存 */
    private Integer totalStock;

    /** 已抢数量 */
    private Integer claimedStock;

    /** 已核销数量 */
    private Integer writeOffStock;

    /** 单用户限领数量 */
    private Integer perUserLimit;

    /** 每日限领数量（NULL不限） */
    private Integer dailyClaimLimit;

    /** 有效期类型：RELATIVE=领取后N天， FIXED=固定时间段 */
    private String validType;

    /** 有效天数（RELATIVE时使用) */
    private Integer validDays;

    /** 固定生效时间（FIXED时使用) */
    private LocalDateTime fixedValidStartTime;

    /** 固定失效时间 (FIXED时使用) */
    private LocalDateTime fixedValidEndTime;

    /** 核销凭证模板ID（NULL表示不需要凭证) */
    private Long formTemplateId;

    /** 状态：ENABLED=启用，DISABLED=禁用 */
    private String status;

    /** 排序 */
    private Integer sortNo;

    /** 乐观锁版本 */
    private Integer version;

    /** 规则JSON */
    private String ruleJson;

    /** 是否删除：0=否,1=是 */
    private Integer isDeleted;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
