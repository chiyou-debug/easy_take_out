package com.easy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easy.dto.SalesReportDTO;
import com.easy.entity.Orders;
import com.easy.entity.User;
import com.easy.mapper.OrdersMapper;
import com.easy.mapper.UserMapper;
import com.easy.service.ReportService;
import com.easy.vo.OrderReportVO;
import com.easy.vo.SalesTop10ReportVO;
import com.easy.vo.TurnoverReportVO;
import com.easy.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements ReportService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {

        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // Query the turnover data for the date
            // select sum(amount) from orders where order_time > ? and order_time < ? and status = 5
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            List<Orders> orders = ordersMapper.selectList(new LambdaQueryWrapper<Orders>()
                    .gt(Orders::getOrderTime, beginTime)
                    .lt(Orders::getOrderTime, endTime)
                    .eq(Orders::getStatus, Orders.ORDER_STATUS_COMPLETED));

            Double amount = 0.0;
            for (Orders order : orders) {
                amount += order.getAmount().doubleValue();
            }
            turnoverList.add(amount);
        }

        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    @Override
    public UserReportVO getUserStatics(LocalDate begin, LocalDate end) {

        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Long newUserCount = userMapper.selectCount(new LambdaQueryWrapper<User>()
                    .gt(User::getCreateTime, beginTime)
                    .lt(User::getCreateTime, endTime));
            Long totalUserCount = userMapper.selectCount(new LambdaQueryWrapper<User>()
                    .lt(User::getCreateTime, endTime));
            totalUserList.add(Integer.valueOf(totalUserCount.intValue()));
            newUserList.add(Integer.valueOf(newUserCount.intValue()));
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .build();
    }

    @Override
    public OrderReportVO getOrdersStatics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            // Query the total number of orders for each day
            Long orderCount = ordersMapper.selectCount(new LambdaQueryWrapper<Orders>()
                    .gt(Orders::getOrderTime, beginTime)
                    .lt(Orders::getOrderTime, endTime));

            // Query the number of valid orders for each day
            Long validOrderCount = ordersMapper.selectCount(new LambdaQueryWrapper<Orders>()
                    .gt(Orders::getOrderTime, beginTime)
                    .lt(Orders::getOrderTime, endTime)
                    .eq(Orders::getStatus, Orders.ORDER_STATUS_COMPLETED));

            orderCountList.add(Integer.valueOf(orderCount.intValue()));
            validOrderCountList.add(Integer.valueOf(validOrderCount.intValue()));
        }

        // Calculate the total number of orders within the time period
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).orElse(0);

        // Calculate the number of valid orders within the time period
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).orElse(0);

        // Calculate the order completion rate
        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount.doubleValue();
        }

        return OrderReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    @Override
    public SalesTop10ReportVO getTop10Sales(LocalDate begin, LocalDate end) {

        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<SalesReportDTO> top10Sales = ordersMapper.getTop10Sales(beginTime, endTime);

        log.info("Top10 sales ==== {}", top10Sales);

        List<String> names = top10Sales.stream().map(SalesReportDTO::getGoodsName).collect(Collectors.toList());
        String nameList = StringUtils.join(names, ",");

        List<Integer> numbers = top10Sales.stream().map(SalesReportDTO::getGoodsNumber).collect(Collectors.toList());
        String numberList = StringUtils.join(numbers, ",");

        return SalesTop10ReportVO
                .builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }
}

