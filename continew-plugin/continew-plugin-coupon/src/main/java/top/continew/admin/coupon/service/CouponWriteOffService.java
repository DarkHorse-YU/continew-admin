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

/**
 * 优惠券核心服务接口
 */
public interface CouponWriteOffService {

    // ==================== 用户端接口 ====================

    /**
     * 查询活动列表
     */
    PageResp<CouponActivityListResp> listActivities(PageQuery pageQuery);

    /**
     * 查询活动下的券模板列表
     */
    PageResp<CouponTemplateListResp> listTemplates(Long activityId, PageQuery pageQuery);

    /**
     * 查询券模板详情（包含是否需要凭证信息）
     */
    CouponTemplateDetailResp getTemplateDetail(Long templateId);

    /**
     * 抢券
     */
    Long claim(CouponClaimReq req);

    /**
     * 查询我的券列表
     */
    PageResp<CouponMyCouponResp> listMyCoupons(CouponMyCouponQuery query, PageQuery pageQuery);

    /**
     * 查询我的券详情
     */
    CouponMyCouponDetailResp getMyCouponDetail(Long id);

    // ==================== 商家端接口 ====================

    /**
     * 核销前查询券信息（扫码/输码）
     */
    CouponWriteOffPrepareResp prepareWriteOff(String couponNo);

    /**
     * 核销（提交核销申请）
     */
    Long writeOff(CouponWriteOffReq req);

    /**
     * 上传文件
     */
    CouponFileUploadResp uploadFile(MultipartFile file, String parentPath);

    /**
     * 查询核销列表（商家端）
     */
    PageResp<CouponWriteOffListResp> listWriteOffs(CouponWriteOffQuery query, PageQuery pageQuery);

    /**
     * 查询核销详情
     */
    CouponWriteOffDetailResp getWriteOffDetail(Long id);

    /**
     * 驳回后重新提交
     */
    Long resubmit(Long writeOffId, CouponWriteOffReq req);

    // ==================== 审核端接口 ====================

    /**
     * 查询待审核列表（审核端）
     */
    PageResp<CouponWriteOffListResp> listPendingReview(CouponWriteOffQuery query, PageQuery pageQuery);

    /**
     * 审核核销申请
     */
    void review(Long writeOffId, CouponReviewReq req);
}
