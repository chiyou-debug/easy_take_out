package com.easy.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishItemVO implements Serializable {

    // Dish name
    private String name;

    // Number of portions
    private Integer copies;

    // Dish image
    private String image;

    // Dish description
    private String description;
}
