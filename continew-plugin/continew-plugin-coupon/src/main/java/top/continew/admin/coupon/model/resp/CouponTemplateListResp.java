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

package top.continew.admin.coupon.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 券模板列表响应
 */
@Data
@Schema(description = "券模板列表响应")
public class CouponTemplateListResp {

    @Schema(description = "模板ID")
    private Long id;

    @Schema(description = "活动ID")
    private Long activityId;

    @Schema(description = "券模板编码")
    private String templateCode;

    @Schema(description = "券名称")
    private String templateName;

    @Schema(description = "券描述")
    private String description;

    @Schema(description = "券类型：DISCOUNT=折扣券，CASH=满减券")
    private String couponType;

    @Schema(description = "折扣率（如0.90表示九折）")
    private BigDecimal discountRate;

    @Schema(description = "减免金额")
    private BigDecimal discountAmount;

    @Schema(description = "门槛金额")
    private BigDecimal thresholdAmount;

    @Schema(description = "总库存")
    private Integer totalStock;

    @Schema(description = "剩余库存")
    private Integer remainingStock;

    @Schema(description = "单用户限领数量")
    private Integer perUserLimit;

    @Schema(description = "当前用户已领数量")
    private Integer userClaimedCount;

    @Schema(description = "是否可领取")
    private Boolean canClaim;

    @Schema(description = "不可领取原因")
    private String cannotClaimReason;
}
