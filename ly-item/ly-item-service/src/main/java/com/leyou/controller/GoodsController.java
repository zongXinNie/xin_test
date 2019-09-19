package com.leyou.controller;

import com.leyou.dto.SkuDTO;
import com.leyou.dto.SpuDTO;
import com.leyou.dto.SpuDetailDTO;
import com.leyou.entity.*;
import com.leyou.pojo.PageResult;
import com.leyou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author Nie ZongXin
 * @date 2019/9/10 15:18
 */
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 查询商品列表分页
     *
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuDTO>> findSpuPage(@RequestParam(name = "key", required = false) String key,
                                                          @RequestParam(name = "saleable", required = false) Boolean saleable,
                                                          @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                          @RequestParam(name = "rows", defaultValue = "5") Integer rows) {

        return ResponseEntity.ok(goodsService.findSpuPage(key, saleable, page, rows));
    }

    /**
     * 增加商品
     *
     * @param hashMap
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity addGoods(@RequestBody HashMap<String, Object> hashMap) {
        goodsService.addGoods(hashMap);
        return ResponseEntity.ok().build();
    }

    /**
     * 修改商品
     *
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity editGoods(@RequestBody SpuDTO spuDTO) {
        goodsService.editGoods(spuDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据spuId查询参数细节
     *
     * @param id
     * @return
     */
    @GetMapping("spu/detail")
    public ResponseEntity<SpuDetailDTO> findSpuDetailBySpuId(@RequestParam("id") Long id) {
        return ResponseEntity.ok(goodsService.findSpuDetailBySpuId(id));
    }

    /**
     * 根据id查询规格参数
     *
     * @param id
     * @return
     */
    @GetMapping("sku/of/spu")
    public ResponseEntity<List<SkuDTO>> findSkuBySpuId(@RequestParam("id") Long id) {
        return ResponseEntity.ok(goodsService.findSkuBySpuId(id));
    }

    /**
     * 下架商品
     *
     * @param spu
     * @return
     */
    @PutMapping("spu/saleable")
    public ResponseEntity saleable(Spu spu) {
        goodsService.editSaleable(spu);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据商品集id查询
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    public ResponseEntity<SpuDTO> findSpuById(@PathVariable("id") Long id){
        return ResponseEntity.ok(goodsService.findSpuById(id));
    }




}
