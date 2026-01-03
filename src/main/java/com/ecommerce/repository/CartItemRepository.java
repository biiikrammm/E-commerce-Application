package com.ecommerce.repository;

import com.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CartItem entity
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

   /**
    * Find all cart items by session ID
    */
   List<CartItem> findBySessionId(String sessionId);

   /**
    * Find cart item by session ID and product ID
    */
   Optional<CartItem> findBySessionIdAndProductId(String sessionId, Long productId);

   /**
    * Delete all cart items for a session
    */
   @Transactional
   @Modifying
   void deleteBySessionId(String sessionId);
}