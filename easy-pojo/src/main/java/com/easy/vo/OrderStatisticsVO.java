package com.easy.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatisticsVO implements Serializable {
    // Number of orders waiting to be confirmed
    private Integer toBeConfirmed;

    // Number of confirmed orders waiting to be delivered
    private Integer confirmed;

    // Number of orders currently in delivery progress
    private Integer deliveryInProgress;
}
