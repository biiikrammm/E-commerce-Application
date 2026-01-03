package com.ecommerce.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * Response object for cart item information
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {

   /**
    * Unique identifier for the cart item
    */
   private Long id;

   /**
    * Associated product information
    */
   private ProductResponse product;

   /**
    * Quantity of the product in cart
    */
   private Integer quantity;

   /**
    * Calculated subtotal for this cart item
    */
   private BigDecimal subtotal;
}