package com.leyou.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.ExceptionEnum;
import com.leyou.constants.AbstractMQConstants;
import com.leyou.dto.*;
import com.leyou.entity.*;
import com.leyou.exception.LyException;
import com.leyou.mapper.SkuMapper;
import com.leyou.mapper.SpuDetailMapper;
import com.leyou.mapper.SpuMapper;
import com.leyou.pojo.PageResult;
import com.leyou.service.BrandService;
import com.leyou.service.CategoryService;
import com.leyou.service.GoodsService;
import com.leyou.utils.BeanHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Nie ZongXin
 * @date 2019/9/10 15:25
 */
@Slf4j
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 查询商品列表分页
     *
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult<SpuDTO> findSpuPage(String key, Boolean saleable, Integer page, Integer rows) {

        ArrayList<SpuDTO> spuDTOArrayList = new ArrayList<>();
        PageHelper.startPage(page, rows);

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%");
        }
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
        example.setOrderByClause("update_time DESC");
        List<Spu> spusList = spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo = new PageInfo<>(spusList);

        if (CollectionUtils.isEmpty(spusList)) {
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        List<SpuDTO> spuDTOS = BeanHelper.copyWithCollection(spusList, SpuDTO.class);

        handleCategoryAndBrandName(spuDTOS);


        return new PageResult<>(pageInfo.getTotal(), spuDTOS);
    }

    /**
     * 添加商品
     *
     * @param hashMap
     */
    @Transactional
    @Override
    public void addGoods(HashMap<String, Object> hashMap) {

        if (hashMap == null) {
            throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        Spu spu = JSON.parseObject(JSON.toJSONString(hashMap), Spu.class);
        SpuDetail spuDetail = JSON.parseObject(JSON.toJSONString(hashMap.get("spuDetail")), SpuDetail.class);
        List<Sku> skus = JSON.parseArray(JSON.toJSONString(hashMap.get("skus")), Sku.class);
//        默认下架
        spu.setSaleable(false);

        spuMapper.insertSelective(spu);

        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insertSelective(spuDetail);
        for (Sku sku : skus) {
            sku.setSpuId(spu.getId());
        }
        skuMapper.insertList(skus);

    }

    /**
     * 根据spuId查询参数细节
     *
     * @param spuId
     * @return
     */
    @Override
    public SpuDetailDTO findSpuDetailBySpuId(Long spuId) {
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(spuId);
        SpuDetail spuDetailById = spuDetailMapper.selectByPrimaryKey(spuDetail);
        if (spuDetailById == null) {
            throw new LyException(ExceptionEnum.SPEC_NOT_FOUND);
        }
        return BeanHelper.copyProperties(spuDetailById, SpuDetailDTO.class);

    }

    /**
     * 根据id查询规格参数
     *
     * @param spuId
     * @return
     */
    @Override
    public List<SkuDTO> findSkuBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skus = skuMapper.select(sku);
        if (skus == null) {
            throw new LyException(ExceptionEnum.SPEC_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(skus, SkuDTO.class);
    }

    /**
     * 修改商品
     *
     * @param spuDTO
     */
    @Transactional
    @Override
    public void editGoods(SpuDTO spuDTO) {
        if (spuDTO == null) {
            throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        Spu spu = BeanHelper.copyProperties(spuDTO, Spu.class);
//        默认下架
        spu.setSaleable(null);
        spu.setCreateTime(null);
        spuMapper.updateByPrimaryKeySelective(spu);

        SpuDetail spuDetail = BeanHelper.copyProperties(spuDTO.getSpuDetail(), SpuDetail.class);
        spuDetail.setUpdateTime(new Date());
        spuDetailMapper.updateByPrimaryKeySelective(spuDetail);


        List<Sku> skus = BeanHelper.copyWithCollection(spuDTO.getSkus(), Sku.class);
        for (Sku sku : skus) {
            sku.setSpuId(spu.getId());
            sku.setEnable(false);
        }
//        删除原有的规格参数
        Example example = new Example(Sku.class);
        example.createCriteria().andEqualTo("spuId", spu.getId());
        skuMapper.deleteByExample(example);
//     批量添加添加新的
        skuMapper.insertList(skus);


    }

    /**
     * 上下架商品
     *
     * @param spu
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editSaleable(Spu spu) {
        if (spu == null) {
            throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        spuMapper.editSaleable(spu);
        Sku sku = new Sku();
        sku.setEnable(spu.getSaleable());
        Example example = new Example(Sku.class);
        example.createCriteria().andEqualTo("spuId", spu.getId());
        skuMapper.updateByExampleSelective(sku, example);

//        消息通讯
        String stand = spu.getSaleable() ? AbstractMQConstants.RoutingKey.ITEM_UP_KEY : AbstractMQConstants.RoutingKey.ITEM_DOWN_KEY;
        amqpTemplate.convertAndSend(AbstractMQConstants.Exchange.ITEM_EXCHANGE_NAME, stand, spu.getId());

    }

    /**
     * 根据商品Id查询商品集
     *
     * @param id
     * @return
     */
    @Override
    public SpuDTO findSpuById(Long id) {


        Spu spu = spuMapper.selectByPrimaryKey(id);
//        spuDetail和skus
        if (spu == null) {
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }

        SpuDTO spuDTO = BeanHelper.copyProperties(spu, SpuDTO.class);
        spuDTO.setSpuDetail(BeanHelper.copyProperties(findSpuDetailBySpuId(id), SpuDetailDTO.class));
        spuDTO.setSkus(BeanHelper.copyWithCollection(findSkuBySpuId(id), SkuDTO.class));
        return spuDTO;
    }

    /**
     * 根据ID集合查询sku
     * @param ids
     * @return
     */
    @Override
    public List<SkuDTO> findSkuByIds(List<Long> ids) {
        List<Sku> skus = skuMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(skus)){
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(skus,SkuDTO.class);
    }

    /**
     * 根据售卖信息减少库存
     * @param cartMap
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void minusStock(Map<Long, Integer> cartMap) {
        for (Map.Entry<Long, Integer> entry : cartMap.entrySet()) {
            try {
                skuMapper.minusStock(entry.getKey(), entry.getValue());
                log.info("servic层减少库存执行");
            } catch (Exception e) {
                throw new LyException(ExceptionEnum.MINUS_STOCK_FALL);
            }

        }
    }

    private void handleCategoryAndBrandName(List<SpuDTO> spuDTOS) {
        for (SpuDTO spuDTO : spuDTOS) {
            String categoryName = categoryService.findCategoryByIds(spuDTO.getCategoryIds())
                    .stream().map(CategoryDTO::getName).
                            collect(Collectors.joining("/"));
            BrandDTO brand = brandService.findBrandById(spuDTO.getBrandId());

            spuDTO.setCategoryName(categoryName);
            spuDTO.setBrandName(brand.getName());
        }
    }


}
