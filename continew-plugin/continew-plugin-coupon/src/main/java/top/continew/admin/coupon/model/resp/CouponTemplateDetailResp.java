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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 券模板详情响应
 */
@Data
@Schema(description = "券模板详情响应")
public class CouponTemplateDetailResp {

    @Schema(description = "模板ID")
    private Long id;

    @Schema(description = "券名称")
    private String templateName;

    @Schema(description = "券描述")
    private String description;

    @Schema(description = "券类型：DISCOUNT=折扣券，CASH=满减券")
    private String couponType;

    @Schema(description = "折扣率")
    private BigDecimal discountRate;

    @Schema(description = "减免金额")
    private BigDecimal discountAmount;

    @Schema(description = "门槛金额")
    private BigDecimal thresholdAmount;

    @Schema(description = "有效期类型：RELATIVE=领取后N天，FIXED=固定时间段")
    private String validType;

    @Schema(description = "有效天数")
    private Integer validDays;

    @Schema(description = "是否需要核销凭证")
    private Boolean requireForm;

    @Schema(description = "核销凭证模板ID（不需要凭证时为null）")
    private Long formTemplateId;

    @Schema(description = "表单模板信息（需要凭证时返回）")
    private FormTemplateResp formTemplate;

    @Data
    @Schema(description = "表单模板响应")
    public static class FormTemplateResp {

        @Schema(description = "模板ID")
        private Long templateId;

        @Schema(description = "模板名称")
        private String templateName;

        @Schema(description = "字段分组列表")
        private List<GroupResp> groups;

        @Data
        @Schema(description = "字段分组")
        public static class GroupResp {

            @Schema(description = "分组名")
            private String groupName;

            @Schema(description = "分组排序")
            private Integer groupSort;

            @Schema(description = "字段列表")
            private List<FieldResp> fields;
        }

        @Data
        @Schema(description = "字段响应")
        public static class FieldResp {

            @Schema(description = "字段ID")
            private Long fieldId;

            @Schema(description = "字段编码")
            private String fieldCode;

            @Schema(description = "字段名称")
            private String fieldName;

            @Schema(description = "字段类型")
            private String fieldType;

            @Schema(description = "是否必填")
            private Integer isRequired;

            @Schema(description = "是否可编辑")
            private Integer isEditable;

            @Schema(description = "排序")
            private Integer sortNo;

            @Schema(description = "枚举选项")
            private String enumOptions;

            @Schema(description = "校验规则")
            private String validationRule;

            @Schema(description = "是否OCR")
            private Integer ocrEnabled;

            @Schema(description = "OCR映射Key")
            private String ocrMappingKey;
        }
    }
}
