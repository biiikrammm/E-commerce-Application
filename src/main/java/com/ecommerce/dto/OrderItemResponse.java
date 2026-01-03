package com.ecommerce.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * Response object for order item information
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

   /**
    * Unique identifier for the order item
    */
   private Long id;

   /**
    * Product information
    */
   private ProductResponse product;

   /**
    * Quantity ordered
    */
   private Integer quantity;

   /**
    * Price at the time of purchase
    */
   private BigDecimal priceAtPurchase;

   /**
    * Subtotal for this order item
    */
   private BigDecimal subtotal;
}