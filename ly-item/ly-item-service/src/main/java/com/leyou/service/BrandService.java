package com.leyou.service;

import com.leyou.dto.BrandDTO;
import com.leyou.entity.Brand;
import com.leyou.pojo.PageResult;
import com.leyou.pojo.QueryPageBean;


import java.util.List;

public interface BrandService {
    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<BrandDTO> findBrand(QueryPageBean queryPageBean);

    /**
     * 添加品牌
     * @param brand
     * @param cids
     */
    void addBrand(Brand brand,String[] cids);

    /**
     * 修改品牌
     * @param brand
     * @param cids
     */
    void editBrand(Brand brand,String[] cids);

    /**
     * 删除品牌
     * @param brand
     */
    void deleteBrand(Brand brand);

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    BrandDTO findBrandById(Long id);
    /**
     * 根据分类查询品牌
     *
     * @param id
     * @return
     */
    List<BrandDTO> findBrandByCtegory(Long id);

    /**
     * 根据品牌id集合查询品牌
     * @param ids
     * @return
     */
    List<BrandDTO> findBrandByBrandIds(List<Long> ids);
}
