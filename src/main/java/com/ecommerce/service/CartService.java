package com.ecommerce.service;

import com.ecommerce.dto.CartResponse;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.InvalidOperationException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.util.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for shopping cart management
 */
@Service
@RequiredArgsConstructor
public class CartService {

   private final CartItemRepository cartItemRepository;
   private final ProductService productService;
   private final DtoMapper dtoMapper;

   /**
    * Get shopping cart for a session
    */
   @Transactional(readOnly = true)
   public CartResponse getCart(String sessionId) {
      validateSessionId(sessionId);
      List<CartItem> items = cartItemRepository.findBySessionId(sessionId);
      return dtoMapper.toCartResponse(items, sessionId);
   }

   /**
    * Get cart items (internal use)
    */
   @Transactional(readOnly = true)
   public List<CartItem> getCartItems(String sessionId) {
      validateSessionId(sessionId);
      return cartItemRepository.findBySessionId(sessionId);
   }

   /**
    * Add item to cart
    */
   @Transactional
   public CartResponse addToCart(String sessionId, Long productId, Integer quantity) {
      validateSessionId(sessionId);

      Product product = productService.getProductEntityById(productId);

      if (!product.getActive()) {
         throw new InvalidOperationException("Product is not available: " + product.getName());
      }

      if (product.getStockQuantity() < quantity) {
         throw new InsufficientStockException("Insufficient stock available for product: " + product.getName());
      }

      cartItemRepository.findBySessionIdAndProductId(sessionId, productId)
              .ifPresentOrElse(
                      existingItem -> {
                         int newQuantity = existingItem.getQuantity() + quantity;
                         if (product.getStockQuantity() < newQuantity) {
                            throw new InsufficientStockException("Insufficient stock available for product: " + product.getName());
                         }
                         existingItem.setQuantity(newQuantity);
                         existingItem.calculateSubtotal();
                         cartItemRepository.save(existingItem);
                      },
                      () -> {
                         CartItem newItem = CartItem.builder()
                                 .sessionId(sessionId)
                                 .product(product)
                                 .quantity(quantity)
                                 .build();
                         newItem.calculateSubtotal();
                         cartItemRepository.save(newItem);
                      }
              );

      return getCart(sessionId);
   }

   /**
    * Update cart item quantity
    */
   @Transactional
   public CartResponse updateCartItem(String sessionId, Long cartItemId, Integer quantity) {
      validateSessionId(sessionId);

      CartItem cartItem = cartItemRepository.findById(cartItemId)
              .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

      if (!cartItem.getSessionId().equals(sessionId)) {
         throw new InvalidOperationException("Cart item does not belong to this session");
      }

      if (cartItem.getProduct().getStockQuantity() < quantity) {
         throw new InsufficientStockException("Insufficient stock available for product: " + cartItem.getProduct().getName());
      }

      cartItem.setQuantity(quantity);
      cartItem.calculateSubtotal();
      cartItemRepository.save(cartItem);

      return getCart(sessionId);
   }

   /**
    * Remove item from cart
    */
   @Transactional
   public CartResponse removeFromCart(String sessionId, Long cartItemId) {
      validateSessionId(sessionId);

      CartItem cartItem = cartItemRepository.findById(cartItemId)
              .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

      if (!cartItem.getSessionId().equals(sessionId)) {
         throw new InvalidOperationException("Cart item does not belong to this session");
      }

      cartItemRepository.deleteById(cartItemId);
      return getCart(sessionId);
   }

   /**
    * Clear all items from cart
    */
   @Transactional
   public void clearCart(String sessionId) {
      validateSessionId(sessionId);
      cartItemRepository.deleteBySessionId(sessionId);
   }

   /**
    * Validate session ID
    */
   private void validateSessionId(String sessionId) {
      if (sessionId == null || sessionId.trim().isEmpty()) {
         throw new InvalidOperationException("Session ID is required");
      }
   }
}