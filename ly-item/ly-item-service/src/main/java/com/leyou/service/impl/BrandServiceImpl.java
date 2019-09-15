package com.leyou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.ExceptionEnum;
import com.leyou.dto.BrandDTO;
import com.leyou.entity.Brand;
import com.leyou.pojo.PageResult;
import com.leyou.pojo.QueryPageBean;
import com.leyou.exception.LyException;
import com.leyou.mapper.BrandMapper;
import com.leyou.service.BrandService;
import com.leyou.utils.BeanHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author Nie ZongXin
 * @date 2019/9/7 21:30
 */
@Service
@Slf4j
@Transactional
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<BrandDTO> findBrand(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getPage(), queryPageBean.getRows());
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
//        模糊查询
        if (StringUtils.isNotBlank(queryPageBean.getKey())) {
//            criteria.orLike("name", "%" + queryPageBean.getKey() + "%").orLike("letter", "%" + queryPageBean.getKey() + "%");
            criteria.orLike("name", "%" + queryPageBean.getKey() + "%").orEqualTo("letter", queryPageBean.getKey().toUpperCase());
        }
//        排序
        if (StringUtils.isNotBlank(queryPageBean.getSortBy())) {
            String order = queryPageBean.getSortBy() + (queryPageBean.getDesc() ? " DESC" : " ASC");
            example.setOrderByClause(order);
        }

        List<Brand> brands = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(brands)) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
//        int i = 1 / 0;
//        解析分页结果
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
//        转换
        List<BrandDTO> brandDTOS = BeanHelper.copyWithCollection(brands, BrandDTO.class);
        return new PageResult<BrandDTO>(pageInfo.getTotal(), brandDTOS);

//        另一种方式的查询解析
     /*     PageResult<Brand> brandPageResult = new PageResult<>();
     Page<Brand> brandPage = (Page<Brand>) brandMapper.selectByExample(example);
        brandPageResult.setTotal(brandPage.getTotal());
        brandPageResult.setItems(brandPage.getResult());
//        brandPageResult.setTotalPage(totalPage);

        */


    }

    /**
     * 添加品牌
     *
     * @param brand
     * @param cids
     */
    @Override
    public void addBrand(Brand brand, String[] cids) {
        if (brand == null || cids == null) {
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
       /* brand.setCreateTime(new Date());
        brand.setUpdateTime(new Date());*/

        brandMapper.insertSelective(brand);
//            异常测试
//            int i=1/0;
        brandMapper.addBrandAndCategory(brand.getId(), cids);


    }

    /**
     * 修改品牌
     *
     * @param brand
     * @param cids
     */
    @Override
    public void editBrand(Brand brand, String[] cids) {
        if (brand == null || cids == null) {
            throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
        brand.setUpdateTime(new Date());
//      删除中间表关联
        brandMapper.deleteCategoryBrandByBrandId(brand.getId());
        brandMapper.updateByPrimaryKey(brand);
        brandMapper.addBrandAndCategory(brand.getId(),cids);

    }

    /**
     * 删除品牌
     * @param brand
     */
    @Override
    public void deleteBrand(Brand brand) {
        if (brand==null||brand.getId()==null){
            throw new LyException(ExceptionEnum.DELETE_OPERATION_FAIL);
        }
        brandMapper.delete(brand);
        brandMapper.deleteCategoryBrandByBrandId(brand.getId());
    }

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @Override
    public BrandDTO findBrandById(Long id) {
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (brand==null){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return BeanHelper.copyProperties(brand,BrandDTO.class);
    }
    /**
     * 根据分类查询品牌
     *
     * @param id
     * @return
     */
    @Override
    public List<BrandDTO> findBrandByCtegory(Long id) {
        List<Brand> brands = brandMapper.findBrandByCtegory(id);
        if (CollectionUtils.isEmpty(brands)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(brands,BrandDTO.class);
    }

    /**
     * 根据品牌id集合查询品牌
     * @param ids
     * @return
     */
    @Override
    public List<BrandDTO> findBrandByBrandIds(List<Long> ids) {
        List<Brand> brands = brandMapper.selectByIdList(ids);
        if (brands==null){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(brands,BrandDTO.class);
    }


}
