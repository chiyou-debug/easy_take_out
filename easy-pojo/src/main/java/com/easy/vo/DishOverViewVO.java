package com.easy.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Dish Overview
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishOverViewVO implements Serializable {
    // Number of dishes currently available for sale
    private Integer sold;

    // Number of discontinued dishes
    private Integer discontinued;
}
