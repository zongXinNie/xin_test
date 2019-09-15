package com.leyou.controller;

import com.leyou.dto.CategoryDTO;
import com.leyou.entity.Category;
import com.leyou.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author Nie ZongXin
 * @date 2019/9/7 19:11
 */
@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询一级分类
     *
     * @param pid
     * @return
     */
    @GetMapping("of/parent")
    public ResponseEntity<List<CategoryDTO>> findCategoryByParentId(@RequestParam(name = "pid", defaultValue = "0") Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return ResponseEntity.ok(categoryService.findCategoryByParentId(category));

    }

    /**
     * 根据品牌id查询分类
     *
     * @param brandId
     * @return
     */
    @GetMapping("of/brand")
    public ResponseEntity<List<CategoryDTO>> findCategoryByBrandId(@RequestParam(name = "id") Long brandId) {
        return ResponseEntity.ok(categoryService.findCategoryByBrandId(brandId));
    }

    /**
     * 根据多个分类ID查询分类
     *
     * @param ids
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<CategoryDTO>> findCategoryByIds(@RequestParam(name = "ids") List<Long> ids) {
        return ResponseEntity.ok(categoryService.findCategoryByIds(ids));
    }

    /**
     * 根据id查询分类级别
     * @param id
     * @return
     */
    @GetMapping("levels")
    public ResponseEntity<List<CategoryDTO>> findCategoryById(@RequestParam("id") Long id){
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }
}
