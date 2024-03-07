package com.easy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easy.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {

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
