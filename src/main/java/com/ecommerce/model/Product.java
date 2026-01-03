package com.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Product {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @EqualsAndHashCode.Include
   private Long id;

   @Version
   private Long version;

   @NotBlank(message = "Product name is required")
   @Column(nullable = false)
   private String name;

   @Column(length = 1000)
   private String description;

   @NotNull(message = "Price is required")
   @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
   @Column(nullable = false, precision = 10, scale = 2)
   private BigDecimal price;

   @NotNull(message = "Stock quantity is required")
   @Min(value = 0, message = "Stock cannot be negative")
   @Column(nullable = false)
   private Integer stockQuantity;

   @NotBlank(message = "Category is required")
   @Column(nullable = false)
   private String category;

   private String imageUrl;

   @Column(nullable = false)
   @Builder.Default
   private Boolean active = true;

   @CreationTimestamp
   @Column(nullable = false, updatable = false)
   private LocalDateTime createdAt;

   @UpdateTimestamp
   @Column(nullable = false)
   private LocalDateTime updatedAt;
}