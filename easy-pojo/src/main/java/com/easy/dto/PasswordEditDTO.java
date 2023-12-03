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
public class PasswordEditDTO implements Serializable {

    // Employee ID
    private Long empId;

    // Old password
    private String oldPassword;

    // New password
    private String newPassword;

}
