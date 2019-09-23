package com.leyou.service;

import com.leyou.dto.SkuDTO;
import com.leyou.dto.SpuDTO;
import com.leyou.dto.SpuDetailDTO;
import com.leyou.pojo.PageResult;

import com.leyou.entity.Spu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GoodsService {

    PageResult<SpuDTO> findSpuPage(String key, Boolean saleable, Integer page, Integer rows);

    void addGoods(HashMap<String, Object> hashMap);

    SpuDetailDTO findSpuDetailBySpuId(Long spuId);

    List<SkuDTO> findSkuBySpuId(Long spuId);

    void editGoods(SpuDTO spuDTO);

    void editSaleable(Spu spu);

    SpuDTO findSpuById(Long id);

    List<SkuDTO> findSkuByIds(List<Long> ids);

    void minusStock(Map<Long, Integer> cartMap);
}
