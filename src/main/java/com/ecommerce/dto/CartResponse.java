package com.ecommerce.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response object containing shopping cart information
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {

   /**
    * List of items in the shopping cart
    */
   private List<CartItemResponse> items;

   /**
    * Total amount for all items in cart
    */
   private BigDecimal totalAmount;

   /**
    * Total number of items in cart
    */
   private Integer itemCount;

   /**
    * Session identifier for the cart
    */
   private String sessionId;
}