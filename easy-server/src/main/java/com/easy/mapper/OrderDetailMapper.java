package com.easy.mapper;

import com.easy.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * Batch insert order details
     *
     * @param orderDetailList the list of order details to be inserted
     */
    void insertBatch(List<OrderDetail> orderDetailList);
}
