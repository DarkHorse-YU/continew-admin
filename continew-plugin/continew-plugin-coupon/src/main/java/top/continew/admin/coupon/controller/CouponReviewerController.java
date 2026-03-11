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
import top.continew.admin.coupon.model.query.CouponWriteOffQuery;
import top.continew.admin.coupon.model.req.CouponReviewReq;
import top.continew.admin.coupon.model.resp.*;
import top.continew.admin.coupon.service.CouponClaimService;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;

/**
 * 审核端优惠券接口
 */
@Tag(name = "审核端优惠券接口")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon/reviewer")
public class CouponReviewerController {

    private final CouponClaimService couponClaimService;

    @Operation(summary = "查询待审核列表", description = "查询待审核的核销记录列表")
    @SaCheckLogin
    @GetMapping("/pending-review")
    public PageResp<CouponWriteOffListResp> listPendingReview(@Valid CouponWriteOffQuery query,
                                                              @Valid PageQuery pageQuery) {
        return couponClaimService.listPendingReview(query, pageQuery);
    }

    @Operation(summary = "查询核销详情", description = "查询核销记录详情（审核用)")
    @SaCheckLogin
    @GetMapping("/write-off/{id}")
    public CouponWriteOffDetailResp getWriteOffDetail(
            @Parameter(description = "核销ID") @PathVariable Long id) {
        return couponClaimService.getWriteOffDetail(id);
    }

    @Operation(summary = "审核核销", description = "审核核销申请（通过/驳回）")
    @SaCheckLogin
    @PostMapping("/write-off/{id}/review")
    public void review(
            @Parameter(description = "核销ID") @PathVariable Long id,
            @RequestBody @Valid CouponReviewReq req) {
        couponClaimService.review(id, req);
    }
}
