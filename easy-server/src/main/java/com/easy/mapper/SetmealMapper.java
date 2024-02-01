package com.easy.mapper;

import com.easy.annotation.AutoFill;
import com.easy.dto.SetmealPageQueryDTO;
import com.easy.entity.Setmeal;
import com.easy.enumeration.OperationType;
import com.easy.vo.DishItemVO;
import com.easy.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * Count the number of setmeals by category ID
     *
     * @param id
     * @return
     */
    @Select("select count(*) from setmeal where category_id = #{categoryId}")
    Long countByCategoryId(Long id);

    /**
     * Update setmeal information
     *
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * Insert setmeal data
     *
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into setmeal(name, category_id, price, status, description, image, create_time, update_time, create_user, update_user) VALUES " +
            "(#{name}, #{categoryId}, #{price}, #{status}, #{description}, #{image}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Setmeal setmeal);

    /**
     * Dynamic conditional query for setmeal information
     *
     * @param setmealPageQueryDTO
     * @return
     */
    List<SetmealVO> listVo(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * Query setmeal by ID
     *
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * Batch delete setmeal information
     *
     * @param setmealIds
     */
    void deleteBatchByIds(List<Long> setmealIds);


    /**
     * Dynamic condition query for packages
     *
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * Query dish options by setmeal ID
     *
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);
}
