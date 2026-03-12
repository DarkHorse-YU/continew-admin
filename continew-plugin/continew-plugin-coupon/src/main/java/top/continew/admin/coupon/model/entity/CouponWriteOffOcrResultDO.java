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
import java.time.LocalDateTime;

/**
 * 核销OCR留痕实体
 */
@Data
@TableName("coupon_write_off_ocr_result")
public class CouponWriteOffOcrResultDO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 提交版本ID */
    private Long submissionId;

    /** 字段ID */
    private Long fieldId;

    /** 文件ID */
    private Long fileId;

    /** OCR原文 */
    private String rawText;

    /** OCR解析结果JSON */
    private String parsedJson;

    /** 是否采用：0=否, 1=是 */
    private Integer isAdopted;

    /** 创建时间 */
    private LocalDateTime createdAt;
}
