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
import top.continew.admin.coupon.model.entity.CouponTemplateDO;
import top.continew.admin.coupon.model.query.CouponClaimTemplateQuery;
import top.continew.starter.data.mapper.BaseMapper;

/**
 * 券模板 Mapper
 */
@Mapper
public interface CouponTemplateMapper extends BaseMapper<CouponTemplateDO> {

    @Select("""
        SELECT
            t.id AS templateId,
            t.activity_id AS activityId,
            t.template_code AS templateCode,
            t.template_name AS templateName,
            t.description AS description,
            t.coupon_type AS couponType,
            t.discount_rate AS discountRate,
            t.discount_amount AS discountAmount,
            t.threshold_amount AS thresholdAmount,
            t.total_stock AS totalStock,
            t.claimed_stock AS claimedStock,
            t.write_off_stock AS writeOffStock,
            t.per_user_limit AS perUserLimit,
            t.daily_claim_limit AS dailyClaimLimit,
            t.valid_type AS validType,
            t.valid_days AS validDays,
            t.fixed_valid_start_time AS fixedValidStartTime,
            t.fixed_valid_end_time AS fixedValidEndTime,
            t.form_template_id AS formTemplateId,
            t.status AS templateStatus,
            t.sort_no AS sortNo,
            t.version AS version,
            t.rule_json AS templateRuleJson,
            a.claim_start_time AS claimStartTime,
            a.claim_end_time AS claimEndTime,
            a.verify_start_time AS verifyStartTime,
            a.verify_end_time AS verifyEndTime,
            a.audit_mode AS auditMode,
            a.status AS activityStatus,
            a.rule_json AS activityRuleJson
        FROM coupon_template t
        INNER JOIN coupon_activity a ON a.id = t.activity_id
        WHERE t.id = #{templateId}
          AND t.is_deleted = 0
          AND a.is_deleted = 0
        """)
    CouponClaimTemplateQuery selectClaimTemplate(@Param("templateId") Long templateId);
}
