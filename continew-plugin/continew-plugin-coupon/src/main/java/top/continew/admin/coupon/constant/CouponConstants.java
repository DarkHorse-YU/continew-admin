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
    public static final String USER_COUPON_STATUS_LOCKED = "LOCKED";
    public static final String USER_COUPON_STATUS_PENDING_AUDIT = "PENDING_AUDIT";
    public static final String USER_COUPON_STATUS_APPROVED = "APPROVED";
    public static final String USER_COUPON_STATUS_REJECTED = "REJECTED";
    public static final String USER_COUPON_STATUS_EXPIRED = "EXPIRED";
    public static final String USER_COUPON_STATUS_CANCELLED = "CANCELLED";

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
