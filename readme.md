
# Coupon Management API

The Coupon Management API provides a flexible way to manage and apply discount coupons for an e-commerce platform. It supports different discount types (cart-based, product-based, and Buy X Get Y), calculates the best discount combinations, and returns a final price summary.

## Table of Contents
- [Features](#features)
- [Installation](#installation)
- [Database Configuration](#database-configuration)
- [API Endpoints](#api-endpoints)
- [Discount Types](#discount-types)
- [Usage Examples](#usage-examples)
- [Assumptions and Design Notes](#assumptions-and-design-notes)

## Features
- **Define Discount Coupons**: Create and manage discounts for the entire cart or specific products.
- **Apply Discounts to Cart**: Calculate applicable discounts for a cart based on predefined rules.
- **Calculate Savings**: Return a final cart summary with applied discounts and updated totals.

## Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/coupon-management.git
   cd coupon-management
   ```

2. **Configure the Database**:  
   Open `src/main/resources/application.properties` and set up your PostgreSQL credentials.

3. **Build and Run**:
   Build and run the project with Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

## Database Configuration

This API uses PostgreSQL for managing discount data. Ensure your database supports JSON data types and has the necessary permissions.

- **SQL Script**: Use `db-cmd.sql` to create the required `discounts` table and configurations. The structure is as follows:

   ```sql
   CREATE TABLE discounts (
       id SERIAL PRIMARY KEY,
       type VARCHAR(50) NOT NULL,         -- Discount type identifier (e.g., 'cart-wise', 'product-wise', 'bxgy')
       details JSON NOT NULL,             -- JSON column for discount parameters
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   ```

## API Endpoints

### Coupon Management

- **GET /coupons** - Retrieve all coupons.
- **GET /coupons/{id}** - Retrieve a specific coupon by ID.
- **POST /coupons** - Create a new coupon.
- **PUT /coupons/{id}** - Update an existing coupon by ID.
- **DELETE /coupons/{id}** - Delete a coupon by ID.

### Apply Coupons

- **POST /apply-coupons** - Apply all eligible coupons to a cart.
    - Request example:
      ```json
      {
        "cart": {
          "items": [
            {"product_id": 1, "quantity": 6, "price": 50},
            {"product_id": 2, "quantity": 3, "price": 30},
            {"product_id": 3, "quantity": 2, "price": 25}
          ]
        }
      }
      ```
    - Response example:
      ```json
      {
        "updated_cart": {
          "items": [
            {"product_id": 1, "quantity": 6, "price": 50, "total_discount": 0},
            {"product_id": 2, "quantity": 3, "price": 30, "total_discount": 0},
            {"product_id": 3, "quantity": 4, "price": 25, "total_discount": 50}
          ],
          "total_price": 490,
          "total_discount": 50,
          "final_price": 440
        }
      }
      ```

## Discount Types

### Cart-Wise Discounts
A discount applied based on the total cart value.
- **Example**: 10% off on a cart total above $100.

### Product-Wise Discounts
A discount applied to specific products based on their quantities.
- **Example**: $10 off each unit of Product X.

### Buy X Get Y (BxGy)
A discount where buying a specific quantity of one product entitles the customer to free quantities of another product.
- **Example**: Buy 3 of Product X, get 1 of Product Y for free.

## Usage Examples

### Create a Cart-Wise Discount
- Request:
  ```json
  {
    "type": "cart-wise",
    "details": {
      "threshold": 100,
      "discount": 10
    }
  }
  ```

### Apply All Discounts to a Cart
- Request:
  ```json
  {
    "cart": {
      "items": [
        {"product_id": 1, "quantity": 5, "price": 50},
        {"product_id": 2, "quantity": 2, "price": 30}
      ]
    }
  }
  ```

## Assumptions and Design Notes

- **Discounts Are Non-Stackable**: Each discount type is applied separately, and discounts are not compounded unless specified.
- **JSON Format for Discount Details**: Storing discount parameters in JSON enables flexible handling of diverse discount types.
- **Limit Repetitions for BxGy**: The BxGy discount can limit the number of times it applies, specified by the `repition_limit` field.

## License
This project is open-source under the MIT License. Contributions are welcome!
```