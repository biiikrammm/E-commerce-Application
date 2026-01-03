package com.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Request object for creating a new order
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {

   /**
    * Session ID containing the cart items
    */
   @NotBlank(message = "Session ID is required")
   private String sessionId;

   /**
    * Customer's full name
    */
   @NotBlank(message = "Customer name is required")
   @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
   private String customerName;

   /**
    * Customer's email address
    */
   @NotBlank(message = "Email is required")
   @Email(message = "Invalid email format")
   private String customerEmail;

   /**
    * Customer's phone number for delivery coordination
    */
   @NotBlank(message = "Phone number is required")
   @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number format")
   private String phoneNumber;

   /**
    * Complete shipping address
    */
   @NotBlank(message = "Shipping address is required")
   @Size(min = 10, max = 500, message = "Shipping address must be between 10 and 500 characters")
   private String shippingAddress;

   /**
    * Optional delivery instructions
    */
   @Size(max = 500, message = "Delivery instructions cannot exceed 500 characters")
   private String deliveryInstructions;
}