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

package top.continew.admin.activity.constant;

/**
 * 补贴活动业务常量。
 *
 * @author OpenCode
 * @since 2026/2/28 15:00
 */
public final class SubsidyConstants {

    private SubsidyConstants() {
    }

    public static final String APP_STATUS_DRAFT = "DRAFT";
    public static final String APP_STATUS_PENDING = "PENDING";
    public static final String APP_STATUS_REJECTED = "REJECTED";
    public static final String APP_STATUS_APPROVED = "APPROVED";

    public static final String SUBMISSION_STATUS_SUBMITTED = "SUBMITTED";
    public static final String SUBMISSION_STATUS_UNDER_REVIEW = "UNDER_REVIEW";
    public static final String SUBMISSION_STATUS_REJECTED = "REJECTED";
    public static final String SUBMISSION_STATUS_APPROVED = "APPROVED";

    public static final String ISSUE_STATUS_OPEN = "OPEN";
    public static final String ISSUE_STATUS_FIXED = "FIXED";

    public static final String AUDIT_MODE_NONE = "NONE";
    public static final String AUDIT_MODE_MANUAL = "MANUAL";
}
