package com.easy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@ApiModel("Employee Data Model")
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO implements Serializable {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("Username")
    private String username;

    @ApiModelProperty("Name")
    private String name;

    @ApiModelProperty("Phone Number")
    private String phone;

    @ApiModelProperty("Gender")
    private Integer sex;

    @ApiModelProperty("ID Number")
    private String idNumber;
}
