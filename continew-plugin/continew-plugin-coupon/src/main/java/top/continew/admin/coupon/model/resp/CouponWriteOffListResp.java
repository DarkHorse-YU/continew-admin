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

/**
 * 核销列表响应参数
 */
@Data
public class CouponWriteOffListResp {

    /** 核销记录 ID */
    private Long id;
    /** 券码 */
    private String couponNo;

    /** 活动 ID */
    private Long activityId;
    /** 活动名称 */
    private String activityName;
    /** 部门 ID */
    private Long deptId;
    /** 部门名称 */
    private String deptName;

    /** 模板 ID */
    private Long templateId;
    /** 模板名称 */
    private String templateName;
    /** 是否需要上传凭证 */
    private Boolean needUploadProof;

    /** 券类型 */
    private String couponType;
    /** 折扣比例 */
    private BigDecimal discountRate;
    /** 优惠金额 */
    private BigDecimal discountAmount;

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

    /** 提交次数 */
    private Integer submissionCount;
    /** 当前提交版本号 */
    private Integer currentSubmissionNo;

    /** 核销时间 */
    private LocalDateTime writeOffTime;
    /** 审核时间 */
    private LocalDateTime auditTime;
}
