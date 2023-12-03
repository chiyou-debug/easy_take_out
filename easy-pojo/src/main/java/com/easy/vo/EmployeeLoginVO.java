package com.easy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@ApiModel("Login Result Wrapper Model")
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLoginVO implements Serializable {

    @ApiModelProperty("Unique Identifier")
    private Long id; // ID

    @ApiModelProperty("Username")
    private String userName; // Username

    @ApiModelProperty("Name")
    private String name; // Name

    @ApiModelProperty("Token")
    private String token; // Token
}
