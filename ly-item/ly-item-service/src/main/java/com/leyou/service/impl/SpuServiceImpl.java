package com.leyou.service.impl;


import com.github.pagehelper.PageHelper;

import com.github.pagehelper.PageInfo;
import com.leyou.common.ExceptionEnum;
import com.leyou.dto.SkuDTO;
import com.leyou.dto.SpuDTO;
import com.leyou.dto.SpuDetailDTO;
import com.leyou.entity.*;
import com.leyou.exception.LyException;
import com.leyou.mapper.*;
import com.leyou.search.pojo.PageResult;
import com.leyou.search.pojo.SpuQueryPageBean;
import com.leyou.service.SpuService;
import com.leyou.utils.BeanHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @author Nie ZongXin
 * @date 2019/9/10 11:41
 */
@Service
@Transactional
public class SpuServiceImpl implements SpuService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;

    /**
     * 查询商品列表
     *
     * @param spuQueryPageBean
     * @return
     */
    @Override
    public PageResult<SpuDTO> findSpuPage(SpuQueryPageBean spuQueryPageBean) {
        ArrayList<SpuDTO> spuDTOArrayList = new ArrayList<>();
        PageHelper.startPage(spuQueryPageBean.getPage(), spuQueryPageBean.getRows());

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(spuQueryPageBean.getKey())) {
            criteria.orLike("name", "%" + spuQueryPageBean.getKey() + "%");
        }
        if (spuQueryPageBean.getSaleable() != null) {
            criteria.orEqualTo("saleable", spuQueryPageBean.getSaleable());
        }
        List<Spu> spusList = spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo = new PageInfo<>(spusList);

        if (CollectionUtils.isEmpty(spusList)) {
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }


        for (Spu spu : spusList) {
            Category categoryOne = new Category();
            categoryOne.setId(spu.getCid1());
            categoryOne = categoryMapper.selectByPrimaryKey(categoryOne);

            Category categoryTwo = new Category();
            categoryTwo.setId(spu.getCid2());
            categoryTwo = categoryMapper.selectByPrimaryKey(categoryTwo);

            Category categoryThree = new Category();
            categoryThree.setId(spu.getCid3());
            categoryThree = categoryMapper.selectByPrimaryKey(categoryThree);

            String categoryName = categoryOne.getName() + "/" + categoryTwo.getName() + "/" + categoryThree.getName();

            Brand brand = new Brand();
            brand.setId(spu.getBrandId());
            brand = brandMapper.selectByPrimaryKey(brand);
            SpuDTO spuDTO = new SpuDTO();
            spuDTO.setId(spu.getId());
            spuDTO.setName(spu.getName());
            spuDTO.setCategoryName(categoryName);
            spuDTO.setBrandName(brand.getName());

            spuDTOArrayList.add(spuDTO);
        }
        PageResult<SpuDTO> pageResult = new PageResult<>();
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setItems(spuDTOArrayList);
        return pageResult;
    }


}
