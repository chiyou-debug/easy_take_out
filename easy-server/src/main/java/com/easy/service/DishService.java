package com.easy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easy.dto.DishDTO;
import com.easy.dto.DishPageQueryDTO;
import com.easy.entity.Dish;
import com.easy.result.PageResult;
import com.easy.vo.DishVO;

import java.util.List;

public interface DishService extends IService<Dish> {

    /**
     * Add a new dish
     *
     * @param dishDTO
     */
    void save(DishDTO dishDTO);

    /**
     * Paginated query for dishes
     *
     * @param dishPageQueryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * Batch delete dishes
     *
     * @param ids
     */
    void delete(List<Long> ids);

    /**
     * Query dish by ID
     *
     * @param id
     * @return
     */
    DishVO getInfo(Long id);

    /**
     * Update dish information
     *
     * @param dishDTO
     */
    void update(DishDTO dishDTO);

    /**
     * Start or stop selling dish
     *
     * @param id
     * @param status
     */
    void startOrStop(Long id, Integer status);

    /**
     * Query dishes by category ID
     *
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId, String name);

    /**
     * Conditional query for dishes with flavors
     *
     * @param categoryId
     * @return
     */
    List<DishVO> listDishWithFlavors(Long categoryId);
}
