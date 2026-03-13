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

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import top.continew.admin.coupon.mapper.CouponTemplateMapper;
import top.continew.admin.coupon.model.entity.CouponTemplateDO;

import java.util.Set;

/**
 * 优惠券库存异步回写服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponStockSyncService {

    private static final String STOCK_KEY_PREFIX = "coupon:stock:";
    private static final String PENDING_SYNC_KEY_PREFIX = "coupon:stock:sync:";
    private static final String FLUSH_LOCK_KEY_PREFIX = "coupon:stock:flush:lock:";
    private static final String DIRTY_TEMPLATE_SET_KEY = "coupon:stock:sync:dirty";

    private final RedissonClient redissonClient;
    private final CouponTemplateMapper templateMapper;

    public long getRemainingStock(CouponTemplateDO template) {
        long dbRemaining = Math.max((long)template.getTotalStock() - template.getClaimedStock(), 0L);
        RAtomicLong stockCounter = redissonClient.getAtomicLong(buildStockKey(template.getId()));
        if (stockCounter.isExists()) {
            return Math.max(stockCounter.get(), 0L);
        }
        long pendingDelta = getPendingSyncCounter(template.getId()).get();
        return Math.max(dbRemaining - pendingDelta, 0L);
    }

    public String buildStockKey(Long templateId) {
        return STOCK_KEY_PREFIX + templateId;
    }

    public void markPendingSyncAfterCommit(Long templateId) {
        Runnable task = () -> {
            getPendingSyncCounter(templateId).incrementAndGet();
            redissonClient.<String>getSet(DIRTY_TEMPLATE_SET_KEY).add(String.valueOf(templateId));
        };
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    task.run();
                }
            });
            return;
        }
        task.run();
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 3000)
    public void flushPendingClaimedStock() {
        Set<String> dirtyTemplateIds = redissonClient.<String>getSet(DIRTY_TEMPLATE_SET_KEY).readAll();
        if (dirtyTemplateIds.isEmpty()) {
            return;
        }
        for (String templateIdText : dirtyTemplateIds) {
            long templateId;
            try {
                templateId = Long.parseLong(templateIdText);
            } catch (NumberFormatException ex) {
                redissonClient.<String>getSet(DIRTY_TEMPLATE_SET_KEY).remove(templateIdText);
                log.warn("库存回写发现非法模板 ID: {}", templateIdText);
                continue;
            }
            flushOneTemplate(templateId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void flushOneTemplate(Long templateId) {
        RLock flushLock = redissonClient.getLock(FLUSH_LOCK_KEY_PREFIX + templateId);
        boolean locked = false;
        try {
            locked = flushLock.tryLock();
            if (!locked) {
                return;
            }
            RAtomicLong pendingCounter = getPendingSyncCounter(templateId);
            long delta = pendingCounter.get();
            if (delta <= 0) {
                redissonClient.<String>getSet(DIRTY_TEMPLATE_SET_KEY).remove(String.valueOf(templateId));
                if (delta < 0) {
                    pendingCounter.set(0);
                }
                return;
            }

            int updated = templateMapper.update(null, new LambdaUpdateWrapper<CouponTemplateDO>()
                .eq(CouponTemplateDO::getId, templateId)
                .eq(CouponTemplateDO::getIsDeleted, 0)
                .setSql("claimed_stock = claimed_stock + " + delta)
                .setSql("version = version + 1"));
            if (updated <= 0) {
                log.warn("库存回写失败，稍后重试: templateId={}, delta={}", templateId, delta);
                return;
            }

            long remain = pendingCounter.addAndGet(-delta);
            if (remain <= 0) {
                redissonClient.<String>getSet(DIRTY_TEMPLATE_SET_KEY).remove(String.valueOf(templateId));
                if (remain < 0) {
                    pendingCounter.set(0);
                }
            }
        } finally {
            if (locked) {
                flushLock.unlock();
            }
        }
    }

    private RAtomicLong getPendingSyncCounter(Long templateId) {
        return redissonClient.getAtomicLong(PENDING_SYNC_KEY_PREFIX + templateId);
    }
}
