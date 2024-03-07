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

    // Creation time
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // Modification time
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // Creator
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    // Modifier
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
