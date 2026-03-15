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

public final class CouponConstants {

    private CouponConstants() {
    }

    public static final String STATUS_ENABLED = "ENABLED";
    public static final String STATUS_DISABLED = "DISABLED";

    public static final String AUDIT_MODE_NONE = "NONE";
    public static final String AUDIT_MODE_MANUAL = "MANUAL";

    public static final String COUPON_TYPE_DISCOUNT = "DISCOUNT";
    public static final String COUPON_TYPE_CASH = "CASH";

    public static final String VALID_TYPE_RELATIVE = "RELATIVE";
    public static final String VALID_TYPE_FIXED = "FIXED";

    public static final String USER_COUPON_STATUS_UNUSED = "UNUSED";
    public static final String USER_COUPON_STATUS_APPROVED = "APPROVED";
    public static final String USER_COUPON_STATUS_EXPIRED = "EXPIRED";
    public static final String USER_COUPON_STATUS_CANCELLED = "CANCELLED";

    public static final String WRITE_OFF_STATUS_PENDING_UPLOAD = "PENDING_UPLOAD";
    public static final String WRITE_OFF_STATUS_PENDING_AUDIT = "PENDING_AUDIT";
    public static final String WRITE_OFF_STATUS_APPROVED = "APPROVED";
    public static final String WRITE_OFF_STATUS_REJECTED = "REJECTED";
    public static final String WRITE_OFF_STATUS_CANCELLED = "CANCELLED";

    public static final String SUBMISSION_STATUS_PENDING = "PENDING";
    public static final String SUBMISSION_STATUS_APPROVED = "APPROVED";
    public static final String SUBMISSION_STATUS_REJECTED = "REJECTED";

    public static final String ISSUE_STATUS_OPEN = "OPEN";
    public static final String ISSUE_STATUS_FIXED = "FIXED";

    public static final String WRITE_OFF_MODE_QR_SCAN = "QR_SCAN";
    public static final String WRITE_OFF_MODE_CODE_INPUT = "CODE_INPUT";
}
