package com.ecommerce.controller;

import com.ecommerce.dto.CreateOrderRequest;
import com.ecommerce.dto.OrderResponse;
import com.ecommerce.dto.PaymentRequest;
import com.ecommerce.model.Order;
import com.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for order management operations
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins:http://localhost:3000}")
public class OrderController {

   private final OrderService orderService;

   /**
    * Create a new order from cart
    * @param request Order creation request
    * @return Created order response
    */
   @PostMapping
   public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
      OrderResponse response = orderService.createOrder(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }

   /**
    * Get all orders with pagination
    * @param page Page number (default 0)
    * @param size Page size (default 20)
    * @return Paginated list of orders
    */
   @GetMapping
   public ResponseEntity<Page<OrderResponse>> getAllOrders(
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "20") int size) {
      Pageable pageable = PageRequest.of(page, size);
      Page<OrderResponse> orders = orderService.getAllOrders(pageable);
      return ResponseEntity.ok(orders);
   }

   /**
    * Get order by ID
    * @param id Order ID
    * @return Order response
    */
   @GetMapping("/{id}")
   public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
      OrderResponse response = orderService.getOrderById(id);
      return ResponseEntity.ok(response);
   }

   /**
    * Get order by order number
    * @param orderNumber Order number
    * @return Order response
    */
   @GetMapping("/number/{orderNumber}")
   public ResponseEntity<OrderResponse> getOrderByNumber(@PathVariable String orderNumber) {
      OrderResponse response = orderService.getOrderByOrderNumber(orderNumber);
      return ResponseEntity.ok(response);
   }

   /**
    * Get orders by customer email
    * @param email Customer email
    * @return List of order responses
    */
   @GetMapping("/customer/{email}")
   public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable String email) {
      List<OrderResponse> orders = orderService.getOrdersByCustomerEmail(email);
      return ResponseEntity.ok(orders);
   }

   /**
    * Update order status
    * @param id Order ID
    * @param status New order status
    * @return Updated order response
    */
   @PatchMapping("/{id}/status")
   public ResponseEntity<OrderResponse> updateOrderStatus(
           @PathVariable Long id,
           @RequestParam Order.OrderStatus status) {
      OrderResponse response = orderService.updateOrderStatus(id, status);
      return ResponseEntity.ok(response);
   }

   /**
    * Process payment for an order
    * @param id Order ID
    * @param request Payment request with transaction details
    * @return Updated order response
    */
   @PostMapping("/{id}/payment")
   public ResponseEntity<OrderResponse> processPayment(
           @PathVariable Long id,
           @Valid @RequestBody PaymentRequest request) {
      OrderResponse response = orderService.processPayment(id, request);
      return ResponseEntity.ok(response);
   }

   /**
    * Cancel an order
    * @param id Order ID
    * @return Updated order response
    */
   @PostMapping("/{id}/cancel")
   public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
      OrderResponse response = orderService.cancelOrder(id);
      return ResponseEntity.ok(response);
   }
}