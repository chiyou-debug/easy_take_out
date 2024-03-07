package com.easy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easy.dto.DishPageQueryDTO;
import com.easy.entity.Dish;
import com.easy.enumeration.OperationType;
import com.easy.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {


    /**
     * Query dishes by setmeal ID
     *
     * @param setmealId
     * @return
     */
    @Select("select d.* from dish d left join setmeal_dish sd on d.id = sd.dish_id where sd.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);

    /**
     * Query dishes and their related data
     *
     * @param dish
     * @return
     */
    List<DishVO> listDishWithFlavors(Dish dish);

    /**
     * dynamic page query from dish and category table
     *
     * @param dishPageQueryDTO
     * @return
     */
    List<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
