package com.easy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Order Detail
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // Order ID
    private Long orderId;

    // Dish ID
    private Long dishId;

    // Set meal ID
    private Long setmealId;

    // Name
    private String name;

    // Image
    private String image;

    // Flavor
    private String dishFlavor;

    // Quantity
    private Integer number;

    // Amount
    private BigDecimal amount;
}
