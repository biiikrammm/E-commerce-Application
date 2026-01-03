package com.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Request object for payment processing
 * Note: This should contain payment gateway tokens, not raw payment card data
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

   /**
    * Whether the payment was successful
    */
   @NotNull(message = "Payment status is required")
   private Boolean paymentSuccessful;

   /**
    * Unique transaction identifier from payment gateway
    */
   @NotBlank(message = "Transaction ID is required")
   private String transactionId;

   /**
    * Payment method used (e.g., CREDIT_CARD, DEBIT_CARD, UPI, WALLET)
    */
   @NotBlank(message = "Payment method is required")
   private String paymentMethod;

   /**
    * Optional payment gateway name
    */
   private String paymentGateway;

   /**
    * Optional additional payment details or notes
    */
   @Size(max = 500, message = "Payment notes cannot exceed 500 characters")
   private String notes;
}