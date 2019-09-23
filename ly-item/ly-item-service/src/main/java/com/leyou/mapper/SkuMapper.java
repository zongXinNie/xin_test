package com.leyou.mapper;

import com.leyou.entity.Sku;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SkuMapper extends BaseMapper<Sku> {

    void addShus(List<Sku> skus);

    /**
     * 减少库存
     * @param skuId
     * @param num
     */
    int minusStock(@Param("skuId") Long skuId,@Param("num") Integer num);
}
