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
 * 字段级审核问题实体。
 */
@Data
@TableName("subsidy_review_issue")
public class SubsidyReviewIssueDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 ID。 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 被驳回提交版本 ID。 */
    private Long submissionId;

    /** 问题字段 ID。 */
    private Long fieldId;

    /** 问题编码。 */
    private String issueCode;

    /** 问题描述。 */
    private String issueMessage;

    /** 状态：OPEN/FIXED。 */
    private String status;

    /** 修复对应的提交版本 ID。 */
    private Long fixedInSubmissionId;

    /** 创建时间。 */
    private LocalDateTime createdAt;

    /** 更新时间。 */
    private LocalDateTime updatedAt;
}
