package com.easy.controller.admin;

import com.easy.dto.OrdersCancelDTO;
import com.easy.dto.OrdersConfirmDTO;
import com.easy.dto.OrdersPageQueryDTO;
import com.easy.dto.OrdersRejectionDTO;
import com.easy.result.PageResult;
import com.easy.result.Result;
import com.easy.service.OrdersService;
import com.easy.vo.OrderStatisticsVO;
import com.easy.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Order Management
 */
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "Order Management Interface")
public class OrderController {

    @Autowired
    private OrdersService orderService;

    /**
     * Order Search
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("Order Search")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Statistics of order quantities by status
     *
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation("Statistics of order quantities by status")
    public Result<OrderStatisticsVO> statistics() {
        log.info("Statistics of order quantities by status");
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * Accept order
     *
     * @return
     */
    @PutMapping("/confirm")
    @ApiOperation("Accept Order")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("Accepting order: {}", ordersConfirmDTO);
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }


    /**
     * Reject order
     *
     * @return
     */
    @PutMapping("/rejection")
    @ApiOperation("Reject Order")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        log.info("Rejecting order: {}", ordersRejectionDTO);
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }


    /**
     * Cancel order
     *
     * @return
     */
    @PutMapping("/cancel")
    @ApiOperation("Cancel Order")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        log.info("Cancelling order: {}", ordersCancelDTO);
        orderService.cancelByAdmin(ordersCancelDTO);
        return Result.success();
    }


    /**
     * Deliver order
     *
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("Deliver Order")
    public Result delivery(@PathVariable Long id) {
        log.info("Delivering order: {}", id);
        orderService.delivery(id);
        return Result.success();
    }


    /**
     * Complete order
     *
     * @return
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("Complete Order")
    public Result complete(@PathVariable Long id) {
        log.info("Completing order: {}", id);
        orderService.complete(id);
        return Result.success();
    }

    /**
     * query oder details on backend side
     *
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    @ApiOperation("query order details")
    public Result<OrderVO> details(@PathVariable("id") Long id) {
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }
}
