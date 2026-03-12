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

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponTemplateListResp {

    private Long id;
    private Long activityId;
    private String templateCode;
    private String templateName;
    private String description;

    private String couponType;
    private BigDecimal discountRate;
    private BigDecimal discountAmount;
    private BigDecimal thresholdAmount;

    private Integer totalStock;
    private Integer claimedStock;
    private Integer remainingStock;

    private Integer perUserLimit;
    private Integer userClaimedCount;
    private Long formTemplateId;

    private Boolean canClaim;
    private String cannotClaimReason;

    private LocalDateTime createdAt;
}
