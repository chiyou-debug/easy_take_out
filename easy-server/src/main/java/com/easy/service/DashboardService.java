package com.easy.service;

import com.easy.vo.BusinessDataVO;
import com.easy.vo.DishOverViewVO;
import com.easy.vo.OrderOverViewVO;
import com.easy.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface DashboardService {
    /**
     * Query today's data on the dashboard
     * @return
     */
    BusinessDataVO getBusinessData();

    /**
     * Query order management data
     * @return
     */
    OrderOverViewVO getOrderOverView();

    /**
     * Query dishes overview
     * @return
     */
    DishOverViewVO getDishOverView();

    /**
     * Query set meals overview
     * @return
     */
    SetmealOverViewVO getSetmealOverView();
}
