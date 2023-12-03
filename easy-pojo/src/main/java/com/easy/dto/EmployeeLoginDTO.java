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
@ApiModel("Employee Login Information Wrapper")
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLoginDTO implements Serializable {

    @ApiModelProperty("Username")
    private String username;

    @ApiModelProperty("Password")
    private String password;

}
