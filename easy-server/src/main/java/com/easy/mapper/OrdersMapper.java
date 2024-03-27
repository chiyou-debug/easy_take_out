package com.easy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easy.dto.SalesReportDTO;
import com.easy.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

    /**
     * Statistics for the top 10 sales within a specified interval
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    @Select("select od.name as goodsName, sum(od.number) as goodsNumber from order_detail od, orders o where od.order_id = o.id and o.status = 5 and o.order_time > #{beginTime} and o.order_time < #{endTime} group by od.name order by sum(od.number) desc limit 0,10")
    List<SalesReportDTO> getTop10Sales(LocalDateTime beginTime, LocalDateTime endTime);
}
