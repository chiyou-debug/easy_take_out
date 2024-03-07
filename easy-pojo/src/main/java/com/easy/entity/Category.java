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
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // Type: 1 for dish category, 2 for combo category
    private Integer type;

    // Category name
    private String name;

    // Order
    private Integer sort;

    // Category status: 0 for disabled, 1 for enabled
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
