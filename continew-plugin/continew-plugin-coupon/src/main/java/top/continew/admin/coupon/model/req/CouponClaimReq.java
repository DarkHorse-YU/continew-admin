package top.continew.admin.coupon.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "用户抢券请求")
public class CouponClaimReq {

    @NotNull(message = "券模板 ID 不能为空")
    @Schema(description = "券模板 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long templateId;

    @Schema(description = "用户 ID（仅压测未登录场景使用，正常业务可不传）")
    private Long userId;

//    @Schema(description = "滑块验证 token", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotBlank(message = "滑块验证 token 不能为空")
//    private String captchaToken;
}
