package com.easy.dto;

import com.easy.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDTO implements Serializable {

    private Long id;

    // Order number
    private String number;

    // Order status: 1 for pending payment, 2 for pending delivery, 3 for delivered, 4 for completed, 5 for canceled
    private Integer status;

    // User ID who placed the order
    private Long userId;

    // Address ID
    private Long addressBookId;

    // Order placement time
    private LocalDateTime orderTime;

    // Checkout time
    private LocalDateTime checkoutTime;

    // Payment method: 1 for WeChat, 2 for Alipay
    private Integer payMethod;

    // Actual amount received
    private BigDecimal amount;

    // Remark
    private String remark;

    // Phone number
    private String phone;

    // Address
    private String address;

    // Recipient
    private String consignee;

    private List<OrderDetail> orderDetails;

}
