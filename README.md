# 🛒 E-Commerce Backend Application

A robust and scalable RESTful API backend for an e-commerce platform built with Spring Boot 3, featuring product management, shopping cart functionality, and order processing.

## 📋 Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Configuration](#configuration)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

### Product Management
- ✅ CRUD operations for products
- ✅ Product search and filtering by category
- ✅ Keyword-based product search
- ✅ Stock management with optimistic locking
- ✅ Soft delete functionality
- ✅ Pagination support for large datasets

### Shopping Cart
- ✅ Session-based cart management
- ✅ Add/update/remove items
- ✅ Real-time cart total calculation
- ✅ Stock validation
- ✅ Automatic subtotal calculation

### Order Processing
- ✅ Create orders from cart items
- ✅ Order status tracking (Pending, Confirmed, Shipped, Delivered, Cancelled)
- ✅ Payment status management
- ✅ Order history by customer email
- ✅ Automatic stock deduction
- ✅ Order cancellation with stock restoration

### Additional Features
- ✅ Comprehensive input validation
- ✅ Custom exception handling
- ✅ DTO pattern for clean separation of concerns
- ✅ Audit timestamps (created/updated dates)
- ✅ H2 in-memory database for development
- ✅ MySQL support for production

## 🛠 Technologies Used

### Core Technologies
- **Java 17** - Programming language
- **Spring Boot 3.3.5** - Application framework
- **Spring Data JPA** - Data persistence
- **Hibernate 6.5** - ORM framework
- **Maven** - Dependency management

### Database
- **H2 Database** - In-memory database (development)
- **MySQL** - Production database support

### Additional Libraries
- **Lombok** - Reduces boilerplate code
- **Jakarta Validation** - Input validation
- **Jackson** - JSON serialization/deserialization

## 🏗 Architecture

The application follows a layered architecture pattern:

```
┌─────────────────────────────────────────┐
│         Controller Layer                │
│    (REST API Endpoints)                 │
└─────────────────────────────────────────┘
                   ↓
┌─────────────────────────────────────────┐
│          Service Layer                  │
│    (Business Logic)                     │
└─────────────────────────────────────────┘
                   ↓
┌─────────────────────────────────────────┐
│        Repository Layer                 │
│    (Data Access)                        │
└─────────────────────────────────────────┘
                   ↓
┌─────────────────────────────────────────┐
│          Database                       │
│    (H2 / MySQL)                         │
└─────────────────────────────────────────┘
```

### Key Components

- **Controllers**: Handle HTTP requests and responses
- **Services**: Contain business logic and transaction management
- **Repositories**: Interface with the database using Spring Data JPA
- **DTOs**: Transfer data between layers
- **Entities**: JPA entities representing database tables
- **Mappers**: Convert between entities and DTOs
- **Exception Handlers**: Global exception handling

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/ecommerce-backend.git
   cd ecommerce-backend
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   Or run directly from your IDE by executing `EcommerceApplication.java`

4. **Access the application**
   - API Base URL: `http://localhost:8081`
   - H2 Console: `http://localhost:8081/h2-console`
     - JDBC URL: `jdbc:h2:mem:ecommerce`
     - Username: `sa`
     - Password: (leave empty)

### Configuration

The application can be configured through `application.properties`:

```properties
# Server Configuration
server.port=8081

# Database Configuration
spring.datasource.url=jdbc:h2:mem:ecommerce
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# CORS Configuration
cors.allowed-origins=http://localhost:3000
```

## 📚 API Documentation

### Products API

#### Get All Products
```http
GET /api/products?page=0&size=20
```

#### Get Product by ID
```http
GET /api/products/{id}
```

#### Get Products by Category
```http
GET /api/products/category/{category}
```

#### Search Products
```http
GET /api/products/search?keyword={keyword}
```

#### Create Product
```http
POST /api/products
Content-Type: application/json

{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "stockQuantity": 50,
  "category": "Electronics",
  "imageUrl": "https://example.com/laptop.jpg",
  "active": true
}
```

#### Update Product
```http
PUT /api/products/{id}
Content-Type: application/json

{
  "name": "Updated Laptop",
  "description": "Updated description",
  "price": 899.99,
  "stockQuantity": 45,
  "category": "Electronics",
  "imageUrl": "https://example.com/laptop.jpg",
  "active": true
}
```

#### Delete Product (Soft Delete)
```http
DELETE /api/products/{id}
```

### Cart API

#### Get Cart
```http
GET /api/cart/{sessionId}
```

#### Add to Cart
```http
POST /api/cart/{sessionId}/items
Content-Type: application/json

{
  "productId": 1,
  "quantity": 2
}
```

#### Update Cart Item
```http
PUT /api/cart/{sessionId}/items/{cartItemId}
Content-Type: application/json

{
  "productId": 1,
  "quantity": 3
}
```

#### Remove from Cart
```http
DELETE /api/cart/{sessionId}/items/{cartItemId}
```

#### Clear Cart
```http
DELETE /api/cart/{sessionId}
```

### Orders API

#### Create Order
```http
POST /api/orders
Content-Type: application/json

{
  "sessionId": "session-123",
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "phoneNumber": "+1234567890",
  "shippingAddress": "123 Main St, City, State 12345",
  "deliveryInstructions": "Leave at door"
}
```

#### Get All Orders
```http
GET /api/orders?page=0&size=20
```

#### Get Order by ID
```http
GET /api/orders/{id}
```

#### Get Order by Order Number
```http
GET /api/orders/number/{orderNumber}
```

#### Get Orders by Customer Email
```http
GET /api/orders/customer/{email}
```

#### Update Order Status
```http
PATCH /api/orders/{id}/status?status=CONFIRMED
```

#### Process Payment
```http
POST /api/orders/{id}/payment
Content-Type: application/json

{
  "paymentSuccessful": true,
  "transactionId": "TXN123456",
  "paymentMethod": "CREDIT_CARD",
  "paymentGateway": "Stripe"
}
```

#### Cancel Order
```http
POST /api/orders/{id}/cancel
```

### Response Examples

#### Success Response (Product)
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "stockQuantity": 50,
  "category": "Electronics",
  "imageUrl": "https://example.com/laptop.jpg",
  "active": true
}
```

#### Success Response (Cart)
```json
{
  "items": [
    {
      "id": 1,
      "product": {
        "id": 1,
        "name": "Laptop",
        "price": 999.99
      },
      "quantity": 2,
      "subtotal": 1999.98
    }
  ],
  "totalAmount": 1999.98,
  "itemCount": 1,
  "sessionId": "session-123"
}
```

#### Error Response
```json
{
  "timestamp": "2026-01-03T21:55:04.346",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with id: 99"
}
```

#### Validation Error Response
```json
{
  "timestamp": "2026-01-03T21:55:04.346",
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid request parameters",
  "validationErrors": {
    "name": "Product name is required",
    "price": "Price must be greater than 0"
  }
}
```

## 🗄 Database Schema

### Products Table
```sql
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INT NOT NULL,
    category VARCHAR(255) NOT NULL,
    image_url VARCHAR(255),
    active BOOLEAN NOT NULL,
    version BIGINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

### Cart Items Table
```sql
CREATE TABLE cart_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    session_id VARCHAR(255) NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    subtotal DECIMAL(10,2),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

### Orders Table
```sql
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_number VARCHAR(255) UNIQUE NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    shipping_address VARCHAR(500) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status ENUM('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELLED'),
    payment_status ENUM('PENDING','COMPLETED','FAILED','REFUNDED'),
    order_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

### Order Items Table
```sql
CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price_at_purchase DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

## 🔧 Configuration

### Production Configuration (MySQL)

1. Update `application.properties` for production:

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update

# Production Settings
spring.jpa.show-sql=false
logging.level.org.springframework=INFO
```

2. Create MySQL database:
```sql
CREATE DATABASE ecommerce CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### Test Coverage
```bash
mvn jacoco:report
```

## 📁 Project Structure

```
ecommerce-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ecommerce/
│   │   │       ├── config/          # Configuration classes
│   │   │       ├── controller/      # REST controllers
│   │   │       ├── dto/             # Data Transfer Objects
│   │   │       ├── exception/       # Custom exceptions & handlers
│   │   │       ├── model/           # JPA entities
│   │   │       ├── repository/      # Spring Data repositories
│   │   │       ├── service/         # Business logic
│   │   │       ├── util/            # Utility classes
│   │   │       └── EcommerceApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql (optional)
│   └── test/
│       └── java/
│           └── com/ecommerce/       # Test classes
├── pom.xml
├── README.md
└── .gitignore
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 👨‍💻 Author

**Bikram Roy Choudhury**
- GitHub: [biiikrammm]([https://github.com/yourusername](https://github.com/biiikrammm))
- LinkedIn: [Bikram Roy Choudhury]([https://linkedin.com/in/yourprofile](https://www.linkedin.com/in/bikram-roy-choudhury/))
- Email: bikram.rc200615@gmail.com

## 🙏 Acknowledgments

- Spring Boot Documentation
- Baeldung Tutorials
- Stack Overflow Community

## 📞 Support

For support, email bikram.rc200615@gmail.com or open an issue in the GitHub repository.

---

⭐ **Star this repository if you find it helpful!**
