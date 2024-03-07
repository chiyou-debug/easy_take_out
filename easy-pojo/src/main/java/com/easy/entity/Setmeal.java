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
