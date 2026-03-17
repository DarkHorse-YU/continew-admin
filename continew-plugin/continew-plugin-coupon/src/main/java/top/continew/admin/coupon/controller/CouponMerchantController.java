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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.continew.admin.coupon.model.req.CouponWriteOffReq;
import top.continew.admin.coupon.model.req.CouponWriteOffResubmitReq;
import top.continew.admin.coupon.model.resp.CouponFileUploadResp;
import top.continew.admin.coupon.model.resp.CouponFormResp;
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
    @Operation(summary = "核销预检", description = "根据二维码令牌查询券状态、权限与券信息")
    @GetMapping("/write-off/prepare")
    public CouponWriteOffPrepareResp prepareWriteOff(@RequestParam String qrToken) {
        return couponClaimService.prepareWriteOff(qrToken);
    }

    @Operation(summary = "查询当前模板表单", description = "根据券模板 ID 查询当前核销凭证表单模板")
    @GetMapping("/form")
    public CouponFormResp getCurrentForm(@RequestParam Long templateId) {
        return couponClaimService.getCurrentForm(templateId);
    }

    /**
     * 提交核销
     */
    @Operation(summary = "提交核销", description = "商家发起核销，若需凭证则进入未提交流程")
    @PostMapping("/write-off")
    public Long writeOff(@RequestBody @Valid CouponWriteOffReq req) {
        return couponClaimService.writeOff(req);
    }

    /**
     * 上传凭证文件
     */
    @Operation(summary = "上传凭证文件", description = "上传图片或文件，返回文件ID与访问地址")
    @PostMapping("/upload")
    public CouponFileUploadResp uploadFile(@Parameter(description = "file") @RequestParam("file") MultipartFile file,
                                           @Parameter(description = "upload parent path") @RequestParam(required = false) String parentPath,
                                           @Parameter(description = "need ocr") @RequestParam(required = false) Boolean needOcr,
                                           @Parameter(description = "ocr mapping key") @RequestParam(required = false) String ocrMappingKey) {
        return couponClaimService.uploadFile(file, parentPath, needOcr, ocrMappingKey);
    }

    /**
     * 查询商家核销记录
     */
    @Operation(summary = "查询核销列表", description = "分页查询当前核销员提交的核销记录")
    @GetMapping("/write-off/list")
    public PageResp<CouponWriteOffListResp> listWriteOffs(@Valid PageQuery pageQuery) {
        return couponClaimService.listWriteOffs(pageQuery);
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
     * 提交凭证/驳回后重提
     */
    @Operation(summary = "首次提交凭证", description = "待上传凭证状态下，提交核销凭证进入审核")
    @PostMapping("/write-off/{id}/submit")
    public Long submit(@PathVariable("id") Long writeOffId, @RequestBody @Valid CouponWriteOffResubmitReq req) {
        return couponClaimService.submit(writeOffId, req);
    }

    @Operation(summary = "重新提交凭证", description = "审核驳回后，重新提交核销凭证进入审核")
    @PostMapping("/write-off/{id}/resubmit")
    public Long resubmit(@PathVariable("id") Long writeOffId, @RequestBody @Valid CouponWriteOffResubmitReq req) {
        return couponClaimService.resubmit(writeOffId, req);
    }
}
