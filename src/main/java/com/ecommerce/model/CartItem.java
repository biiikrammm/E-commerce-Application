package com.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class CartItem {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @EqualsAndHashCode.Include
   private Long id;

   @NotNull(message = "Product is required")
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "product_id", nullable = false)
   private Product product;

   @NotNull(message = "Quantity is required")
   @Min(value = 1, message = "Quantity must be at least 1")
   @Column(nullable = false)
   private Integer quantity;

   @Column(nullable = false)
   private String sessionId;

   @Column(precision = 10, scale = 2)
   private BigDecimal subtotal;

   @CreationTimestamp
   @Column(nullable = false, updatable = false)
   private LocalDateTime createdAt;

   @UpdateTimestamp
   @Column(nullable = false)
   private LocalDateTime updatedAt;

   @PrePersist
   @PreUpdate
   public void calculateSubtotal() {
      if (product != null && quantity != null) {
         this.subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
      }
   }
}