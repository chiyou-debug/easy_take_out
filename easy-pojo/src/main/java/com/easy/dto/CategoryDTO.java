package com.easy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO implements Serializable {

    // Primary Key
    private Long id;

    // Type: 1 for dish category, 2 for combo category
    private Integer type;

    // Category Name
    private String name;

    // Sorting Order
    private Integer sort;

}
