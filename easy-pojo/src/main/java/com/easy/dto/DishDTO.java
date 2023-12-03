package com.easy.dto;

import com.easy.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishDTO implements Serializable {

    private Long id;
    // Dish name
    private String name;
    // Category ID of the dish
    private Long categoryId;
    // Dish price
    private BigDecimal price;
    // Image
    private String image;
    // Description
    private String description;
    // 0 for discontinued, 1 for available
    private Integer status;
    // Flavors
    private List<DishFlavor> flavors = new ArrayList<>();

}
