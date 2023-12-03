package com.easy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Set meal and dish relationship
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealDish implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // Set meal ID
    private Long setmealId;

    // Dish ID
    private Long dishId;

    // Dish name (redundant field)
    private String name;

    // Original price of the dish
    private BigDecimal price;

    // Number of portions
    private Integer copies;
}
