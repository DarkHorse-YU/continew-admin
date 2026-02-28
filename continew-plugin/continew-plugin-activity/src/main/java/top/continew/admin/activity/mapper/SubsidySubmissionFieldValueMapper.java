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

package top.continew.admin.activity.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.continew.admin.activity.model.entity.SubsidySubmissionFieldValueDO;
import top.continew.starter.data.mapper.BaseMapper;

import java.util.List;

/**
 * 补贴提交字段值 Mapper。
 *
 * @author OpenCode
 * @since 2026/2/28 15:00
 */
@Mapper
public interface SubsidySubmissionFieldValueMapper extends BaseMapper<SubsidySubmissionFieldValueDO> {

    /**
     * 按购车类型查询对应的提交版本 ID 列表。
     *
     * @param fieldIds 购车类型字段 ID 列表
     * @param carType 购车类型
     * @return 提交版本 ID 列表
     */
    @Select({"<script>",
        "select distinct submission_id from subsidy_submission_field_value",
        "where field_id in",
        "<foreach collection='fieldIds' item='item' open='(' close=')' separator=','>#{item}</foreach>",
        "and (value_enum = #{carType} or value_text = #{carType})",
        "</script>"})
    List<Long> selectSubmissionIdsByFieldIdsAndCarType(@Param("fieldIds") List<Long> fieldIds,
                                                        @Param("carType") String carType);
}
