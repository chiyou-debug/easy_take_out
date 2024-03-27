package com.easy.controller.admin;

import com.easy.result.Result;
import com.easy.service.ReportService;
import com.easy.vo.OrderReportVO;
import com.easy.vo.SalesTop10ReportVO;
import com.easy.vo.TurnoverReportVO;
import com.easy.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Statistics-related interfaces
 */
@RestController
@RequestMapping("/admin/report")
@Api(tags = "Data statistics-related interfaces")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * Turnover statistics
     *
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation("Turnover statistics")
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("Turnover statistics, {}, {}", begin, end);
        return Result.success(reportService.getTurnoverStatistics(begin, end));
    }

    /**
     * User count statistics interface
     *
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation("User count statistics interface")
    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("User count statistics: {}, {}", begin, end);
        return Result.success(reportService.getUserStatics(begin, end));
    }

    /**
     * Order statistics
     *
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation("Order statistics")
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> ordersStatics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("Order statistics: {}, {}", begin, end);
        return Result.success(reportService.getOrdersStatics(begin, end));
    }

    /**
     * Get top 10 sales of dishes or sets
     *
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation("Top 10 sales ranking")
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> salesTop10(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                 @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("Top 10 sales ranking: {}, {}", begin, end);
        return Result.success(reportService.getTop10Sales(begin, end));
    }
}
