package com.easy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.easy.context.BaseContext;
import com.easy.dto.ShoppingCartDTO;
import com.easy.entity.Dish;
import com.easy.entity.Setmeal;
import com.easy.entity.ShoppingCart;
import com.easy.mapper.DishMapper;
import com.easy.mapper.SetmealMapper;
import com.easy.mapper.ShoppingCartMapper;
import com.easy.service.ShoppingCartService;
import com.easy.utils.BeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    // Add to shopping cart
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //1. Check if the specified product (dish-flavor, setmeal) already exists in the current user's shopping cart.
        ShoppingCart shoppingCart = BeanHelper.copyProperties(shoppingCartDTO, ShoppingCart.class);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectList(new LambdaQueryWrapper<ShoppingCart>()
                .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId()));

        //2. If the product already exists in the shopping cart, increase the quantity by 1.
        if (!CollectionUtils.isEmpty(shoppingCartList)) {
            ShoppingCart cart = shoppingCartList.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateById(cart);
        } else {
            //3. If the product does not exist in the shopping cart, add a new record.
            Long dishId = shoppingCart.getDishId();
            if (dishId != null) { // For dishes
                Dish dish = dishMapper.selectById(dishId);
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setName(dish.getName());
            } else { // For setmeals
                Setmeal setmeal = setmealMapper.selectById(shoppingCart.getSetmealId());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setName(setmeal.getName());
            }

            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);

            // Insert the data
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> list() {
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(BaseContext.getCurrentId()).build();
        return shoppingCartMapper.selectList(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, shoppingCart.getUserId()));
    }

    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //1. Retrieve the shopping cart list for the current user and the current dish/setmeal.
        ShoppingCart shoppingCart = BeanHelper.copyProperties(shoppingCartDTO, ShoppingCart.class);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectList(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, shoppingCart.getUserId()));

        //2. If the quantity of the product in the shopping cart is greater than 1, decrease it by 1 and update the database.
        if (!CollectionUtils.isEmpty(shoppingCartList)) {
            shoppingCart = shoppingCartList.get(0);
            Integer number = shoppingCart.getNumber();
            if (number > 1) {
                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
                shoppingCartMapper.updateById(shoppingCart);
            } else {
                //3. If the quantity is 1, delete the product from the shopping cart.
                shoppingCartMapper.deleteById(shoppingCart.getId());
            }
        }
    }

    @Override
    public void cleanShoppingCart() {
        shoppingCartMapper.delete(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, BaseContext.getCurrentId()));
    }
}
