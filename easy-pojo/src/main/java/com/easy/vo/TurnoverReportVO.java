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
public class TurnoverReportVO implements Serializable {

    // List of dates, e.g., 2022-10-01, 2022-10-02, 2022-10-03
    private String dateList; //private List dateList;

    // List of turnovers, e.g., 406.0, 1520.0, 75.0
    private String turnoverList; //private List turnoverList;
}
