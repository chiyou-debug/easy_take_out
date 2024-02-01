package com.easy.mapper;

import com.easy.annotation.AutoFill;
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
public interface DishMapper {

    /**
     * Count the number of dishes by category ID
     *
     * @param categoryId
     * @return
     */
    @Select("select count(*) from dish where category_id = #{categoryId}")
    Long countByCategoryId(Long categoryId);

    /**
     * Save basic information of a dish
     *
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into dish (name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) VALUES " +
            "(#{name}, #{categoryId}, #{price}, #{image}, #{description}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Dish dish);

    /**
     * Conditional query for dishes
     *
     * @param pageQueryDTO
     * @return
     */
    List<DishVO> list(DishPageQueryDTO pageQueryDTO);

    /**
     * Count the number of dishes on sale by their IDs
     *
     * @param ids
     * @return
     */
    Long countEnableDishByIds(List<Long> ids);

    /**
     * Batch delete dish data
     *
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * Query by dish ID
     *
     * @param id
     * @return
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * Dynamically update dish information
     *
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * Query dishes by setmeal ID
     *
     * @param setmealId
     * @return
     */
    @Select("select d.* from dish d left join setmeal_dish sd on d.id = sd.dish_id where sd.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);

    /**
     * Query dishes by category ID and name
     *
     * @param categoryId
     * @param name
     * @return
     */
    List<Dish> selectDishByCondition(Long categoryId, String name);

    /**
     * Query dishes and their related data
     *
     * @param dish
     * @return
     */
    List<DishVO> listDishWithFlavors(Dish dish);
}
