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

package top.continew.admin.coupon.service;

import org.springframework.web.multipart.MultipartFile;
import top.continew.admin.coupon.model.query.CouponMyCouponQuery;
import top.continew.admin.coupon.model.query.CouponWriteOffQuery;
import top.continew.admin.coupon.model.req.CouponClaimReq;
import top.continew.admin.coupon.model.req.CouponReviewReq;
import top.continew.admin.coupon.model.req.CouponWriteOffReq;
import top.continew.admin.coupon.model.resp.*;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;
import java.util.List;

public interface CouponClaimService {

    /**
     * 查询活动下券模板列表（不分页）。
     */
    List<CouponTemplateListResp> listTemplates(Long activityId);

    /**
     * 用户抢券。
     */
    Long claim(CouponClaimReq req);

    /**
     * 查询我的券分页列表。
     */
    PageResp<CouponMyCouponResp> listMyCoupons(CouponMyCouponQuery query, PageQuery pageQuery);

    /**
     * 查询我的券详情。
     */
    CouponMyCouponDetailResp getMyCouponDetail(Long id);

    /**
     * 核销前准备信息查询。
     */
    CouponWriteOffPrepareResp prepareWriteOff(String couponNo);

    /**
     * 商家提交核销。
     */
    Long writeOff(CouponWriteOffReq req);

    /**
     * 上传凭证文件。
     */
    CouponFileUploadResp uploadFile(MultipartFile file, String parentPath);

    /**
     * 商家查询核销列表。
     */
    PageResp<CouponWriteOffListResp> listWriteOffs(CouponWriteOffQuery query, PageQuery pageQuery);

    /**
     * 查询核销详情。
     */
    CouponWriteOffDetailResp getWriteOffDetail(Long id);

    /**
     * 驳回后重新提交。
     */
    Long resubmit(Long writeOffId, CouponWriteOffReq req);

    /**
     * 审核端查询待审核列表。
     */
    PageResp<CouponWriteOffListResp> listPendingReview(CouponWriteOffQuery query, PageQuery pageQuery);

    /**
     * 审核端审核提交。
     */
    void review(Long writeOffId, CouponReviewReq req);
}
