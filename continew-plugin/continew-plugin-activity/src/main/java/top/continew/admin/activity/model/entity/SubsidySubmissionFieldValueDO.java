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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 提交字段值快照实体。
 */
@Data
@TableName("subsidy_submission_field_value")
public class SubsidySubmissionFieldValueDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 ID。 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 提交版本 ID。 */
    private Long submissionId;

    /** 字段 ID。 */
    private Long fieldId;

    /** 多值序号。 */
    private Integer valueSeq;

    /** 文本值。 */
    private String valueText;

    /** 数值。 */
    private BigDecimal valueNumber;

    /** 日期值。 */
    private LocalDate valueDate;

    /** 枚举值。 */
    private String valueEnum;

    /** JSON 值。 */
    private String valueJson;

    /** 图片/附件文件 ID。 */
    private Long fileId;

    /** 是否 OCR 自动填充：1 是 0 否。 */
    private Integer ocrAutofill;

    /** 创建时间。 */
    private LocalDateTime createdAt;

    /** 更新时间。 */
    private LocalDateTime updatedAt;
}
