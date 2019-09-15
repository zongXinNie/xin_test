package com.leyou.service;

import com.leyou.dto.SpecParamDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpecParamService {
    /**
     * 根据规格分组id查询规格参数
     * @param groupId
     * @return
     */
    List<SpecParamDTO> findSpecParamByGroupId( Long groupId,Long categoryId, Boolean searching);
}
