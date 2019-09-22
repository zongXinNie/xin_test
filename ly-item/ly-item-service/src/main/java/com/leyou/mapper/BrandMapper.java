package com.leyou.mapper;

import com.leyou.entity.Brand;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends BaseMapper<Brand> {
    /**
     * 添加分类与商品的中间表
     * @param brand
     * @param cids
     */
    void addBrandAndCategory(@Param("brand") Long brand, @Param("cids") String[] cids);

    /**
     * 根据品牌id删除中间表中关联的分类
     * @param brandId
     */
    void deleteCategoryBrandByBrandId(@Param("brandId") Long brandId);

    /**
     * 根据分类查询品牌
     * @param id
     * @return
     */
    List<Brand> findBrandByCtegory(@Param("id") Long id);

}
