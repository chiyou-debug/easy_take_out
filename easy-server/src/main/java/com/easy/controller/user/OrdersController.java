package com.easy.controller.user;

import com.easy.dto.OrdersPaymentDTO;
import com.easy.dto.OrdersSubmitDTO;
import com.easy.result.PageResult;
import com.easy.result.Result;
import com.easy.service.OrdersService;
import com.easy.vo.OrderPaymentVO;
import com.easy.vo.OrderSubmitVO;
import com.easy.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "C-end Order Operations")
@RestController
@RequestMapping("/user/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @ApiOperation("Submit Order")
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("User placing an order...");
        OrderSubmitVO orderSubmitVO = ordersService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * Order payment
     *
     * @param ordersPaymentDTO the DTO containing payment details
     * @return the result of the payment operation
     * @throws Exception if an error occurs during the payment process
     */
    @PutMapping("/payment")
    @ApiOperation("Order Payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("Order payment: {}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = ordersService.payment(ordersPaymentDTO);
        log.info("Generating prepayment transaction order: {}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * page query for the history orders
     *
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    @GetMapping("/historyOrders")
    @ApiOperation("query history orders")
    public Result<PageResult> page(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   Integer status) {
        PageResult pageResult = ordersService.pageQueryHistoryOrders(page, pageSize, status);
        return Result.success(pageResult);
    }

    /**
     * query order details
     *
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("query order details")
    public Result<OrderVO> details(@PathVariable("id") Long id) {
        OrderVO orderVO = ordersService.details(id);
        return Result.success(orderVO);
    }

    /**
     * cancel order
     *
     * @return
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("cancel order")
    public Result cancelOrder(@PathVariable Long id) throws Exception {
        log.info("cancel order, order ID: {}", id);
        ordersService.cancelOrder(id);
        return Result.success();
    }


}
