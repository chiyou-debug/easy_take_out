package com.easy.dto;

import com.easy.entity.SetmealDish;
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
public class SetmealDTO implements Serializable {

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

    // Set meal and dish relationships
    private List<SetmealDish> setmealDishes = new ArrayList<>();

}
