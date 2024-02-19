package com.easy.service;

import com.easy.dto.OrdersPaymentDTO;
import com.easy.dto.OrdersSubmitDTO;
import com.easy.vo.OrderPaymentVO;
import com.easy.vo.OrderSubmitVO;

public interface OrdersService {
    /**
     * Submit an order / User places an order
     *
     * @param ordersSubmitDTO the DTO containing order submission details
     * @return the VO representing the submitted order
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * Order payment
     *
     * @param ordersPaymentDTO the DTO containing payment details
     * @return the payment result represented by OrderPaymentVO
     * @throws Exception if an error occurs during the payment process
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * Pay success, modify order status.
     *
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

}
