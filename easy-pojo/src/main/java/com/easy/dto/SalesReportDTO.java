package com.easy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesReportDTO implements Serializable {
    // Product name
    private String goodsName;

    // Sales quantity
    private Integer goodsNumber;
}
