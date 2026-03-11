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

package top.continew.admin.coupon.constant;

/**
 * 优惠券常量
 */
public class CouponConstants {

    // ========== 活动状态 ==========
    /** 启用 */
    public static final String STATUS_ENABLED = "ENABLED";
    /** 禁用 */
    public static final String STATUS_DISABLED = "DISABLED";

    // ========== 审核模式 ==========
    /** 免审 */
    public static final String AUDIT_MODE_NONE = "NONE";
    /** 人工审核 */
    public static final String AUDIT_MODE_MANUAL = "MANUAL";

    // ========== 券类型 ==========
    /** 折扣券 */
    public static final String COUPON_TYPE_DISCOUNT = "DISCOUNT";
    /** 满减券 */
    public static final String COUPON_TYPE_CASH = "CASH";

    // ========== 有效期类型 ==========
    /** 相对有效期（领取后N天） */
    public static final String VALID_TYPE_RELATIVE = "RELATIVE";
    /** 固定有效期 */
    public static final String VALID_TYPE_FIXED = "FIXED";

    // ========== 用户券状态 ==========
    /** 未使用 */
    public static final String USER_COUPON_STATUS_UNUSED = "UNUSED";
    /** 锁定中 */
    public static final String USER_COUPON_STATUS_LOCKED = "LOCKED";
    /** 待审核 */
    public static final String USER_COUPON_STATUS_PENDING_AUDIT = "PENDING_AUDIT";
    /** 审核通过 */
    public static final String USER_COUPON_STATUS_APPROVED = "APPROVED";
    /** 审核驳回 */
    public static final String USER_COUPON_STATUS_REJECTED = "REJECTED";
    /** 已过期 */
    public static final String USER_COUPON_STATUS_EXPIRED = "EXPIRED";
    /** 已作废 */
    public static final String USER_COUPON_STATUS_CANCELLED = "CANCELLED";

    // ========== 核销状态 ==========
    /** 待审核 */
    public static final String WRITE_OFF_STATUS_PENDING_AUDIT = "PENDING_AUDIT";
    /** 通过 */
    public static final String WRITE_OFF_STATUS_APPROVED = "APPROVED";
    /** 驳回 */
    public static final String WRITE_OFF_STATUS_REJECTED = "REJECTED";
    /** 已撤销 */
    public static final String WRITE_OFF_STATUS_CANCELLED = "CANCELLED";

    // ========== 提交版本状态 ==========
    /** 待审核 */
    public static final String SUBMISSION_STATUS_PENDING = "PENDING";
    /** 通过 */
    public static final String SUBMISSION_STATUS_APPROVED = "APPROVED";
    /** 驳回 */
    public static final String SUBMISSION_STATUS_REJECTED = "REJECTED";

    // ========== 审核问题状态 ==========
    /** 未修复 */
    public static final String ISSUE_STATUS_OPEN = "OPEN";
    /** 已修复 */
    public static final String ISSUE_STATUS_FIXED = "FIXED";

    // ========== 核销方式 ==========
    /** 扫码核销 */
    public static final String WRITE_OFF_MODE_QR_SCAN = "QR_SCAN";
    /** 输码核销 */
    public static final String WRITE_OFF_MODE_CODE_INPUT = "CODE_INPUT";

    private CouponConstants() {
    }
}
