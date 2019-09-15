package com.leyou.mapper;

import com.leyou.entity.Sku;

import java.util.List;

public interface SkuMapper extends BaseMapper<Sku> {

    void addShus(List<Sku> skus);
}
