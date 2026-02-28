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
 * OCR 识别留痕实体。
 */
@Data
@TableName("subsidy_ocr_result")
public class SubsidyOcrResultDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 ID。 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 提交版本 ID。 */
    private Long submissionId;

    /** 字段 ID。 */
    private Long fieldId;

    /** 文件 ID。 */
    private Long fileId;

    /** OCR 引擎名称。 */
    private String ocrEngine;

    /** OCR 原文。 */
    private String rawText;

    /** 结构化识别值。 */
    private String parsedValue;

    /** 识别置信度。 */
    private BigDecimal confidence;

    /** 是否采用：1 是 0 否。 */
    private Integer isAdopted;

    /** 创建时间。 */
    private LocalDateTime createdAt;
}
