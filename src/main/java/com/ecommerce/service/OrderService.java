package com.ecommerce.service;

import com.ecommerce.dto.CreateOrderRequest;
import com.ecommerce.dto.OrderResponse;
import com.ecommerce.dto.PaymentRequest;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.InvalidOperationException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.*;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.util.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service layer for order management
 */
@Service
@RequiredArgsConstructor
public class OrderService {

   private final OrderRepository orderRepository;
   private final CartService cartService;
   private final ProductService productService;
   private final DtoMapper dtoMapper;

   /**
    * Create a new order from cart items
    */
   @Transactional
   public OrderResponse createOrder(CreateOrderRequest request) {
      List<CartItem> cartItems = cartService.getCartItems(request.getSessionId());

      if (cartItems.isEmpty()) {
         throw new InvalidOperationException("Cannot create order from empty cart");
      }

      Order order = Order.builder()
              .customerName(request.getCustomerName())
              .customerEmail(request.getCustomerEmail())
              .shippingAddress(request.getShippingAddress())
              .status(Order.OrderStatus.PENDING)
              .paymentStatus(Order.PaymentStatus.PENDING)
              .build();

      for (CartItem cartItem : cartItems) {
         Product product = cartItem.getProduct();

         if (!product.getActive()) {
            throw new InvalidOperationException("Product is no longer available: " + product.getName());
         }

         if (product.getStockQuantity() < cartItem.getQuantity()) {
            throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
         }

         OrderItem orderItem = OrderItem.builder()
                 .product(product)
                 .quantity(cartItem.getQuantity())
                 .priceAtPurchase(product.getPrice())
                 .subtotal(cartItem.getSubtotal())
                 .build();

         order.addOrderItem(orderItem);

         productService.updateStock(product.getId(), cartItem.getQuantity());
      }

      order.calculateTotalAmount();
      Order savedOrder = orderRepository.save(order);

      cartService.clearCart(request.getSessionId());

      return dtoMapper.toOrderResponse(savedOrder);
   }

   /**
    * Get all orders with pagination
    */
   @Transactional(readOnly = true)
   public Page<OrderResponse> getAllOrders(Pageable pageable) {
      Page<Order> orders = orderRepository.findAll(pageable);
      return orders.map(dtoMapper::toOrderResponse);
   }

   /**
    * Get order by ID (returns entity for internal use)
    */
   @Transactional(readOnly = true)
   public Order getOrderEntityById(Long id) {
      return orderRepository.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
   }

   /**
    * Get order by ID (returns DTO)
    */
   @Transactional(readOnly = true)
   public OrderResponse getOrderById(Long id) {
      Order order = getOrderEntityById(id);
      return dtoMapper.toOrderResponse(order);
   }

   /**
    * Get order by order number
    */
   @Transactional(readOnly = true)
   public OrderResponse getOrderByOrderNumber(String orderNumber) {
      Order order = orderRepository.findByOrderNumber(orderNumber)
              .orElseThrow(() -> new ResourceNotFoundException("Order not found with number: " + orderNumber));
      return dtoMapper.toOrderResponse(order);
   }

   /**
    * Get orders by customer email
    */
   @Transactional(readOnly = true)
   public List<OrderResponse> getOrdersByCustomerEmail(String email) {
      List<Order> orders = orderRepository.findByCustomerEmail(email);
      return dtoMapper.toOrderResponseList(orders);
   }

   /**
    * Update order status
    */
   @Transactional
   public OrderResponse updateOrderStatus(Long orderId, Order.OrderStatus status) {
      Order order = getOrderEntityById(orderId);

      if (order.getStatus() == Order.OrderStatus.CANCELLED) {
         throw new InvalidOperationException("Cannot update status of cancelled order");
      }

      if (order.getStatus() == Order.OrderStatus.DELIVERED) {
         throw new InvalidOperationException("Cannot update status of delivered order");
      }

      order.setStatus(status);
      Order updatedOrder = orderRepository.save(order);
      return dtoMapper.toOrderResponse(updatedOrder);
   }

   /**
    * Process payment for an order
    */
   @Transactional
   public OrderResponse processPayment(Long orderId, PaymentRequest request) {
      Order order = getOrderEntityById(orderId);

      if (order.getPaymentStatus() == Order.PaymentStatus.COMPLETED) {
         throw new InvalidOperationException("Payment has already been completed for this order");
      }

      if (order.getStatus() == Order.OrderStatus.CANCELLED) {
         throw new InvalidOperationException("Cannot process payment for cancelled order");
      }

      if (request.getPaymentSuccessful()) {
         order.setPaymentStatus(Order.PaymentStatus.COMPLETED);
         order.setStatus(Order.OrderStatus.CONFIRMED);
      } else {
         order.setPaymentStatus(Order.PaymentStatus.FAILED);
      }

      Order updatedOrder = orderRepository.save(order);
      return dtoMapper.toOrderResponse(updatedOrder);
   }

   /**
    * Cancel an order
    */
   @Transactional
   public OrderResponse cancelOrder(Long orderId) {
      Order order = getOrderEntityById(orderId);

      if (order.getStatus() == Order.OrderStatus.DELIVERED) {
         throw new InvalidOperationException("Cannot cancel delivered order");
      }

      if (order.getStatus() == Order.OrderStatus.CANCELLED) {
         throw new InvalidOperationException("Order is already cancelled");
      }

      if (order.getStatus() == Order.OrderStatus.SHIPPED) {
         throw new InvalidOperationException("Cannot cancel shipped order. Please contact support.");
      }

      // Restore stock if payment was completed
      if (order.getPaymentStatus() == Order.PaymentStatus.COMPLETED) {
         for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            product.setStockQuantity(product.getStockQuantity() + orderItem.getQuantity());
         }
      }

      order.setStatus(Order.OrderStatus.CANCELLED);

      if (order.getPaymentStatus() == Order.PaymentStatus.COMPLETED) {
         order.setPaymentStatus(Order.PaymentStatus.REFUNDED);
      }

      Order updatedOrder = orderRepository.save(order);
      return dtoMapper.toOrderResponse(updatedOrder);
   }
}