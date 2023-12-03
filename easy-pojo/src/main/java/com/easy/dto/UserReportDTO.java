package com.easy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReportDTO implements Serializable {

    private String createDate; // Registration date, e.g., 2023-01-01
    private Integer userCount; // Number of registered users, e.g., 23

}
