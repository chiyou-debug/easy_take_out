package com.easy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easy.entity.DishFlavor;
import com.easy.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

    /**
     * Batch save dish flavor information
     *
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

}
