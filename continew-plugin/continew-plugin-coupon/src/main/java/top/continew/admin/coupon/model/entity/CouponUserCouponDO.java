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
 * 用户券实例实体
 */
@Data
@TableName("coupon_user_coupon")
public class CouponUserCouponDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 活动ID */
    private Long activityId;

    /** 券模板ID */
    private Long templateId;

    /** 券码（支持手输核销) */
    private String couponNo;

    /** 二维码令牌（支持扫码核销) */
    private String qrToken;

    /** 用户ID */
    private Long userId;

    /** 抢券时间 */
    private LocalDateTime claimTime;

    /** 生效时间 */
    private LocalDateTime validStartTime;

    /** 失效时间 */
    private LocalDateTime validEndTime;

    /** 状态：UNUSED=未使用，LOCKED=锁定中, PENDING_AUDIT=待审核, APPROVED=审核通过, REJECTED=审核驳回, EXPIRED=已过期, CANCELLED=已作废 */
    private String status;

    /** 核销记录ID */
    private Long writeOffId;

    /** 乐观锁版本 */
    private Integer version;

    /** 是否删除：0=否,1=是 */
    private Integer isDeleted;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
