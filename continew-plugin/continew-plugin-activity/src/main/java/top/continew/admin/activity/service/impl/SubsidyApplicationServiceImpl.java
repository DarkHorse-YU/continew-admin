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

package top.continew.admin.activity.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.continew.admin.activity.config.ActivityProperties;
import top.continew.admin.activity.constant.SubsidyConstants;
import top.continew.admin.activity.mapper.*;
import top.continew.admin.activity.model.entity.*;
import top.continew.admin.activity.model.query.SubsidyAdminApplicationQuery;
import top.continew.admin.activity.model.query.SubsidyMyApplicationQuery;
import top.continew.admin.activity.model.req.SubsidyReviewReq;
import top.continew.admin.activity.model.req.SubsidySubmitReq;
import top.continew.admin.activity.model.resp.*;
import top.continew.admin.activity.service.SubsidyApplicationService;
import top.continew.admin.activity.service.safety.SubsidyImageContentCheckService;
import top.continew.admin.activity.service.safety.SubsidyOcrService;
import top.continew.admin.common.context.UserContextHolder;
import top.continew.admin.system.model.entity.StorageDO;
import top.continew.admin.system.enums.FileTypeEnum;
import top.continew.admin.system.service.FileService;
import top.continew.admin.system.service.StorageService;
import top.continew.starter.core.constant.StringConstants;
import top.continew.starter.core.exception.BusinessException;
import top.continew.starter.core.util.validation.CheckUtils;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 补贴申报业务实现。
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SubsidyApplicationServiceImpl implements SubsidyApplicationService {

    private final SubsidyActivityMapper activityMapper;
    private final SubsidyFormTemplateMapper formTemplateMapper;
    private final SubsidyFormFieldMapper formFieldMapper;
    private final SubsidyApplicationMapper applicationMapper;
    private final SubsidySubmissionMapper submissionMapper;
    private final SubsidySubmissionFieldValueMapper submissionFieldValueMapper;
    private final SubsidyReviewIssueMapper reviewIssueMapper;
    private final SubsidyFileMapper subsidyFileMapper;
    private final SubsidyOcrResultMapper ocrResultMapper;
    private final SubsidyReviewerScopeMapper reviewerScopeMapper;

    private final FileService fileService;
    private final FileStorageService fileStorageService;
    private final StorageService storageService;
    private final SubsidyOcrService subsidyOcrService;
    private final SubsidyImageContentCheckService imageContentCheckService;
    private final ActivityProperties properties;

    /**
     * 根据活动编码查询表单模板与字段定义，供前端动态渲染填报页。
     */
    @Override
    public SubsidyFormResp getCurrentForm(String activityCode) {
        SubsidyActivityDO activity = this.getActiveActivity(activityCode);
        SubsidyFormTemplateDO template = formTemplateMapper.selectById(activity.getTemplateId());
        CheckUtils.throwIfNull(template, "模板不存在");

        List<SubsidyFormFieldDO> fields = formFieldMapper.selectList(new LambdaQueryWrapper<SubsidyFormFieldDO>()
                .eq(SubsidyFormFieldDO::getTemplateId, template.getId())
                .eq(SubsidyFormFieldDO::getStatus, 1)
                .orderByAsc(SubsidyFormFieldDO::getSortNo));

        SubsidyFormResp resp = new SubsidyFormResp();
        resp.setActivityId(activity.getId());
        resp.setActivityCode(activity.getActivityCode());
        resp.setActivityName(activity.getActivityName());
        resp.setAuditMode(activity.getAuditMode());
        resp.setStartTime(activity.getStartTime());
        resp.setEndTime(activity.getEndTime());
        resp.setTemplateId(template.getId());
        resp.setTemplateCode(template.getTemplateCode());
        resp.setTemplateName(template.getTemplateName());
        resp.setTemplateVersion(template.getVersionNo());
        resp.setFields(fields.stream().map(field -> {
            SubsidyFormResp.FieldResp fieldResp = BeanUtil.copyProperties(field, SubsidyFormResp.FieldResp.class);
            fieldResp.setFieldId(field.getId());
            return fieldResp;
        }).toList());
        return resp;
    }

    /**
     * 上传附件并执行图片安全检测，按需执行 OCR 识别。
     */
    @Override
    public SubsidyFileUploadResp upload(MultipartFile file, String parentPath, Boolean needOcr, String ocrMappingKey) {
        CheckUtils.throwIf(file.isEmpty(), "文件不能为空");
        try {
            // 获取存储配置
            StorageDO storage = storageService.getByCode(properties.getStorageCode());
            String uploadPath = StrUtil.blankToDefault(parentPath, properties.getUploadParentPath());

            // 生成唯一文件名：UUID + 原始扩展名
            String originalFilename = file.getOriginalFilename();
            String extName = FileNameUtil.extName(originalFilename);
            String uniqueFilename = IdUtil.fastSimpleUUID() + (StrUtil.isNotBlank(extName) ? "." + extName : "");

            // 处理路径格式
            String path = pretreatmentPath(uploadPath);

            // 创建父级目录
            fileService.createParentDir(uploadPath, storage);

            // 直接使用 FileStorageService 上传
            var uploadPretreatment = fileStorageService.of(file)
                    .setPlatform(storage.getCode())
                    .setPath(path)
                    .setSaveFilename(uniqueFilename)
                    .setOriginalFilename(originalFilename)
                    .setHashCalculatorSha256(true)
                    .putAttr(ClassUtil.getClassName(StorageDO.class, false), storage);

            // 图片文件生成缩略图
            if (FileTypeEnum.IMAGE.getExtensions().contains(extName)) {
                uploadPretreatment.setIgnoreThumbnailException(true, true);
                uploadPretreatment.thumbnail(img -> img.size(100, 100));
            }

            FileInfo fileInfo = uploadPretreatment.upload();

            imageContentCheckService.checkImage(fileInfo.getUrl());

            Map<String, String> ocrResult = null;
            if (Boolean.TRUE.equals(needOcr)) {
                ocrResult = subsidyOcrService.recognize(file, ocrMappingKey);
            }

            SubsidyFileDO fileDO = new SubsidyFileDO();
            fileDO.setUserId(UserContextHolder.getUserId());
            fileDO.setStorageProvider(fileInfo.getPlatform());
            fileDO.setObjectKey(StrUtil.blankToDefault(fileInfo.getPath(), "") + fileInfo.getFilename());
            fileDO.setFileName(originalFilename);
            fileDO.setMimeType(fileInfo.getContentType());
            fileDO.setFileSize(fileInfo.getSize());
            fileDO.setSha256(fileInfo.getHashInfo() == null ? null : fileInfo.getHashInfo().getSha256());
            subsidyFileMapper.insert(fileDO);

            return SubsidyFileUploadResp.builder()
                    .subsidyFileId(fileDO.getId())
                    .url(fileInfo.getUrl())
                    .thUrl(fileInfo.getThUrl())
                    .ocrResult(ocrResult)
                    .build();
        } catch (Exception e) {
            Throwable root = e;
            while (root.getCause() != null) {
                root = root.getCause();
            }
            String rootMsg = StrUtil.blankToDefault(root.getMessage(), "");
            log.error("subsidy file upload failed, storageCode={}, parentPath={}, fileName={}, rootCause={}",
                    properties.getStorageCode(), StrUtil.blankToDefault(parentPath, properties.getUploadParentPath()),
                    file.getOriginalFilename(), rootMsg, e);
            throw new BusinessException(StrUtil.format("上传失败: {}", StrUtil.blankToDefault(rootMsg, e.getMessage())));
        }
    }

    /**
     * ???????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submit(SubsidySubmitReq req) {
        Long userId = UserContextHolder.getUserId();
        SubsidyActivityDO activity = this.getActiveActivity(req.getActivityId());

        SubsidyApplicationDO application = applicationMapper.selectOne(new LambdaQueryWrapper<SubsidyApplicationDO>()
                .eq(SubsidyApplicationDO::getActivityId, req.getActivityId())
                .eq(SubsidyApplicationDO::getUserId, userId));

        if (application == null) {
            application = this.createApplication(req.getActivityId(), activity.getCustomerId(), userId);
        } else {
            CheckUtils.throwIfNotEqual(activity.getCustomerId(), application.getCustomerId(), "客户归属不一致");
            CheckUtils.throwIf(StrUtil.equals(application.getCurrentStatus(), SubsidyConstants.APP_STATUS_PENDING),
                    "当前申报正在审核中，请勿重复提交");
            CheckUtils.throwIf(StrUtil.equals(application.getCurrentStatus(), SubsidyConstants.APP_STATUS_APPROVED),
                    "当前活动已申报通过，无需重复提交");
        }
        return this.doSubmit(activity, application, req, userId, false);
    }

    /**
     * 驳回后重新提交申报。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long resubmit(Long applicationId, SubsidySubmitReq req) {
        Long userId = UserContextHolder.getUserId();
        SubsidyApplicationDO application = applicationMapper.selectById(applicationId);
        CheckUtils.throwIfNull(application, "申报记录不存在");
        CheckUtils.throwIfNotEqual(userId, application.getUserId(), "无权限操作该申报记录");
        CheckUtils.throwIfNotEqual(SubsidyConstants.APP_STATUS_REJECTED, application.getCurrentStatus(),
                "仅驳回记录允许重新提交");

        SubsidyActivityDO activity = this.getActiveActivity(req.getActivityId());
        CheckUtils.throwIfNotEqual(application.getActivityId(), activity.getId(), "活动 ID 与申请不一致");
        CheckUtils.throwIfNotEqual(activity.getCustomerId(), application.getCustomerId(), "客户归属不一致");

        return this.doSubmit(activity, application, req, userId, true);
    }

    /**
     * 分页查询当前用户的申报记录。
     */
    @Override
    public PageResp<SubsidyMyApplicationResp> pageMyApplications(SubsidyMyApplicationQuery query, PageQuery pageQuery) {
        Long userId = UserContextHolder.getUserId();
        LambdaQueryWrapper<SubsidyApplicationDO> wrapper = new LambdaQueryWrapper<SubsidyApplicationDO>()
                .eq(SubsidyApplicationDO::getUserId, userId)
                .eq(StrUtil.isNotBlank(query.getCurrentStatus()), SubsidyApplicationDO::getCurrentStatus, query.getCurrentStatus())
                .ge(query.getStartTime() != null, SubsidyApplicationDO::getCreatedAt, query.getStartTime())
                .le(query.getEndTime() != null, SubsidyApplicationDO::getCreatedAt, query.getEndTime())
                .orderByDesc(SubsidyApplicationDO::getCreatedAt);

        IPage<SubsidyApplicationDO> page = applicationMapper.selectPage(new Page<>(pageQuery.getPage(), pageQuery.getSize()), wrapper);
        Map<Long, SubsidyActivityDO> activityMap = this.listActivityMap(page.getRecords().stream().map(SubsidyApplicationDO::getActivityId).toList());

        PageResp<SubsidyMyApplicationResp> resp = PageResp.build(page, SubsidyMyApplicationResp.class);
        resp.getList().forEach(item -> {
            SubsidyActivityDO activity = activityMap.get(item.getActivityId());
            if (activity != null) {
                item.setActivityName(activity.getActivityName());
                item.setCarType(this.getCarType(item.getId(), activity.getTemplateId()));
            }
        });
        return resp;
    }

    /**
     * 查询当前用户的申报详情。
     */
    @Override
    public SubsidyApplicationDetailResp getMyDetail(Long id) {
        SubsidyApplicationDO application = applicationMapper.selectById(id);
        CheckUtils.throwIfNull(application, "申报记录不存在");
        CheckUtils.throwIfNotEqual(UserContextHolder.getUserId(), application.getUserId(), "无权限访问该申报记录");
        return this.buildDetail(application);
    }

    /**
     * 管理端分页查询申报列表，自动按审核员授权 customer 范围过滤。
     */
    @Override
    public PageResp<SubsidyAdminApplicationResp> pageApplicationsForAdmin(SubsidyAdminApplicationQuery query,
                                                                          PageQuery pageQuery) {
        List<Long> customerIds = this.listReviewerCustomerIds(UserContextHolder.getUserId());
        if (CollUtil.isEmpty(customerIds)) {
            return PageResp.build(pageQuery.getPage(), pageQuery.getSize(), Collections.emptyList());
        }

        LambdaQueryWrapper<SubsidyApplicationDO> wrapper = new LambdaQueryWrapper<SubsidyApplicationDO>()
                .in(SubsidyApplicationDO::getCustomerId, customerIds)
                .like(StrUtil.isNotBlank(query.getApplicationNo()), SubsidyApplicationDO::getApplicationNo, query.getApplicationNo())
                .eq(StrUtil.isNotBlank(query.getCurrentStatus()), SubsidyApplicationDO::getCurrentStatus, query.getCurrentStatus())
                .eq(query.getCustomerId() != null, SubsidyApplicationDO::getCustomerId, query.getCustomerId())
                .ge(query.getStartTime() != null, SubsidyApplicationDO::getCreatedAt, query.getStartTime())
                .le(query.getEndTime() != null, SubsidyApplicationDO::getCreatedAt, query.getEndTime())
                .orderByDesc(SubsidyApplicationDO::getCreatedAt);

        if (StrUtil.isNotBlank(query.getCarType())) {
            List<Long> carTypeFieldIds = formFieldMapper.selectList(new LambdaQueryWrapper<SubsidyFormFieldDO>()
                            .eq(SubsidyFormFieldDO::getFieldCode, "car_type"))
                    .stream()
                    .map(SubsidyFormFieldDO::getId)
                    .toList();
            if (CollUtil.isEmpty(carTypeFieldIds)) {
                return PageResp.build(pageQuery.getPage(), pageQuery.getSize(), Collections.emptyList());
            }
            List<Long> submissionIds = submissionFieldValueMapper.selectSubmissionIdsByFieldIdsAndCarType(carTypeFieldIds,
                    query.getCarType());
            if (CollUtil.isEmpty(submissionIds)) {
                return PageResp.build(pageQuery.getPage(), pageQuery.getSize(), Collections.emptyList());
            }
            wrapper.in(SubsidyApplicationDO::getCurrentSubmissionId, submissionIds);
        }

        IPage<SubsidyApplicationDO> page = applicationMapper.selectPage(new Page<>(pageQuery.getPage(), pageQuery.getSize()), wrapper);
        PageResp<SubsidyAdminApplicationResp> resp = PageResp.build(page, SubsidyAdminApplicationResp.class);

        Map<Long, SubsidyActivityDO> activityMap = this.listActivityMap(page.getRecords().stream().map(SubsidyApplicationDO::getActivityId).toList());
        resp.getList().forEach(item -> {
            SubsidyActivityDO activity = activityMap.get(item.getActivityId());
            if (activity != null) {
                item.setActivityName(activity.getActivityName());
                item.setCarType(this.getCarType(item.getId(), activity.getTemplateId()));
            }
        });
        return resp;
    }

    /**
     * 管理端查询申报详情。
     */
    @Override
    public SubsidyApplicationDetailResp getDetailForAdmin(Long id) {
        SubsidyApplicationDO application = applicationMapper.selectById(id);
        CheckUtils.throwIfNull(application, "申报记录不存在");
        this.checkAdminScope(application.getCustomerId());
        return this.buildDetail(application);
    }

    /**
     * 管理端审核申报，支持通过和驳回。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void review(Long id, SubsidyReviewReq req) {
        SubsidyApplicationDO application = applicationMapper.selectById(id);
        CheckUtils.throwIfNull(application, "申报记录不存在");
        this.checkAdminScope(application.getCustomerId());
        CheckUtils.throwIfNotEqual(SubsidyConstants.APP_STATUS_PENDING, application.getCurrentStatus(),
                "当前状态不允许审核");

        SubsidySubmissionDO submission = submissionMapper.selectById(application.getCurrentSubmissionId());
        CheckUtils.throwIfNull(submission, "当前提交版本不存在");

        LocalDateTime now = LocalDateTime.now();
        submission.setReviewerId(UserContextHolder.getUserId());
        submission.setReviewedAt(now);
        submission.setReviewComment(req.getReviewComment());

        if (Boolean.TRUE.equals(req.getApproved())) {
            submission.setStatus(SubsidyConstants.SUBMISSION_STATUS_APPROVED);
            application.setCurrentStatus(SubsidyConstants.APP_STATUS_APPROVED);
            application.setApprovedAt(now);
            application.setFinalSubsidyAmount(req.getFinalSubsidyAmount());
            application.setFinalSubsidyDecidedAt(now);
            submissionMapper.updateById(submission);

            this.updateApplicationWithVersion(application,
                    new LambdaUpdateWrapper<SubsidyApplicationDO>()
                            .set(SubsidyApplicationDO::getCurrentStatus, application.getCurrentStatus())
                            .set(SubsidyApplicationDO::getApprovedAt, application.getApprovedAt())
                            .set(SubsidyApplicationDO::getFinalSubsidyAmount, application.getFinalSubsidyAmount())
                            .set(SubsidyApplicationDO::getFinalSubsidyDecidedAt, application.getFinalSubsidyDecidedAt()));
            return;
        }

        CheckUtils.throwIf(CollUtil.isEmpty(req.getIssues()), "驳回时请填写驳回问题");
        submission.setStatus(SubsidyConstants.SUBMISSION_STATUS_REJECTED);
        submissionMapper.updateById(submission);
        this.createReviewIssues(application, submission, req.getIssues());

        this.updateApplicationWithVersion(application,
                new LambdaUpdateWrapper<SubsidyApplicationDO>()
                        .set(SubsidyApplicationDO::getCurrentStatus, SubsidyConstants.APP_STATUS_REJECTED)
                        .setSql("reject_count = reject_count + 1"));
    }

    private SubsidyApplicationDetailResp buildDetail(SubsidyApplicationDO application) {
        SubsidyActivityDO activity = activityMapper.selectById(application.getActivityId());
        CheckUtils.throwIfNull(activity, "活动不存在");

        SubsidyApplicationDetailResp resp = BeanUtil.copyProperties(application, SubsidyApplicationDetailResp.class);
        resp.setActivityName(activity.getActivityName());

        SubsidySubmissionDO submission = submissionMapper.selectById(application.getCurrentSubmissionId());
        if (submission == null) {
            return resp;
        }

        SubsidyApplicationDetailResp.SubmissionResp submissionResp = BeanUtil.copyProperties(submission,
                SubsidyApplicationDetailResp.SubmissionResp.class);
        submissionResp.setSubmissionId(submission.getId());

        List<SubsidyFormFieldDO> fields = formFieldMapper.selectList(new LambdaQueryWrapper<SubsidyFormFieldDO>()
                .eq(SubsidyFormFieldDO::getTemplateId, activity.getTemplateId()));
        Map<Long, SubsidyFormFieldDO> fieldMap = fields.stream().collect(Collectors.toMap(SubsidyFormFieldDO::getId, s -> s));

        List<SubsidySubmissionFieldValueDO> values = submissionFieldValueMapper.selectList(
                new LambdaQueryWrapper<SubsidySubmissionFieldValueDO>()
                        .eq(SubsidySubmissionFieldValueDO::getSubmissionId, submission.getId())
                        .orderByAsc(SubsidySubmissionFieldValueDO::getFieldId, SubsidySubmissionFieldValueDO::getValueSeq));
        submissionResp.setFieldValues(values.stream().map(value -> {
            SubsidyApplicationDetailResp.FieldValueResp item = BeanUtil.copyProperties(value,
                    SubsidyApplicationDetailResp.FieldValueResp.class);
            SubsidyFormFieldDO field = fieldMap.get(value.getFieldId());
            if (field != null) {
                item.setFieldCode(field.getFieldCode());
                item.setFieldName(field.getFieldName());
            }
            return item;
        }).toList());

        List<SubsidyReviewIssueDO> issues = reviewIssueMapper.selectList(new LambdaQueryWrapper<SubsidyReviewIssueDO>()
                .eq(SubsidyReviewIssueDO::getSubmissionId, submission.getId())
                .orderByDesc(SubsidyReviewIssueDO::getCreatedAt));
        submissionResp.setIssues(issues.stream().map(issue -> {
            SubsidyApplicationDetailResp.ReviewIssueResp item = BeanUtil.copyProperties(issue,
                    SubsidyApplicationDetailResp.ReviewIssueResp.class);
            SubsidyFormFieldDO field = fieldMap.get(issue.getFieldId());
            if (field != null) {
                item.setFieldCode(field.getFieldCode());
                item.setFieldName(field.getFieldName());
            }
            return item;
        }).toList());

        resp.setCurrentSubmission(submissionResp);
        return resp;
    }

    private Long doSubmit(SubsidyActivityDO activity,
                          SubsidyApplicationDO application,
                          SubsidySubmitReq req,
                          Long userId,
                          boolean resubmit) {
        Map<String, SubsidyFormFieldDO> fieldMap = formFieldMapper.selectList(new LambdaQueryWrapper<SubsidyFormFieldDO>()
                        .eq(SubsidyFormFieldDO::getTemplateId, activity.getTemplateId())
                        .eq(SubsidyFormFieldDO::getStatus, 1))
                .stream()
                .collect(Collectors.toMap(SubsidyFormFieldDO::getFieldCode, s -> s));

        this.validateSubmitFields(req, fieldMap);

        int submissionNo = submissionMapper.selectCount(new LambdaQueryWrapper<SubsidySubmissionDO>()
                .eq(SubsidySubmissionDO::getApplicationId, application.getId())).intValue() + 1;

        SubsidySubmissionDO submission = new SubsidySubmissionDO();
        submission.setCustomerId(activity.getCustomerId());
        submission.setApplicationId(application.getId());
        submission.setSubmissionNo(submissionNo);
        submission.setStatus(StrUtil.equals(activity.getAuditMode(), SubsidyConstants.AUDIT_MODE_NONE)
                ? SubsidyConstants.SUBMISSION_STATUS_APPROVED
                : SubsidyConstants.SUBMISSION_STATUS_UNDER_REVIEW);
        submission.setSubmittedAt(LocalDateTime.now());
        if (StrUtil.equals(submission.getStatus(), SubsidyConstants.SUBMISSION_STATUS_APPROVED)) {
            submission.setReviewedAt(LocalDateTime.now());
            submission.setReviewerId(userId);
            submission.setReviewComment("免审自动通过");
        }
        submissionMapper.insert(submission);

        // 记录所有 OCR 自动填充的字段信息（fieldId, fileId, ocrResult）
        List<Object[]> ocrAutofillFields = new ArrayList<>();
        for (SubsidySubmitReq.FieldValueReq item : req.getFieldValues()) {
            SubsidyFormFieldDO field = fieldMap.get(item.getFieldCode());
            if (field == null) {
                continue;
            }
            SubsidySubmissionFieldValueDO value = new SubsidySubmissionFieldValueDO();
            value.setSubmissionId(submission.getId());
            value.setFieldId(field.getId());
            value.setValueSeq(Optional.ofNullable(item.getValueSeq()).orElse(1));
            value.setFileId(item.getFileId());
            value.setOcrAutofill(Optional.ofNullable(item.getOcrAutofill()).orElse(0));
            this.fillValue(field.getFieldType(), item.getValue(), value);
            submissionFieldValueMapper.insert(value);
            // 记录 OCR 自动填充的字段（有 fileId 且 ocrAutofill = 1）
            if (item.getFileId() != null && Objects.equals(item.getOcrAutofill(), 1)) {
                ocrAutofillFields.add(new Object[]{field.getId(), item.getFileId(), item.getOcrResult(), item.getValue()});
            }
        }

        String nextStatus = StrUtil.equals(activity.getAuditMode(), SubsidyConstants.AUDIT_MODE_NONE)
                ? SubsidyConstants.APP_STATUS_APPROVED
                : SubsidyConstants.APP_STATUS_PENDING;
        LambdaUpdateWrapper<SubsidyApplicationDO> updateWrapper = new LambdaUpdateWrapper<SubsidyApplicationDO>()
                .set(SubsidyApplicationDO::getCurrentStatus, nextStatus)
                .set(SubsidyApplicationDO::getCurrentSubmissionId, submission.getId())
                .set(StrUtil.equals(nextStatus, SubsidyConstants.APP_STATUS_APPROVED), SubsidyApplicationDO::getApprovedAt,
                        LocalDateTime.now());
        this.updateApplicationWithVersion(application, updateWrapper);

        if (resubmit && application.getCurrentSubmissionId() != null) {
            reviewIssueMapper.update(null, new LambdaUpdateWrapper<SubsidyReviewIssueDO>()
                    .eq(SubsidyReviewIssueDO::getSubmissionId, application.getCurrentSubmissionId())
                    .eq(SubsidyReviewIssueDO::getStatus, SubsidyConstants.ISSUE_STATUS_OPEN)
                    .set(SubsidyReviewIssueDO::getStatus, SubsidyConstants.ISSUE_STATUS_FIXED)
                    .set(SubsidyReviewIssueDO::getFixedInSubmissionId, submission.getId()));
        }

        // 保存所有 OCR 识别留痕（每个 OCR 自动填充的字段都记录完整信息）
        for (Object[] fieldFile : ocrAutofillFields) {
            Long fieldId = (Long) fieldFile[0];
            Long fileId = (Long) fieldFile[1];
            @SuppressWarnings("unchecked")
            Map<String, String> ocrResultMap = (Map<String, String>) fieldFile[2];
            String adoptedValue = (String) fieldFile[3];

            SubsidyOcrResultDO ocrResult = new SubsidyOcrResultDO();
            ocrResult.setSubmissionId(submission.getId());
            ocrResult.setFieldId(fieldId);
            ocrResult.setFileId(fileId);
            ocrResult.setOcrEngine("BAIDU");
            ocrResult.setIsAdopted(1);

            // 存储 OCR 原文和结构化 JSON
            if (ocrResultMap != null && !ocrResultMap.isEmpty()) {
                // rawText: 格式化原文（如 "姓名: 张三\n民族: 汉"）
                String rawText = ocrResultMap.entrySet().stream()
                        .map(e -> e.getKey() + ": " + e.getValue())
                        .collect(Collectors.joining("\n"));
                ocrResult.setRawText(rawText);
                // parsedValue: 结构化 JSON（如 {"姓名":"张三","民族":"汉"}）
                ocrResult.setParsedValue(JSONUtil.toJsonStr(ocrResultMap));
            }

            // 注：实际采用的值已存储在 submission_field_value 表中，此处无需重复存储

            ocrResultMapper.insert(ocrResult);
        }
        return application.getId();
    }

    private void createReviewIssues(SubsidyApplicationDO application,
                                    SubsidySubmissionDO submission,
                                    List<SubsidyReviewReq.IssueReq> issues) {
        SubsidyActivityDO activity = activityMapper.selectById(application.getActivityId());
        Map<String, SubsidyFormFieldDO> fieldMap = formFieldMapper.selectList(new LambdaQueryWrapper<SubsidyFormFieldDO>()
                        .eq(SubsidyFormFieldDO::getTemplateId, activity.getTemplateId()))
                .stream()
                .collect(Collectors.toMap(SubsidyFormFieldDO::getFieldCode, s -> s));

        for (SubsidyReviewReq.IssueReq issueReq : issues) {
            SubsidyFormFieldDO field = fieldMap.get(issueReq.getFieldCode());
            CheckUtils.throwIfNull(field, "驳回字段编码无效: %s", issueReq.getFieldCode());
            SubsidyReviewIssueDO issue = new SubsidyReviewIssueDO();
            issue.setSubmissionId(submission.getId());
            issue.setFieldId(field.getId());
            issue.setIssueCode(issueReq.getIssueCode());
            issue.setIssueMessage(issueReq.getIssueMessage());
            issue.setStatus(SubsidyConstants.ISSUE_STATUS_OPEN);
            reviewIssueMapper.insert(issue);
        }
    }

    private void validateSubmitFields(SubsidySubmitReq req, Map<String, SubsidyFormFieldDO> fieldMap) {
        Set<String> submitFieldCodes = req.getFieldValues().stream()
                .map(SubsidySubmitReq.FieldValueReq::getFieldCode)
                .collect(Collectors.toSet());

        List<String> missing = fieldMap.values().stream()
                .filter(field -> Objects.equals(field.getIsRequired(), 1))
                .map(SubsidyFormFieldDO::getFieldCode)
                .filter(code -> !submitFieldCodes.contains(code))
                .toList();

        CheckUtils.throwIf(CollUtil.isNotEmpty(missing), "缺少必填字段: {}", String.join(",", missing));
    }

    private void fillValue(String fieldType, String rawValue, SubsidySubmissionFieldValueDO value) {
        if (StrUtil.isBlank(rawValue)) {
            return;
        }
        switch (StrUtil.blankToDefault(fieldType, "text").toLowerCase()) {
            case "number", "money" -> {
                if (NumberUtil.isNumber(rawValue)) {
                    value.setValueNumber(NumberUtil.toBigDecimal(rawValue));
                } else {
                    value.setValueText(rawValue);
                }
            }
            case "date" -> {
                value.setValueDate(LocalDate.parse(rawValue));
                value.setValueText(rawValue);
            }
            case "enum" -> value.setValueEnum(rawValue);
            case "json" -> {
                value.setValueJson(JSONUtil.parse(rawValue).toString());
                value.setValueText(rawValue);
            }
            default -> value.setValueText(rawValue);
        }
    }

    private SubsidyActivityDO getActiveActivity(String activityCode) {
        SubsidyActivityDO activity = activityMapper.selectOne(new LambdaQueryWrapper<SubsidyActivityDO>()
                .eq(SubsidyActivityDO::getActivityCode, activityCode)
                .eq(SubsidyActivityDO::getStatus, 1)
                .orderByDesc(SubsidyActivityDO::getId)
                .last("limit 1"));
        CheckUtils.throwIfNull(activity, "活动不存在或未启用");

        LocalDateTime now = LocalDateTime.now();
        CheckUtils.throwIf(now.isBefore(activity.getStartTime()) || now.isAfter(activity.getEndTime()),
                "当前不在活动时间内");
        return activity;
    }

    private SubsidyActivityDO getActiveActivity(Long activityId) {
        SubsidyActivityDO activity = activityMapper.selectById(activityId);
        CheckUtils.throwIfNull(activity, "活动不存在");
        CheckUtils.throwIfNotEqual(1, activity.getStatus(), "活动未启用");

        LocalDateTime now = LocalDateTime.now();
        CheckUtils.throwIf(now.isBefore(activity.getStartTime()) || now.isAfter(activity.getEndTime()),
                "当前不在活动时间内");
        return activity;
    }

    private SubsidyApplicationDO createApplication(Long activityId, Long customerId, Long userId) {
        SubsidyApplicationDO application = new SubsidyApplicationDO();
        application.setApplicationNo("APP" + System.currentTimeMillis() + (int)(Math.random() * 1000));
        application.setCustomerId(customerId);
        application.setActivityId(activityId);
        application.setUserId(userId);
        application.setCurrentStatus(SubsidyConstants.APP_STATUS_DRAFT);
        application.setRejectCount(0);
        application.setVersion(0);
        applicationMapper.insert(application);
        return application;
    }

    private void checkAdminScope(Long customerId) {
        if (UserContextHolder.isSuperAdmin()) {
            return;
        }
        List<Long> customerIds = this.listReviewerCustomerIds(UserContextHolder.getUserId());
        CheckUtils.throwIf(CollUtil.isEmpty(customerIds) || !customerIds.contains(customerId),
                "无权限访问该客户数据");
    }

    private List<Long> listReviewerCustomerIds(Long reviewerUserId) {
        if (UserContextHolder.isSuperAdmin()) {
            return activityMapper.selectList(new LambdaQueryWrapper<SubsidyActivityDO>()
                            .select(SubsidyActivityDO::getCustomerId)
                            .groupBy(SubsidyActivityDO::getCustomerId))
                    .stream()
                    .map(SubsidyActivityDO::getCustomerId)
                    .filter(Objects::nonNull)
                    .toList();
        }

        return reviewerScopeMapper.selectList(new LambdaQueryWrapper<SubsidyReviewerScopeDO>()
                        .eq(SubsidyReviewerScopeDO::getReviewerUserId, reviewerUserId)
                        .eq(SubsidyReviewerScopeDO::getStatus, 1))
                .stream()
                .map(SubsidyReviewerScopeDO::getCustomerId)
                .distinct()
                .toList();
    }

    private void updateApplicationWithVersion(SubsidyApplicationDO application,
                                              LambdaUpdateWrapper<SubsidyApplicationDO> updateWrapper) {
        int updated = applicationMapper.update(null, updateWrapper
                .eq(SubsidyApplicationDO::getId, application.getId())
                .eq(SubsidyApplicationDO::getVersion, application.getVersion())
                .setSql("version = version + 1"));
        CheckUtils.throwIf(updated == 0, "数据已变更，请刷新后重试");
        application.setVersion(application.getVersion() + 1);
    }

    private Map<Long, SubsidyActivityDO> listActivityMap(List<Long> activityIds) {
        if (CollUtil.isEmpty(activityIds)) {
            return Collections.emptyMap();
        }
        return activityMapper.selectBatchIds(activityIds).stream()
                .collect(Collectors.toMap(SubsidyActivityDO::getId, s -> s, (a, b) -> a));
    }

    private String getCarType(Long applicationId, Long templateId) {
        SubsidyApplicationDO application = applicationMapper.selectById(applicationId);
        if (application == null || application.getCurrentSubmissionId() == null) {
            return null;
        }
        SubsidyFormFieldDO field = formFieldMapper.selectOne(new LambdaQueryWrapper<SubsidyFormFieldDO>()
                .eq(SubsidyFormFieldDO::getTemplateId, templateId)
                .eq(SubsidyFormFieldDO::getFieldCode, "car_type")
                .last("limit 1"));
        if (field == null) {
            return null;
        }
        SubsidySubmissionFieldValueDO value = submissionFieldValueMapper.selectOne(new LambdaQueryWrapper<SubsidySubmissionFieldValueDO>()
                .eq(SubsidySubmissionFieldValueDO::getSubmissionId, application.getCurrentSubmissionId())
                .eq(SubsidySubmissionFieldValueDO::getFieldId, field.getId())
                .orderByAsc(SubsidySubmissionFieldValueDO::getValueSeq)
                .last("limit 1"));
        if (value == null) {
            return null;
        }
        return StrUtil.blankToDefault(value.getValueEnum(), value.getValueText());
    }

    /**
     * 处理路径格式。
     *
     * <p>
     * 1.如果 path 为 {@code /}，则设置为空 <br />
     * 2.如果 path 不以 {@code /} 结尾，则添加后缀 {@code /} <br />
     * 3.如果 path 以 {@code /} 开头，则移除前缀 {@code /} <br />
     * 示例：yyyy/MM/dd/
     * </p>
     *
     * @param path 路径
     * @return 处理后的路径
     */
    private String pretreatmentPath(String path) {
        if (StringConstants.SLASH.equals(path)) {
            return StringConstants.EMPTY;
        }
        return StrUtil.appendIfMissing(StrUtil.removePrefix(path, StringConstants.SLASH), StringConstants.SLASH);
    }
}
