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
 * 表单字段定义实体。
 */
@Data
@TableName("subsidy_form_field")
public class SubsidyFormFieldDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 ID。 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 模板 ID。 */
    private Long templateId;

    /** 字段编码。 */
    private String fieldCode;

    /** 字段名称。 */
    private String fieldName;

    /** 字段类型。 */
    private String fieldType;

    /** 是否必填：1 是 0 否。 */
    private Integer isRequired;

    /** 是否允许用户编辑：1 是 0 否。 */
    private Integer isUserEditable;

    /** 排序号。 */
    private Integer sortNo;

    /** 字段分组名称（如：基本信息、车辆信息、银行卡信息）。 */
    private String groupName;

    /** 分组排序号（用于确定步骤顺序）。 */
    private Integer groupSort;

    /** 枚举选项（JSON）。 */
    private String enumOptions;

    /** 校验规则（JSON）。 */
    private String validationRule;

    /** 是否启用 OCR：1 是 0 否。 */
    private Integer ocrEnabled;

    /** OCR 映射键。 */
    private String ocrMappingKey;

    /** 状态：1 启用 0 停用。 */
    private Integer status;

    /** 创建时间。 */
    private LocalDateTime createdAt;

    /** 更新时间。 */
    private LocalDateTime updatedAt;
}
