package com.leyou.item.client;

import com.leyou.dto.*;
import com.leyou.pojo.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "item-service")
public interface ItemClient {

    /**
     * 根据品牌ID查询品牌
     *
     * @param id
     * @return
     */
    @GetMapping("/brand/{id}")
    BrandDTO findBrandById(@PathVariable(value = "id") Long id);

    /**
     * 根据多个分类ID查询分类
     *
     * @param ids
     * @return
     */
    @GetMapping("/category/list")
    List<CategoryDTO> findCategoryByIds(@RequestParam(name = "ids") List<Long> ids);

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
    PageResult<SpuDTO> findSpuPage(@RequestParam(name = "key", required = false) String key,
                                   @RequestParam(name = "saleable", required = false) Boolean saleable,
                                   @RequestParam(name = "page", defaultValue = "0") Integer page,
                                   @RequestParam(name = "rows", defaultValue = "5") Integer rows);

    /**
     * 根据spuId查询参数细节
     *
     * @param id
     * @return
     */
    @GetMapping("spu/detail")
    SpuDetailDTO findSpuDetailBySpuId(@RequestParam("id") Long id);

    /**
     * 根据id查询规格参数
     *
     * @param id
     * @return
     */
    @GetMapping("sku/of/spu")
    List<SkuDTO> findSkuBySpuId(@RequestParam("id") Long id);
    /**
     * 查询规格参数
     * @param gid 组id
     * @param cid 分类id
     * @param searching 是否用于搜索
     * @return 规格组集合
     */
    @GetMapping("spec/params")
    List<SpecParamDTO> findSpecParamByGroupId(@RequestParam(value = "gid", required = false) Long gid,
                                                              @RequestParam(value = "cid", required = false) Long cid,
                                                              @RequestParam(value = "searching", required = false) Boolean searching);
    /**
     * 根据品牌id集合查询品牌
     * @param ids
     * @return
     */
    @GetMapping("brand/list")
    List<BrandDTO> findBrandByBrandIds(@RequestParam("ids")List<Long> ids);

    /**
     * 根据商品集id查询
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    SpuDTO findSpuById(@PathVariable("id") Long id);

    @GetMapping("spec/groups/of/category")
    List<SpecGroupDTO> findSpecGroupByCid(@RequestParam("id") Long id);

    /**
     * 根据skuId集合查询商品详情集合
     * @param ids
     * @return
     */
    @GetMapping("sku/list")
    List<SkuDTO> findSkuByIds(@RequestParam("ids") List<Long> ids);

    /**
     * 根据售卖信息减少库存
     *
     * @param cartMap
     */
    @PutMapping("stock/minus")
    void minusStock(Map<Long, Integer> cartMap);

}
