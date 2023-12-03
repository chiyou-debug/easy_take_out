package com.easy.vo;

import com.easy.entity.DishFlavor;
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
public class DishVO implements Serializable {

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

    // 0 for discontinued, 1 for available
    private Integer status;

    // Update time
    private LocalDateTime updateTime;

    // Category name
    private String categoryName;

    // List of flavors associated with the dish
    private List<DishFlavor> flavors = new ArrayList<>();

}
