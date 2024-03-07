package com.easy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // Username
    private String username;

    // Name
    private String name;

    // Password
    private String password;

    // Phone number
    private String phone;

    // Gender: 0 for male, 1 for female
    private Integer sex;

    // ID number
    private String idNumber;

    // Status: 1 for active, 0 for disabled
    private Integer status;

    // Creation time
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // Modification time
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // Creator
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    // Modifier
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
