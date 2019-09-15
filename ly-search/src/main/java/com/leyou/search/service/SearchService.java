package com.leyou.search.service;

import com.leyou.dto.SpuDTO;
import com.leyou.pojo.PageResult;
import com.leyou.search.dto.GoodsDTO;
import com.leyou.search.dto.SearchRequest;
import com.leyou.search.pojo.Goods;

import java.util.List;
import java.util.Map;


public interface SearchService {

    Goods buildGoods(SpuDTO spuDTO);

    PageResult<GoodsDTO> search(SearchRequest searchRequest);

    Map<String, List<?>> queryFilter(SearchRequest searchRequest);


}
