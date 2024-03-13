package com.easy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easy.dto.CategoryDTO;
import com.easy.dto.CategoryPageQueryDTO;
import com.easy.entity.Category;
import com.easy.result.PageResult;

import java.util.List;

public interface CategoryService extends IService<Category> {

    /**
     * Add a new category
     *
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     * Paginated query
     *
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * Delete a category by its id
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * Update a category
     *
     * @param categoryDTO
     */
    void update(CategoryDTO categoryDTO);

    /**
     * Enable or disable a category
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * Query categories by type
     *
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
