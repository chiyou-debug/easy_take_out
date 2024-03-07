package com.easy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Shopping Cart
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // Name
    private String name;

    // User ID
    private Long userId;

    // Dish ID
    private Long dishId;

    // Set meal ID
    private Long setmealId;

    // Flavor
    private String dishFlavor;

    // Quantity
    private Integer number;

    // Amount
    private BigDecimal amount;

    // Image
    private String image;

    // Creation time
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
