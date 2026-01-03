package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "order")
public class OrderItem {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @EqualsAndHashCode.Include
   private Long id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "order_id", nullable = false)
   private Order order;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "product_id", nullable = false)
   private Product product;

   @Column(nullable = false)
   private Integer quantity;

   @Column(nullable = false, precision = 10, scale = 2)
   private BigDecimal priceAtPurchase;

   @Column(nullable = false, precision = 10, scale = 2)
   private BigDecimal subtotal;

   @PrePersist
   @PreUpdate
   public void calculateSubtotal() {
      if (priceAtPurchase != null && quantity != null) {
         this.subtotal = priceAtPurchase.multiply(BigDecimal.valueOf(quantity));
      }
   }
}