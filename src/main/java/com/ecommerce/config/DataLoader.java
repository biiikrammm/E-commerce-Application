package com.ecommerce.config;

import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Data loader to populate initial sample data on application startup
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

   private final ProductRepository productRepository;

   @Override
   @Transactional
   public void run(String... args) {
      try {
         if (productRepository.count() == 0) {
            log.info("Loading sample products into database...");

            Product p1 = Product.builder()
                    .name("Laptop")
                    .description("High-performance laptop with 16GB RAM")
                    .price(new BigDecimal("999.99"))
                    .stockQuantity(50)
                    .category("Electronics")
                    .imageUrl("https://via.placeholder.com/300x300?text=Laptop")
                    .active(true)
                    .build();

            Product p2 = Product.builder()
                    .name("Wireless Mouse")
                    .description("Ergonomic wireless mouse with precision tracking")
                    .price(new BigDecimal("29.99"))
                    .stockQuantity(200)
                    .category("Electronics")
                    .imageUrl("https://via.placeholder.com/300x300?text=Mouse")
                    .active(true)
                    .build();

            Product p3 = Product.builder()
                    .name("Office Chair")
                    .description("Comfortable ergonomic office chair")
                    .price(new BigDecimal("199.99"))
                    .stockQuantity(30)
                    .category("Furniture")
                    .imageUrl("https://via.placeholder.com/300x300?text=Chair")
                    .active(true)
                    .build();

            Product p4 = Product.builder()
                    .name("Smartphone")
                    .description("Latest smartphone with 5G connectivity")
                    .price(new BigDecimal("699.99"))
                    .stockQuantity(100)
                    .category("Electronics")
                    .imageUrl("https://via.placeholder.com/300x300?text=Smartphone")
                    .active(true)
                    .build();

            Product p5 = Product.builder()
                    .name("Desk Lamp")
                    .description("LED desk lamp with adjustable brightness")
                    .price(new BigDecimal("39.99"))
                    .stockQuantity(75)
                    .category("Furniture")
                    .imageUrl("https://via.placeholder.com/300x300?text=Lamp")
                    .active(true)
                    .build();

            Product p6 = Product.builder()
                    .name("Keyboard")
                    .description("Mechanical keyboard with RGB lighting")
                    .price(new BigDecimal("89.99"))
                    .stockQuantity(150)
                    .category("Electronics")
                    .imageUrl("https://via.placeholder.com/300x300?text=Keyboard")
                    .active(true)
                    .build();

            Product p7 = Product.builder()
                    .name("Monitor")
                    .description("27-inch 4K UHD monitor")
                    .price(new BigDecimal("399.99"))
                    .stockQuantity(60)
                    .category("Electronics")
                    .imageUrl("https://via.placeholder.com/300x300?text=Monitor")
                    .active(true)
                    .build();

            Product p8 = Product.builder()
                    .name("Bookshelf")
                    .description("Wooden bookshelf with 5 shelves")
                    .price(new BigDecimal("149.99"))
                    .stockQuantity(25)
                    .category("Furniture")
                    .imageUrl("https://via.placeholder.com/300x300?text=Bookshelf")
                    .active(true)
                    .build();

            productRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8));

            log.info("Successfully loaded {} sample products into database", 8);
         } else {
            log.info("Database already contains products. Skipping data load.");
         }
      } catch (Exception e) {
         log.error("Error loading sample data: {}", e.getMessage(), e);
         // Don't throw exception to prevent application startup failure
      }
   }
}