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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.coupon.model.query.CouponWriteOffQuery;
import top.continew.admin.coupon.model.req.CouponReviewReq;
import top.continew.admin.coupon.model.resp.CouponWriteOffDetailResp;
import top.continew.admin.coupon.model.resp.CouponWriteOffListResp;
import top.continew.admin.coupon.service.CouponClaimService;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;

@Tag(name = "抢券-审核端接口")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon/reviewer")
public class CouponReviewerController {

    private final CouponClaimService couponClaimService;

    /**
     * 查询待审核列表
     */
    @Operation(summary = "查询待审核列表", description = "分页查询当前审核员可审核的核销记录")
    @GetMapping("/pending-review")
    public PageResp<CouponWriteOffListResp> listPendingReview(@Valid CouponWriteOffQuery query,
                                                              @Valid PageQuery pageQuery) {
        return couponClaimService.listPendingReview(query, pageQuery);
    }

    /**
     * 查询核销详情
     */
    @Operation(summary = "查询核销详情", description = "按核销ID查询审核详情")
    @GetMapping("/write-off/{id}")
    public CouponWriteOffDetailResp getWriteOffDetail(@PathVariable Long id) {
        return couponClaimService.getWriteOffDetail(id);
    }

    /**
     * 审核核销
     */
    @Operation(summary = "审核核销", description = "对核销申请执行通过或驳回")
    @PostMapping("/write-off/{id}/review")
    public void review(@PathVariable Long id, @RequestBody @Valid CouponReviewReq req) {
        couponClaimService.review(id, req);
    }
}
