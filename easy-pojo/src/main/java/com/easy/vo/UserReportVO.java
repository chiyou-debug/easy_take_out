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
public class UserReportVO implements Serializable {

    // List of dates, e.g., 2022-10-01, 2022-10-02, 2022-10-03
    private List dateList;

    // Total number of users, e.g., 200, 210, 220
    private List totalUserList;

    // New users, e.g., 20, 21, 10
    private List newUserList;

}
