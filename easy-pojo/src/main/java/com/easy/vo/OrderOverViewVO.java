package com.easy.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Order Overview Data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderOverViewVO implements Serializable {
    // Number of orders waiting to be accepted
    private Integer waitingOrders;

    // Number of orders waiting to be delivered
    private Integer deliveredOrders;

    // Number of completed orders
    private Integer completedOrders;

    // Number of cancelled orders
    private Integer cancelledOrders;

    // Total number of orders
    private Integer allOrders;
}
