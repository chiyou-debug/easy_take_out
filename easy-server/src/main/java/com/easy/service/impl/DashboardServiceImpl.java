package com.easy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.easy.constant.StatusConstant;
import com.easy.entity.Dish;
import com.easy.entity.Orders;
import com.easy.entity.Setmeal;
import com.easy.entity.User;
import com.easy.mapper.DishMapper;
import com.easy.mapper.OrdersMapper;
import com.easy.mapper.SetmealMapper;
import com.easy.mapper.UserMapper;
import com.easy.service.DashboardService;
import com.easy.vo.BusinessDataVO;
import com.easy.vo.DishOverViewVO;
import com.easy.vo.OrderOverViewVO;
import com.easy.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private OrdersMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * Statistics business data by time period
     *
     * @return
     */
    public BusinessDataVO getBusinessData() {

        // Get the start time of the day
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        // Get the end time of the day
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);

        // Query total order count
        Integer totalOrderCount = Math.toIntExact(orderMapper.selectCount(new LambdaQueryWrapper<Orders>()
                .gt(Orders::getOrderTime, begin)
                .lt(Orders::getOrderTime, end)));


        // Turnover
        List<Orders> ordersList = orderMapper.selectList(new LambdaQueryWrapper<Orders>()
                .gt(Orders::getOrderTime, begin)
                .lt(Orders::getOrderTime, end)
                .eq(Orders::getStatus, Orders.ORDER_STATUS_COMPLETED));
        Double turnover = 0.0;
        for (Orders orders : ordersList) {
            turnover += orders.getAmount().doubleValue();
        }

        // Valid order count
        Integer validOrderCount = Math.toIntExact(orderMapper.selectCount(new LambdaQueryWrapper<Orders>()
                .gt(Orders::getOrderTime, begin)
                .lt(Orders::getOrderTime, end)
                .eq(Orders::getStatus, Orders.ORDER_STATUS_COMPLETED)));

        Double unitPrice = 0.0;  // Average unit price
        Double orderCompletionRate = 0.0; // Order completion rate
        if (totalOrderCount != 0 && validOrderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            unitPrice = turnover / validOrderCount;
        }

        // New user count
        Integer newUsers = Math.toIntExact(userMapper.selectCount(new LambdaQueryWrapper<User>()
                .gt(User::getCreateTime, begin)
                .lt(User::getCreateTime, end)));

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }


    /**
     * Query order management data
     *
     * @return
     */
    public OrderOverViewVO getOrderOverView() {

        LocalDateTime begin = LocalDateTime.now().with(LocalDateTime.MIN);

        // Waiting for acceptance
        Integer waitingOrders = orderMapper.selectCount(new LambdaQueryWrapper<Orders>()
                        .gt(Orders::getOrderTime, begin)
                        .eq(Orders::getStatus, Orders.ORDER_STATUS_PENDING_ACCEPTANCE))
                .intValue();

        // Pending delivery
        Integer deliveredOrders = orderMapper.selectCount(new LambdaQueryWrapper<Orders>()
                        .gt(Orders::getOrderTime, begin)
                        .eq(Orders::getStatus, Orders.ORDER_STATUS_ACCEPTED))
                .intValue();

        // Completed
        Integer completedOrders = orderMapper.selectCount(new LambdaQueryWrapper<Orders>()
                        .gt(Orders::getOrderTime, begin)
                        .eq(Orders::getStatus, Orders.ORDER_STATUS_COMPLETED))
                .intValue();

        // Canceled
        Integer cancelledOrders = orderMapper.selectCount(new LambdaQueryWrapper<Orders>()
                        .gt(Orders::getOrderTime, begin)
                        .eq(Orders::getStatus, Orders.ORDER_STATUS_CANCELED))
                .intValue();

        // All orders
        Integer allOrders = orderMapper.selectCount(new LambdaQueryWrapper<Orders>()
                .gt(Orders::getOrderTime, begin)).intValue();

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    /**
     * Query dish overview
     *
     * @return
     */
    public DishOverViewVO getDishOverView() {

        Integer sold = dishMapper.selectCount(new LambdaQueryWrapper<Dish>()
                .eq(Dish::getStatus, StatusConstant.ENABLE)).intValue();

        Integer discontinued = dishMapper.selectCount(new LambdaQueryWrapper<Dish>()
                .eq(Dish::getStatus, StatusConstant.DISABLE)).intValue();

        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * Query setmeal overview
     *
     * @return
     */
    public SetmealOverViewVO getSetmealOverView() {

        Integer sold = setmealMapper.selectCount(new LambdaQueryWrapper<Setmeal>()
                .eq(Setmeal::getStatus, StatusConstant.ENABLE)).intValue();

        Integer discontinued = setmealMapper.selectCount(new LambdaQueryWrapper<Setmeal>()
                .eq(Setmeal::getStatus, StatusConstant.DISABLE)).intValue();

        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
