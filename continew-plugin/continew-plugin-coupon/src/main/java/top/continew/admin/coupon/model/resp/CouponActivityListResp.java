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

import java.time.LocalDateTime;

@Data
@Schema(description = "Activity list item")
public class CouponActivityListResp {

    private Long id;
    private String activityCode;
    private String activityName;
    private String description;
    private LocalDateTime claimStartTime;
    private LocalDateTime claimEndTime;
    private LocalDateTime verifyStartTime;
    private LocalDateTime verifyEndTime;
    private String auditMode;
    private String status;
    private String statusDesc;
}
