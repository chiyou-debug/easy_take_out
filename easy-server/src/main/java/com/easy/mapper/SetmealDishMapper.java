package com.easy.mapper;

import com.easy.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * Query setmeal IDs associated with given dish IDs
     *
     * @param ids
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> ids);

    /**
     * Batch insert association information between setmeal and dishes
     *
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * Batch delete association information of setmeal dishes by a collection of setmeal IDs
     *
     * @param setmealIds
     */
    void deleteBatchBySetmealIds(List<Long> setmealIds);

    /**
     * Query the list of dishes associated with a setmeal by setmeal ID
     *
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);
}
