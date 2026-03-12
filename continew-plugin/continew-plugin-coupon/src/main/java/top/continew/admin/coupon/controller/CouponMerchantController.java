package top.continew.admin.coupon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.continew.admin.coupon.model.query.CouponWriteOffQuery;
import top.continew.admin.coupon.model.req.CouponWriteOffReq;
import top.continew.admin.coupon.model.resp.CouponFileUploadResp;
import top.continew.admin.coupon.model.resp.CouponWriteOffDetailResp;
import top.continew.admin.coupon.model.resp.CouponWriteOffListResp;
import top.continew.admin.coupon.model.resp.CouponWriteOffPrepareResp;
import top.continew.admin.coupon.service.CouponClaimService;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;

@Tag(name = "抢券-商家端接口")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon/merchant")
public class CouponMerchantController {

    private final CouponClaimService couponClaimService;

    /**
     * 核销前校验券信息
     */
    @Operation(summary = "核销预检", description = "根据券码查询券状态、权限与凭证要求")
    @GetMapping("/write-off/prepare")
    public CouponWriteOffPrepareResp prepareWriteOff(@RequestParam String couponNo) {
        return couponClaimService.prepareWriteOff(couponNo);
    }

    /**
     * 提交核销
     */
    @Operation(summary = "提交核销", description = "商家发起核销，按活动配置决定是否进入审核")
    @PostMapping("/write-off")
    public Long writeOff(@RequestBody @Valid CouponWriteOffReq req) {
        return couponClaimService.writeOff(req);
    }

    /**
     * 上传凭证文件
     */
    @Operation(summary = "上传凭证文件", description = "上传图片或文件，返回文件ID与访问地址")
    @PostMapping("/upload")
    public CouponFileUploadResp uploadFile(
            @Parameter(description = "file") @RequestParam("file") MultipartFile file,
            @Parameter(description = "upload parent path") @RequestParam(required = false) String parentPath) {
        return couponClaimService.uploadFile(file, parentPath);
    }

    /**
     * 查询商家核销记录
     */
    @Operation(summary = "查询核销列表", description = "分页查询当前核销员提交的核销记录")
    @GetMapping("/write-off/list")
    public PageResp<CouponWriteOffListResp> listWriteOffs(@Valid CouponWriteOffQuery query, @Valid PageQuery pageQuery) {
        return couponClaimService.listWriteOffs(query, pageQuery);
    }

    /**
     * 查询核销详情
     */
    @Operation(summary = "查询核销详情", description = "按核销ID查询核销详情及提交版本信息")
    @GetMapping("/write-off/{id}")
    public CouponWriteOffDetailResp getWriteOffDetail(@PathVariable Long id) {
        return couponClaimService.getWriteOffDetail(id);
    }

    /**
     * 驳回后重提
     */
    @Operation(summary = "驳回后重新提交", description = "核销审核驳回后，重新提交凭证内容")
    @PostMapping("/write-off/{id}/resubmit")
    public Long resubmit(@PathVariable Long id, @RequestBody @Valid CouponWriteOffReq req) {
        return couponClaimService.resubmit(id, req);
    }
}
