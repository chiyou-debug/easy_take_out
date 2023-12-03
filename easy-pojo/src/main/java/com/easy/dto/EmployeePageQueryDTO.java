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
public class EmployeePageQueryDTO implements Serializable {

    // Employee name
    private String name;

    // Page number
    private int page;

    // Number of records per page
    private int pageSize;
}
