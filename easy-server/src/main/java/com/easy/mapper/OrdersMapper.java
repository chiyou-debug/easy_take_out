package com.easy.mapper;

import com.easy.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrdersMapper {

    /**
     * Add a new order
     *
     * @param orders the order to be added
     */
    void insert(Orders orders);

    /**
     * Query orders by order number and user ID.
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);


    /**
     * Update order information.
     *
     * @param orders
     */
    void update(Orders orders);
}
