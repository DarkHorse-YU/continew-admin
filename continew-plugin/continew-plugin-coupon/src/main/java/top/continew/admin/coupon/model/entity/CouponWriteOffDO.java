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
 * 核销主表
 */
@Data
@TableName("coupon_write_off")
public class CouponWriteOffDO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户券实例ID */
    private Long claimId;

    /** 券码 */
    private String couponNo;

    /** 活动ID */
    private Long activityId;

    /** 券模板ID */
    private Long templateId;

    /** 领券用户ID */
    private Long userId;

    /** 商家主体ID */
    private Long merchantId;

    /** 核销员ID */
    private Long verifierUserId;

    /** 核销方式：QR_SCAN=扫码， CODE_INPUT=输码 */
    private String writeOffMode;

    /** 幂等请求号 */
    private String requestNo;

    /** 状态： UNSUBMITTED=待上传凭证, PENDING=待审核, APPROVED=通过, REJECTED=驳回, CANCELLED=已撤销 */
    private String status;

    /** 核销时间 */
    private LocalDateTime writeOffTime;

    /** 当前提交版本ID */
    private Long currentSubmissionId;

    /** 审核人ID */
    private Long auditReviewerId;

    /** 审核时间 */
    private LocalDateTime auditTime;

    /** 审核意见 */
    private String auditComment;

    /** 备注 */
    private String remark;

    /** 乐观锁版本 */
    private Integer version;

    /** 是否删除：0=否,1=是 */
    private Integer isDeleted;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
