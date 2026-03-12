package top.continew.admin.coupon.model.resp;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CouponWriteOffDetailResp {

    private Long id;
    private Long claimId;
    private String couponNo;

    private Long activityId;
    private String activityName;
    private Long templateId;
    private String templateName;

    private String couponType;
    private BigDecimal discountRate;
    private BigDecimal discountAmount;
    private BigDecimal thresholdAmount;

    private Long userId;
    private Long verifierUserId;
    private String writeOffMode;

    private String status;
    private String statusDesc;
    private String remark;

    private LocalDateTime writeOffTime;
    private Long currentSubmissionId;

    private Long auditReviewerId;
    private LocalDateTime auditTime;
    private String auditComment;

    private SubmissionResp currentSubmission;

    @Data
    public static class SubmissionResp {

        private Long submissionId;
        private Integer submissionNo;
        private String status;
        private Long submittedBy;
        private LocalDateTime submittedAt;
        private Long reviewerId;
        private LocalDateTime reviewedAt;
        private String reviewComment;
        private List<FieldGroupResp> groups;
        private List<ReviewIssueResp> issues;
    }

    @Data
    public static class FieldGroupResp {
        private String groupName;
        private Integer groupSort;
        private List<FieldValueResp> fields;
    }

    @Data
    public static class FieldValueResp {
        private Long fieldId;
        private String fieldCode;
        private String fieldName;
        private String fieldType;
        private Integer isRequired;
        private Integer isEditable;
        private Integer sortNo;
        private Integer valueSeq;
        private String value;
        private Long fileId;
        private String fileUrl;
    }

    @Data
    public static class ReviewIssueResp {
        private Long id;
        private Long fieldId;
        private String fieldCode;
        private String fieldName;
        private String issueCode;
        private String issueMessage;
        private String status;
        private Long fixedInSubmissionId;
        private LocalDateTime createdAt;
    }
}
