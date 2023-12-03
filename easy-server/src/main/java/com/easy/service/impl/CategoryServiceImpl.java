package com.easy.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.easy.constant.MessageConstant;
import com.easy.constant.StatusConstant;
import com.easy.context.BaseContext;
import com.easy.dto.CategoryDTO;
import com.easy.dto.CategoryPageQueryDTO;
import com.easy.entity.Category;
import com.easy.exception.BusinessException;
import com.easy.mapper.CategoryMapper;
import com.easy.mapper.DishMapper;
import com.easy.mapper.SetmealMapper;
import com.easy.result.PageResult;
import com.easy.service.CategoryService;
import com.easy.utils.BeanHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Category Service Layer
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void save(CategoryDTO categoryDTO) {
        // Copy properties
        Category category = BeanHelper.copyProperties(categoryDTO, Category.class);

        // Default category status is set to disabled (0)
        category.setStatus(StatusConstant.DISABLE);

        categoryMapper.insert(category);
    }

    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        // 1. Set pagination parameters
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        // 2. Perform the query
        List<Category> categoryList = categoryMapper.pageQuery(categoryPageQueryDTO);

        // 3. Parse and encapsulate the results
        Page page = (Page) categoryList;
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void deleteById(Long id) {
        // 1. Check if the category is related to any dishes; if so, throw a business exception
        Long count = dishMapper.countByCategoryId(id);
        if (count > 0) {
            // There are dishes under this category; cannot delete
            throw new BusinessException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        // 2. Check if the category is related to any set meals; if so, throw a business exception
        count = setmealMapper.countByCategoryId(id);
        if (count > 0) {
            // There are set meals under this category; cannot delete
            throw new BusinessException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        // 3. Delete the category data
        categoryMapper.deleteById(id);
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        // Copy properties
        Category category = BeanHelper.copyProperties(categoryDTO, Category.class);
        categoryMapper.update(category);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .build();
        categoryMapper.update(category);
    }

    @Override
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }

    @Override
    public Category getById(Long id) {
        return categoryMapper.getById(id);
    }
}
