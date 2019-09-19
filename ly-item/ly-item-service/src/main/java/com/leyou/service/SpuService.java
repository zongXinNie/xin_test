package com.leyou.service;

import com.leyou.dto.SpuDTO;
import com.leyou.search.pojo.PageResult;
import com.leyou.search.pojo.SpuQueryPageBean;

public interface SpuService {

   PageResult<SpuDTO> findSpuPage(SpuQueryPageBean spuQueryPageBean);


}
