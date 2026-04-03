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

package top.continew.admin.activity.service;

import org.springframework.web.multipart.MultipartFile;
import top.continew.admin.activity.model.query.SubsidyAdminApplicationQuery;
import top.continew.admin.activity.model.query.SubsidyMyApplicationQuery;
import top.continew.admin.activity.model.req.SubsidyReviewReq;
import top.continew.admin.activity.model.req.SubsidySubmitReq;
import top.continew.admin.activity.model.resp.*;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;

/**
 * 补贴申报业务服务。
 *
 * @author OpenCode
 * @since 2026/2/28 15:00
 */
public interface SubsidyApplicationService {

    /** 查询当前活动表单。 */
    SubsidyFormResp getCurrentForm(String activityCode);

    /** 上传附件并按需 OCR。 */
    SubsidyFileUploadResp upload(MultipartFile file, String parentPath, Boolean needOcr, String ocrMappingKey);

    /** 首次提交申报。 */
    Long submit(SubsidySubmitReq req);

    /** 驳回后重新提交。 */
    Long resubmit(Long applicationId, SubsidySubmitReq req);

    /** 查询当前用户申报列表。 */
    PageResp<SubsidyMyApplicationResp> pageMyApplications(SubsidyMyApplicationQuery query, PageQuery pageQuery);

    /** 查询当前用户申报详情。 */
    SubsidyApplicationDetailResp getMyDetail(Long id);

    /** 管理端查询申报列表。 */
    PageResp<SubsidyAdminApplicationResp> pageApplicationsForAdmin(SubsidyAdminApplicationQuery query,
                                                                   PageQuery pageQuery);

    /** 管理端查询申报详情。 */
    SubsidyApplicationDetailResp getDetailForAdmin(Long id);

    /** 管理端审核申报。 */
    void review(Long id, SubsidyReviewReq req);
}
