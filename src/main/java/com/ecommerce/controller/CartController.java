package com.ecommerce.controller;

import com.ecommerce.dto.AddToCartRequest;
import com.ecommerce.dto.CartResponse;
import com.ecommerce.dto.UpdateCartRequest;
import com.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for shopping cart operations
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins:http://localhost:3000}")
public class CartController {

   private final CartService cartService;

   /**
    * Get shopping cart for a session
    * @param sessionId The session identifier
    * @return Cart response with items and total
    */
   @GetMapping("/{sessionId}")
   public ResponseEntity<CartResponse> getCart(@PathVariable String sessionId) {
      CartResponse response = cartService.getCart(sessionId);
      return ResponseEntity.ok(response);
   }

   /**
    * Add item to shopping cart
    * @param sessionId The session identifier
    * @param request Add to cart request with product ID and quantity
    * @return Updated cart response
    */
   @PostMapping("/{sessionId}/items")
   public ResponseEntity<CartResponse> addToCart(
           @PathVariable String sessionId,
           @Valid @RequestBody AddToCartRequest request) {
      CartResponse response = cartService.addToCart(
              sessionId,
              request.getProductId(),
              request.getQuantity()
      );
      return ResponseEntity.ok(response);
   }

   /**
    * Update cart item quantity
    * @param sessionId The session identifier
    * @param cartItemId The cart item ID to update
    * @param request Update cart request with new quantity
    * @return Updated cart response
    */
   @PutMapping("/{sessionId}/items/{cartItemId}")
   public ResponseEntity<CartResponse> updateCartItem(
           @PathVariable String sessionId,
           @PathVariable Long cartItemId,
           @Valid @RequestBody UpdateCartRequest request) {
      CartResponse response = cartService.updateCartItem(
              sessionId,
              cartItemId,
              request.getQuantity()
      );
      return ResponseEntity.ok(response);
   }

   /**
    * Remove item from cart
    * @param sessionId The session identifier
    * @param cartItemId The cart item ID to remove
    * @return Updated cart response
    */
   @DeleteMapping("/{sessionId}/items/{cartItemId}")
   public ResponseEntity<CartResponse> removeFromCart(
           @PathVariable String sessionId,
           @PathVariable Long cartItemId) {
      CartResponse response = cartService.removeFromCart(sessionId, cartItemId);
      return ResponseEntity.ok(response);
   }

   /**
    * Clear all items from cart
    * @param sessionId The session identifier
    * @return No content
    */
   @DeleteMapping("/{sessionId}")
   public ResponseEntity<Void> clearCart(@PathVariable String sessionId) {
      cartService.clearCart(sessionId);
      return ResponseEntity.noContent().build();
   }
}