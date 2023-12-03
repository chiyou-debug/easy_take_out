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
public class OrdersConfirmDTO implements Serializable {

    private Long id;
    // Order status: 1 for pending payment, 2 for pending acceptance, 3 for accepted, 4 for in delivery, 5 for completed, 6 for canceled, 7 for refunded
    private Integer status;

}
