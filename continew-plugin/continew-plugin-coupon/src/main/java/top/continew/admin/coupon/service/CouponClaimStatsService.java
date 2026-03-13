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

import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import top.continew.admin.coupon.mapper.CouponUserCouponMapper;
import top.continew.admin.coupon.model.query.CouponUserClaimStatsQuery;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 用户领券统计缓存服务
 */
@Service
@RequiredArgsConstructor
public class CouponClaimStatsService {

    private static final String USER_TOTAL_CLAIM_KEY_PREFIX = "coupon:claim:user:total:";
    private static final String USER_DAILY_CLAIM_KEY_PREFIX = "coupon:claim:user:daily:";

    private final RedissonClient redissonClient;
    private final CouponUserCouponMapper userCouponMapper;

    public CouponUserClaimStatsQuery getUserClaimStats(Long templateId,
                                                       Long userId,
                                                       LocalDateTime dayStart,
                                                       LocalDateTime dayEnd) {
        String totalKey = buildUserTotalClaimKey(templateId, userId);
        String dailyKey = buildUserDailyClaimKey(templateId, userId, dayStart.toLocalDate());
        RBucket<Long> totalBucket = redissonClient.getBucket(totalKey);
        RBucket<Long> dailyBucket = redissonClient.getBucket(dailyKey);

        Long totalClaimedCount = totalBucket.get();
        Long todayClaimedCount = dailyBucket.get();
        if (totalClaimedCount != null && todayClaimedCount != null) {
            CouponUserClaimStatsQuery query = new CouponUserClaimStatsQuery();
            query.setTotalClaimedCount(totalClaimedCount);
            query.setTodayClaimedCount(todayClaimedCount);
            return query;
        }

        CouponUserClaimStatsQuery dbStats = userCouponMapper.selectUserClaimStats(templateId, userId, dayStart, dayEnd);
        long total = dbStats == null || dbStats.getTotalClaimedCount() == null ? 0L : dbStats.getTotalClaimedCount();
        long today = dbStats == null || dbStats.getTodayClaimedCount() == null ? 0L : dbStats.getTodayClaimedCount();
        totalBucket.trySet(total);
        dailyBucket.trySet(today);
        dailyBucket.expire(Duration.between(LocalDateTime.now(), dayEnd.plusMinutes(5)));

        CouponUserClaimStatsQuery query = new CouponUserClaimStatsQuery();
        query.setTotalClaimedCount(total);
        query.setTodayClaimedCount(today);
        return query;
    }

    public void incrementAfterClaimCommit(Long templateId, Long userId, LocalDate claimDate) {
        Runnable task = () -> {
            redissonClient.getAtomicLong(buildUserTotalClaimKey(templateId, userId)).incrementAndGet();
            String dailyKey = buildUserDailyClaimKey(templateId, userId, claimDate);
            redissonClient.getAtomicLong(dailyKey).incrementAndGet();
            long ttlMillis = Math.max(Duration.between(LocalDateTime.now(), claimDate.plusDays(1)
                .atStartOfDay()
                .plusMinutes(5)).toMillis(), 1L);
            redissonClient.getBucket(dailyKey).expire(ttlMillis, TimeUnit.MILLISECONDS);
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

    private String buildUserTotalClaimKey(Long templateId, Long userId) {
        return USER_TOTAL_CLAIM_KEY_PREFIX + templateId + ":" + userId;
    }

    private String buildUserDailyClaimKey(Long templateId, Long userId, LocalDate claimDate) {
        return USER_DAILY_CLAIM_KEY_PREFIX + templateId + ":" + userId + ":" + claimDate;
    }
}
