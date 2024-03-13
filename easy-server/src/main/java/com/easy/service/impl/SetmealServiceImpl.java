package com.easy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easy.constant.MessageConstant;
import com.easy.constant.StatusConstant;
import com.easy.dto.SetmealDTO;
import com.easy.dto.SetmealPageQueryDTO;
import com.easy.entity.Dish;
import com.easy.entity.Setmeal;
import com.easy.entity.SetmealDish;
import com.easy.exception.BusinessException;
import com.easy.mapper.DishMapper;
import com.easy.mapper.SetmealDishMapper;
import com.easy.mapper.SetmealMapper;
import com.easy.result.PageResult;
import com.easy.service.SetmealDishService;
import com.easy.service.SetmealService;
import com.easy.utils.BeanHelper;
import com.easy.vo.DishItemVO;
import com.easy.vo.SetmealVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealDishService setmealDishService;

    @CacheEvict(cacheNames = "setmeal:cache", key = "#setmealDTO.categoryId")
    @Override
    public void save(SetmealDTO setmealDTO) {
        //1. Save basic information of the setmeal
        Setmeal setmeal = BeanHelper.copyProperties(setmealDTO, Setmeal.class);
        setmealMapper.insert(setmeal);

        //2. Get the primary key ID of the setmeal
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (!CollectionUtils.isEmpty(setmealDishes)) {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });
        }

        //3. Save the relationship between setmeal and dishes
        setmealDishService.saveBatch(setmealDishes);
    }


    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        //1. Set pagination parameters
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        //2. Execute the query
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);

        //3. Parse and encapsulate the results
        return new PageResult(page.getTotal(), page.getResult());
    }


    @CacheEvict(cacheNames = "setmeal:cache", allEntries = true)
    @Transactional
    @Override
    public void delete(List<Long> ids) {
        //1. Setmeals on sale cannot be deleted
        ids.forEach(id -> {
            Setmeal setmeal = setmealMapper.selectById(id);
            if (StatusConstant.ENABLE.equals(setmeal.getStatus())) {
                throw new BusinessException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        //2. Delete setmeal data and its association with dishes
        setmealMapper.deleteBatchIds(ids);
        setmealDishMapper.deleteBatchIds(ids);
    }

    @Override
    public SetmealVO getById(Long id) {
        //1. Query basic information of the setmeal by ID
        Setmeal setmeal = setmealMapper.selectById(id);

        //2. Query the list of dishes associated with the setmeal by setmeal ID
        List<SetmealDish> setmealDishList = setmealDishMapper.selectList(
                new LambdaQueryWrapper<SetmealDish>().eq(id != null, SetmealDish::getSetmealId, id));

        //3. Encapsulate the result
        SetmealVO setmealVO = BeanHelper.copyProperties(setmeal, SetmealVO.class);
        if (setmealVO != null) {
            setmealVO.setSetmealDishes(setmealDishList);
        }
        return setmealVO;
    }

    @CacheEvict(cacheNames = "setmeal:cache", allEntries = true)
    @Transactional
    @Override
    public void update(SetmealDTO setmealDTO) {
        //1. Modify basic information of the setmeal
        Setmeal setmeal = BeanHelper.copyProperties(setmealDTO, Setmeal.class);
        setmealMapper.updateById(setmeal);


        //2. First delete the relationship between setmeal and dishes
        Long setmealId = setmealDTO.getId();
        setmealDishMapper.delete(new LambdaQueryWrapper<SetmealDish>().in(setmealId != null, SetmealDish::getSetmealId, setmealId));

        //3. Then insert the relationship between setmeal and dishes
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (!CollectionUtils.isEmpty(setmealDishes)) {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });
            setmealDishService.saveBatch(setmealDishes);
        }
    }

    @CacheEvict(cacheNames = "setmeal:cache", allEntries = true)
    @Transactional
    @Override
    public void startOrStop(Integer status, Long id) {
        //1. If starting, ensure all associated dishes are also on sale
        if (status.equals(StatusConstant.ENABLE)) {
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            if (!CollectionUtils.isEmpty(dishList)) {
                dishList.forEach(dish -> {
                    if (StatusConstant.DISABLE.equals(dish.getStatus())) {
                        throw new BusinessException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }

        //2. Update the status of the setmeal
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.updateById(setmeal);
    }


    @Cacheable(cacheNames = "setmeal:cache", key = "#setmeal.categoryId")
    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        return setmealMapper.selectList(new LambdaQueryWrapper<Setmeal>().eq(setmeal.getId() != null, Setmeal::getId, setmeal.getId()));
    }

    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
