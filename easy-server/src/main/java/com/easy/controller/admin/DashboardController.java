package com.easy.controller.admin;

import com.easy.result.Result;
import com.easy.service.DashboardService;
import com.easy.vo.BusinessDataVO;
import com.easy.vo.DishOverViewVO;
import com.easy.vo.OrderOverViewVO;
import com.easy.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Dashboard
 */
@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "Dashboard related interfaces")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * Query today's data on the dashboard
     * @return
     */
    @GetMapping("/businessData")
    @ApiOperation("Query today's data on the dashboard")
    public Result<BusinessDataVO> businessData(){
        BusinessDataVO businessDataVO = dashboardService.getBusinessData();
        return Result.success(businessDataVO);
    }

    /**
     * Query order management data
     * @return
     */
    @GetMapping("/overviewOrders")
    @ApiOperation("Query order management data")
    public Result<OrderOverViewVO> orderOverView(){
        return Result.success(dashboardService.getOrderOverView());
    }

    /**
     * Query dishes overview
     * @return
     */
    @GetMapping("/overviewDishes")
    @ApiOperation("Query dishes overview")
    public Result<DishOverViewVO> dishOverView(){
        return Result.success(dashboardService.getDishOverView());
    }

    /**
     * Query set meals overview
     * @return
     */
    @GetMapping("/overviewSetmeals")
    @ApiOperation("Query set meals overview")
    public Result<SetmealOverViewVO> setmealOverView(){
        return Result.success(dashboardService.getSetmealOverView());
    }
}
