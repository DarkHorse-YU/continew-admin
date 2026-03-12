package top.continew.admin.coupon.model.resp;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CouponWriteOffPrepareResp {

    private Boolean canWriteOff;
    private String cannotReason;

    private Long claimId;
    private String couponNo;
    private String qrToken;

    private Long activityId;
    private String activityName;
    private Long templateId;
    private String templateName;

    private String couponType;
    private BigDecimal discountRate;
    private BigDecimal discountAmount;
    private BigDecimal thresholdAmount;

    private Long userId;
    private LocalDateTime validStartTime;
    private LocalDateTime validEndTime;

    private String auditMode;
    private Boolean requireForm;
    private FormTemplateResp formTemplate;

    @Data
    public static class FormTemplateResp {
        private Long templateId;
        private String templateName;
        private List<CouponTemplateDetailResp.FormTemplateResp.GroupResp> groups;
    }
}
