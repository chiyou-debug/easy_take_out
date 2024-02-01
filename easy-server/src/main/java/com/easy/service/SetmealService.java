package com.easy.service;

import com.easy.dto.SetmealDTO;
import com.easy.dto.SetmealPageQueryDTO;
import com.easy.entity.Setmeal;
import com.easy.result.PageResult;
import com.easy.vo.DishItemVO;
import com.easy.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    /**
     * Add a new setmeal
     *
     * @param setmealDTO
     */
    void save(SetmealDTO setmealDTO);

    /**
     * Paginated query for setmeals
     *
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * Batch delete setmeals
     *
     * @param ids
     */
    void delete(List<Long> ids);

    /**
     * Query setmeal data by ID, including the list of dishes associated with the setmeal
     *
     * @param id
     * @return
     */
    SetmealVO getById(Long id);

    /**
     * Update setmeal data
     *
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * Start or stop selling setmeals
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);


    /**
     * Conditional query
     *
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * Query the dish information associated with a specific setmeal
     */
    List<DishItemVO> getDishItemById(Long id);
}
