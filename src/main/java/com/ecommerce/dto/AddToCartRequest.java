package com.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Request object for adding items to shopping cart
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddToCartRequest {

   /**
    * Product ID to add to cart
    */
   @NotNull(message = "Product ID is required")
   private Long productId;

   /**
    * Quantity of product to add
    */
   @NotNull(message = "Quantity is required")
   @Min(value = 1, message = "Quantity must be at least 1")
   @Max(value = 100, message = "Quantity cannot exceed 100")
   private Integer quantity;
}