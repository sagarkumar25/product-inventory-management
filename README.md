# Product Inventory Management REST API

## 1. Project Overview

The Product Inventory Management REST API is a Spring Boot application developed as part of the Java Development assignment for Ikaansh Edutech Private Limited.

The application provides RESTful APIs to manage products and categories. It demonstrates CRUD operations, JPA entity relationships, request and response DTOs, input validation, exception handling, database operations, pagination, sorting, searching, and unit testing.

## 2. Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- Hibernate ORM
- Maven
- H2 In-Memory Database
- Jakarta Bean Validation
- JUnit 5
- Mockito
- REST APIs

## 3. Features

- Create, retrieve, update, and delete products.
- Create, retrieve, update, and delete categories.
- Manage relationships between products and categories using JPA.
- Validate incoming request data using Jakarta Bean Validation.
- Handle exceptions and validation errors.
- Retrieve products using pagination and sorting.
- Search products by name using a JPA Named Query.
- Test service and controller layers using JUnit 5 and Mockito.
- Initialize the database using an SQL schema script.

## 4. Prerequisites

Install the following before running the application:

- Java JDK compatible with the version configured in `pom.xml`.
- Eclipse IDE or another Java IDE.
- Maven, or the Maven Wrapper if included in the project.

## 5. Project Structure

```text
product-inventory/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ikaansh/inventory/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── entity/
│   │   │       ├── exception/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── ProductInventoryApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── schema.sql
│   └── test/
│       └── java/
├── database/
│   └── postgresql-schema.sql
├── pom.xml
├── Dockerfile
└── README.md
```

The `database/postgresql-schema.sql` file and `Dockerfile` should be included if they have been created. The package structure above describes the expected organization; adjust it to match the actual files in the project.

## 6. Database Configuration

The application uses an H2 in-memory database for local development.

The database configuration is available in:

`src/main/resources/application.properties`

The configured database URL is:

```properties
spring.datasource.url=jdbc:h2:mem:inventorydb;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

The database schema is initialized through `schema.sql`.

No separate database server is required for the default H2 configuration.

### H2 Console

If the application is running and the H2 console is enabled, open:

`http://localhost:8080/h2-console`

Use the following connection details:

- JDBC URL: `jdbc:h2:mem:inventorydb;DB_CLOSE_DELAY=-1`
- Username: `sa`
- Password: leave blank

## 7. How to Run the Application

### Option 1: Run Using Eclipse

1. Open Eclipse.
2. Import the project as an existing Maven project.
3. Wait for Maven dependencies to download.
4. Locate `ProductInventoryApplication.java`.
5. Right-click the main class.
6. Select **Run As → Java Application** or **Spring Boot App**, if available.
7. Wait for the application to start successfully.

The application runs at:

`http://localhost:8080`

### Option 2: Run Using Maven

Open a terminal in the project root directory and execute:

```bash
mvn spring-boot:run
```

To run the tests:

```bash
mvn test
```

To build the application:

```bash
mvn clean package
```

These commands require Maven to be installed and configured, unless you use the project's Maven Wrapper.

## 8. Product API Endpoints

Base URL:

`http://localhost:8080/api/products`

| HTTP Method | Endpoint | Description | Success Status |
|---|---|---|---|
| POST | `/api/products` | Create a product | 201 Created |
| GET | `/api/products` | Retrieve all products | 200 OK |
| GET | `/api/products/{id}` | Retrieve a product by ID | 200 OK |
| PUT | `/api/products/{id}` | Update a product | 200 OK |
| DELETE | `/api/products/{id}` | Delete a product | 204 No Content |
| GET | `/api/products/page` | Retrieve paginated products | 200 OK |
| GET | `/api/products/search?name={name}` | Search products by name | 200 OK |

### 8.1 Create a Product

**Method:** `POST`

**URL:** `/api/products`

**Header:** `Content-Type: application/json`

Example request body:

```json
{
  "name": "Laptop",
  "description": "Laptop for office and personal use",
  "price": 55000.00,
  "quantity": 10,
  "primaryCategoryId": 1,
  "categoryIds": [1, 2]
}
```

The category IDs must refer to existing categories if required by the service implementation.

### 8.2 Retrieve All Products

**Method:** `GET`

**URL:** `/api/products`

Returns a list of products.

### 8.3 Retrieve a Product by ID

**Method:** `GET`

**URL:** `/api/products/1`

Returns the product associated with the specified ID.

### 8.4 Update a Product

**Method:** `PUT`

**URL:** `/api/products/1`

Example request body:

```json
{
  "name": "Updated Laptop",
  "description": "Updated laptop description",
  "price": 58000.00,
  "quantity": 15,
  "primaryCategoryId": 1,
  "categoryIds": [1, 2]
}
```

### 8.5 Delete a Product

**Method:** `DELETE`

**URL:** `/api/products/1`

A successful deletion returns `204 No Content`.

### 8.6 Pagination and Sorting

**Method:** `GET`

Example URL:

`http://localhost:8080/api/products/page?page=0&size=5&sort=id,asc`

Query parameters:

- `page`: Page number, starting from `0`.
- `size`: Number of records per page. Defaults to `5`.
- `sort`: Field and sort direction. Defaults to sorting by `id`.

For descending order, use:

`http://localhost:8080/api/products/page?page=0&size=5&sort=id,desc`

### 8.7 Search Products by Name

**Method:** `GET`

Example URL:

`http://localhost:8080/api/products/search?name=Laptop`

The `name` query parameter is required. The response contains the products returned by the name-search service method.

## 9. Category API Endpoints

Base URL:

`http://localhost:8080/api/categories`

| HTTP Method | Endpoint | Description | Success Status |
|---|---|---|---|
| POST | `/api/categories` | Create a category | 201 Created |
| GET | `/api/categories` | Retrieve all categories | 200 OK |
| GET | `/api/categories/{id}` | Retrieve a category by ID | 200 OK |
| PUT | `/api/categories/{id}` | Update a category | 200 OK |
| DELETE | `/api/categories/{id}` | Delete a category | 204 No Content |

### 9.1 Create a Category

**Method:** `POST`

**URL:** `/api/categories`

**Header:** `Content-Type: application/json`

Example request body:

```json
{
  "name": "Electronics",
  "description": "Electronic devices and accessories"
}
```

### 9.2 Retrieve All Categories

**Method:** `GET`

**URL:** `/api/categories`

### 9.3 Retrieve a Category by ID

**Method:** `GET`

**URL:** `/api/categories/1`

### 9.4 Update a Category

**Method:** `PUT`

**URL:** `/api/categories/1`

Example request body:

```json
{
  "name": "Books",
  "description": "Books and educational materials"
}
```

### 9.5 Delete a Category

**Method:** `DELETE`

**URL:** `/api/categories/1`

A successful deletion returns `204 No Content`.

## 10. Request Validation

The application uses Jakarta Bean Validation annotations to validate incoming requests.

### Product Validation

| Field | Validation Rule |
|---|---|
| `name` | Required and cannot be blank |
| `name` | Maximum 150 characters |
| `description` | Maximum 1000 characters |
| `price` | Required and must be at least 0.01 |
| `quantity` | Required and must be zero or greater |

### Category Validation

| Field | Validation Rule |
|---|---|
| `name` | Required and cannot be blank |
| `name` | Maximum 100 characters |
| `description` | Maximum 500 characters |

Validation errors are handled through the application's exception-handling implementation.

## 11. Error Handling

The application includes centralized exception handling to provide consistent responses for errors such as invalid request data and unsuccessful resource lookups.

The exact error response format depends on the implementation of `GlobalExceptionHandler`.

## 12. Unit Testing

The project includes tests for service and controller layers using JUnit 5 and Mockito.

Run all tests using:

```bash
mvn test
```

To create a build package:

```bash
mvn clean package
```

## 13. PostgreSQL Schema

The application uses H2 as its default database, as specified in the assignment's core requirements.

The assignment submission instructions also request a PostgreSQL schema or database dump. If included, the separate PostgreSQL-compatible schema is located at:

`database/postgresql-schema.sql`

This file is a submission artifact and does not change the application's default H2 configuration.

## 14. Docker

A `Dockerfile` is included if the containerization setup has been completed and tested.

Docker instructions should be added here once the image build and application startup have been verified.

## 15. Author

**Sagar Kumar**

## 16. Assignment Details

- **Position:** Java Development
- **Company:** Ikaansh Edutech Private Limited
- **Assignment:** Spring Boot REST API for Product Inventory Management
- **Date:** 26 October 2026