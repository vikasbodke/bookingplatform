# Project: Booking Platform API

An API for a booking platform, to help users browse movies, check showtimes, and make bookings.

---

## 🚀 Features

- **User Facing Features**
    - Browse movies in a city, for today or a future date
    - See show timings for a selected date, across theaters
    - Filter by language and format (2D, 3D, IMAX) & genres
    - **Booking Features**
        - Apply promotional offers and discounts
        - Receive booking confirmation
- **Partner Facing Features**
    - Manage movies, theaters, and showtimes

## Technical Stack
- Reactive stack with **Spring WebFlux** & **R2DBC**
- Standard **CRUD APIs** for managing entities
- **Caffeine Cache** for in-memory caching
- **Embedded Postgres** for local development and testing
- Supports **Unit & Integration Tests** with isolated database
- **Distributed Tracing** with Micrometer Tracing and Brave

---

## 📂 Project Structure

```
booking-platform/
├── src/
│   ├── main/
│   │   ├── java/com/bookingplatform/
│   │   │   ├── config/          # Application and database configuration
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entities/        # Entities
│   │   │   ├── error/           # Exception and error handling
│   │   │   └── usecases/        # Business logic and use cases eg Browse, Book, etc
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── data.sql         # Initial data
│   │       └── schema.sql       # Database schema
│   └── test/                    # Test classes
│       └── ...
├── pom.xml                      # Maven build configuration
└── README.md
```

---

## Design details

**`docs/`** contains the design documentation for the project.

## ⚙️ Tech Stack

- **Java 24**
- **Spring Boot WebFlux**
- **Caffeine Cache** (in memory for development, recommended Redis)
- **Spring Data R2DBC**
- **Embedded Postgres**(for development)
- **Maven**
- **Docker**

---

## 🔧 Configuration

### 1️⃣ Application Properties

**`src/main/resources/application.yml`**  

```yaml
spring:
  application:
    name: BookingPlatform

  # Database Configuration
  r2dbc:
    url: r2dbc:postgresql://...
```
### 2️⃣ Logging
**`src/main/resources/log4j2.xml`**.

### Packaging
    dockerfile: infra/Dockerfile
---

## 📡 Sample API

### Update User

```http
POST /api/v1/users/{id}
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com"
}
```

**Sample Controller**

```java
@PutMapping("/{id}")
public Mono<AppUser> updateUser(@PathVariable(name = "id") Long id,
                                @RequestBody AppUser user) {
    user.setId(id);
    return service.save(user);
}
```

---

## 🧪 Running Tests

The project uses **Embedded Postgres** for isolated testing.  

```bash
# Run all tests
./mvnw test
```

---

## ▶️ Running the Application

```bash
# Build and run
./mvnw spring-boot:run
```

The application will start at **http://localhost:8080**.

---

