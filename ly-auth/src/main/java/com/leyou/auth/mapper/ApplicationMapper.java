package com.leyou.auth.mapper;

import com.leyou.auth.entity.Application;
import com.leyou.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;


/**
 * @author Nie ZongXin
 * @date 2019/9/19 22:39
 */
public interface ApplicationMapper extends BaseMapper<Application> {


    /**
     * 查询中间表
     * @param id
     * @return
     */
    List<Long> findTargetIdList(@Param("id") Long id);
}
