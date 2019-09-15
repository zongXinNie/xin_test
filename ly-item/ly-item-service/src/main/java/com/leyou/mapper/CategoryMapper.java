package com.leyou.mapper;

import com.leyou.entity.Category;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends BaseMapper<Category> {
    /**
     * 根据品牌id查询分类
     * @param brandId
     * @return
     */
    List<Category> findCategoryByBrandId(@Param("brandId") Long brandId);
}
