package com.ecommerce.controller;

import com.ecommerce.dto.ProductRequest;
import com.ecommerce.dto.ProductResponse;
import com.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for product management operations
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins:http://localhost:3000}")
public class ProductController {

   private final ProductService productService;

   /**
    * Get all active products with pagination
    * @param page Page number (default 0)
    * @param size Page size (default 20)
    * @return Paginated list of products
    */
   @GetMapping
   public ResponseEntity<Page<ProductResponse>> getAllProducts(
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "20") int size) {
      Pageable pageable = PageRequest.of(page, size);
      Page<ProductResponse> products = productService.getActiveProducts(pageable);
      return ResponseEntity.ok(products);
   }

   /**
    * Get product by ID
    * @param id Product ID
    * @return Product response
    */
   @GetMapping("/{id}")
   public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
      ProductResponse response = productService.getProductById(id);
      return ResponseEntity.ok(response);
   }

   /**
    * Get products by category
    * @param category Product category
    * @return List of product responses
    */
   @GetMapping("/category/{category}")
   public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) {
      List<ProductResponse> products = productService.getProductsByCategory(category);
      return ResponseEntity.ok(products);
   }

   /**
    * Search products by keyword
    * @param keyword Search keyword
    * @return List of matching products
    */
   @GetMapping("/search")
   public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
      List<ProductResponse> products = productService.searchProducts(keyword);
      return ResponseEntity.ok(products);
   }

   /**
    * Create a new product
    * @param request Product creation request
    * @return Created product response
    */
   @PostMapping
   public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
      ProductResponse response = productService.createProduct(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }

   /**
    * Update an existing product
    * @param id Product ID
    * @param request Product update request
    * @return Updated product response
    */
   @PutMapping("/{id}")
   public ResponseEntity<ProductResponse> updateProduct(
           @PathVariable Long id,
           @Valid @RequestBody ProductRequest request) {
      ProductResponse response = productService.updateProduct(id, request);
      return ResponseEntity.ok(response);
   }

   /**
    * Delete a product (soft delete)
    * @param id Product ID
    * @return No content
    */
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
      productService.deleteProduct(id);
      return ResponseEntity.noContent().build();
   }

   /**
    * Get all product categories
    * @return List of unique categories
    */
   @GetMapping("/categories")
   public ResponseEntity<List<String>> getAllCategories() {
      List<String> categories = productService.getAllCategories();
      return ResponseEntity.ok(categories);
   }
}