package com.leyou.controller;

import com.leyou.dto.BrandDTO;
import com.leyou.entity.Brand;
import com.leyou.pojo.PageResult;
import com.leyou.pojo.QueryPageBean;
import com.leyou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Nie ZongXin
 * @date 2019/9/7 21:27
 */
@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 查询分页
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("page")
    public ResponseEntity<PageResult<BrandDTO>> findBrand(QueryPageBean queryPageBean) {
        if (queryPageBean.getPage() == null || queryPageBean.getPage() < 0) {
            queryPageBean.setPage(1);
        }
        if (queryPageBean.getRows() == null || queryPageBean.getRows() < 0) {
            queryPageBean.setRows(5);
        }
        return ResponseEntity.ok(brandService.findBrand(queryPageBean));
    }

    /**
     * 新增品牌
     *
     * @return
     */
    @PostMapping("addBrand")
    public ResponseEntity addBrand(Brand brand, String[] cids) {
        brandService.addBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改品牌
     *
     * @param brand
     * @param cids
     * @return
     */
    @PutMapping("addBrand")
    public ResponseEntity editBrand(Brand brand, String[] cids) {
        brandService.editBrand(brand, cids);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除品牌
     *
     * @param brand
     * @return
     */
    @DeleteMapping("deleteBrand")
    public ResponseEntity<Void> deleteBrand(Brand brand) {
        brandService.deleteBrand(brand);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据分类查询品牌
     *
     * @param id
     * @return
     */
    @GetMapping("of/category")
    public ResponseEntity<List<BrandDTO>> findBrandByCtegory(@RequestParam("id") Long id) {
        return ResponseEntity.ok(brandService.findBrandByCtegory(id));
    }

    /**
     * 根据品牌ID查询品牌
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<BrandDTO> findBrandById(@PathVariable(value = "id")Long id){
        return ResponseEntity.ok(brandService.findBrandById(id));
    }

    /**
     * 根据品牌id集合查询品牌
     * @param ids
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<BrandDTO>> findBrandByBrandIds(@RequestParam("ids")List<Long> ids){
        return ResponseEntity.ok(brandService.findBrandByBrandIds(ids));
    }
}
