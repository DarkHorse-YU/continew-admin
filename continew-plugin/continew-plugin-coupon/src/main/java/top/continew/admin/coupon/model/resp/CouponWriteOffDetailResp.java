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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 核销详情响应参数
 */
@Data
public class CouponWriteOffDetailResp {

    /** 核销记录 ID */
    private Long id;
    /** 用户券 ID */
    private Long claimId;
    /** 券码 */
    private String couponNo;

    /** 活动 ID */
    private Long activityId;
    /** 活动名称 */
    private String activityName;
    /** 模板 ID */
    private Long templateId;
    /** 模板名称 */
    private String templateName;

    /** 券类型 */
    private String couponType;
    /** 折扣比例 */
    private BigDecimal discountRate;
    /** 优惠金额 */
    private BigDecimal discountAmount;
    /** 使用门槛金额 */
    private BigDecimal thresholdAmount;
    /** 表单模板 ID */
    private Long formTemplateId;

    /** 核销方式 */
    private String writeOffMode;

    /** 用户券核销状态 */
    private String userWriteOffStatus;
    /** 用户券核销状态描述 */
    private String userWriteOffStatusDesc;

    /** 审核状态 */
    private String auditStatus;
    /** 审核状态描述 */
    private String auditStatusDesc;

    /** 当前状态 */
    private String status;
    /** 当前状态描述 */
    private String statusDesc;
    /** 备注 */
    private String remark;

    /** 核销时间 */
    private LocalDateTime writeOffTime;
    /** 当前提交版本 ID */
    private Long currentSubmissionId;

    /** 审核人 ID */
    private Long auditReviewerId;
    /** 审核时间 */
    private LocalDateTime auditTime;
    /** 审核意见 */
    private String auditComment;

    /** 模板字段分组 */
    private List<FieldGroupResp> templateGroups;
    /** 当前提交版本 */
    private SubmissionResp currentSubmission;

    /**
     * 提交版本信息
     */
    @Data
    public static class SubmissionResp {

        /** 提交版本 ID */
        private Long submissionId;
        /** 提交版本号 */
        private Integer submissionNo;
        /** 提交状态 */
        private String status;
        /** 提交人 ID */
        private Long submittedBy;
        /** 提交时间 */
        private LocalDateTime submittedAt;
        /** 审核人 ID */
        private Long reviewerId;
        /** 审核时间 */
        private LocalDateTime reviewedAt;
        /** 审核意见 */
        private String reviewComment;
        /** 字段分组 */
        private List<FieldGroupResp> groups;
        /** 审核问题列表 */
        private List<ReviewIssueResp> issues;
    }

    /**
     * 字段分组信息
     */
    @Data
    public static class FieldGroupResp {
        /** 分组名称 */
        private String groupName;
        /** 分组排序 */
        private Integer groupSort;
        /** 字段列表 */
        private List<FieldValueResp> fields;
    }

    /**
     * 字段值信息
     */
    @Data
    public static class FieldValueResp {
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
        /** 同字段多值序号 */
        private Integer valueSeq;
        /** 字段值 */
        private String value;
        /** 文件 ID */
        private Long fileId;
        /** 文件地址 */
        private String fileUrl;
    }

    /**
     * 审核问题信息
     */
    @Data
    public static class ReviewIssueResp {
        /** 问题 ID */
        private Long id;
        /** 字段 ID */
        private Long fieldId;
        /** 字段编码 */
        private String fieldCode;
        /** 字段名称 */
        private String fieldName;
        /** 问题编码 */
        private String issueCode;
        /** 问题说明 */
        private String issueMessage;
        /** 问题状态 */
        private String status;
        /** 修复所在提交版本 ID */
        private Long fixedInSubmissionId;
        /** 创建时间 */
        private LocalDateTime createdAt;
    }
}
