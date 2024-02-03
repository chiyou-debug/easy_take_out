package com.easy.mapper;

import com.easy.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * Query shopping cart data based on specified conditions
     *
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * Update quantity
     *
     * @param cart
     */
    @Update("update shopping_cart set  number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart cart);

    /**
     * Add to shopping cart
     *
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) VALUES " +
            "(#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime} )")
    void insert(ShoppingCart shoppingCart);

    /**
     * Delete shopping cart data by ID
     *
     * @param id
     */
    @Delete("delete from shopping_cart where  id = #{id}")
    void deleteById(Long id);

    /**
     * Delete shopping cart data by user ID
     *
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);
}
