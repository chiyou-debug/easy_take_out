package com.easy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.easy.constant.MessageConstant;
import com.easy.context.BaseContext;
import com.easy.dto.OrdersPaymentDTO;
import com.easy.dto.OrdersSubmitDTO;
import com.easy.entity.*;
import com.easy.exception.BusinessException;
import com.easy.mapper.*;
import com.easy.service.OrdersService;
import com.easy.utils.BeanHelper;
import com.easy.utils.WeChatPayUtil;
import com.easy.vo.OrderPaymentVO;
import com.easy.vo.OrderSubmitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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

    @Transactional
    @Override
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        log.info("User placing an order...");

        // 0. Querying recipient address information. Cannot place an order if the address is empty.
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new BusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // 0. Cannot place an order if the shopping cart data is empty.
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(BaseContext.getCurrentId()).build();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
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
        orders.setAddress(addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee()); // Recipient

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
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());

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
                "SkyTakeout Order", // Product description
                user.getOpenid() // WeChat user's openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new BusinessException("This order has been paid");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * Pay success, modify order status.
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {
        // Query the current user's order based on the order number
        Orders ordersDB = ordersMapper.getByNumber(outTradeNo);

        // Update the order status, payment method, payment status, and checkout time based on the order ID
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.ORDER_STATUS_PENDING_ACCEPTANCE)
                .payStatus(Orders.PAYMENT_STATUS_PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        ordersMapper.update(orders);
    }
}
