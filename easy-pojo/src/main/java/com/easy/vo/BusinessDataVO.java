package com.easy.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Business Data Overview
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDataVO implements Serializable {

    private Double turnover; // Turnover

    private Integer validOrderCount; // Valid order count

    private Double orderCompletionRate; // Order completion rate

    private Double unitPrice; // Average unit price

    private Integer newUsers; // New users count
}
