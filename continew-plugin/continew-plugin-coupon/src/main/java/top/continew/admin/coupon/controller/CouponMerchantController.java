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
import org.springframework.web.multipart.MultipartFile;
import top.continew.admin.coupon.model.query.CouponWriteOffQuery;
import top.continew.admin.coupon.model.req.CouponWriteOffReq;
import top.continew.admin.coupon.model.resp.*;
import top.continew.admin.coupon.service.CouponClaimService;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;

/**
 * 商家端优惠券接口
 */
@Tag(name = "商家端优惠券接口")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon/merchant")
public class CouponMerchantController {

    private final CouponClaimService couponClaimService;

    @Operation(summary = "核销前查询券信息", description = "扫码或输入券码后查询券信息，            @SaCheckLogin
    @GetMapping("/write-off/prepare")
    public CouponWriteOffPrepareResp prepareWriteOff(
            @Parameter(description = "券码") @RequestParam String couponNo) {
        return couponClaimService.prepareWriteOff(couponNo);
    }

    @Operation(summary = "核销", description = "提交核销申请")
    @SaCheckLogin
    @PostMapping("/write-off")
    public Long writeOff(@RequestBody @Valid CouponWriteOffReq req) {
        return couponClaimService.writeOff(req);
    }

    @Operation(summary = "上传文件", description = "上传核销凭证文件（图片、文件)")
    @SaCheckLogin
    @PostMapping("/upload")
    public CouponFileUploadResp uploadFile(
            @Parameter(description = "文件") @RequestParam("file") MultipartFile file,
            @Parameter(description = "父路径") @RequestParam(required = false) String parentPath) {
        return couponClaimService.uploadFile(file, parentPath);
    }

    @Operation(summary = "查询核销列表", description = "查询当前核销员的核销记录列表")
    @SaCheckLogin
    @GetMapping("/write-off/list")
    public PageResp<CouponWriteOffListResp> listWriteOffs(@Valid CouponWriteOffQuery query,
                                                          @Valid PageQuery pageQuery) {
        return couponClaimService.listWriteOffs(query, pageQuery);
    }

    @Operation(summary = "查询核销详情", description = "查询核销记录详情")
    @SaCheckLogin
    @GetMapping("/write-off/{id}")
    public CouponWriteOffDetailResp getWriteOffDetail(
            @Parameter(description = "核销ID") @PathVariable Long id) {
        return couponClaimService.getWriteOffDetail(id);
    }

    @Operation(summary = "驳回后重新提交", description = "核销被驳回后重新提交凭证")
    @SaCheckLogin
    @PostMapping("/write-off/{id}/resubmit")
    public Long resubmit(
            @Parameter(description = "核销ID") @PathVariable Long id,
            @RequestBody @Valid CouponWriteOffReq req) {
        return couponClaimService.resubmit(id, req);
    }
}
