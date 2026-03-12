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

package top.continew.admin.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.redisson.api.RLock;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.continew.admin.common.context.UserContextHolder;
import top.continew.admin.coupon.constant.CouponConstants;
import top.continew.admin.coupon.mapper.*;
import top.continew.admin.coupon.model.entity.*;
import top.continew.admin.coupon.model.query.CouponClaimTemplateQuery;
import top.continew.admin.coupon.model.query.CouponMyCouponQuery;
import top.continew.admin.coupon.model.query.CouponWriteOffQuery;
import top.continew.admin.coupon.model.req.CouponClaimReq;
import top.continew.admin.coupon.model.req.CouponReviewReq;
import top.continew.admin.coupon.model.req.CouponWriteOffReq;
import top.continew.admin.coupon.model.resp.*;
import top.continew.admin.coupon.service.CouponClaimService;
import top.continew.admin.coupon.service.CouponClaimTemplateCacheService;
import top.continew.admin.coupon.service.CouponStockSyncService;
import top.continew.admin.system.model.entity.StorageDO;
import top.continew.admin.system.enums.FileTypeEnum;
import top.continew.admin.system.service.FileService;
import top.continew.admin.system.service.StorageService;
import top.continew.starter.core.constant.StringConstants;
import top.continew.starter.core.exception.BusinessException;
import top.continew.starter.core.util.validation.CheckUtils;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 优惠券核心服务实现
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CouponClaimServiceImpl implements CouponClaimService {

    private final CouponActivityMapper activityMapper;
    private final CouponTemplateMapper templateMapper;
    private final CouponUserCouponMapper userCouponMapper;
    private final CouponVerifierScopeMapper verifierScopeMapper;
    private final CouponReviewerScopeMapper reviewerScopeMapper;
    private final CouponFileMapper fileMapper;
    private final CouponFormTemplateMapper formTemplateMapper;
    private final CouponFormFieldMapper formFieldMapper;
    private final CouponWriteOffMapper writeOffMapper;
    private final CouponWriteOffSubmissionMapper submissionMapper;
    private final CouponWriteOffSubmissionValueMapper submissionValueMapper;
    private final CouponWriteOffReviewIssueMapper reviewIssueMapper;

    private final FileService fileService;
    private final FileStorageService fileStorageService;
    private final StorageService storageService;
    private final RedissonClient redissonClient;
    private final ObjectProvider<CaptchaService> captchaServiceProvider;
    private final CouponClaimTemplateCacheService couponClaimTemplateCacheService;
    private final CouponStockSyncService couponStockSyncService;

    // ==================== 用户端接口 ====================

    @Override
    public List<CouponTemplateListResp> listTemplates(Long activityId) {
        // 验证活动存在且有效
        CouponActivityDO activity = activityMapper.selectById(activityId);
        CheckUtils.throwIfNull(activity, "活动不存在");
        CheckUtils.throwIfNotEqual(CouponConstants.STATUS_ENABLED, activity.getStatus(), "活动未启用");

        Long userId = UserContextHolder.getUserId();
        LocalDateTime now = LocalDateTime.now();

        LambdaQueryWrapper<CouponTemplateDO> wrapper = new LambdaQueryWrapper<CouponTemplateDO>()
            .eq(CouponTemplateDO::getActivityId, activityId)
            .eq(CouponTemplateDO::getStatus, CouponConstants.STATUS_ENABLED)
            .eq(CouponTemplateDO::getIsDeleted, 0)
            .orderByAsc(CouponTemplateDO::getSortNo);

        List<CouponTemplateDO> templates = templateMapper.selectList(wrapper);
        List<CouponTemplateListResp> resp = BeanUtil.copyToList(templates, CouponTemplateListResp.class);

        // 填充用户已领数量和是否可领取
        for (CouponTemplateListResp item : resp) {
            CouponTemplateDO template = templates.stream()
                .filter(t -> t.getId().equals(item.getId()))
                .findFirst()
                .orElse(null);

            if (template != null) {
                // 查询用户已领数量
                Long userClaimedCount = userCouponMapper.selectCount(new LambdaQueryWrapper<CouponUserCouponDO>()
                    .eq(CouponUserCouponDO::getTemplateId, template.getId())
                    .eq(CouponUserCouponDO::getUserId, userId)
                    .eq(CouponUserCouponDO::getIsDeleted, 0));
                item.setUserClaimedCount(userClaimedCount.intValue());

                LocalDateTime dayStart = now.toLocalDate().atStartOfDay();
                LocalDateTime dayEnd = dayStart.plusDays(1);
                Long userTodayClaimedCount = userCouponMapper.selectCount(new LambdaQueryWrapper<CouponUserCouponDO>()
                    .eq(CouponUserCouponDO::getTemplateId, template.getId())
                    .eq(CouponUserCouponDO::getUserId, userId)
                    .eq(CouponUserCouponDO::getIsDeleted, 0)
                    .ge(CouponUserCouponDO::getClaimTime, dayStart)
                    .lt(CouponUserCouponDO::getClaimTime, dayEnd));

                // 计算剩余库存
                int remainingStock = (int)couponStockSyncService.getRemainingStock(template);
                item.setRemainingStock(remainingStock);

                // 判断是否可领取
                boolean canClaim = true;
                String cannotReason = null;

                if (now.isBefore(activity.getClaimStartTime())) {
                    canClaim = false;
                    cannotReason = "活动未开始";
                } else if (now.isAfter(activity.getClaimEndTime())) {
                    canClaim = false;
                    cannotReason = "活动已结束";
                } else if (remainingStock <= 0) {
                    canClaim = false;
                    cannotReason = "库存不足";
                } else if (userTodayClaimedCount != null && userTodayClaimedCount >= 1) {
                    canClaim = false;
                    cannotReason = "今日已领取";
                }

                item.setCanClaim(canClaim);
                item.setCannotClaimReason(cannotReason);
            }
        }

        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long claim(CouponClaimReq req) {
        Long userId = Optional.ofNullable(UserContextHolder.getUserId()).orElse(req.getUserId());
        CheckUtils.throwIfNull(userId, "未登录，请传 userId（仅压测）");
        //        this.verifyBehaviorCaptcha(req.getCaptchaToken());
        RLock claimLock = redissonClient.getLock("coupon:claim:lock:" + req.getTemplateId() + ":" + userId);
        boolean stockDeducted = false;
        boolean stockRollback = false;
        boolean dailyClaimReserved = false;
        String stockKey = null;
        String dailyClaimKey = null;
        try {
            boolean locked = claimLock.tryLock(3, 8, TimeUnit.SECONDS);
            CheckUtils.throwIf(!locked, "请求过于频繁，请稍后重试");

            CouponClaimTemplateQuery claimTemplate = couponClaimTemplateCacheService.getClaimTemplate(req
                .getTemplateId());
            CheckUtils.throwIfNull(claimTemplate, "券不存在");
            CheckUtils.throwIfNotEqual(CouponConstants.STATUS_ENABLED, claimTemplate.getTemplateStatus(), "券未启用");
            CheckUtils.throwIfNotEqual(CouponConstants.STATUS_ENABLED, claimTemplate.getActivityStatus(), "活动未启用");

            LocalDateTime now = LocalDateTime.now();
            CheckUtils.throwIf(now.isBefore(claimTemplate.getClaimStartTime()), "抢券活动未开始");
            CheckUtils.throwIf(now.isAfter(claimTemplate.getClaimEndTime()), "抢券活动已结束");

            LocalDateTime nextDay = now.toLocalDate().plusDays(1).atStartOfDay().plusMinutes(5);
            long ttlMillis = Math.max(Duration.between(now, nextDay).toMillis(), 1L);
            dailyClaimKey = "coupon:claim:daily:" + req.getTemplateId() + ":" + userId + ":" + now.toLocalDate();
            Long reserveResult = redissonClient.getScript(StringCodec.INSTANCE)
                .eval(RScript.Mode.READ_WRITE, """
                    local ok = redis.call('SET', KEYS[1], ARGV[1], 'PX', ARGV[2], 'NX')
                    if ok then
                        return 1
                    end
                    return 0
                    """, RScript.ReturnType.INTEGER, Collections.singletonList(dailyClaimKey), "1", String
                    .valueOf(ttlMillis));
            boolean firstClaimToday = Objects.equals(reserveResult, 1L);
            CheckUtils.throwIf(!firstClaimToday, "今日已领取");
            dailyClaimReserved = true;

            CouponTemplateDO template = BeanUtil.copyProperties(claimTemplate, CouponTemplateDO.class);
            template.setId(claimTemplate.getTemplateId());
            template.setStatus(claimTemplate.getTemplateStatus());
            stockKey = couponStockSyncService.buildStockKey(template.getId());
            long dbRemaining = couponStockSyncService.getRemainingStock(template);
            RScript script = redissonClient.getScript(StringCodec.INSTANCE);
            Long remainingStock = script.eval(RScript.Mode.READ_WRITE, """
                local stock = redis.call('GET', KEYS[1])
                if (not stock) then
                    stock = tonumber(ARGV[1])
                    redis.call('SET', KEYS[1], stock)
                else
                    stock = tonumber(stock)
                end
                if (stock <= 0) then
                    return -1
                end
                return redis.call('DECR', KEYS[1])
                """, RScript.ReturnType.INTEGER, Collections.singletonList(stockKey), String.valueOf(dbRemaining));
            if (remainingStock == null || remainingStock < 0) {
                throw new BusinessException("券已抢光");
            }
            stockDeducted = true;

            LocalDateTime validStartTime;
            LocalDateTime validEndTime;
            if (CouponConstants.VALID_TYPE_RELATIVE.equals(template.getValidType())) {
                validStartTime = now;
                validEndTime = now.plusDays(Optional.ofNullable(template.getValidDays()).orElse(0));
            } else {
                validStartTime = template.getFixedValidStartTime();
                validEndTime = template.getFixedValidEndTime();
            }

            CouponUserCouponDO userCoupon = new CouponUserCouponDO();
            userCoupon.setActivityId(claimTemplate.getActivityId());
            userCoupon.setTemplateId(template.getId());
            userCoupon.setCouponNo(generateCouponNo());
            userCoupon.setQrToken(IdUtil.fastSimpleUUID());
            userCoupon.setUserId(userId);
            userCoupon.setClaimTime(now);
            userCoupon.setValidStartTime(validStartTime);
            userCoupon.setValidEndTime(validEndTime);
            userCoupon.setStatus(CouponConstants.USER_COUPON_STATUS_UNUSED);
            userCoupon.setVersion(0);
            userCoupon.setIsDeleted(0);
            userCouponMapper.insert(userCoupon);
            couponStockSyncService.markPendingSyncAfterCommit(template.getId());
            return userCoupon.getId();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("抢券处理中断: templateId={}, userId={}", req.getTemplateId(), userId, e);
            throw new BusinessException("请求处理中断，请重试");
        } catch (Exception e) {
            if (stockDeducted && !stockRollback && StrUtil.isNotBlank(stockKey)) {
                redissonClient.getAtomicLong(stockKey).incrementAndGet();
            }
            if (dailyClaimReserved && StrUtil.isNotBlank(dailyClaimKey)) {
                redissonClient.getBucket(dailyClaimKey).delete();
            }
            if (e instanceof BusinessException) {
                throw (BusinessException)e;
            }
            log.error("抢券异常: templateId={}, userId={}", req.getTemplateId(), userId, e);
            throw new BusinessException("系统繁忙，请稍后重试");
        } finally {
            if (claimLock.isHeldByCurrentThread()) {
                claimLock.unlock();
            }
        }
    }

    /**
     * 生成券码
     */
    private String generateCouponNo() {
        // 稳定唯一券码：CP + yyyyMMddHHmmss + 8位全局递增序号（跨实例唯一）
        String timePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String dayKey = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long seq = redissonClient.getAtomicLong("coupon:no:seq:" + dayKey).incrementAndGet();
        return "CP" + timePart + String.format("%08d", seq % 100000000);
    }

    @Override
    public PageResp<CouponMyCouponResp> listMyCoupons(CouponMyCouponQuery query, PageQuery pageQuery) {
        Long userId = UserContextHolder.getUserId();

        LambdaQueryWrapper<CouponUserCouponDO> wrapper = new LambdaQueryWrapper<CouponUserCouponDO>()
            .eq(CouponUserCouponDO::getUserId, userId)
            .eq(CouponUserCouponDO::getIsDeleted, 0)
            .eq(StrUtil.isNotBlank(query.getStatus()), CouponUserCouponDO::getStatus, query.getStatus())
            .eq(query.getActivityId() != null, CouponUserCouponDO::getActivityId, query.getActivityId())
            .orderByDesc(CouponUserCouponDO::getClaimTime);

        IPage<CouponUserCouponDO> page = userCouponMapper.selectPage(new Page<>(pageQuery.getPage(), pageQuery
            .getSize()), wrapper);

        PageResp<CouponMyCouponResp> resp = PageResp.build(page, CouponMyCouponResp.class);

        // 填充活动名称和模板名称
        Set<Long> activityIds = page.getRecords()
            .stream()
            .map(CouponUserCouponDO::getActivityId)
            .collect(Collectors.toSet());
        Set<Long> templateIds = page.getRecords()
            .stream()
            .map(CouponUserCouponDO::getTemplateId)
            .collect(Collectors.toSet());

        Map<Long, CouponActivityDO> activityMap = CollUtil.isEmpty(activityIds)
            ? Collections.emptyMap()
            : activityMapper.selectBatchIds(activityIds)
                .stream()
                .collect(Collectors.toMap(CouponActivityDO::getId, a -> a));

        Map<Long, CouponTemplateDO> templateMap = CollUtil.isEmpty(templateIds)
            ? Collections.emptyMap()
            : templateMapper.selectBatchIds(templateIds)
                .stream()
                .collect(Collectors.toMap(CouponTemplateDO::getId, t -> t));

        resp.getList().forEach(item -> {
            CouponActivityDO activity = activityMap.get(item.getActivityId());
            if (activity != null) {
                item.setActivityName(activity.getActivityName());
            }

            CouponTemplateDO template = templateMap.get(item.getTemplateId());
            if (template != null) {
                item.setTemplateName(template.getTemplateName());
                item.setCouponType(template.getCouponType());
                item.setDiscountRate(template.getDiscountRate());
                item.setDiscountAmount(template.getDiscountAmount());
            }

            // 状态描述
            item.setStatusDesc(getStatusDesc(item.getStatus()));
        });

        return resp;
    }

    private void verifyBehaviorCaptcha(String captchaToken) {
        CheckUtils.throwIf(StrUtil.isBlank(captchaToken), "滑块验证 token 不能为空");
        CaptchaService captchaService = captchaServiceProvider.getIfAvailable();
        if (captchaService == null) {
            return;
        }
        CaptchaVO captchaVO = new CaptchaVO();
        BeanUtil.setProperty(captchaVO, "captchaVerification", captchaToken);
        ResponseModel verificationRes = captchaService.verification(captchaVO);
        CheckUtils.throwIfNotEqual(verificationRes.getRepCode(), RepCodeEnum.SUCCESS.getCode(), verificationRes
            .getRepMsg());
    }

    private String getStatusDesc(String status) {
        return switch (status) {
            case CouponConstants.USER_COUPON_STATUS_UNUSED -> "未使用";
            case CouponConstants.USER_COUPON_STATUS_LOCKED -> "处理中";
            case CouponConstants.USER_COUPON_STATUS_PENDING_AUDIT -> "待审核";
            case CouponConstants.USER_COUPON_STATUS_APPROVED -> "已核销";
            case CouponConstants.USER_COUPON_STATUS_REJECTED -> "审核驳回";
            case CouponConstants.USER_COUPON_STATUS_EXPIRED -> "已过期";
            case CouponConstants.USER_COUPON_STATUS_CANCELLED -> "已作废";
            default -> status;
        };
    }

    @Override
    public CouponMyCouponDetailResp getMyCouponDetail(Long id) {
        Long userId = UserContextHolder.getUserId();

        CouponUserCouponDO userCoupon = userCouponMapper.selectById(id);
        CheckUtils.throwIfNull(userCoupon, "券不存在");
        CheckUtils.throwIfNotEqual(userId, userCoupon.getUserId(), "无权访问该券");

        CouponMyCouponDetailResp resp = BeanUtil.copyProperties(userCoupon, CouponMyCouponDetailResp.class);
        resp.setStatusDesc(getStatusDesc(userCoupon.getStatus()));

        // 填充活动和模板信息
        CouponActivityDO activity = activityMapper.selectById(userCoupon.getActivityId());
        if (activity != null) {
            resp.setActivityName(activity.getActivityName());
        }

        CouponTemplateDO template = templateMapper.selectById(userCoupon.getTemplateId());
        if (template != null) {
            resp.setTemplateName(template.getTemplateName());
            resp.setDescription(template.getDescription());
            resp.setCouponType(template.getCouponType());
        }

        // 如果已核销，填充核销详情
        if (userCoupon.getWriteOffId() != null) {
            CouponWriteOffDO writeOff = writeOffMapper.selectById(userCoupon.getWriteOffId());
            if (writeOff != null) {
                resp.setWriteOffTime(writeOff.getWriteOffTime());

                CouponMyCouponDetailResp.WriteOffResp writeOffResp = new CouponMyCouponDetailResp.WriteOffResp();
                writeOffResp.setStatus(writeOff.getStatus());
                writeOffResp.setVerifierUserId(writeOff.getVerifierUserId());
                writeOffResp.setWriteOffTime(writeOff.getWriteOffTime());
                writeOffResp.setAuditComment(writeOff.getAuditComment());
                resp.setWriteOffDetail(writeOffResp);
            }
        }

        return resp;
    }

    // ==================== 商家端接口 ====================

    @Override
    public CouponWriteOffPrepareResp prepareWriteOff(String couponNo) {
        Long verifierUserId = UserContextHolder.getUserId();

        CouponUserCouponDO userCoupon = userCouponMapper.selectOne(new LambdaQueryWrapper<CouponUserCouponDO>()
            .eq(CouponUserCouponDO::getCouponNo, couponNo)
            .eq(CouponUserCouponDO::getIsDeleted, 0));
        CheckUtils.throwIfNull(userCoupon, "券码不存在");

        CouponTemplateDO template = templateMapper.selectById(userCoupon.getTemplateId());
        CheckUtils.throwIfNull(template, "券模板不存在");

        CouponActivityDO activity = activityMapper.selectById(userCoupon.getActivityId());
        CheckUtils.throwIfNull(activity, "活动不存在");

        CouponWriteOffPrepareResp resp = new CouponWriteOffPrepareResp();

        // 检查券状态
        if (!CouponConstants.USER_COUPON_STATUS_UNUSED.equals(userCoupon.getStatus())) {
            resp.setCanWriteOff(false);
            resp.setCannotReason(getStatusDesc(userCoupon.getStatus()));
            return resp;
        }

        // 检查有效期
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(userCoupon.getValidStartTime()) || now.isAfter(userCoupon.getValidEndTime())) {
            resp.setCanWriteOff(false);
            resp.setCannotReason("券已过期");
            return resp;
        }

        // 检查核销员权限
        boolean hasPermission = checkVerifierPermission(verifierUserId, activity.getId(), template.getId());
        if (!hasPermission) {
            resp.setCanWriteOff(false);
            resp.setCannotReason("无核销权限");
            return resp;
        }

        resp.setCanWriteOff(true);
        resp.setClaimId(userCoupon.getId());
        resp.setCouponNo(userCoupon.getCouponNo());
        resp.setTemplateName(template.getTemplateName());
        resp.setCouponType(template.getCouponType());
        resp.setDiscountRate(template.getDiscountRate());
        resp.setDiscountAmount(template.getDiscountAmount());
        resp.setThresholdAmount(template.getThresholdAmount());
        resp.setUserId(userCoupon.getUserId());
        resp.setActivityId(activity.getId());
        resp.setActivityName(activity.getActivityName());
        resp.setAuditMode(activity.getAuditMode());

        // 是否需要凭证
        boolean requireForm = template.getFormTemplateId() != null;
        resp.setRequireForm(requireForm);

        if (requireForm) {
            CouponFormTemplateDO formTemplate = formTemplateMapper.selectById(template.getFormTemplateId());
            if (formTemplate != null) {
                CouponWriteOffPrepareResp.FormTemplateResp formResp = new CouponWriteOffPrepareResp.FormTemplateResp();
                formResp.setTemplateId(formTemplate.getId());
                formResp.setTemplateName(formTemplate.getTemplateName());

                // 查询字段
                List<CouponFormFieldDO> fields = formFieldMapper.selectList(new LambdaQueryWrapper<CouponFormFieldDO>()
                    .eq(CouponFormFieldDO::getTemplateId, formTemplate.getId())
                    .eq(CouponFormFieldDO::getStatus, CouponConstants.STATUS_ENABLED)
                    .eq(CouponFormFieldDO::getIsDeleted, 0)
                    .orderByAsc(CouponFormFieldDO::getGroupSort)
                    .orderByAsc(CouponFormFieldDO::getSortNo));

                // 按分组整理
                Map<String, List<CouponFormFieldDO>> groupedFields = fields.stream()
                    .collect(Collectors.groupingBy(f -> StrUtil.blankToDefault(f
                        .getGroupName(), "默认分组"), LinkedHashMap::new, Collectors.toList()));

                List<CouponTemplateDetailResp.FormTemplateResp.GroupResp> groups = new ArrayList<>();
                groupedFields.forEach((groupName, fieldList) -> {
                    CouponTemplateDetailResp.FormTemplateResp.GroupResp group = new CouponTemplateDetailResp.FormTemplateResp.GroupResp();
                    group.setGroupName(groupName);
                    group.setGroupSort(fieldList.get(0).getGroupSort());
                    group.setFields(fieldList.stream().map(f -> {
                        CouponTemplateDetailResp.FormTemplateResp.FieldResp fieldResp = BeanUtil
                            .copyProperties(f, CouponTemplateDetailResp.FormTemplateResp.FieldResp.class);
                        fieldResp.setFieldId(f.getId());
                        return fieldResp;
                    }).toList());
                    groups.add(group);
                });

                formResp.setGroups(groups);
                resp.setFormTemplate(formResp);
            }
        }

        return resp;
    }

    /**
     * 检查核销员权限
     */
    private boolean checkVerifierPermission(Long verifierUserId, Long activityId, Long templateId) {
        if (UserContextHolder.isSuperAdmin()) {
            return true;
        }

        Long count = verifierScopeMapper.selectCount(new LambdaQueryWrapper<CouponVerifierScopeDO>()
            .eq(CouponVerifierScopeDO::getVerifierUserId, verifierUserId)
            .eq(CouponVerifierScopeDO::getStatus, CouponConstants.STATUS_ENABLED)
            .eq(CouponVerifierScopeDO::getIsDeleted, 0)
            .and(w -> w.eq(CouponVerifierScopeDO::getActivityId, 0L)
                .or()
                .eq(CouponVerifierScopeDO::getActivityId, activityId))
            .and(w -> w.eq(CouponVerifierScopeDO::getTemplateId, 0L)
                .or()
                .eq(CouponVerifierScopeDO::getTemplateId, templateId)));

        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long writeOff(CouponWriteOffReq req) {
        Long verifierUserId = UserContextHolder.getUserId();

        // 幂等性检查
        CouponWriteOffDO existingWriteOff = writeOffMapper.selectOne(new LambdaQueryWrapper<CouponWriteOffDO>()
            .eq(CouponWriteOffDO::getRequestNo, req.getRequestNo()));
        if (existingWriteOff != null) {
            return existingWriteOff.getId();
        }

        // 查询券信息
        CouponUserCouponDO userCoupon = userCouponMapper.selectOne(new LambdaQueryWrapper<CouponUserCouponDO>()
            .eq(CouponUserCouponDO::getCouponNo, req.getCouponNo())
            .eq(CouponUserCouponDO::getIsDeleted, 0));
        CheckUtils.throwIfNull(userCoupon, "券码不存在");
        CheckUtils.throwIfNotEqual(CouponConstants.USER_COUPON_STATUS_UNUSED, userCoupon.getStatus(), "券状态不允许核销");

        CouponTemplateDO template = templateMapper.selectById(userCoupon.getTemplateId());
        CheckUtils.throwIfNull(template, "券模板不存在");

        CouponActivityDO activity = activityMapper.selectById(userCoupon.getActivityId());
        CheckUtils.throwIfNull(activity, "活动不存在");

        // 检查核销员权限
        boolean hasPermission = checkVerifierPermission(verifierUserId, activity.getId(), template.getId());
        CheckUtils.throwIf(!hasPermission, "无核销权限");

        LocalDateTime now = LocalDateTime.now();
        boolean requireForm = template.getFormTemplateId() != null;

        // 创建核销记录
        CouponWriteOffDO writeOff = new CouponWriteOffDO();
        writeOff.setClaimId(userCoupon.getId());
        writeOff.setCouponNo(userCoupon.getCouponNo());
        writeOff.setActivityId(activity.getId());
        writeOff.setTemplateId(template.getId());
        writeOff.setUserId(userCoupon.getUserId());
        writeOff.setVerifierUserId(verifierUserId);
        writeOff.setWriteOffMode(req.getWriteOffMode());
        writeOff.setRequestNo(req.getRequestNo());
        writeOff.setWriteOffTime(now);
        writeOff.setRemark(req.getRemark());
        writeOff.setVersion(0);
        writeOff.setIsDeleted(0);

        // 如果需要凭证
        if (requireForm && CollUtil.isNotEmpty(req.getFieldValues())) {
            // 需要审核
            writeOff.setStatus(CouponConstants.WRITE_OFF_STATUS_PENDING_AUDIT);

            // 创建提交版本
            CouponWriteOffSubmissionDO submission = new CouponWriteOffSubmissionDO();
            submission.setWriteOffId(writeOff.getId());
            submission.setSubmissionNo(1);
            submission.setStatus(CouponConstants.SUBMISSION_STATUS_PENDING);
            submission.setSubmittedBy(verifierUserId);
            submission.setSubmittedAt(now);
            submission.setVersion(0);
            submissionMapper.insert(submission);

            writeOff.setCurrentSubmissionId(submission.getId());

            // 保存字段值
            this.saveSubmissionValues(submission.getId(), template.getFormTemplateId(), req.getFieldValues());

            // 更新券状态为待审核
            userCoupon.setStatus(CouponConstants.USER_COUPON_STATUS_PENDING_AUDIT);
        } else {
            // 不需要凭证或免审，直接通过
            String finalStatus = CouponConstants.AUDIT_MODE_NONE.equals(activity.getAuditMode())
                ? CouponConstants.WRITE_OFF_STATUS_APPROVED
                : CouponConstants.WRITE_OFF_STATUS_PENDING_AUDIT;
            writeOff.setStatus(finalStatus);

            if (CouponConstants.WRITE_OFF_STATUS_APPROVED.equals(finalStatus)) {
                writeOff.setAuditTime(now);
                userCoupon.setStatus(CouponConstants.USER_COUPON_STATUS_APPROVED);
            } else {
                userCoupon.setStatus(CouponConstants.USER_COUPON_STATUS_PENDING_AUDIT);
            }
        }

        writeOffMapper.insert(writeOff);

        // 更新券的核销ID和状态
        userCoupon.setWriteOffId(writeOff.getId());
        userCouponMapper.updateById(userCoupon);

        // 如果核销通过，更新模板核销数量
        if (CouponConstants.WRITE_OFF_STATUS_APPROVED.equals(writeOff.getStatus())) {
            templateMapper.update(null, new LambdaUpdateWrapper<CouponTemplateDO>().eq(CouponTemplateDO::getId, template
                .getId()).setSql("write_off_stock = write_off_stock + 1"));
        }

        return writeOff.getId();
    }

    private void saveSubmissionValues(Long submissionId,
                                      Long formTemplateId,
                                      List<CouponWriteOffReq.FieldValueReq> fieldValues) {
        // 查询字段映射
        Map<String, CouponFormFieldDO> fieldMap = formFieldMapper.selectList(new LambdaQueryWrapper<CouponFormFieldDO>()
            .eq(CouponFormFieldDO::getTemplateId, formTemplateId)
            .eq(CouponFormFieldDO::getStatus, CouponConstants.STATUS_ENABLED)
            .eq(CouponFormFieldDO::getIsDeleted, 0))
            .stream()
            .collect(Collectors.toMap(CouponFormFieldDO::getFieldCode, f -> f));

        for (CouponWriteOffReq.FieldValueReq fieldValue : fieldValues) {
            CouponFormFieldDO field = fieldMap.get(fieldValue.getFieldCode());
            if (field == null) {
                continue;
            }

            CouponWriteOffSubmissionValueDO value = new CouponWriteOffSubmissionValueDO();
            value.setSubmissionId(submissionId);
            value.setFieldId(field.getId());
            value.setValueSeq(Optional.ofNullable(fieldValue.getValueSeq()).orElse(1));
            value.setValueText(fieldValue.getValue());
            value.setFileId(fieldValue.getFileId());
            value.setOcrAutofill(Optional.ofNullable(fieldValue.getOcrAutofill()).orElse(0));
            submissionValueMapper.insert(value);
        }
    }

    @Override
    public CouponFileUploadResp uploadFile(MultipartFile file, String parentPath) {
        CheckUtils.throwIf(file.isEmpty(), "文件不能为空");

        try {
            StorageDO storage = storageService.getByCode("dev-minio");
            String uploadPath = StrUtil.blankToDefault(parentPath, "/coupon/writeoff/");

            String originalFilename = file.getOriginalFilename();
            String extName = FileNameUtil.extName(originalFilename);
            String uniqueFilename = IdUtil.fastSimpleUUID() + (StrUtil.isNotBlank(extName) ? "." + extName : "");

            String path = pretreatmentPath(uploadPath);
            fileService.createParentDir(uploadPath, storage);

            var uploadPretreatment = fileStorageService.of(file)
                .setPlatform(storage.getCode())
                .setPath(path)
                .setSaveFilename(uniqueFilename)
                .setOriginalFilename(originalFilename)
                .setHashCalculatorSha256(true);

            if (FileTypeEnum.IMAGE.getExtensions().contains(extName)) {
                uploadPretreatment.setIgnoreThumbnailException(true, true);
                uploadPretreatment.thumbnail(img -> img.size(100, 100));
            }

            FileInfo fileInfo = uploadPretreatment.upload();

            CouponFileDO fileDO = new CouponFileDO();
            fileDO.setUserId(UserContextHolder.getUserId());
            fileDO.setStorageProvider(fileInfo.getPlatform());
            fileDO.setBucketName(storage == null ? null : storage.getBucketName());
            fileDO.setFileName(StrUtil.blankToDefault(fileInfo.getPath(), "") + fileInfo.getFilename());
            fileDO.setOriginalName(originalFilename);
            fileDO.setUrl(fileInfo.getUrl());
            fileDO.setMimeType(fileInfo.getContentType());
            fileDO.setFileSize(fileInfo.getSize());
            fileDO.setSha256(fileInfo.getHashInfo() == null ? null : fileInfo.getHashInfo().getSha256());
            fileMapper.insert(fileDO);

            return CouponFileUploadResp.builder()
                .fileId(fileDO.getId())
                .url(fileInfo.getUrl())
                .thUrl(fileInfo.getThUrl())
                .build();
        } catch (Exception e) {
            log.error("文件上传失败", e);
            if (e instanceof BusinessException) {
                throw (BusinessException)e;
            }
            throw new BusinessException("文件上传失败，请稍后重试");
        }
    }

    private String pretreatmentPath(String path) {
        if (StringConstants.SLASH.equals(path)) {
            return StringConstants.EMPTY;
        }
        return StrUtil.appendIfMissing(StrUtil.removePrefix(path, StringConstants.SLASH), StringConstants.SLASH);
    }

    // ==================== 商家端与审核端接口 ====================

    @Override
    public PageResp<CouponWriteOffListResp> listWriteOffs(CouponWriteOffQuery query, PageQuery pageQuery) {
        Long verifierUserId = UserContextHolder.getUserId();

        LambdaQueryWrapper<CouponWriteOffDO> wrapper = new LambdaQueryWrapper<CouponWriteOffDO>()
            .eq(CouponWriteOffDO::getVerifierUserId, verifierUserId)
            .eq(CouponWriteOffDO::getIsDeleted, 0)
            .like(StrUtil.isNotBlank(query.getCouponNo()), CouponWriteOffDO::getCouponNo, query.getCouponNo())
            .eq(query.getActivityId() != null, CouponWriteOffDO::getActivityId, query.getActivityId())
            .eq(StrUtil.isNotBlank(query.getStatus()), CouponWriteOffDO::getStatus, query.getStatus())
            .ge(query.getWriteOffStartTime() != null, CouponWriteOffDO::getWriteOffTime, query.getWriteOffStartTime())
            .le(query.getWriteOffEndTime() != null, CouponWriteOffDO::getWriteOffTime, query.getWriteOffEndTime())
            .orderByDesc(CouponWriteOffDO::getWriteOffTime);

        IPage<CouponWriteOffDO> page = writeOffMapper.selectPage(new Page<>(pageQuery.getPage(), pageQuery
            .getSize()), wrapper);

        PageResp<CouponWriteOffListResp> resp = PageResp.build(page, CouponWriteOffListResp.class);
        this.fillWriteOffListResp(resp, page.getRecords());

        return resp;
    }

    @Override
    public CouponWriteOffDetailResp getWriteOffDetail(Long id) {
        CouponWriteOffDO writeOff = writeOffMapper.selectById(id);
        CheckUtils.throwIfNull(writeOff, "核销记录不存在");

        // 验证权限
        Long userId = UserContextHolder.getUserId();
        boolean isVerifier = userId.equals(writeOff.getVerifierUserId());
        boolean isReviewer = checkReviewerPermission(userId, writeOff.getActivityId());
        CheckUtils.throwIf(!isVerifier && !isReviewer, "无权访问该核销记录");

        return buildWriteOffDetailResp(writeOff);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long resubmit(Long writeOffId, CouponWriteOffReq req) {
        Long verifierUserId = UserContextHolder.getUserId();

        CouponWriteOffDO writeOff = writeOffMapper.selectById(writeOffId);
        CheckUtils.throwIfNull(writeOff, "核销记录不存在");
        CheckUtils.throwIfNotEqual(verifierUserId, writeOff.getVerifierUserId(), "无权操作该核销记录");
        CheckUtils.throwIfNotEqual(CouponConstants.WRITE_OFF_STATUS_REJECTED, writeOff.getStatus(), "仅驳回状态可重新提交");

        CouponTemplateDO template = templateMapper.selectById(writeOff.getTemplateId());
        CheckUtils.throwIfNull(template, "券模板不存在");

        LocalDateTime now = LocalDateTime.now();

        // 查询当前最大版本号
        Long maxSubmissionNo = submissionMapper.selectCount(new LambdaQueryWrapper<CouponWriteOffSubmissionDO>()
            .eq(CouponWriteOffSubmissionDO::getWriteOffId, writeOffId));
        int nextSubmissionNo = maxSubmissionNo.intValue() + 1;

        // 创建新提交版本
        CouponWriteOffSubmissionDO submission = new CouponWriteOffSubmissionDO();
        submission.setWriteOffId(writeOffId);
        submission.setSubmissionNo(nextSubmissionNo);
        submission.setStatus(CouponConstants.SUBMISSION_STATUS_PENDING);
        submission.setSubmittedBy(verifierUserId);
        submission.setSubmittedAt(now);
        submission.setVersion(0);
        submissionMapper.insert(submission);

        // 保存字段值
        if (template.getFormTemplateId() != null && CollUtil.isNotEmpty(req.getFieldValues())) {
            this.saveSubmissionValues(submission.getId(), template.getFormTemplateId(), req.getFieldValues());
        }

        // 更新核销记录状态
        writeOffMapper.update(null, new LambdaUpdateWrapper<CouponWriteOffDO>().eq(CouponWriteOffDO::getId, writeOffId)
            .set(CouponWriteOffDO::getStatus, CouponConstants.WRITE_OFF_STATUS_PENDING_AUDIT)
            .set(CouponWriteOffDO::getCurrentSubmissionId, submission.getId()));

        // 标记之前的问题为已修复
        if (writeOff.getCurrentSubmissionId() != null) {
            reviewIssueMapper.update(null, new LambdaUpdateWrapper<CouponWriteOffReviewIssueDO>()
                .eq(CouponWriteOffReviewIssueDO::getSubmissionId, writeOff.getCurrentSubmissionId())
                .eq(CouponWriteOffReviewIssueDO::getStatus, CouponConstants.ISSUE_STATUS_OPEN)
                .set(CouponWriteOffReviewIssueDO::getStatus, CouponConstants.ISSUE_STATUS_FIXED)
                .set(CouponWriteOffReviewIssueDO::getFixedInSubmissionId, submission.getId()));
        }

        return writeOffId;
    }

    // ==================== 审核端接口 ====================

    @Override
    public PageResp<CouponWriteOffListResp> listPendingReview(CouponWriteOffQuery query, PageQuery pageQuery) {
        Long reviewerUserId = UserContextHolder.getUserId();

        // 获取审核员有权限的活动ID列表
        List<Long> activityIds = getAllowedActivityIds(reviewerUserId);
        if (CollUtil.isEmpty(activityIds)) {
            return PageResp.build(pageQuery.getPage(), pageQuery.getSize(), Collections.emptyList());
        }

        LambdaQueryWrapper<CouponWriteOffDO> wrapper = new LambdaQueryWrapper<CouponWriteOffDO>()
            .in(CouponWriteOffDO::getActivityId, activityIds)
            .eq(CouponWriteOffDO::getStatus, CouponConstants.WRITE_OFF_STATUS_PENDING_AUDIT)
            .eq(CouponWriteOffDO::getIsDeleted, 0)
            .like(StrUtil.isNotBlank(query.getCouponNo()), CouponWriteOffDO::getCouponNo, query.getCouponNo())
            .eq(query.getActivityId() != null, CouponWriteOffDO::getActivityId, query.getActivityId())
            .ge(query.getWriteOffStartTime() != null, CouponWriteOffDO::getWriteOffTime, query.getWriteOffStartTime())
            .le(query.getWriteOffEndTime() != null, CouponWriteOffDO::getWriteOffTime, query.getWriteOffEndTime())
            .orderByDesc(CouponWriteOffDO::getWriteOffTime);

        IPage<CouponWriteOffDO> page = writeOffMapper.selectPage(new Page<>(pageQuery.getPage(), pageQuery
            .getSize()), wrapper);

        PageResp<CouponWriteOffListResp> resp = PageResp.build(page, CouponWriteOffListResp.class);
        this.fillWriteOffListResp(resp, page.getRecords());

        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void review(Long writeOffId, CouponReviewReq req) {
        Long reviewerUserId = UserContextHolder.getUserId();

        CouponWriteOffDO writeOff = writeOffMapper.selectById(writeOffId);
        CheckUtils.throwIfNull(writeOff, "核销记录不存在");
        CheckUtils.throwIfNotEqual(CouponConstants.WRITE_OFF_STATUS_PENDING_AUDIT, writeOff.getStatus(), "当前状态不允许审核");

        // 检查审核员权限
        boolean hasPermission = checkReviewerPermission(reviewerUserId, writeOff.getActivityId());
        CheckUtils.throwIf(!hasPermission, "无审核权限");

        CouponWriteOffSubmissionDO submission = submissionMapper.selectById(writeOff.getCurrentSubmissionId());
        CheckUtils.throwIfNull(submission, "提交版本不存在");

        LocalDateTime now = LocalDateTime.now();

        if (Boolean.TRUE.equals(req.getApproved())) {
            // 审核通过
            submission.setStatus(CouponConstants.SUBMISSION_STATUS_APPROVED);
            submission.setReviewerId(reviewerUserId);
            submission.setReviewedAt(now);
            submission.setReviewComment(req.getReviewComment());
            submissionMapper.updateById(submission);

            // 更新核销记录状态
            writeOffMapper.update(null, new LambdaUpdateWrapper<CouponWriteOffDO>()
                .eq(CouponWriteOffDO::getId, writeOffId)
                .set(CouponWriteOffDO::getStatus, CouponConstants.WRITE_OFF_STATUS_APPROVED)
                .set(CouponWriteOffDO::getAuditReviewerId, reviewerUserId)
                .set(CouponWriteOffDO::getAuditTime, now)
                .set(CouponWriteOffDO::getAuditComment, req.getReviewComment()));

            // 更新券状态
            userCouponMapper.update(null, new LambdaUpdateWrapper<CouponUserCouponDO>()
                .eq(CouponUserCouponDO::getId, writeOff.getClaimId())
                .set(CouponUserCouponDO::getStatus, CouponConstants.USER_COUPON_STATUS_APPROVED));

            // 更新模板核销数量
            templateMapper.update(null, new LambdaUpdateWrapper<CouponTemplateDO>().eq(CouponTemplateDO::getId, writeOff
                .getTemplateId()).setSql("write_off_stock = write_off_stock + 1"));

        } else {
            // 审核驳回
            CheckUtils.throwIf(CollUtil.isEmpty(req.getIssues()), "驳回时请填写驳回问题");

            submission.setStatus(CouponConstants.SUBMISSION_STATUS_REJECTED);
            submission.setReviewerId(reviewerUserId);
            submission.setReviewedAt(now);
            submission.setReviewComment(req.getReviewComment());
            submissionMapper.updateById(submission);

            // 创建驳回问题
            this.createReviewIssues(writeOff, submission, req.getIssues());

            // 更新核销记录状态
            writeOffMapper.update(null, new LambdaUpdateWrapper<CouponWriteOffDO>()
                .eq(CouponWriteOffDO::getId, writeOffId)
                .set(CouponWriteOffDO::getStatus, CouponConstants.WRITE_OFF_STATUS_REJECTED)
                .set(CouponWriteOffDO::getAuditReviewerId, reviewerUserId)
                .set(CouponWriteOffDO::getAuditTime, now)
                .set(CouponWriteOffDO::getAuditComment, req.getReviewComment()));

            // 更新券状态
            userCouponMapper.update(null, new LambdaUpdateWrapper<CouponUserCouponDO>()
                .eq(CouponUserCouponDO::getId, writeOff.getClaimId())
                .set(CouponUserCouponDO::getStatus, CouponConstants.USER_COUPON_STATUS_REJECTED));
        }
    }

    private void createReviewIssues(CouponWriteOffDO writeOff,
                                    CouponWriteOffSubmissionDO submission,
                                    List<CouponReviewReq.IssueReq> issues) {
        CouponTemplateDO template = templateMapper.selectById(writeOff.getTemplateId());
        if (template == null || template.getFormTemplateId() == null) {
            return;
        }

        Map<String, CouponFormFieldDO> fieldMap = formFieldMapper.selectList(new LambdaQueryWrapper<CouponFormFieldDO>()
            .eq(CouponFormFieldDO::getTemplateId, template.getFormTemplateId()))
            .stream()
            .collect(Collectors.toMap(CouponFormFieldDO::getFieldCode, f -> f));

        for (CouponReviewReq.IssueReq issueReq : issues) {
            CouponFormFieldDO field = fieldMap.get(issueReq.getFieldCode());
            if (field == null) {
                continue;
            }

            CouponWriteOffReviewIssueDO issue = new CouponWriteOffReviewIssueDO();
            issue.setSubmissionId(submission.getId());
            issue.setFieldId(field.getId());
            issue.setIssueCode(issueReq.getIssueCode());
            issue.setIssueMessage(issueReq.getIssueMessage());
            issue.setStatus(CouponConstants.ISSUE_STATUS_OPEN);
            reviewIssueMapper.insert(issue);
        }
    }

    private boolean checkReviewerPermission(Long reviewerUserId, Long activityId) {
        if (UserContextHolder.isSuperAdmin()) {
            return true;
        }

        Long count = reviewerScopeMapper.selectCount(new LambdaQueryWrapper<CouponReviewerScopeDO>()
            .eq(CouponReviewerScopeDO::getReviewerUserId, reviewerUserId)
            .eq(CouponReviewerScopeDO::getStatus, CouponConstants.STATUS_ENABLED)
            .eq(CouponReviewerScopeDO::getIsDeleted, 0)
            .and(w -> w.eq(CouponReviewerScopeDO::getActivityId, 0L)
                .or()
                .eq(CouponReviewerScopeDO::getActivityId, activityId)));

        return count > 0;
    }

    private List<Long> getAllowedActivityIds(Long reviewerUserId) {
        if (UserContextHolder.isSuperAdmin()) {
            return activityMapper.selectList(new LambdaQueryWrapper<CouponActivityDO>().select(CouponActivityDO::getId)
                .eq(CouponActivityDO::getIsDeleted, 0)).stream().map(CouponActivityDO::getId).toList();
        }

        return reviewerScopeMapper.selectList(new LambdaQueryWrapper<CouponReviewerScopeDO>()
            .eq(CouponReviewerScopeDO::getReviewerUserId, reviewerUserId)
            .eq(CouponReviewerScopeDO::getStatus, CouponConstants.STATUS_ENABLED)
            .eq(CouponReviewerScopeDO::getIsDeleted, 0))
            .stream()
            .map(CouponReviewerScopeDO::getActivityId)
            .distinct()
            .toList();
    }

    private void fillWriteOffListResp(PageResp<CouponWriteOffListResp> resp, List<CouponWriteOffDO> records) {
        Set<Long> activityIds = records.stream().map(CouponWriteOffDO::getActivityId).collect(Collectors.toSet());
        Set<Long> templateIds = records.stream().map(CouponWriteOffDO::getTemplateId).collect(Collectors.toSet());

        Map<Long, CouponActivityDO> activityMap = CollUtil.isEmpty(activityIds)
            ? Collections.emptyMap()
            : activityMapper.selectBatchIds(activityIds)
                .stream()
                .collect(Collectors.toMap(CouponActivityDO::getId, a -> a));

        Map<Long, CouponTemplateDO> templateMap = CollUtil.isEmpty(templateIds)
            ? Collections.emptyMap()
            : templateMapper.selectBatchIds(templateIds)
                .stream()
                .collect(Collectors.toMap(CouponTemplateDO::getId, t -> t));

        resp.getList().forEach(item -> {
            CouponActivityDO activity = activityMap.get(item.getActivityId());
            if (activity != null) {
                item.setActivityName(activity.getActivityName());
            }

            CouponTemplateDO template = templateMap.get(item.getTemplateId());
            if (template != null) {
                item.setTemplateName(template.getTemplateName());
                item.setCouponType(template.getCouponType());
                item.setDiscountRate(template.getDiscountRate());
                item.setDiscountAmount(template.getDiscountAmount());
            }

            item.setStatusDesc(getWriteOffStatusDesc(item.getStatus()));

            // 查询提交次数
            Long submissionCount = submissionMapper.selectCount(new LambdaQueryWrapper<CouponWriteOffSubmissionDO>()
                .eq(CouponWriteOffSubmissionDO::getWriteOffId, item.getId()));
            item.setSubmissionCount(submissionCount.intValue());
        });
    }

    private String getWriteOffStatusDesc(String status) {
        return switch (status) {
            case CouponConstants.WRITE_OFF_STATUS_PENDING_AUDIT -> "待审核";
            case CouponConstants.WRITE_OFF_STATUS_APPROVED -> "已通过";
            case CouponConstants.WRITE_OFF_STATUS_REJECTED -> "已驳回";
            case CouponConstants.WRITE_OFF_STATUS_CANCELLED -> "已撤销";
            default -> status;
        };
    }

    private CouponWriteOffDetailResp buildWriteOffDetailResp(CouponWriteOffDO writeOff) {
        CouponWriteOffDetailResp resp = BeanUtil.copyProperties(writeOff, CouponWriteOffDetailResp.class);
        resp.setStatusDesc(getWriteOffStatusDesc(writeOff.getStatus()));

        // 填充活动和模板信息
        CouponActivityDO activity = activityMapper.selectById(writeOff.getActivityId());
        if (activity != null) {
            resp.setActivityName(activity.getActivityName());
        }

        CouponTemplateDO template = templateMapper.selectById(writeOff.getTemplateId());
        if (template != null) {
            resp.setTemplateName(template.getTemplateName());
            resp.setCouponType(template.getCouponType());
            resp.setDiscountRate(template.getDiscountRate());
            resp.setDiscountAmount(template.getDiscountAmount());
            resp.setThresholdAmount(template.getThresholdAmount());
        }

        // 填充当前提交版本详情
        if (writeOff.getCurrentSubmissionId() != null) {
            CouponWriteOffSubmissionDO submission = submissionMapper.selectById(writeOff.getCurrentSubmissionId());
            if (submission != null) {
                CouponWriteOffDetailResp.SubmissionResp submissionResp = BeanUtil
                    .copyProperties(submission, CouponWriteOffDetailResp.SubmissionResp.class);
                submissionResp.setSubmissionId(submission.getId());

                // 查询字段值
                if (template != null && template.getFormTemplateId() != null) {
                    List<CouponFormFieldDO> fields = formFieldMapper
                        .selectList(new LambdaQueryWrapper<CouponFormFieldDO>()
                            .eq(CouponFormFieldDO::getTemplateId, template.getFormTemplateId())
                            .orderByAsc(CouponFormFieldDO::getGroupSort)
                            .orderByAsc(CouponFormFieldDO::getSortNo));

                    Map<Long, CouponFormFieldDO> fieldMap = fields.stream()
                        .collect(Collectors.toMap(CouponFormFieldDO::getId, f -> f));

                    List<CouponWriteOffSubmissionValueDO> values = submissionValueMapper
                        .selectList(new LambdaQueryWrapper<CouponWriteOffSubmissionValueDO>()
                            .eq(CouponWriteOffSubmissionValueDO::getSubmissionId, submission.getId())
                            .orderByAsc(CouponWriteOffSubmissionValueDO::getFieldId, CouponWriteOffSubmissionValueDO::getValueSeq));

                    // 按分组整理
                    Map<String, List<CouponWriteOffDetailResp.FieldValueResp>> groupedValues = new LinkedHashMap<>();
                    for (CouponWriteOffSubmissionValueDO value : values) {
                        CouponFormFieldDO field = fieldMap.get(value.getFieldId());
                        String groupName = (field != null && StrUtil.isNotBlank(field.getGroupName()))
                            ? field.getGroupName()
                            : "默认分组";

                        CouponWriteOffDetailResp.FieldValueResp fieldValue = new CouponWriteOffDetailResp.FieldValueResp();
                        fieldValue.setFieldId(value.getFieldId());
                        fieldValue.setValueSeq(value.getValueSeq());
                        fieldValue.setValue(value.getValueText());
                        fieldValue.setFileId(value.getFileId());

                        if (field != null) {
                            fieldValue.setFieldCode(field.getFieldCode());
                            fieldValue.setFieldName(field.getFieldName());
                            fieldValue.setFieldType(field.getFieldType());
                            fieldValue.setIsRequired(field.getIsRequired());
                            fieldValue.setIsEditable(field.getIsEditable());
                            fieldValue.setSortNo(field.getSortNo());
                        }

                        // 查询文件URL
                        if (value.getFileId() != null) {
                            CouponFileDO file = fileMapper.selectById(value.getFileId());
                            if (file != null) {
                                fieldValue.setFileUrl(file.getUrl());
                            }
                        }

                        groupedValues.computeIfAbsent(groupName, k -> new ArrayList<>()).add(fieldValue);
                    }

                    // 构建分组列表
                    Map<String, Integer> groupNameSortMap = new LinkedHashMap<>();
                    fields.stream()
                        .filter(f -> StrUtil.isNotBlank(f.getGroupName()))
                        .forEach(f -> groupNameSortMap.putIfAbsent(f.getGroupName(), f.getGroupSort()));

                    List<CouponWriteOffDetailResp.FieldGroupResp> groups = new ArrayList<>();
                    groupedValues.forEach((groupName, fieldValues) -> {
                        CouponWriteOffDetailResp.FieldGroupResp group = new CouponWriteOffDetailResp.FieldGroupResp();
                        group.setGroupName(groupName);
                        group.setGroupSort(groupNameSortMap.getOrDefault(groupName, 999));
                        group.setFields(fieldValues);
                        groups.add(group);
                    });
                    groups.sort(Comparator.comparingInt(CouponWriteOffDetailResp.FieldGroupResp::getGroupSort));
                    submissionResp.setGroups(groups);
                }

                // 查询驳回问题
                List<CouponWriteOffReviewIssueDO> issues = reviewIssueMapper
                    .selectList(new LambdaQueryWrapper<CouponWriteOffReviewIssueDO>()
                        .eq(CouponWriteOffReviewIssueDO::getSubmissionId, submission.getId())
                        .orderByDesc(CouponWriteOffReviewIssueDO::getCreatedAt));

                if (CollUtil.isNotEmpty(issues)) {
                    Map<Long, CouponFormFieldDO> fieldMap = formFieldMapper
                        .selectList(new LambdaQueryWrapper<CouponFormFieldDO>()
                            .eq(CouponFormFieldDO::getTemplateId, template.getFormTemplateId()))
                        .stream()
                        .collect(Collectors.toMap(CouponFormFieldDO::getId, f -> f));

                    submissionResp.setIssues(issues.stream().map(issue -> {
                        CouponWriteOffDetailResp.ReviewIssueResp issueResp = BeanUtil
                            .copyProperties(issue, CouponWriteOffDetailResp.ReviewIssueResp.class);
                        CouponFormFieldDO field = fieldMap.get(issue.getFieldId());
                        if (field != null) {
                            issueResp.setFieldCode(field.getFieldCode());
                            issueResp.setFieldName(field.getFieldName());
                        }
                        return issueResp;
                    }).toList());
                }

                resp.setCurrentSubmission(submissionResp);
            }
        }

        return resp;
    }
}
