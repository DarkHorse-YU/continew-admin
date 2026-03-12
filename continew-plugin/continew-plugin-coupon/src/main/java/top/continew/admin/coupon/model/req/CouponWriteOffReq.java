package top.continew.admin.coupon.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Write off request")
public class CouponWriteOffReq {

    @NotBlank(message = "couponNo can not be blank")
    private String couponNo;

    @NotBlank(message = "writeOffMode can not be blank")
    private String writeOffMode;

    @NotBlank(message = "requestNo can not be blank")
    private String requestNo;

    private String remark;

    private List<FieldValueReq> fieldValues;

    @Data
    public static class FieldValueReq {

        @NotBlank(message = "fieldCode can not be blank")
        private String fieldCode;

        private Integer valueSeq;

        private String value;

        private Long fileId;

        private Integer ocrAutofill;
    }
}
