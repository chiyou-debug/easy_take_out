package com.easy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Set meal
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Setmeal implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // Category ID
    private Long categoryId;

    // Set meal name
    private String name;

    // Set meal price
    private BigDecimal price;

    // Status: 0 for disabled, 1 for enabled
    private Integer status;

    // Description
    private String description;

    // Image
    private String image;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}
