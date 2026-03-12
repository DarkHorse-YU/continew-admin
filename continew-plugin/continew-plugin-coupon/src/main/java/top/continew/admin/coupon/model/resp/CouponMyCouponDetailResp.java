package top.continew.admin.coupon.model.resp;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponMyCouponDetailResp {

    private Long id;
    private Long activityId;
    private String activityName;
    private Long templateId;
    private String templateName;
    private String description;

    private String couponNo;
    private String qrToken;

    private String couponType;
    private BigDecimal discountRate;
    private BigDecimal discountAmount;
    private BigDecimal thresholdAmount;

    private String status;
    private String statusDesc;

    private LocalDateTime claimTime;
    private LocalDateTime validStartTime;
    private LocalDateTime validEndTime;

    private Long writeOffId;
    private LocalDateTime writeOffTime;
    private WriteOffResp writeOffDetail;

    @Data
    public static class WriteOffResp {
        private String status;
        private Long verifierUserId;
        private String auditComment;
        private LocalDateTime writeOffTime;
    }
}
