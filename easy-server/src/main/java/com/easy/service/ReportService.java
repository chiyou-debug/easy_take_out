package com.easy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easy.entity.Orders;
import com.easy.vo.OrderReportVO;
import com.easy.vo.SalesTop10ReportVO;
import com.easy.vo.TurnoverReportVO;
import com.easy.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService extends IService<Orders> {

    /**
     * Statistics of turnover data within a specified time period
     *
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * User count statistics
     *
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserStatics(LocalDate begin, LocalDate end);

    /**
     * Order statistics
     *
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrdersStatics(LocalDate begin, LocalDate end);

    /**
     * Get top 10 sales of dishes or sets
     *
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getTop10Sales(LocalDate begin, LocalDate end);
}
