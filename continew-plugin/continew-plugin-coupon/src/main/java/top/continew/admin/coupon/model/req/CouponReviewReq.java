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

package top.continew.admin.coupon.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 核销审核请求参数
 */
@Data
@Schema(description = "核销审核请求")
public class CouponReviewReq {

    /** 是否通过审核 */
    @NotNull(message = "approved can not be null")
    private Boolean approved;

    /** 审核意见 */
    private String reviewComment;

    /** 驳回问题列表 */
    private List<IssueReq> issues;

    /**
     * 驳回问题项
     */
    @Data
    public static class IssueReq {

        /** 字段编码 */
        private String fieldCode;

        /** 问题编码 */
        private String issueCode;

        /** 问题说明 */
        private String issueMessage;
    }
}
