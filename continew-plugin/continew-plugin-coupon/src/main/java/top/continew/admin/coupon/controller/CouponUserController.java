package top.continew.admin.coupon.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.utils.JsonUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.coupon.model.query.CouponMyCouponQuery;
import top.continew.admin.coupon.model.req.CouponClaimReq;
import top.continew.admin.coupon.model.resp.*;
import top.continew.admin.coupon.service.CouponClaimService;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;
import java.util.List;

@Tag(name = "抢券-用户端接口")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon/user")
public class CouponUserController {

    private final CouponClaimService couponClaimService;

    /**
     * 查询活动下券模板
     */
    @Operation(summary = "查询活动券列表", description = "按活动查询可领取券模板列表（不分页）")
    @GetMapping("/activity/{activityId}/templates")
    public List<CouponTemplateListResp> listTemplates(@PathVariable Long activityId) {
        return couponClaimService.listTemplates(activityId);
    }

    /**
     * 用户抢券
     */
    @Operation(summary = "抢券", description = "完成滑块验证后发起抢券")
    @SaIgnore
    @PostMapping("/claim")
    public Long claim(@RequestBody @Valid CouponClaimReq req) {
        return couponClaimService.claim(req);
    }

    /**
     * 查询我的券
     */
    @Operation(summary = "查询我的券", description = "分页查询当前用户已领取券")
    @GetMapping("/my-coupons")
    public PageResp<CouponMyCouponResp> listMyCoupons(@Valid CouponMyCouponQuery query, @Valid PageQuery pageQuery) {
        return couponClaimService.listMyCoupons(query, pageQuery);
    }

    /**
     * 查询我的券详情
     */
    @Operation(summary = "查询我的券详情", description = "按券实例ID查询详情")
    @GetMapping("/my-coupons/{id}")
    public CouponMyCouponDetailResp getMyCouponDetail(
            @Parameter(description = "User coupon id") @PathVariable Long id) {
        return couponClaimService.getMyCouponDetail(id);
    }
}
