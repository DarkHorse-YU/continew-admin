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

package top.continew.admin.coupon.model.resp;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 券表单响应参数
 */
@Data
public class CouponFormResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 活动 ID */
    private Long activityId;
    /** 活动编码 */
    private String activityCode;
    /** 活动名称 */
    private String activityName;
    /** 审核模式 */
    private String auditMode;
    /** 模板 ID */
    private Long templateId;
    /** 模板编码 */
    private String templateCode;
    /** 模板名称 */
    private String templateName;
    /** 模板版本号 */
    private Integer templateVersion;
    /** 表单模板 ID */
    private Long formTemplateId;

    /** 分组列表 */
    private List<GroupResp> groups = new ArrayList<>();

    /**
     * 字段分组信息
     */
    @Data
    public static class GroupResp implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /** 分组名称 */
        private String groupName;
        /** 分组排序 */
        private Integer groupSort;
        /** 字段列表 */
        private List<FieldResp> fields = new ArrayList<>();
    }

    /**
     * 字段信息
     */
    @Data
    public static class FieldResp implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /** 字段 ID */
        private Long fieldId;
        /** 字段编码 */
        private String fieldCode;
        /** 字段名称 */
        private String fieldName;
        /** 字段类型 */
        private String fieldType;
        /** 是否必填 */
        private Integer isRequired;
        /** 是否可编辑 */
        private Integer isEditable;
        /** 排序号 */
        private Integer sortNo;
        /** 枚举选项 */
        private String enumOptions;
        /** 校验规则 */
        private String validationRule;
        /** 是否启用 OCR */
        private Integer ocrEnabled;
        /** OCR 映射键 */
        private String ocrMappingKey;
    }
}
