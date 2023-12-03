package com.easy.vo;

import com.easy.entity.OrderDetail;
import com.easy.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends Orders implements Serializable {

    // Order dishes information
    private String orderDishes;

    // Order details
    private List<OrderDetail> orderDetailList;

}
