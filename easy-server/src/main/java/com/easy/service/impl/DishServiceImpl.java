package com.easy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easy.constant.MessageConstant;
import com.easy.constant.StatusConstant;
import com.easy.dto.DishDTO;
import com.easy.dto.DishPageQueryDTO;
import com.easy.entity.Dish;
import com.easy.entity.DishFlavor;
import com.easy.entity.Setmeal;
import com.easy.entity.SetmealDish;
import com.easy.exception.BusinessException;
import com.easy.mapper.DishFlavorMapper;
import com.easy.mapper.DishMapper;
import com.easy.mapper.SetmealDishMapper;
import com.easy.mapper.SetmealMapper;
import com.easy.result.PageResult;
import com.easy.service.DishFlavorService;
import com.easy.service.DishService;
import com.easy.utils.BeanHelper;
import com.easy.vo.DishVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    @Override
    public void saveWithFlavors(DishDTO dishDTO) {
        Dish dish = BeanHelper.copyProperties(dishDTO, Dish.class);

        //1. Save basic information of the dish
        dish.setStatus(StatusConstant.DISABLE);
        this.save(dish);

        //2. Save dish flavor information
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dish.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);

        // 3. Delete the dish data in the redis cache
        cleanCache(dishDTO.getCategoryId().toString());
    }

    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        //1. Set pagination parameters
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        //2. Execute query
        List<DishVO> dishVoList = dishMapper.pageQuery(dishPageQueryDTO);
        Page page = (Page) dishVoList;

        //3. Parse and package pagination results
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Transactional
    @Override
    public void delete(List<Long> ids) {
        //1. Check dish status, dishes on sale cannot be deleted
        if (dishMapper.selectCount(new LambdaQueryWrapper<Dish>().in(Dish::getId, ids).eq(Dish::getStatus, 1)) > 0) {
            //There are dishes on sale in this batch
            throw new BusinessException(MessageConstant.DISH_ON_SALE);
        }

        //2. Check if the dish is associated with a setmeal, if so, cannot delete
        if (setmealDishMapper.selectCount(new LambdaQueryWrapper<SetmealDish>().in(SetmealDish::getDishId, ids)) > 0) {
            //Associated with setmeal
            throw new BusinessException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //3. Delete the dish and its flavors
        dishMapper.deleteBatchIds(ids);
        dishFlavorMapper.deleteBatchIds(ids);

        // 4、Delete the dish data in the redis cache
        cleanCache("*");
    }

    private void cleanCache(String suffix) {
        log.info("Clear the specified key{} in the cache", "dish:cache:" + suffix);
        redisTemplate.delete(redisTemplate.keys("dish:cache:" + suffix));
    }

    @Override
    public DishVO getInfo(Long id) {
        //1. Query basic information of the dish by ID
        Dish dish = dishMapper.selectById(id);

        //2. Query the list of dish flavors by dish ID
        List<DishFlavor> flavorList = dishFlavorMapper.selectList(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, id));

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
        dishMapper.updateById(dish);

        //2. Modify dish flavor information (delete first, then add)
        //2.1 Delete existing flavor data based on dish ID
        dishFlavorMapper.delete(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, dish.getId()));

        //2.2 Then add new flavor data
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (!CollectionUtils.isEmpty(flavors)) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
            });
            dishFlavorService.saveBatch(flavors);
        }

        // 3、Delete the dish data in the redis cache
        cleanCache("*");
    }

    @Transactional
    @Override
    public void startOrStop(Long id, Integer status) {
        //1. Update dish status
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.updateById(dish);

        //2. If stopping sale, also stop sale of setmeals associated with the dish
        if (status.equals(StatusConstant.DISABLE)) {
            List<SetmealDish> setmealDishes = setmealDishMapper.selectList(new LambdaQueryWrapper<SetmealDish>()
                    .in(SetmealDish::getDishId, id));

            if (!CollectionUtils.isEmpty(setmealDishes)) {
                setmealDishes.forEach((item) -> {
                    setmealMapper.update(Setmeal.builder().id(item.getSetmealId()).status(StatusConstant.DISABLE).build());
                });
            }
        }

        // 3、Delete the dish data in the redis cache
        cleanCache("*");
    }

    @Override
    public List<Dish> list(Long categoryId) {
        return dishMapper.selectList(new LambdaQueryWrapper<Dish>().eq(Dish::getCategoryId, categoryId));
    }

    @Override
    public List<DishVO> listDishWithFlavors(Long categoryId) {
        String redisDishKey = "dish:cache:" + categoryId;

        // 1. First, check the Redis cache. If there is data in the cache, return it directly.
        List<DishVO> dishVOList = (List<DishVO>) redisTemplate.opsForValue().get(redisDishKey);
        if (!CollectionUtils.isEmpty(dishVOList)) {
            log.info("Query Redis cache, data found, returning directly...");
            return dishVOList;
        }

        // 2. If there is no data in the cache, query the database.
        Dish dish = Dish.builder().categoryId(categoryId).status(StatusConstant.ENABLE).build();
        dishVOList = dishMapper.listDishWithFlavors(dish);

        // 3. Add the database query result to the cache.
        redisTemplate.opsForValue().set(redisDishKey, dishVOList);
        log.info("Query the database, caching the retrieved data in Redis...");
        return dishVOList;
    }
}
