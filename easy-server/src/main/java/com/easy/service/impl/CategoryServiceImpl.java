package com.easy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easy.constant.MessageConstant;
import com.easy.constant.StatusConstant;
import com.easy.dto.CategoryDTO;
import com.easy.dto.CategoryPageQueryDTO;
import com.easy.entity.Category;
import com.easy.entity.Dish;
import com.easy.entity.Setmeal;
import com.easy.exception.BusinessException;
import com.easy.mapper.CategoryMapper;
import com.easy.mapper.DishMapper;
import com.easy.mapper.SetmealMapper;
import com.easy.result.PageResult;
import com.easy.service.CategoryService;
import com.easy.utils.BeanHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.Count;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Category Service Layer
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

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
        IPage<Category> page = new Page<Category>(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        // 2. Dynamic condition query
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<Category>()
                .like(StringUtils.isNotBlank(categoryPageQueryDTO.getName()), Category::getName, categoryPageQueryDTO.getName())
                .eq(categoryPageQueryDTO.getType() != null, Category::getType, categoryPageQueryDTO.getType())
                .orderByAsc(Category::getSort);
        categoryMapper.selectPage(page, lqw);

        // 3. Parse and encapsulate the results
        return new PageResult(page.getTotal(), page.getRecords());
    }

    @Override
    public void deleteById(Long id) {
        // 1. Check if the category is related to any dishes; if so, throw a business exception
        if (dishMapper.selectCount(new LambdaQueryWrapper<Dish>().eq(Dish::getCategoryId, id)) > 0) {
            // There are dishes under this category; cannot delete
            throw new BusinessException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        // 2. Check if the category is related to any set meals; if so, throw a business exception
        if (setmealMapper.selectCount(new LambdaQueryWrapper<Setmeal>().eq(Setmeal::getCategoryId, id)) > 0) {
            // There are set meals under this category; cannot delete
            throw new BusinessException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        // 3. Delete a category by its id
        categoryMapper.deleteById(id);
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        // Copy properties
        Category category = BeanHelper.copyProperties(categoryDTO, Category.class);
        categoryMapper.updateById(category);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder().id(id).status(status).build();
        categoryMapper.updateById(category);
    }

    @Override
    public Category getById(Long id) {
        // Query category information by its ID
        return categoryMapper.selectById(id);
    }

    @Override
    public List<Category> list(Integer type) {
        // Query categories by their type
        return categoryMapper.selectList(new LambdaQueryWrapper<Category>().eq(Category::getType, type));
    }
}
