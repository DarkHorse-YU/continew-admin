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
 * 上传文件元数据实体。
 */
@Data
@TableName("subsidy_file")
public class SubsidyFileDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 ID。 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 上传用户 ID。 */
    private Long userId;

    /** 存储服务商编码。 */
    private String storageProvider;

    /** 存储桶名称。 */
    private String bucketName;

    /** 对象 Key。 */
    private String objectKey;

    /** 原始文件名。 */
    private String fileName;

    /** MIME 类型。 */
    private String mimeType;

    /** 文件大小（字节）。 */
    private Long fileSize;

    /** SHA-256 摘要。 */
    private String sha256;

    /** 创建时间。 */
    private LocalDateTime createdAt;
}
