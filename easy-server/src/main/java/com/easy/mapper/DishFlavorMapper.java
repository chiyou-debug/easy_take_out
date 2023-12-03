package com.easy.mapper;

import com.easy.annotation.AutoFill;
import com.easy.entity.DishFlavor;
import com.easy.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * Batch save dish flavor information
     *
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * Batch delete dish flavor data
     *
     * @param ids
     */
    void deleteByDishIds(List<Long> ids);

    /**
     * Query dish flavor list data by dish ID
     *
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getByDishId(Long id);
}
