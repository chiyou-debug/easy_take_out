package com.easy.service.impl;

import com.easy.constant.MessageConstant;
import com.easy.context.BaseContext;
import com.easy.dto.OrdersSubmitDTO;
import com.easy.entity.AddressBook;
import com.easy.entity.OrderDetail;
import com.easy.entity.Orders;
import com.easy.entity.ShoppingCart;
import com.easy.exception.BusinessException;
import com.easy.mapper.AddressBookMapper;
import com.easy.mapper.OrderDetailMapper;
import com.easy.mapper.OrdersMapper;
import com.easy.mapper.ShoppingCartMapper;
import com.easy.service.OrdersService;
import com.easy.utils.BeanHelper;
import com.easy.vo.OrderSubmitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

}
