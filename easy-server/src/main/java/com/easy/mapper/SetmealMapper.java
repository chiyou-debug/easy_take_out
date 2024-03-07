package com.easy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface SetmealMapper extends BaseMapper<Setmeal> {

    /**
     * Update setmeal information
     *
     * @param setmeal
     */
    void update(Setmeal setmeal);


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
