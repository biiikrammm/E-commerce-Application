package com.ecommerce.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * Response object for product information
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

   /**
    * Unique identifier for the product
    */
   private Long id;

   /**
    * Product name
    */
   private String name;

   /**
    * Product description
    */
   private String description;

   /**
    * Current price of the product
    */
   private BigDecimal price;

   /**
    * Available stock quantity
    */
   private Integer stockQuantity;

   /**
    * Product category
    */
   private String category;

   /**
    * URL to product image
    */
   private String imageUrl;

   /**
    * Whether the product is active and available for purchase
    */
   private Boolean active;
}