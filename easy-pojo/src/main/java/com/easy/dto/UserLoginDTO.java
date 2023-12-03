package com.easy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * C-end user login
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO implements Serializable {

    private String code;

}
