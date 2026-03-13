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

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import top.continew.admin.coupon.mapper.CouponTemplateMapper;
import top.continew.admin.coupon.model.query.CouponClaimTemplateQuery;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 抢券模板缓存服务
 */
@Service
@RequiredArgsConstructor
public class CouponClaimTemplateCacheService {

    private static final String CLAIM_TEMPLATE_CACHE_KEY_PREFIX = "coupon:claim:template:";

    private final RedissonClient redissonClient;
    private final CouponTemplateMapper templateMapper;

    public CouponClaimTemplateQuery getClaimTemplate(Long templateId) {
        RBucket<CouponClaimTemplateQuery> bucket = redissonClient.getBucket(buildCacheKey(templateId));
        CouponClaimTemplateQuery cached = bucket.get();
        if (cached != null) {
            return cached;
        }
        CouponClaimTemplateQuery query = templateMapper.selectClaimTemplate(templateId);
        if (query != null) {
            bucket.set(query, Duration.ofMinutes(10).toMillis(), TimeUnit.MILLISECONDS);
        }
        return query;
    }

    public void evict(Long templateId) {
        redissonClient.getBucket(buildCacheKey(templateId)).delete();
    }

    private String buildCacheKey(Long templateId) {
        return CLAIM_TEMPLATE_CACHE_KEY_PREFIX + templateId;
    }
}
