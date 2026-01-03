package com.ecommerce.util;

import com.ecommerce.dto.*;
import com.ecommerce.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for mapping between entities and DTOs
 */
@Component
public class DtoMapper {

   /**
    * Convert Product entity to ProductResponse DTO
    */
   public ProductResponse toProductResponse(Product product) {
      if (product == null) {
         return null;
      }

      return ProductResponse.builder()
              .id(product.getId())
              .name(product.getName())
              .description(product.getDescription())
              .price(product.getPrice())
              .stockQuantity(product.getStockQuantity())
              .category(product.getCategory())
              .imageUrl(product.getImageUrl())
              .active(product.getActive())
              .build();
   }

   /**
    * Convert ProductRequest DTO to Product entity
    */
   public Product toProduct(ProductRequest request) {
      if (request == null) {
         return null;
      }

      return Product.builder()
              .name(request.getName())
              .description(request.getDescription())
              .price(request.getPrice())
              .stockQuantity(request.getStockQuantity())
              .category(request.getCategory())
              .imageUrl(request.getImageUrl())
              .active(request.getActive() != null ? request.getActive() : true)
              .build();
   }

   /**
    * Update Product entity from ProductRequest DTO
    */
   public void updateProductFromRequest(Product product, ProductRequest request) {
      if (product == null || request == null) {
         return;
      }

      product.setName(request.getName());
      product.setDescription(request.getDescription());
      product.setPrice(request.getPrice());
      product.setStockQuantity(request.getStockQuantity());
      product.setCategory(request.getCategory());
      product.setImageUrl(request.getImageUrl());
      if (request.getActive() != null) {
         product.setActive(request.getActive());
      }
   }

   /**
    * Convert CartItem entity to CartItemResponse DTO
    */
   public CartItemResponse toCartItemResponse(CartItem cartItem) {
      if (cartItem == null) {
         return null;
      }

      return CartItemResponse.builder()
              .id(cartItem.getId())
              .product(toProductResponse(cartItem.getProduct()))
              .quantity(cartItem.getQuantity())
              .subtotal(cartItem.getSubtotal())
              .build();
   }

   /**
    * Convert list of CartItems to CartResponse DTO
    */
   public CartResponse toCartResponse(List<CartItem> cartItems, String sessionId) {
      if (cartItems == null) {
         return CartResponse.builder()
                 .items(List.of())
                 .totalAmount(java.math.BigDecimal.ZERO)
                 .itemCount(0)
                 .sessionId(sessionId)
                 .build();
      }

      List<CartItemResponse> itemResponses = cartItems.stream()
              .map(this::toCartItemResponse)
              .collect(Collectors.toList());

      java.math.BigDecimal totalAmount = cartItems.stream()
              .map(CartItem::getSubtotal)
              .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

      return CartResponse.builder()
              .items(itemResponses)
              .totalAmount(totalAmount)
              .itemCount(cartItems.size())
              .sessionId(sessionId)
              .build();
   }

   /**
    * Convert OrderItem entity to OrderItemResponse DTO
    */
   public OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
      if (orderItem == null) {
         return null;
      }

      return OrderItemResponse.builder()
              .id(orderItem.getId())
              .product(toProductResponse(orderItem.getProduct()))
              .quantity(orderItem.getQuantity())
              .priceAtPurchase(orderItem.getPriceAtPurchase())
              .subtotal(orderItem.getSubtotal())
              .build();
   }

   /**
    * Convert Order entity to OrderResponse DTO
    */
   public OrderResponse toOrderResponse(Order order) {
      if (order == null) {
         return null;
      }

      List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
              .map(this::toOrderItemResponse)
              .collect(Collectors.toList());

      return OrderResponse.builder()
              .id(order.getId())
              .orderNumber(order.getOrderNumber())
              .customerName(order.getCustomerName())
              .customerEmail(order.getCustomerEmail())
              .shippingAddress(order.getShippingAddress())
              .orderItems(orderItemResponses)
              .totalAmount(order.getTotalAmount())
              .status(order.getStatus().name())
              .paymentStatus(order.getPaymentStatus().name())
              .orderDate(order.getOrderDate())
              .createdAt(order.getCreatedAt())
              .updatedAt(order.getUpdatedAt())
              .build();
   }

   /**
    * Convert list of Products to list of ProductResponses
    */
   public List<ProductResponse> toProductResponseList(List<Product> products) {
      if (products == null) {
         return List.of();
      }

      return products.stream()
              .map(this::toProductResponse)
              .collect(Collectors.toList());
   }

   /**
    * Convert list of Orders to list of OrderResponses
    */
   public List<OrderResponse> toOrderResponseList(List<Order> orders) {
      if (orders == null) {
         return List.of();
      }

      return orders.stream()
              .map(this::toOrderResponse)
              .collect(Collectors.toList());
   }
}