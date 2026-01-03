package com.ecommerce.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response object containing order information
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

   /**
    * Unique identifier for the order
    */
   private Long id;

   /**
    * Human-readable order number
    */
   private String orderNumber;

   /**
    * Customer name
    */
   private String customerName;

   /**
    * Customer email
    */
   private String customerEmail;

   /**
    * Shipping address
    */
   private String shippingAddress;

   /**
    * List of items in the order
    */
   private List<OrderItemResponse> orderItems;

   /**
    * Total amount for the order
    */
   private BigDecimal totalAmount;

   /**
    * Current order status
    */
   private String status;

   /**
    * Payment status
    */
   private String paymentStatus;

   /**
    * Date and time when order was placed
    */
   private LocalDateTime orderDate;

   /**
    * Date and time when order was created
    */
   private LocalDateTime createdAt;

   /**
    * Date and time when order was last updated
    */
   private LocalDateTime updatedAt;
}