package top.continew.admin.coupon.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Write off query")
public class CouponWriteOffQuery {

    @Schema(description = "coupon no")
    private String couponNo;

    @Schema(description = "activity id")
    private Long activityId;

    @Schema(description = "write off status")
    private String status;

    @Schema(description = "write off start time")
    private LocalDateTime writeOffStartTime;

    @Schema(description = "write off end time")
    private LocalDateTime writeOffEndTime;
}
