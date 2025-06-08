# Inventra Backend API

A Spring Boot-based backend system supporting:

- Organization and user registration
- Role-based admin creation
- JWT-based authentication
- Multiple database support (MySQL, PostgreSQL)
- DTO-based clean response structure
- Centralized error handling

---

## 🚀 Features

- ✅ Register organization with admin user
- ✅ Login with JWT generation
- ✅ Token validation
- ✅ Role table with default values (ORG_ADMIN, BRANCH_ADMIN)
- ✅ DTO-based structured API responses
- ✅ Centralized error/validation handler
- ✅ Multiple database support (MySQL, PostgreSQL)
- ✅ Profile-based configuration

---

## 🧰 Tech Stack

- Java 17
- Spring Boot
- Spring Security + JWT
- MySQL / PostgreSQL
- Spring Data JPAn
- Maven

---

## 🔧 Database Configuration

The application uses a flexible configuration approach with templates:

1. Database configuration is stored in a **developer-specific** `application-local.properties` file
2. This file is **not** committed to git (added to .gitignore)
3. Template files are provided for different database setups

### Database Setup

Choose your preferred database and copy the appropriate template:

**For MySQL:**
```bash
# Windows
copy src\main\resources\application-mysql.properties.template src\main\resources\application-local.properties

# Linux/macOS
cp src/main/resources/application-mysql.properties.template src/main/resources/application-local.properties
```

**For PostgreSQL:**
```bash
# Windows
copy src\main\resources\application-postgres.properties.template src\main\resources\application-local.properties

# Linux/macOS
cp src/main/resources/application-postgres.properties.template src/main/resources/application-local.properties
```

After copying, you can customize your `application-local.properties` with your specific database credentials.

## 🚀 Running the Application

### MySQL Setup
1. Make sure MySQL is installed and running
2. Configure your database settings in `application-local.properties`
3. Run the application:
   ```
   mvn spring-boot:run
   ```

### PostgreSQL Setup
1. Make sure PostgreSQL is installed and running
2. Create a database named `inventra_db`:
   ```sql
   CREATE DATABASE inventra_db;
   ```
3. Configure your database settings in `application-local.properties`
4. Run the application:
   ```
   mvn spring-boot:run
   ```

## 🧪 Testing the API

After running the application, you can test the API using:

1. Register a new organization/admin:
   ```
   POST http://localhost:8889/api/v1/signup
   {
     "email": "admin@example.com",
     "userName": "admin123",
     "password": "securePassword123",
     "firstName": "John",
     "middleName": "XYZ",
     "lastName": "Doe",
     "orgName": "Acme Corporation"
   }
   ```

2. Login:
   ```
   POST http://localhost:8889/api/v1/login
   {
     "username": "admin123",
     "password": "securePassword123"
   }
   ```