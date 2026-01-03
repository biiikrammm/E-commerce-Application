package com.ecommerce.service;

import com.ecommerce.dto.ProductRequest;
import com.ecommerce.dto.ProductResponse;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.util.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for product management
 */
@Service
@RequiredArgsConstructor
public class ProductService {

   private final ProductRepository productRepository;
   private final DtoMapper dtoMapper;

   /**
    * Get all active products with pagination
    */
   @Transactional(readOnly = true)
   public Page<ProductResponse> getActiveProducts(Pageable pageable) {
      Page<Product> products = productRepository.findByActiveTrue(pageable);
      return products.map(dtoMapper::toProductResponse);
   }

   /**
    * Get all active products (no pagination)
    */
   @Transactional(readOnly = true)
   public List<ProductResponse> getActiveProducts() {
      List<Product> products = productRepository.findByActiveTrue();
      return dtoMapper.toProductResponseList(products);
   }

   /**
    * Get product by ID (returns entity for internal use)
    */
   @Transactional(readOnly = true)
   public Product getProductEntityById(Long id) {
      return productRepository.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
   }

   /**
    * Get product by ID (returns DTO)
    */
   @Transactional(readOnly = true)
   public ProductResponse getProductById(Long id) {
      Product product = getProductEntityById(id);
      return dtoMapper.toProductResponse(product);
   }

   /**
    * Get products by category
    */
   @Transactional(readOnly = true)
   public List<ProductResponse> getProductsByCategory(String category) {
      List<Product> products = productRepository.findByCategory(category);
      return dtoMapper.toProductResponseList(products);
   }

   /**
    * Search products by keyword
    */
   @Transactional(readOnly = true)
   public List<ProductResponse> searchProducts(String keyword) {
      List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
      return dtoMapper.toProductResponseList(products);
   }

   /**
    * Get all unique categories
    */
   @Transactional(readOnly = true)
   public List<String> getAllCategories() {
      List<Product> products = productRepository.findAll();
      return products.stream()
              .map(Product::getCategory)
              .distinct()
              .sorted()
              .collect(Collectors.toList());
   }

   /**
    * Create a new product
    */
   @Transactional
   public ProductResponse createProduct(ProductRequest request) {
      Product product = dtoMapper.toProduct(request);
      Product savedProduct = productRepository.save(product);
      return dtoMapper.toProductResponse(savedProduct);
   }

   /**
    * Update an existing product
    */
   @Transactional
   public ProductResponse updateProduct(Long id, ProductRequest request) {
      Product product = getProductEntityById(id);
      dtoMapper.updateProductFromRequest(product, request);
      Product updatedProduct = productRepository.save(product);
      return dtoMapper.toProductResponse(updatedProduct);
   }

   /**
    * Delete a product (soft delete - set active to false)
    */
   @Transactional
   public void deleteProduct(Long id) {
      Product product = getProductEntityById(id);
      product.setActive(false);
      productRepository.save(product);
   }

   /**
    * Update product stock (decrease by quantity)
    */
   @Transactional
   public void updateStock(Long productId, int quantity) {
      Product product = getProductEntityById(productId);
      int newStock = product.getStockQuantity() - quantity;

      if (newStock < 0) {
         throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
      }

      product.setStockQuantity(newStock);
      productRepository.save(product);
   }

   /**
    * Check if product has sufficient stock
    */
   @Transactional(readOnly = true)
   public boolean hasStock(Long productId, int quantity) {
      Product product = getProductEntityById(productId);
      return product.getStockQuantity() >= quantity;
   }
}