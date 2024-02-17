package com.easy.mapper;

import com.easy.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper {

    /**
     * Add a new order
     *
     * @param orders the order to be added
     */
    void insert(Orders orders);
}
