package com.leyou.service.impl;

import com.leyou.common.ExceptionEnum;
import com.leyou.dto.CategoryDTO;
import com.leyou.entity.Category;
import com.leyou.exception.LyException;
import com.leyou.mapper.CategoryMapper;
import com.leyou.service.CategoryService;
import com.leyou.utils.BeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nie ZongXin
 * @date 2019/9/7 19:00
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询一级分类
     *
     * @param category
     * @return
     */
    @Override
    public List<CategoryDTO> findCategoryByParentId(Category category) {
        List<Category> categories = categoryMapper.select(category);

        if (CollectionUtils.isEmpty(categories)) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        List<CategoryDTO> categoryDTOS = BeanHelper.copyWithCollection(categories, CategoryDTO.class);

        return BeanHelper.copyWithCollection(categories, CategoryDTO.class);
    }

    /**
     * 根据品牌id查询分类
     *
     * @param brandId
     * @return
     */
    @Override
    public List<CategoryDTO> findCategoryByBrandId(Long brandId) {
        List<Category> category = categoryMapper.findCategoryByBrandId(brandId);
        if (CollectionUtils.isEmpty(category)) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(category, CategoryDTO.class);
    }

    /**
     * 查询三级目录
     *
     * @param ids
     * @return
     */
    @Override
    public List<CategoryDTO> findCategoryByIds(List<Long> ids) {
        List<Category> categories = categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(categories)) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(categories, CategoryDTO.class);
    }

    /**
     * 根据子级分类id查询查询父分类
     *
     * @param id
     * @return
     */
    @Override
    public List<CategoryDTO> findCategoryById(Long id) {
        ArrayList<Category> categoryList = new ArrayList<>();
        Category category = new Category();
        category.setId(id);
        category = categoryMapper.selectByPrimaryKey(category);

        if (category!=null){
            categoryList.add(category);
//            最多5级分类
            for (int i = 0; i < 5&&category.getParentId()!=0; i++) {
                Category categoryParent = new Category();
                categoryParent.setId(category.getParentId());
                category = categoryMapper.selectByPrimaryKey(categoryParent);
                categoryList.add(category);
            }
         /*   while (category.getParentId()!=0){
//                如果一个分类中父类字段与自身id字段重合，打断循环
                if (category.getParentId().equals(category.getId())){
                    break;
                }
                Category categoryParent = new Category();
                categoryParent.setId(category.getParentId());
                category = categoryMapper.selectByPrimaryKey(categoryParent);
                categoryList.add(category);
            }*/
        }else {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(categoryList,CategoryDTO.class);
    }

}
