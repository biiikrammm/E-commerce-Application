package com.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Request object for creating or updating products
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

   /**
    * Product name
    */
   @NotBlank(message = "Product name is required")
   @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
   private String name;

   /**
    * Product description
    */
   @Size(max = 1000, message = "Description cannot exceed 1000 characters")
   private String description;

   /**
    * Product price
    */
   @NotNull(message = "Price is required")
   @DecimalMin(value = "0.01", message = "Price must be greater than 0")
   @DecimalMax(value = "999999.99", message = "Price cannot exceed 999999.99")
   private BigDecimal price;

   /**
    * Stock quantity
    */
   @NotNull(message = "Stock quantity is required")
   @Min(value = 0, message = "Stock cannot be negative")
   @Max(value = 999999, message = "Stock cannot exceed 999999")
   private Integer stockQuantity;

   /**
    * Product category
    */
   @NotBlank(message = "Category is required")
   @Size(min = 2, max = 50, message = "Category must be between 2 and 50 characters")
   private String category;

   /**
    * Product image URL
    */
   @Size(max = 500, message = "Image URL cannot exceed 500 characters")
   private String imageUrl;

   /**
    * Whether the product is active
    */
   private Boolean active;
}