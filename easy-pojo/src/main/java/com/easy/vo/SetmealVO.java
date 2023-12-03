package com.easy.vo;

import com.easy.entity.SetmealDish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealVO implements Serializable {

    private Long id;

    // Category ID
    private Long categoryId;

    // Setmeal name
    private String name;

    // Setmeal price
    private BigDecimal price;

    // Status: 0 for disabled, 1 for enabled
    private Integer status;

    // Description
    private String description;

    // Image
    private String image;

    // Update time
    private LocalDateTime updateTime;

    // Category name
    private String categoryName;

    // Relationship between setmeal and dishes
    private List<SetmealDish> setmealDishes = new ArrayList<>();
}
