package com.leyou.mapper;

import com.leyou.entity.SpecParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpecParamMapper extends BaseMapper<SpecParam> {
    /**
     * 根据规格分组id查询规格参数
     * @param groupId
     * @return
     */
    List<SpecParam> findSpecParamByGroupId(@Param("groupId")Long groupId);
}
