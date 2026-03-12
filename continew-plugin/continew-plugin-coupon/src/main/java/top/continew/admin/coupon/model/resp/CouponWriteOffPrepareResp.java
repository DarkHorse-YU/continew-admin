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
import java.util.List;

@Data
public class CouponWriteOffPrepareResp {

    private Boolean canWriteOff;
    private String cannotReason;

    private Long claimId;
    private String couponNo;
    private String qrToken;

    private Long activityId;
    private String activityName;
    private Long templateId;
    private String templateName;

    private String couponType;
    private BigDecimal discountRate;
    private BigDecimal discountAmount;
    private BigDecimal thresholdAmount;

    private Long userId;
    private LocalDateTime validStartTime;
    private LocalDateTime validEndTime;

    private String auditMode;
    private Boolean requireForm;
    private FormTemplateResp formTemplate;

    @Data
    public static class FormTemplateResp {
        private Long templateId;
        private String templateName;
        private List<CouponTemplateDetailResp.FormTemplateResp.GroupResp> groups;
    }
}
