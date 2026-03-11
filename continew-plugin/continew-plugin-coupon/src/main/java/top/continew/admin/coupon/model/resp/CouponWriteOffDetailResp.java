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
import java.time.LocalDateTime;
import java.util.List;

/**
 * 核销详情响应
 */
@Data
@Schema(description = "核销详情响应")
public class CouponWriteOffDetailResp {

    @Schema(description = "核销ID")
    private Long id;

    @Schema(description = "券码")
    private String couponNo;

    @Schema(description = "活动ID")
    private Long activityId;

    @Schema(description = "活动名称")
    private String activityName;

    @Schema(description = "券模板ID")
    private Long templateId;

    @Schema(description = "券名称")
    private String templateName;

    @Schema(description = "券类型")
    private String couponType;

    @Schema(description = "折扣率")
    private BigDecimal discountRate;

    @Schema(description = "减免金额")
    private BigDecimal discountAmount;

    @Schema(description = "门槛金额")
    private BigDecimal thresholdAmount;

    @Schema(description = "领券用户ID")
    private Long userId;

    @Schema(description = "领券用户名")
    private String userName;

    @Schema(description = "核销员ID")
    private Long verifierUserId;

    @Schema(description = "核销员名称")
    private String verifierName;

    @Schema(description = "核销方式")
    private String writeOffMode;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "状态描述")
    private String statusDesc;

    @Schema(description = "核销时间")
    private LocalDateTime writeOffTime;

    @Schema(description = "审核人ID")
    private Long auditReviewerId;

    @Schema(description = "审核人名称")
    private String auditReviewerName;

    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "审核意见")
    private String auditComment;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "当前提交版本详情")
    private SubmissionResp currentSubmission;

    @Data
    @Schema(description = "提交版本详情")
    public static class SubmissionResp {

        @Schema(description = "提交版本ID")
        private Long submissionId;

        @Schema(description = "版本号")
        private Integer submissionNo;

        @Schema(description = "状态")
        private String status;

        @Schema(description = "提交人ID")
        private Long submittedBy;

        @Schema(description = "提交人名称")
        private String submittedByName;

        @Schema(description = "提交时间")
        private LocalDateTime submittedAt;

        @Schema(description = "审核人ID")
        private Long reviewerId;

        @Schema(description = "审核人名称")
        private String reviewerName;

        @Schema(description = "审核时间")
        private LocalDateTime reviewedAt;

        @Schema(description = "审核备注")
        private String reviewComment;

        @Schema(description = "字段值分组列表")
        private List<FieldGroupResp> groups;

        @Schema(description = "驳回问题列表")
        private List<ReviewIssueResp> issues;
    }

    @Data
    @Schema(description = "字段值分组")
    public static class FieldGroupResp {

        @Schema(description = "分组名")
        private String groupName;

        @Schema(description = "分组排序")
        private Integer groupSort;

        @Schema(description = "字段值列表")
        private List<FieldValueResp> fields;
    }

    @Data
    @Schema(description = "字段值")
    public static class FieldValueResp {

        @Schema(description = "字段ID")
        private Long fieldId;

        @Schema(description = "字段编码")
        private String fieldCode;

        @Schema(description = "字段名称")
        private String fieldName;

        @Schema(description = "字段类型")
        private String fieldType;

        @Schema(description = "值序号")
        private Integer valueSeq;

        @Schema(description = "字段值")
        private String value;

        @Schema(description = "关联文件ID")
        private Long fileId;

        @Schema(description = "关联文件URL")
        private String fileUrl;

        @Schema(description = "是否必填")
        private Integer isRequired;

        @Schema(description = "是否可编辑")
        private Integer isEditable;

        @Schema(description = "排序")
        private Integer sortNo;
    }

    @Data
    @Schema(description = "驳回问题")
    public static class ReviewIssueResp {

        @Schema(description = "问题ID")
        private Long id;

        @Schema(description = "字段ID")
        private Long fieldId;

        @Schema(description = "字段编码")
        private String fieldCode;

        @Schema(description = "字段名称")
        private String fieldName;

        @Schema(description = "问题编码")
        private String issueCode;

        @Schema(description = "问题描述")
        private String issueMessage;

        @Schema(description = "状态")
        private String status;

        @Schema(description = "修复于哪个版本ID")
        private Long fixedInSubmissionId;
    }
}
