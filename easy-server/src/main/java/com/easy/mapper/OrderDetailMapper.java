package com.easy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easy.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

    /**
     * Batch insert order details
     *
     * @param orderDetailList the list of order details to be inserted
     */
    void insertBatch(List<OrderDetail> orderDetailList);
}
