package com.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Request object for updating cart item quantity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCartRequest {

   /**
    * Product ID to update in the cart
    */
   @NotNull(message = "Product ID is required")
   private Long productId;

   /**
    * New quantity for the cart item
    */
   @NotNull(message = "Quantity is required")
   @Min(value = 1, message = "Quantity must be at least 1")
   @Max(value = 100, message = "Quantity cannot exceed 100")
   private Integer quantity;
}