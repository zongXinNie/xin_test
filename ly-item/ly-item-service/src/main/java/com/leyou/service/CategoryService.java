package com.leyou.service;

import com.leyou.dto.CategoryDTO;
import com.leyou.entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> findCategoryByParentId(Category category);

    List<CategoryDTO> findCategoryByBrandId(Long brandId);

    List<CategoryDTO> findCategoryByIds(List<Long> ids);

    List<CategoryDTO> findCategoryById(Long id);
}
