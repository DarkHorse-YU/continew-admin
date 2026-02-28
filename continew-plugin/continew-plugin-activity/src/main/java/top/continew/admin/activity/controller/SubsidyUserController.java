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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.continew.admin.activity.model.query.SubsidyMyApplicationQuery;
import top.continew.admin.activity.model.req.SubsidySubmitReq;
import top.continew.admin.activity.model.resp.*;
import top.continew.admin.activity.service.SubsidyApplicationService;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;

/**
 * 用户端补贴申报接口。
 *
 * @author OpenCode
 * @since 2026/2/28 15:00
 */
@Tag(name = "用户端补贴申报接口")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/activity/subsidy/user")
public class SubsidyUserController {

    private final SubsidyApplicationService subsidyApplicationService;

    @Operation(summary = "查询活动表单", description = "根据活动编码查询当前可填报的表单模板")
    @GetMapping("/form")
    public SubsidyFormResp getCurrentForm(@RequestParam @NotBlank String activityCode) {
        return subsidyApplicationService.getCurrentForm(activityCode);
    }

    @Operation(summary = "上传资料", description = "上传图片并执行涉黄检测，按需进行 OCR 识别")
    @PostMapping("/file")
    public SubsidyFileUploadResp upload(@RequestPart @NotNull MultipartFile file,
                                        @RequestParam(required = false) String parentPath,
                                        @RequestParam(required = false, defaultValue = "false") Boolean needOcr,
                                        @RequestParam(required = false) String ocrMappingKey) {
        return subsidyApplicationService.upload(file, parentPath, needOcr, ocrMappingKey);
    }

    @Operation(summary = "首次提交申报", description = "用户首次提交补贴申报")
    @PostMapping("/application")
    public Long submit(@RequestBody @Valid SubsidySubmitReq req) {
        return subsidyApplicationService.submit(req);
    }

    @Operation(summary = "驳回后重提", description = "用户对驳回记录重新提交申报")
    @PostMapping("/application/{id}/resubmit")
    public Long resubmit(@PathVariable Long id, @RequestBody @Valid SubsidySubmitReq req) {
        return subsidyApplicationService.resubmit(id, req);
    }

    @Operation(summary = "查询我的申报列表", description = "分页查询当前用户的申报记录")
    @GetMapping("/application")
    public PageResp<SubsidyMyApplicationResp> pageMyApplications(@Valid SubsidyMyApplicationQuery query,
                                                                  @Valid PageQuery pageQuery) {
        return subsidyApplicationService.pageMyApplications(query, pageQuery);
    }

    @Operation(summary = "查询我的申报详情", description = "查询当前用户指定申报记录的详情")
    @GetMapping("/application/{id}")
    public SubsidyApplicationDetailResp getMyDetail(@PathVariable Long id) {
        return subsidyApplicationService.getMyDetail(id);
    }
}
