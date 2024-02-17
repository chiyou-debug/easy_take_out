package com.easy.service;

import com.easy.dto.OrdersSubmitDTO;
import com.easy.vo.OrderSubmitVO;

public interface OrdersService {
    /**
     * Submit an order / User places an order
     *
     * @param ordersSubmitDTO the DTO containing order submission details
     * @return the VO representing the submitted order
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);
}
