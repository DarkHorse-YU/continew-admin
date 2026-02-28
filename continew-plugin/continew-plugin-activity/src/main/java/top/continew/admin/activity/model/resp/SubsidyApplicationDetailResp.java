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

package top.continew.admin.activity.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 申报详情返回。
 *
 * @author OpenCode
 * @since 2026/2/28 15:00
 */
@Data
@Schema(description = "申报详情返回")
public class SubsidyApplicationDetailResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long customerId;
    private String applicationNo;
    private Long activityId;
    private String activityName;
    private Long userId;
    private String currentStatus;
    private Integer rejectCount;
    private BigDecimal finalSubsidyAmount;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private SubmissionResp currentSubmission;

    @Data
    public static class SubmissionResp implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private Long submissionId;
        private Integer submissionNo;
        private String status;
        private LocalDateTime submittedAt;
        private LocalDateTime reviewedAt;
        private Long reviewerId;
        private String reviewComment;
        private List<FieldValueResp> fieldValues = new ArrayList<>();
        private List<ReviewIssueResp> issues = new ArrayList<>();
    }

    @Data
    public static class FieldValueResp implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private Long fieldId;
        private String fieldCode;
        private String fieldName;
        private Integer valueSeq;
        private String valueText;
        private BigDecimal valueNumber;
        private LocalDate valueDate;
        private String valueEnum;
        private String valueJson;
        private Long fileId;
    }

    @Data
    public static class ReviewIssueResp implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private Long id;
        private Long fieldId;
        private String fieldCode;
        private String fieldName;
        private String issueCode;
        private String issueMessage;
        private String status;
        private LocalDateTime createdAt;
    }
}
