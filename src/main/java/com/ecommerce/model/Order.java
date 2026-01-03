package com.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "orderItems")
public class Order {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @EqualsAndHashCode.Include
   private Long id;

   @Column(nullable = false, unique = true)
   private String orderNumber;

   @NotBlank(message = "Customer name is required")
   @Column(nullable = false)
   private String customerName;

   @NotBlank(message = "Email is required")
   @Email(message = "Invalid email format")
   @Column(nullable = false)
   private String customerEmail;

   @NotBlank(message = "Shipping address is required")
   @Column(nullable = false, length = 500)
   private String shippingAddress;

   @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
   @Builder.Default
   private List<OrderItem> orderItems = new ArrayList<>();

   @Column(nullable = false, precision = 10, scale = 2)
   private BigDecimal totalAmount;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   @Builder.Default
   private OrderStatus status = OrderStatus.PENDING;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   @Builder.Default
   private PaymentStatus paymentStatus = PaymentStatus.PENDING;

   @Column(nullable = false)
   private LocalDateTime orderDate;

   @CreationTimestamp
   @Column(nullable = false, updatable = false)
   private LocalDateTime createdAt;

   @UpdateTimestamp
   @Column(nullable = false)
   private LocalDateTime updatedAt;

   @PrePersist
   public void prePersist() {
      this.orderDate = LocalDateTime.now();
      this.orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
      calculateTotalAmount();
   }

   @PreUpdate
   public void preUpdate() {
      calculateTotalAmount();
   }

   // Bidirectional relationship helper methods
   public void addOrderItem(OrderItem item) {
      orderItems.add(item);
      item.setOrder(this);
   }

   public void removeOrderItem(OrderItem item) {
      orderItems.remove(item);
      item.setOrder(null);
   }

   // Calculate total amount from order items
   public void calculateTotalAmount() {
      if (orderItems != null && !orderItems.isEmpty()) {
         this.totalAmount = orderItems.stream()
                 .map(OrderItem::getSubtotal)
                 .reduce(BigDecimal.ZERO, BigDecimal::add);
      } else {
         this.totalAmount = BigDecimal.ZERO;
      }
   }

   public enum OrderStatus {
      PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
   }

   public enum PaymentStatus {
      PENDING, COMPLETED, FAILED, REFUNDED
   }
}