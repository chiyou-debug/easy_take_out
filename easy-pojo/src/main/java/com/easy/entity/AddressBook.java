package com.easy.entity;

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
    private String consignee;

    // Phone number
    private String phone;

    // Gender: 0 for male, 1 for female
    private Integer sex;

    // Province code
    private String provinceCode;

    // Province name
    private String provinceName;

    // City code
    private String cityCode;

    // City name
    private String cityName;

    // District code
    private String districtCode;

    // District name
    private String districtName;

    // Detailed address
    private String detail;

    // Label
    private String label;

    // Default: 0 for no, 1 for yes
    private Integer isDefault;

    // Creation time
    //private LocalDateTime createTime;
}
