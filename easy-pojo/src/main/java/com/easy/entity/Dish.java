package com.easy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Dish
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // Dish name
    private String name;

    // Dish category ID
    private Long categoryId;

    // Dish price
    private BigDecimal price;

    // Image
    private String image;

    // Description
    private String description;

    // Status: 0 for discontinued, 1 for available
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}
