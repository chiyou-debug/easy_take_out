package com.easy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easy.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {
}
