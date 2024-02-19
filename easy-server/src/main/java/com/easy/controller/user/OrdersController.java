package com.easy.controller.user;

import com.easy.dto.OrdersPaymentDTO;
import com.easy.dto.OrdersSubmitDTO;
import com.easy.result.Result;
import com.easy.service.OrdersService;
import com.easy.vo.OrderPaymentVO;
import com.easy.vo.OrderSubmitVO;
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

}
