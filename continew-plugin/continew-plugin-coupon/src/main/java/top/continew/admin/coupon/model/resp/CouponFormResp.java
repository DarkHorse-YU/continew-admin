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

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CouponFormResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long activityId;
    private String activityCode;
    private String activityName;
    private String auditMode;
    private Long templateId;
    private String templateCode;
    private String templateName;
    private Integer templateVersion;
    private Long formTemplateId;

    private List<GroupResp> groups = new ArrayList<>();

    @Data
    public static class GroupResp implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private String groupName;
        private Integer groupSort;
        private List<FieldResp> fields = new ArrayList<>();
    }

    @Data
    public static class FieldResp implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private Long fieldId;
        private String fieldCode;
        private String fieldName;
        private String fieldType;
        private Integer isRequired;
        private Integer isEditable;
        private Integer sortNo;
        private String enumOptions;
        private String validationRule;
        private Integer ocrEnabled;
        private String ocrMappingKey;
    }
}
