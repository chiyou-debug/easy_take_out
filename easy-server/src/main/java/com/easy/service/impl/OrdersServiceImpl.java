package com.easy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easy.constant.MessageConstant;
import com.easy.context.BaseContext;
import com.easy.dto.*;
import com.easy.entity.*;
import com.easy.exception.BusinessException;
import com.easy.mapper.*;
import com.easy.result.PageResult;
import com.easy.service.OrdersService;
import com.easy.utils.BeanHelper;
import com.easy.utils.WeChatPayUtil;
import com.easy.vo.OrderPaymentVO;
import com.easy.vo.OrderStatisticsVO;
import com.easy.vo.OrderSubmitVO;
import com.easy.vo.OrderVO;
import com.easy.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private WebSocketServer webSocketServer;

    @Transactional
    @Override
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        log.info("User placing an order...");

        // 0. Querying recipient address information. Cannot place an order if the address is empty.
        AddressBook addressBook = addressBookMapper.selectById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new BusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // 0. Cannot place an order if the shopping cart data is empty.
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(BaseContext.getCurrentId()).build();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectList(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, shoppingCart.getUserId()));
        if (CollectionUtils.isEmpty(shoppingCartList)) {
            throw new BusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 1. Saving order data.
        Orders orders = BeanHelper.copyProperties(ordersSubmitDTO, Orders.class);
        orders.setNumber(String.valueOf(System.nanoTime())); // IdWorker -- Snowflake algorithm.
        orders.setStatus(Orders.ORDER_STATUS_PENDING_PAYMENT); // Pending payment
        orders.setUserId(BaseContext.getCurrentId());
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.PAYMENT_STATUS_UNPAID); // Unpaid

        // Recipient
        orders.setPhone(addressBook.getPhone()); // Phone number
        orders.setAddress(addressBook.getAddress());
        orders.setConsignee(addressBook.getName()); // Recipient

        ordersMapper.insert(orders);

        // 2. Saving order detail data. -- From the shopping cart.
        List<OrderDetail> orderDetailList = shoppingCartList.stream().map(cart -> {
            // Copying properties
            OrderDetail orderDetail = BeanHelper.copyProperties(cart, OrderDetail.class);
            // Assigning order ID
            orderDetail.setOrderId(orders.getId());
            return orderDetail;
        }).collect(Collectors.toList());

        orderDetailMapper.insertBatch(orderDetailList);

        // 3. Clearing the current user's shopping cart data.
        shoppingCartMapper.delete(new LambdaQueryWrapper<ShoppingCart>()
                .in(ShoppingCart::getUserId, BaseContext.getCurrentId()));

        // 4. Assembling data and returning.
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();
        return orderSubmitVO;
    }

    /**
     * Order payment
     *
     * @param ordersPaymentDTO the DTO containing payment details
     * @return the payment result represented by OrderPaymentVO
     * @throws Exception if an error occurs during the payment process
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // Get the ID of the current logged-in user
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        // Call the WeChat payment API to generate a prepayment transaction order
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), // Merchant order number
                new BigDecimal(0.01), // Payment amount in yuan
                "EasyTakeout Order", // Product description
                user.getOpenid() // WeChat user's openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new BusinessException("This order has been paid");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        /**
         * Push a message to the management terminal - remind of new orders -
         *   --- {"type": 1, "orderId": 12, "content": "Ordernumber: 273872873823"}
         */
        Orders order = ordersMapper.selectOne(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getNumber, ordersPaymentDTO.getOrderNumber()));
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("type", 0);
        paramMap.put("orderId", order.getId());
        paramMap.put("content", "OrderNumber: " + order.getNumber());
        log.info("Push a message to the management terminal , {}", paramMap);
        webSocketServer.sendMessageToBackendSystem(JSONObject.toJSONString(paramMap));

        return vo;
    }

    /**
     * Pay success, modify order status.
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {
        // Query the current user's order based on the order number
        Orders ordersDB = ordersMapper.selectOne(new LambdaQueryWrapper<Orders>()
                .eq(outTradeNo != null, Orders::getNumber, outTradeNo));

        // Update the order status, payment method, payment status, and checkout time based on the order ID
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.ORDER_STATUS_PENDING_ACCEPTANCE)
                .payStatus(Orders.PAYMENT_STATUS_PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        ordersMapper.updateById(orders);
    }

    @Override
    public PageResult pageQueryHistoryOrders(Integer page, Integer pageSize, Integer status) {
        log.info("query history orders");
        // Set pagination
        IPage<Orders> pageInfo = new Page<>(page, pageSize);

        // Pagination and conditional query
        ordersMapper.selectPage(pageInfo, new LambdaQueryWrapper<Orders>()
                .eq(Orders::getUserId, BaseContext.getCurrentId())
                .eq(status != null, Orders::getStatus, status));

        List<OrderVO> list = new ArrayList<>();

        // Query order details and encapsulate in OrderVO for response
        if (pageInfo.getTotal() > 0) {
            for (Orders orders : pageInfo.getRecords()) {
                Long orderId = orders.getId(); // Order ID

                // Query order details
                List<OrderDetail> orderDetails = orderDetailMapper.selectList(new LambdaQueryWrapper<OrderDetail>()
                        .eq(orderId != null, OrderDetail::getOrderId, orderId));

                System.out.println("orderDetails" + orderDetails);

                OrderVO orderVO = BeanHelper.copyProperties(orders, OrderVO.class);
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }
        return new PageResult(pageInfo.getTotal(), list);
    }

    @Override
    public OrderVO details(Long id) {
        // Query order by ID
        Orders orders = ordersMapper.selectById(id);

        // Query the dish/set meal details for this order
        List<OrderDetail> orderDetails = orderDetailMapper.selectList(new LambdaQueryWrapper<OrderDetail>()
                .eq(orders.getId() != null, OrderDetail::getOrderId, orders.getId()));

        // Encapsulate the order and its details into OrderVO and return
        OrderVO orderVO = BeanHelper.copyProperties(orders, OrderVO.class);
        orderVO.setOrderDetailList(orderDetails);
        return orderVO;
    }

    @Override
    public void cancelOrder(Long id) throws Exception {
        //1. Query order information based on ID
        Orders orders = ordersMapper.selectById(id);

        //2. Verify if the order exists
        if (orders == null) {
            throw new BusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //3. Check if the order can be canceled - Only orders in the status of Pending Payment and Pending Acceptance can be directly canceled
        Integer status = orders.getStatus();
        if (status > 2) { //  1 Pending Payment 2 Pending Acceptance 3 Accepted 4 Delivering 5 Completed 6 Canceled 7 Refunded
            throw new BusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //4. If the order is in the Pending Acceptance status and has been paid, a refund operation is also needed
        Integer payStatus = orders.getPayStatus();
        if (status.equals(Orders.ORDER_STATUS_PENDING_ACCEPTANCE) && payStatus.equals(Orders.PAYMENT_STATUS_PAID)) { //2 Pending Acceptance and Paid
            //Call WeChat Pay API for refund
            weChatPayUtil.refund(orders.getNumber(), orders.getNumber(), new BigDecimal("0.01"), new BigDecimal("0.01"));
            orders.setPayStatus(Orders.PAYMENT_STATUS_REFUNDED);
        }

        //5. Update order status, cancellation reason, cancellation time, etc.
        orders.setStatus(Orders.ORDER_STATUS_CANCELED);
        orders.setCancelReason("User canceled");
        orders.setCancelTime(LocalDateTime.now());

        ordersMapper.updateById(orders);
    }


    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        IPage<Orders> page = new Page<>(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        // Select pages based on conditions, improving query efficiency by checking for blank strings
        ordersMapper.selectPage(page, new LambdaQueryWrapper<Orders>()
                .like(!StringUtils.isBlank(ordersPageQueryDTO.getNumber()), Orders::getNumber, ordersPageQueryDTO.getNumber())
                .like(!StringUtils.isBlank(ordersPageQueryDTO.getPhone()), Orders::getPhone, ordersPageQueryDTO.getPhone())
                .gt(ordersPageQueryDTO.getBeginTime() != null, Orders::getOrderTime, ordersPageQueryDTO.getBeginTime())
                .lt(ordersPageQueryDTO.getEndTime() != null, Orders::getOrderTime, ordersPageQueryDTO.getEndTime())
                .orderByDesc(Orders::getOrderTime));

        // For certain order statuses, additional order dish information is returned, converting Orders to OrderVO
        List<OrderVO> orderVOList = getOrderVOList(page);

        return new PageResult(page.getTotal(), orderVOList);
    }

    @Override
    public OrderStatisticsVO statistics() {
        // Query the quantity of orders by status: to be confirmed, confirmed, and delivery in progress
        Long toBeConfirmed = ordersMapper.selectCount(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getStatus, Orders.ORDER_STATUS_PENDING_ACCEPTANCE));
        Long confirmed = ordersMapper.selectCount(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getStatus, Orders.ORDER_STATUS_ACCEPTED));
        Long deliveryInProgress = ordersMapper.selectCount(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getStatus, Orders.ORDER_STATUS_DELIVERY_IN_PROGRESS));

        OrderStatisticsVO statisticsVO = OrderStatisticsVO.builder()
                .toBeConfirmed(toBeConfirmed.intValue())
                .confirmed(confirmed.intValue())
                .deliveryInProgress(deliveryInProgress.intValue())
                .build();
        return statisticsVO;
    }


    /**
     * Accept order
     *
     * @param ordersConfirmDTO
     */
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.ORDER_STATUS_ACCEPTED)
                .build();

        ordersMapper.updateById(orders);
    }


    /**
     * Reject order
     *
     * @param ordersRejectionDTO
     */
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        // Query order by ID
        Orders ordersDB = ordersMapper.selectById(ordersRejectionDTO.getId());

        // The order can only be rejected if it exists and its status is 2 (pending acceptance)
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.ORDER_STATUS_PENDING_ACCEPTANCE)) {
            throw new BusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // Payment status
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == Orders.PAYMENT_STATUS_PAID) {
            // If the customer has paid, a refund is needed
            String refund = weChatPayUtil.refund(
                    ordersDB.getNumber(),
                    ordersDB.getNumber(),
                    new BigDecimal(0.01),
                    new BigDecimal(0.01));
            log.info("Refund applied: {}", refund);
        }

        // For rejecting the order with a refund, update the order status, rejection reason, and cancellation time by order ID
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.ORDER_STATUS_CANCELED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());

        ordersMapper.updateById(orders);
    }


    @Override
    public void cancelByAdmin(OrdersCancelDTO ordersCancelDTO) throws Exception {
        // Query order by ID
        Orders ordersDB = ordersMapper.selectById(ordersCancelDTO.getId());

        // Payment status
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == Orders.PAYMENT_STATUS_PAID) { // Paid status
            // If the customer has paid, a refund is needed
            String refund = weChatPayUtil.refund(
                    ordersDB.getNumber(),
                    ordersDB.getNumber(),
                    new BigDecimal(0.01),
                    new BigDecimal(0.01));
            log.info("Refund applied: {}", refund);
        }

        // For admin cancellation with a refund, update the order status, cancellation reason, and cancellation time by order ID
        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.ORDER_STATUS_CANCELED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        ordersMapper.updateById(orders);
    }


    @Override
    public void delivery(Long id) {
        //1. Query the order by ID
        Orders orders = ordersMapper.selectById(id);

        //2. Check if the order exists and if the status is correct
        if (orders == null || orders.getStatus() != Orders.ORDER_STATUS_ACCEPTED) {
            throw new BusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //3. Change the order status to delivery in progress
        Orders o = Orders.builder()
                .id(id)
                .status(Orders.ORDER_STATUS_DELIVERY_IN_PROGRESS)
                .build();

        ordersMapper.updateById(o);
    }


    @Override
    public void complete(Long id) {
        //1. Query the order by ID
        Orders orders = ordersMapper.selectById(id);

        //2. Check if the order exists and if the status is correct
        if (orders == null || orders.getStatus() != Orders.ORDER_STATUS_DELIVERY_IN_PROGRESS) {
            throw new BusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //3. Change the order status to completed
        Orders o = Orders.builder()
                .id(id)
                .status(Orders.ORDER_STATUS_COMPLETED)
                .deliveryTime(LocalDateTime.now())
                .build();

        ordersMapper.updateById(o);
    }


    private List<OrderVO> getOrderVOList(IPage<Orders> page) {
        // Returning order dish information, customizing OrderVO for response
        List<OrderVO> orderVOList = new ArrayList<>();

        List<Orders> ordersList = page.getRecords();
        if (!CollectionUtils.isEmpty(ordersList)) {
            for (Orders orders : ordersList) {
                // Copy common fields to OrderVO
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                String orderDishes = getOrderDishesStr(orders);

                // Encapsulate the order dish information into OrderVO and add it to the list
                orderVO.setOrderDishes(orderDishes);
                orderVOList.add(orderVO);
            }
        }
        return orderVOList;
    }

    private String getOrderDishesStr(Orders orders) {
        // Query order dish details (dish and quantity in the order)
        List<OrderDetail> orderDetailList = orderDetailMapper.selectList(new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getOrderId, orders.getId()));

        // Concatenate each order dish information into a string (format: Kung Pao Chicken*3;)
        List<String> orderDishList = orderDetailList.stream().map(x -> {
            String orderDish = x.getName() + "*" + x.getNumber() + ";";
            return orderDish;
        }).collect(Collectors.toList());

        // Concatenate all dish information for this order
        return String.join("", orderDishList);
    }
}
