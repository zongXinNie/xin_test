package com.leyou.mapper;

import com.leyou.entity.Spu;


public interface SpuMapper extends BaseMapper<Spu> {
    /**
     * 下架商品
     * @param spu
     */
    void  editSaleable(Spu spu);
}
