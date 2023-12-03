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
public class CategoryPageQueryDTO implements Serializable {

    // Page number
    private int page;

    // Records per page
    private int pageSize;

    // Category name
    private String name;

    // Category type: 1 for dish category, 2 for combo category
    private Integer type;

}
