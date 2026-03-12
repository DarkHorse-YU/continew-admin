package top.continew.admin.coupon.model.resp;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponTemplateListResp {

    private Long id;
    private Long activityId;
    private String templateCode;
    private String templateName;
    private String description;

    private String couponType;
    private BigDecimal discountRate;
    private BigDecimal discountAmount;
    private BigDecimal thresholdAmount;

    private Integer totalStock;
    private Integer claimedStock;
    private Integer remainingStock;

    private Integer perUserLimit;
    private Integer userClaimedCount;
    private Long formTemplateId;

    private Boolean canClaim;
    private String cannotClaimReason;

    private LocalDateTime createdAt;
}
