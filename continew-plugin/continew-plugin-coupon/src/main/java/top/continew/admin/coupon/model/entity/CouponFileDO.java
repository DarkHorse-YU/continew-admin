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

package top.continew.admin.coupon.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件实体
 */
@Data
@TableName("coupon_file")
public class CouponFileDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 上传用户ID */
    private Long userId;

    /** 存储平台编码 */
    private String storageProvider;

    /** Bucket名称 */
    private String bucketName;

    /** 文件名（存储路径） */
    private String fileName;

    /** 原始文件名 */
    private String originalName;

    /** 访问URL */
    private String url;

    /** MIME类型 */
    private String mimeType;

    /** 文件大小(Byte) */
    private Long fileSize;

    /** SHA256 */
    private String sha256;

    /** 创建时间 */
    private LocalDateTime createdAt;
}
