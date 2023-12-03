package com.easy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {

    /**
     * Order status: 1 for pending payment, 2 for pending acceptance, 3 for accepted, 4 for in delivery,
     * 5 for completed, 6 for canceled, 7 for refunded
     */
    public static final Integer ORDER_STATUS_PENDING_PAYMENT = 1; // Pending payment
    public static final Integer ORDER_STATUS_PENDING_ACCEPTANCE = 2; // Pending acceptance
    public static final Integer ORDER_STATUS_ACCEPTED = 3; // Accepted
    public static final Integer ORDER_STATUS_DELIVERY_IN_PROGRESS = 4; // Delivery in progress
    public static final Integer ORDER_STATUS_COMPLETED = 5; // Completed
    public static final Integer ORDER_STATUS_CANCELED = 6; // Canceled
    public static final Integer ORDER_STATUS_REFUNDED = 7; // Refunded

    /**
     * Payment status: 0 for unpaid, 1 for paid, 2 for refunded
     */
    public static final Integer PAYMENT_STATUS_UNPAID = 0; // Unpaid
    public static final Integer PAYMENT_STATUS_PAID = 1; // Paid
    public static final Integer PAYMENT_STATUS_REFUNDED = 2; // Refunded

    private static final long serialVersionUID = 1L;

    private Long id;

    // Order number
    private String number;

    // Order status: 1 for pending payment, 2 for pending acceptance, 3 for accepted, 4 for in delivery,
    // 5 for completed, 6 for canceled, 7 for refunded
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

    // Payment status: 0 for unpaid, 1 for paid, 2 for refunded
    private Integer payStatus;

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

    // Order cancellation reason
    private String cancelReason;

    // Order rejection reason
    private String rejectionReason;

    // Order cancellation time
    private LocalDateTime cancelTime;

    // Estimated delivery time
    private LocalDateTime estimatedDeliveryTime;

    // Delivery status: 1 for immediate delivery, 0 for choosing a specific time
    private Integer deliveryStatus;

    // Delivery time
    private LocalDateTime deliveryTime;

    // Packaging fee
    private int packAmount;

    // Tableware quantity
    private int tablewareNumber;

    // Tableware status: 1 for providing according to the number of diners, 0 for choosing a specific quantity
    private Integer tablewareStatus;
}
