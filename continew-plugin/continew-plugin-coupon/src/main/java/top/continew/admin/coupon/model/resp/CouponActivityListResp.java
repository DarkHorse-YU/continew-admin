package top.continew.admin.coupon.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Activity list item")
public class CouponActivityListResp {

    private Long id;
    private String activityCode;
    private String activityName;
    private String description;
    private LocalDateTime claimStartTime;
    private LocalDateTime claimEndTime;
    private LocalDateTime verifyStartTime;
    private LocalDateTime verifyEndTime;
    private String auditMode;
    private String status;
    private String statusDesc;
}
