package top.continew.admin.coupon.model.resp;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponMyCouponResp {

    private Long id;
    private Long activityId;
    private String activityName;
    private Long templateId;
    private String templateName;
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
}
