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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动表单返回。
 *
 * @author OpenCode
 * @since 2026/2/28 15:00
 */
@Data
@Schema(description = "活动表单返回")
public class SubsidyFormResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long activityId;
    private String activityCode;
    private String activityName;
    private String auditMode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long templateId;
    private String templateCode;
    private String templateName;
    private Integer templateVersion;

    /** 字段分组列表（按分组返回，支持多步填写）。 */
    private List<GroupResp> groups = new ArrayList<>();

    /**
     * 字段分组。
     */
    @Data
    @Schema(description = "字段分组")
    public static class GroupResp implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        @Schema(description = "分组名称", example = "基本信息")
        private String groupName;

        @Schema(description = "分组排序号", example = "1")
        private Integer groupSort;

        @Schema(description = "分组下的字段列表")
        private List<FieldResp> fields = new ArrayList<>();
    }

    /**
     * 字段信息。
     */
    @Data
    public static class FieldResp implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private Long fieldId;
        private String fieldCode;
        private String fieldName;
        private String fieldType;
        private Integer isRequired;
        private Integer isUserEditable;
        private Integer sortNo;
        private String enumOptions;
        private String validationRule;
        private Integer ocrEnabled;
        private String ocrMappingKey;
    }
}
