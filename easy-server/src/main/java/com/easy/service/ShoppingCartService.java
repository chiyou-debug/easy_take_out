package com.easy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easy.dto.ShoppingCartDTO;
import com.easy.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    /**
     * Add to shopping cart
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * Fetch shopping cart
     */
    List<ShoppingCart> list();

    /**
     * Remove an item from the shopping cart
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * Clear the shopping cart data
     */
    void cleanShoppingCart();
}
