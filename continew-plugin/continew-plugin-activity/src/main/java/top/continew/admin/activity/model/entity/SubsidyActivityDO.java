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

package top.continew.admin.activity.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 补贴活动实体。
 */
@Data
@TableName("subsidy_activity")
public class SubsidyActivityDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 ID。 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 客户 ID（业务隔离维度）。 */
    private Long customerId;

    /** 活动编码（唯一）。 */
    private String activityCode;

    /** 活动名称。 */
    private String activityName;

    /** 活动封面图片 URL。 */
    private String cover;

    /** 绑定模板 ID。 */
    private Long templateId;

    /** 审核模式：NONE/MANUAL/AUTO。 */
    private String auditMode;

    /** 状态：1 启用 0 停用。 */
    private Integer status;

    /** 活动开始时间。 */
    private LocalDateTime startTime;

    /** 活动结束时间。 */
    private LocalDateTime endTime;

    /** 创建时间。 */
    private LocalDateTime createdAt;

    /** 更新时间。 */
    private LocalDateTime updatedAt;
}
