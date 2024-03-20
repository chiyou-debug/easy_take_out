package com.easy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Address Book
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // User ID
    private Long userId;

    // Recipient
    private String name;

    // Phone number
    private String phone;

    // Address
    private String address;

    // City or Town
    private String city;

    // Eir code
    private String eircode;

    // Default: 0 for no, 1 for yes
    private Integer isDefault;

    // Address Label
    private String addressLabel;

    // Creation time
    private LocalDateTime createTime;

    // Update time
    private LocalDateTime updateTime;
}
