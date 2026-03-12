package top.continew.admin.coupon.model.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CouponFileUploadResp {

    private Long fileId;
    private String url;
    private String thUrl;
    private String fileName;
    private String originalName;
    private Long size;
}
