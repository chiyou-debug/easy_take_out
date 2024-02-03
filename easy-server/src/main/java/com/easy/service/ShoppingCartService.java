package com.easy.service;

import com.easy.dto.ShoppingCartDTO;
import com.easy.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
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
