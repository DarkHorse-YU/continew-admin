package top.continew.admin.coupon.model.resp;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CouponTemplateDetailResp {

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
    private Integer writeOffStock;
    private Integer perUserLimit;
    private Integer dailyClaimLimit;

    private String validType;
    private Integer validDays;
    private LocalDateTime fixedValidStartTime;
    private LocalDateTime fixedValidEndTime;

    private Long formTemplateId;
    private FormTemplateResp formTemplate;

    @Data
    public static class FormTemplateResp {

        private Long templateId;
        private String templateName;
        private List<GroupResp> groups;

        @Data
        public static class GroupResp {
            private String groupName;
            private Integer groupSort;
            private List<FieldResp> fields;
        }

        @Data
        public static class FieldResp {
            private Long fieldId;
            private String fieldCode;
            private String fieldName;
            private String fieldType;
            private Integer isRequired;
            private Integer isEditable;
            private String groupName;
            private Integer groupSort;
            private Integer sortNo;
            private String enumOptions;
            private String validationRule;
            private Integer ocrEnabled;
            private String ocrMappingKey;
        }
    }
}
