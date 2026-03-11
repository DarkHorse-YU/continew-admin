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
 * 核销凭证表单字段
 */
@Data
@TableName("coupon_form_field")
public class CouponFormFieldDO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 模板ID */
    private Long templateId;

    /** 字段编码 */
    private String fieldCode;

    /** 字段名称 */
    private String fieldName;

    /** 字段类型：text=文本, number=数字, date=日期, enum=枚举, image=图片, file=文件, json=JSON */
    private String fieldType;

    /** 是否必填： 0=否, 1=是 */
    private Integer isRequired;

    /** 是否可编辑: 0=否, 1=是 */
    private Integer isEditable;

    /** 分组名 */
    private String groupName;

    /** 分组排序 */
    private Integer groupSort;

    /** 字段排序 */
    private Integer sortNo;

    /** 枚举选项 */
    private String enumOptions;

    /** 校验规则 */
    private String validationRule;

    /** 是否OCR: 0=否, 1=是 */
    private Integer ocrEnabled;

    /** OCR映射Key */
    private String ocrMappingKey;

    /** 状态: ENABLED=启用, DISABLED=禁用 */
    private String status;

    /** 是否删除:0=否, 1=是 */
    private Integer isDeleted;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
