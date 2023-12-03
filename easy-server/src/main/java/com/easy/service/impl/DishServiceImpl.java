package com.easy.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.easy.constant.MessageConstant;
import com.easy.constant.StatusConstant;
import com.easy.dto.DishDTO;
import com.easy.dto.DishPageQueryDTO;
import com.easy.entity.Dish;
import com.easy.entity.DishFlavor;
import com.easy.entity.Setmeal;
import com.easy.exception.BusinessException;
import com.easy.mapper.DishFlavorMapper;
import com.easy.mapper.DishMapper;
import com.easy.mapper.SetmealDishMapper;
import com.easy.mapper.SetmealMapper;
import com.easy.result.PageResult;
import com.easy.service.DishService;
import com.easy.utils.BeanHelper;
import com.easy.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Transactional
    @Override
    public void save(DishDTO dishDTO) {
        Dish dish = BeanHelper.copyProperties(dishDTO, Dish.class);

        //1. Save basic information of the dish
        dish.setStatus(StatusConstant.DISABLE);
        dishMapper.insert(dish);

        //2. Save dish flavor information
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (!CollectionUtils.isEmpty(flavors)) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public PageResult page(DishPageQueryDTO pageQueryDTO) {
        //1. Set pagination parameters
        PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());

        //2. Execute query
        List<DishVO> dishVOList = dishMapper.list(pageQueryDTO);

        //3. Parse and package pagination results
        Page<DishVO> page = (Page<DishVO>) dishVOList;
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Transactional
    @Override
    public void delete(List<Long> ids) {
        //1. Check dish status, dishes on sale cannot be deleted
        Long count = dishMapper.countEnableDishByIds(ids);
        if (count > 0) { //There are dishes on sale in this batch
            throw new BusinessException(MessageConstant.DISH_ON_SALE);
        }

        //2. Check if the dish is associated with a setmeal, if so, cannot delete
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (!CollectionUtils.isEmpty(setmealIds)) {//Associated with setmeal
            throw new BusinessException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //3. Delete the dish and its flavors
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);
    }

    @Override
    public DishVO getInfo(Long id) {
        //1. Query basic information of the dish by ID
        Dish dish = dishMapper.getById(id);

        //2. Query the list of dish flavors by dish ID
        List<DishFlavor> flavorList = dishFlavorMapper.getByDishId(id);

        //3. Assemble data
        DishVO dishVO = BeanHelper.copyProperties(dish, DishVO.class);
        if (dishVO != null) {
            dishVO.setFlavors(flavorList);
        }
        return dishVO;
    }

    @Transactional
    @Override
    public void update(DishDTO dishDTO) {
        //1. Modify basic information of the dish
        Dish dish = BeanHelper.copyProperties(dishDTO, Dish.class);
        dishMapper.update(dish);

        //2. Modify dish flavor information (delete first, then add)
        //2.1 Delete existing flavor data based on dish ID
        dishFlavorMapper.deleteByDishIds(Collections.singletonList(dish.getId()));

        //2.2 Then add new flavor data
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (!CollectionUtils.isEmpty(flavors)) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Transactional
    @Override
    public void startOrStop(Long id, Integer status) {
        //1. Update dish status
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);

        //2. If stopping sale, also stop sale of setmeals associated with the dish
        if (status == StatusConstant.DISABLE) {
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(Collections.singletonList(id));
            if (!CollectionUtils.isEmpty(setmealIds)) {
                setmealIds.forEach(setmealId -> {
                    Setmeal setmeal = Setmeal.builder().id(setmealId).status(StatusConstant.DISABLE).build();
                    setmealMapper.update(setmeal);//Stop sale of corresponding setmeal
                });
            }
        }
    }

    @Override
    public List<Dish> list(Long categoryId, String name) {
        return dishMapper.selectDishByCondition(categoryId, name);
    }
}
