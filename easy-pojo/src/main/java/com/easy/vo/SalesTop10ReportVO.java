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
public class SalesTop10ReportVO implements Serializable {

    // List of product names, e.g., Sweet and Sour Pork, Kung Pao Chicken, Boiled Fish
    private String nameList;

    // List of sales quantities, e.g., 260, 215, 200
    private String numberList;

}
