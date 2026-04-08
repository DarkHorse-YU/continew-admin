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
 * 抢券活动实体
 */
@Data
@TableName("coupon_activity")
public class CouponActivityDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 活动编码 */
    private String activityCode;

    /** 活动名称 */
    private String activityName;

    /** 活动封面图片 URL */
    private String cover;

    /** 活动描述 */
    private String description;

    /** 活动总预算 */
    private BigDecimal totalBudget;

    /** 抢券开始时间 */
    private LocalDateTime claimStartTime;

    /** 抢券结束时间 */
    private LocalDateTime claimEndTime;

    /** 核销开始时间 */
    private LocalDateTime verifyStartTime;

    /** 核销结束时间 */
    private LocalDateTime verifyEndTime;

    /** 审核模式：NONE=免审，MANUAL=人工审核 */
    private String auditMode;

    /** 状态：ENABLED=启用，DISABLED=禁用 */
    private String status;

    /** 排序 */
    private Integer sortNo;

    /** 活动规则JSON */
    private String ruleJson;

    /** 是否删除：0=否，1=是 */
    private Integer isDeleted;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
