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

package top.continew.admin.coupon.service.impl;

import org.springframework.stereotype.Service;
import top.continew.admin.coupon.mapper.CouponFormTemplateMapper;
import top.continew.admin.coupon.model.entity.CouponFormTemplateDO;
import top.continew.admin.coupon.service.CouponFormTemplateService;
import top.continew.starter.extension.crud.service.impl.BaseServiceImpl;

/**
 * 核销凭证表单模板服务实现
 */
@Service
public class CouponFormTemplateServiceImpl extends BaseServiceImpl<CouponFormTemplateMapper, CouponFormTemplateDO> implements CouponFormTemplateService {
}
