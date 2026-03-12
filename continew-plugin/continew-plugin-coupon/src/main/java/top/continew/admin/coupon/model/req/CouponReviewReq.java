package top.continew.admin.coupon.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Review request")
public class CouponReviewReq {

    @NotNull(message = "approved can not be null")
    private Boolean approved;

    private String reviewComment;

    private List<IssueReq> issues;

    @Data
    public static class IssueReq {

        private String fieldCode;

        private String issueCode;

        private String issueMessage;
    }
}
