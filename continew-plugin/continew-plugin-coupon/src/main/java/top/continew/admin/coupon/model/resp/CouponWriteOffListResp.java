package top.continew.admin.coupon.model.resp;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponWriteOffListResp {

    private Long id;
    private String couponNo;

    private Long activityId;
    private String activityName;

    private Long templateId;
    private String templateName;

    private String couponType;
    private BigDecimal discountRate;
    private BigDecimal discountAmount;

    private Long userId;
    private Long verifierUserId;
    private String writeOffMode;

    private String status;
    private String statusDesc;

    private Integer submissionCount;
    private Integer currentSubmissionNo;

    private LocalDateTime writeOffTime;
    private LocalDateTime auditTime;
}
