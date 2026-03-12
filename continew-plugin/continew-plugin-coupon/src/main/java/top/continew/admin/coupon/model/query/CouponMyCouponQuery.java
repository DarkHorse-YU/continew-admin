package top.continew.admin.coupon.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "My coupon query")
public class CouponMyCouponQuery {

    @Schema(description = "coupon status")
    private String status;

    @Schema(description = "activity id")
    private Long activityId;
}
