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

package top.continew.admin.activity.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户申报主单实体。
 */
@Data
@TableName("subsidy_application")
public class SubsidyApplicationDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 ID。 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 客户 ID（冗余自活动）。 */
    private Long customerId;

    /** 申请单号（业务唯一）。 */
    private String applicationNo;

    /** 活动 ID。 */
    private Long activityId;

    /** 申请用户 ID。 */
    private Long userId;

    /** 当前状态：DRAFT/PENDING/REJECTED/APPROVED。 */
    private String currentStatus;

    /** 当前提交版本 ID。 */
    private Long currentSubmissionId;

    /** 驳回次数。 */
    private Integer rejectCount;

    /** 最终补贴金额。 */
    private BigDecimal finalSubsidyAmount;

    /** 最终补贴金额确定时间。 */
    private LocalDateTime finalSubsidyDecidedAt;

    /** 审核通过时间。 */
    private LocalDateTime approvedAt;

    /** 乐观锁版本号。 */
    private Integer version;

    /** 创建时间。 */
    private LocalDateTime createdAt;

    /** 更新时间。 */
    private LocalDateTime updatedAt;
}
