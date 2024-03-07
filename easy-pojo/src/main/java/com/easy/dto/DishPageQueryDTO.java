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
public class DishPageQueryDTO implements Serializable {

    private int page;

    private int pageSize;

    private String name;

    // Category ID
    private Long categoryId;

    // Status: 0 for disabled, 1 for enabled
    private Integer status;

}
