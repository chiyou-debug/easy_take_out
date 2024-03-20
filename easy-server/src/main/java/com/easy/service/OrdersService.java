package com.easy.service;

import com.easy.dto.*;
import com.easy.result.PageResult;
import com.easy.vo.OrderPaymentVO;
import com.easy.vo.OrderStatisticsVO;
import com.easy.vo.OrderSubmitVO;
import com.easy.vo.OrderVO;

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

    /**
     * page query for the history orders
     *
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult pageQueryHistoryOrders(Integer page, Integer pageSize, Integer status);

    /**
     * query order details
     *
     * @param id
     * @return
     */
    OrderVO details(Long id);


    /**
     * cancel order by id
     *
     * @param id
     */
    void cancelOrder(Long id) throws Exception;

    /**
     * conditional search
     *
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * Statistics of order quantities by status
     *
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * Accept order
     *
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * Reject order
     *
     * @param ordersRejectionDTO
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * Cancel order (admin system)
     *
     * @param ordersCancelDTO
     */
    void cancelByAdmin(OrdersCancelDTO ordersCancelDTO) throws Exception;

    /**
     * Deliver order
     *
     * @param id
     */
    void delivery(Long id);

    /**
     * Complete order
     *
     * @param id
     */
    void complete(Long id);

}
