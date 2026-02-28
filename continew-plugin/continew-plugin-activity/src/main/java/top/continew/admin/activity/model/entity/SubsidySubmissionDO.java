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
import java.time.LocalDateTime;

/**
 * 补贴提交版本实体。
 */
@Data
@TableName("subsidy_submission")
public class SubsidySubmissionDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 ID。 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 客户 ID（冗余字段）。 */
    private Long customerId;

    /** 申请主单 ID。 */
    private Long applicationId;

    /** 提交序号（1,2,3...）。 */
    private Integer submissionNo;

    /** 状态：SUBMITTED/UNDER_REVIEW/REJECTED/APPROVED。 */
    private String status;

    /** 提交时间。 */
    private LocalDateTime submittedAt;

    /** 审核完成时间。 */
    private LocalDateTime reviewedAt;

    /** 审核人用户 ID。 */
    private Long reviewerId;

    /** 审核备注。 */
    private String reviewComment;

    /** 乐观锁版本号。 */
    private Integer version;

    /** 创建时间。 */
    private LocalDateTime createdAt;
}
