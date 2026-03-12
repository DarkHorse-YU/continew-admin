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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 核销提交字段值实体
 */
@Data
@TableName("coupon_write_off_submission_value")
public class CouponWriteOffSubmissionValueDO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 提交版本ID */
    private Long submissionId;

    /** 字段ID */
    private Long fieldId;

    /** 值序号（支持多值） */
    private Integer valueSeq;

    /** 文本值 */
    private String valueText;

    /** 数字值 */
    private BigDecimal valueNumber;

    /** 日期值 */
    private LocalDate valueDate;

    /** 枚举值 */
    private String valueEnum;

    /** JSON值 */
    private String valueJson;

    /** 关联文件ID */
    private Long fileId;

    /** 是否OCR自动填充： 0=否, 1=是 */
    private Integer ocrAutofill;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
