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

package top.continew.admin.activity.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户端申报分页查询条件。
 *
 * @author OpenCode
 * @since 2026/2/28 15:00
 */
@Data
@Schema(description = "用户端申报分页查询条件")
public class SubsidyMyApplicationQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "当前状态", example = "PENDING")
    private String currentStatus;

    @Schema(description = "创建开始时间", type = "string")
    private LocalDateTime startTime;

    @Schema(description = "创建结束时间", type = "string")
    private LocalDateTime endTime;
}
