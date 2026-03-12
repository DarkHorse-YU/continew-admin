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

package top.continew.admin.coupon.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;
import top.continew.admin.coupon.model.entity.CouponUserCouponDO;
import top.continew.admin.coupon.model.query.CouponUserClaimStatsQuery;
import top.continew.starter.data.mapper.BaseMapper;

import java.time.LocalDateTime;

/**
 * 用户券实例 Mapper
 */
@Mapper
public interface CouponUserCouponMapper extends BaseMapper<CouponUserCouponDO> {

    @Select("""
        SELECT
            COUNT(1) AS totalClaimedCount,
            COALESCE(SUM(CASE
                WHEN claim_time >= #{dayStart} AND claim_time < #{dayEnd} THEN 1
                ELSE 0
            END), 0) AS todayClaimedCount
        FROM coupon_user_coupon
        WHERE template_id = #{templateId}
          AND user_id = #{userId}
          AND is_deleted = 0
        """)
    CouponUserClaimStatsQuery selectUserClaimStats(@Param("templateId") Long templateId,
                                                   @Param("userId") Long userId,
                                                   @Param("dayStart") LocalDateTime dayStart,
                                                   @Param("dayEnd") LocalDateTime dayEnd);
}
