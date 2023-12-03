package com.easy.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderReportVO implements Serializable {

    // Dates, e.g., 2022-10-01, 2022-10-02, 2022-10-03
    private List dateList;

    // Daily order counts, e.g., 260, 210, 215
    private List orderCountList;

    // Daily valid order counts, e.g., 20, 21, 10
    private List validOrderCountList;

    // Total order count
    private Integer totalOrderCount;

    // Valid order count
    private Integer validOrderCount;

    // Order completion rate
    private Double orderCompletionRate;
}
