package com.easy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersSubmitDTO implements Serializable {
    // Address book ID
    private Long addressBookId;
    // Payment method
    private int payMethod;
    // Remark
    private String remark;
    // Estimated delivery time
    private LocalDateTime estimatedDeliveryTime;
    // Delivery status: 1 for immediate delivery, 0 for choosing a specific time
    private Integer deliveryStatus;
    // Tableware quantity
    private Integer tablewareNumber;
    // Tableware status: 1 for providing according to the number of diners, 0 for choosing a specific quantity
    private Integer tablewareStatus;
    // Packaging fee
    private Integer packAmount;
    // Total amount
    private BigDecimal amount;
}
