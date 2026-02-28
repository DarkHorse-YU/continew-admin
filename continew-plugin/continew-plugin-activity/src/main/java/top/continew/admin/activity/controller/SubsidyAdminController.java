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

package top.continew.admin.activity.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.continew.admin.activity.model.query.SubsidyAdminApplicationQuery;
import top.continew.admin.activity.model.req.SubsidyReviewReq;
import top.continew.admin.activity.model.resp.SubsidyAdminApplicationResp;
import top.continew.admin.activity.model.resp.SubsidyApplicationDetailResp;
import top.continew.admin.activity.service.SubsidyApplicationService;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;

/**
 * 管理端补贴审核接口。
 *
 * @author OpenCode
 * @since 2026/2/28 15:00
 */
@Tag(name = "管理端补贴审核接口")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/activity/subsidy/admin")
public class SubsidyAdminController {

    private final SubsidyApplicationService subsidyApplicationService;

    @Operation(summary = "查询申报列表", description = "按条件分页查询待审或已审申报记录")
    @SaCheckPermission("activity:subsidy:review:list")
    @GetMapping("/application")
    public PageResp<SubsidyAdminApplicationResp> pageApplications(@Valid SubsidyAdminApplicationQuery query,
                                                                   @Valid PageQuery pageQuery) {
        return subsidyApplicationService.pageApplicationsForAdmin(query, pageQuery);
    }

    @Operation(summary = "查询申报详情", description = "查询单条申报的完整字段与审核问题")
    @SaCheckPermission("activity:subsidy:review:get")
    @GetMapping("/application/{id}")
    public SubsidyApplicationDetailResp getDetail(@PathVariable Long id) {
        return subsidyApplicationService.getDetailForAdmin(id);
    }

    @Operation(summary = "审核申报", description = "对申报记录执行通过或驳回")
    @SaCheckPermission("activity:subsidy:review:audit")
    @PostMapping("/application/{id}/review")
    public void review(@PathVariable Long id, @RequestBody @Valid SubsidyReviewReq req) {
        subsidyApplicationService.review(id, req);
    }
}
