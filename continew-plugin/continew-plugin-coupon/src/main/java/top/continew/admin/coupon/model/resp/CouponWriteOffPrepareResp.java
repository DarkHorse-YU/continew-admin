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
import java.time.LocalDateTime;
import java.util.List;

/**
 * 核销前查询响应（供前端判断是否需要填写凭证）
 */
@Data
@Schema(description = "核销前查询响应")
public class CouponWriteOffPrepareResp {

    @Schema(description = "是否可以核销")
    private Boolean canWriteOff;

    @Schema(description = "不可核销原因")
    private String cannotReason;

    @Schema(description = "券实例ID")
    private Long claimId;

    @Schema(description = "券码")
    private String couponNo;

    @Schema(description = "券名称")
    private String templateName;

    @Schema(description = "券类型")
    private String couponType;

    @Schema(description = "折扣率")
    private BigDecimal discountRate;

    @Schema(description = "减免金额")
    private BigDecimal discountAmount;

    @Schema(description = "门槛金额")
    private BigDecimal thresholdAmount;

    @Schema(description = "领券用户ID")
    private Long userId;

    @Schema(description = "活动ID")
    private Long activityId;

    @Schema(description = "活动名称")
    private String activityName;

    @Schema(description = "是否需要凭证")
    private Boolean requireForm;

    @Schema(description = "审核模式：NONE=免审，MANUAL=人工审核")
    private String auditMode;

    @Schema(description = "表单模板信息（需要凭证时返回）")
    private FormTemplateResp formTemplate;

    @Data
    @Schema(description = "表单模板响应")
    public static class FormTemplateResp {

        @Schema(description = "模板ID")
        private Long templateId;

        @Schema(description = "模板名称")
        private String templateName;

        @Schema(description = "字段分组列表")
        private List<CouponTemplateDetailResp.FormTemplateResp.GroupResp> groups;
    }
}
