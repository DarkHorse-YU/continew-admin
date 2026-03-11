/*
 * Copyright (c) 2022-present Charles7c Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.continew.admin.coupon.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.coupon.model.query.CouponMyCouponQuery;
import top.continew.admin.coupon.model.req.CouponClaimReq;
import top.continew.admin.coupon.model.resp.*;
import top.continew.admin.coupon.service.CouponClaimService;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;
import top.continew.starter.log.core.annotation.AccessLog;

/**
 * 用户端优惠券接口
 */
@Tag(name = "用户端优惠券接口")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon/user")
public class CouponUserController {

    private final CouponClaimService couponClaimService;

    @Operation(summary = "查询活动列表", description = "查询当前可参与的抢券活动列表")
    @SaCheckLogin
    @GetMapping("/activity")
    public PageResp<CouponActivityListResp> listActivities(@Valid PageQuery pageQuery) {
        return couponClaimService.listActivities(pageQuery);
    }

    @Operation(summary = "查询活动下的券模板列表", description = "查询指定活动下可抢的券模板")
    @SaCheckLogin
    @GetMapping("/activity/{activityId}/templates")
    public PageResp<CouponTemplateListResp> listTemplates(
            @Parameter(description = "活动ID") @PathVariable Long activityId,
            @Valid PageQuery pageQuery) {
        return couponClaimService.listTemplates(activityId, pageQuery);
    }

    @Operation(summary = "查询券模板详情", description = "查询券模板详情，包含是否需要凭证信息")
    @SaCheckLogin
    @GetMapping("/template/{templateId}")
    public CouponTemplateDetailResp getTemplateDetail(
            @Parameter(description = "券模板ID") @PathVariable Long templateId) {
        return couponClaimService.getTemplateDetail(templateId);
    }

    @Operation(summary = "抢券", description = "用户抢券，需要先完成滑块验证")
    @SaCheckLogin
    @AccessLog(value = "抢券")
    @PostMapping("/claim")
    public Long claim(@RequestBody @Valid CouponClaimReq req) {
        return couponClaimService.claim(req);
    }

    @Operation(summary = "查询我的券列表", description = "查询当前用户已抢到的券列表")
    @SaCheckLogin
    @GetMapping("/my-coupons")
    public PageResp<CouponMyCouponResp> listMyCoupons(@Valid CouponMyCouponQuery query, @Valid PageQuery pageQuery) {
        return couponClaimService.listMyCoupons(query, pageQuery);
    }

    @Operation(summary = "查询我的券详情", description = "查询指定券的详细信息")
    @SaCheckLogin
    @GetMapping("/my-coupons/{id}")
    public CouponMyCouponDetailResp getMyCouponDetail(
            @Parameter(description = "券实例ID") @PathVariable Long id) {
        return couponClaimService.getMyCouponDetail(id);
    }
}
