# 🚀 Spring Boot User Authentication System Template

A comprehensive, production-ready Spring Boot 3 template with JWT authentication, role-based access control, and complete user management system.

## 📋 Features

- **🔐 JWT Authentication**: Secure token-based authentication
- **👥 User Management**: Complete CRUD operations for users
- **🛡️ Role-Based Access Control**: Admin and User roles
- **📚 API Documentation**: Auto-generated Swagger/OpenAPI docs
- **🐳 Docker Support**: Containerized application with Docker Compose
- **🗄️ Database**: PostgreSQL for production, H2 for testing
- **⚡ Modern Stack**: Spring Boot 3.x, Java 21, Gradle
- **📊 Monitoring**: Spring Boot Actuator integration
- **🧪 Testing Ready**: Pre-configured test structure

## 🏗️ Architecture

```
src/main/java/com/example/usertemplate/
├── 📁 global/                    # Global configurations & utilities
│   ├── 📁 config/               # Security, JPA, OpenAPI configs
│   ├── 📁 security/             # JWT provider, filters, user details
│   ├── 📁 exception/            # Global exception handling
│   └── 📁 common/               # Common DTOs and base entities
├── 📁 auth/                     # Authentication module
│   ├── 📁 controller/           # Auth endpoints
│   ├── 📁 service/              # Auth business logic
│   └── 📁 dto/                  # Auth DTOs
├── 📁 user/                     # User management module
│   ├── 📁 controller/           # User endpoints
│   ├── 📁 service/              # User business logic
│   ├── 📁 repository/           # User data access
│   ├── 📁 entity/               # User entity & enums
│   └── 📁 dto/                  # User DTOs
└── 📁 admin/                    # Admin management module
    ├── 📁 controller/           # Admin endpoints
    └── 📁 service/              # Admin business logic
```

## 🚀 Quick Start

### Prerequisites

- **Java 21+**
- **PostgreSQL 12+** (for production)
- **Docker & Docker Compose** (optional)
- **Gradle 7+**

### 1. Clone & Setup

```bash
git clone <repository-url>
cd usertemplate
```

### 2. Database Setup

#### Option A: Docker (Recommended)

```bash
# Start PostgreSQL with Docker
docker-compose up postgres -d

# Check if PostgreSQL is running
docker-compose logs postgres
```

#### Option B: Local PostgreSQL

```sql
-- Create database
CREATE DATABASE usertemplate_dev;
CREATE USER postgres WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE usertemplate_dev TO postgres;
```

### 3. Environment Variables

Create `.env` file (optional):

```env
DB_USERNAME=postgres
DB_PASSWORD=password
JWT_SECRET=myVerySecretKeyForJwtTokenGenerationAndValidationThatMustBeLongEnough
```

### 4. Run Application

```bash
# Development mode
./gradlew bootRun

# Or with custom profile
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### 5. Access the Application

- **Application**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs
- **Health Check**: http://localhost:8080/actuator/health

## 🔑 API Endpoints

### Authentication Endpoints

| Method | Endpoint                | Description       | Public |
| ------ | ----------------------- | ----------------- | ------ |
| POST   | `/api/v1/auth/register` | Register new user | ✅     |
| POST   | `/api/v1/auth/login`    | Login user        | ✅     |
| POST   | `/api/v1/auth/refresh`  | Refresh JWT token | ✅     |

### User Management Endpoints

| Method | Endpoint           | Description                 | Roles       |
| ------ | ------------------ | --------------------------- | ----------- |
| GET    | `/api/v1/users/me` | Get current user profile    | USER, ADMIN |
| PUT    | `/api/v1/users/me` | Update current user profile | USER, ADMIN |
| DELETE | `/api/v1/users/me` | Delete current user account | USER, ADMIN |

### Admin Endpoints

| Method | Endpoint                           | Description               | Roles |
| ------ | ---------------------------------- | ------------------------- | ----- |
| GET    | `/api/v1/admin/users`              | Get all users (paginated) | ADMIN |
| GET    | `/api/v1/admin/users/search`       | Search users              | ADMIN |
| GET    | `/api/v1/admin/users/{id}`         | Get user by ID            | ADMIN |
| PUT    | `/api/v1/admin/users/{id}`         | Update user               | ADMIN |
| DELETE | `/api/v1/admin/users/{id}`         | Delete user               | ADMIN |
| POST   | `/api/v1/admin/users/{id}/enable`  | Enable user account       | ADMIN |
| POST   | `/api/v1/admin/users/{id}/disable` | Disable user account      | ADMIN |

## 🧪 Testing the API

### 1. Register a new user

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

### 2. Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### 3. Use the JWT token

```bash
# Replace YOUR_JWT_TOKEN with the actual token from login response
curl -X GET http://localhost:8080/api/v1/users/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🐳 Docker Deployment

### Development Environment

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop all services
docker-compose down
```

### Production Deployment

```bash
# Build and run in production mode
docker-compose -f docker-compose.yml up -d --build

# With custom environment
SPRING_PROFILES_ACTIVE=prod DB_PASSWORD=your_prod_password docker-compose up -d
```

## 🔧 Configuration

### Application Profiles

- **dev**: Development with create-drop schema
- **test**: Testing with H2 in-memory database
- **prod**: Production with validate schema

### Environment Variables

| Variable                 | Description        | Default          |
| ------------------------ | ------------------ | ---------------- |
| `SPRING_PROFILES_ACTIVE` | Active profile     | `dev`            |
| `DB_USERNAME`            | Database username  | `postgres`       |
| `DB_PASSWORD`            | Database password  | `password`       |
| `DATABASE_URL`           | Full database URL  | -                |
| `JWT_SECRET`             | JWT signing secret | `default_secret` |

## 🔐 Security Features

- **BCrypt Password Hashing**: Secure password storage
- **JWT Token Authentication**: Stateless authentication
- **Role-Based Access Control**: USER and ADMIN roles
- **CORS Configuration**: Configurable cross-origin requests
- **Input Validation**: Request validation with Bean Validation
- **Security Headers**: Standard security headers

## 📊 Monitoring & Health Checks

- **Health Endpoint**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Application Info**: `/actuator/info`
- **Docker Health Check**: Built-in container health monitoring

## 🧪 Testing

### Run Tests

```bash
# Run all tests
./gradlew test

# Run tests with coverage
./gradlew test jacocoTestReport

# Run integration tests
./gradlew integrationTest
```

### Test Data

Default test accounts:

- **Admin**: `admin` / `admin123`
- **User**: `user` / `user123`

## 📚 API Documentation

Interactive API documentation is available at:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## 🚀 Adding New Features

### 1. Create New Domain Module

```bash
mkdir -p src/main/java/com/example/usertemplate/newmodule/{controller,service,repository,entity,dto}
```

### 2. Follow the Pattern

```java
// Controller
@RestController
@RequestMapping("/api/v1/newmodule")
@RequiredArgsConstructor
public class NewModuleController {
    private final NewModuleService service;
    // ... endpoints
}

// Service Interface
public interface NewModuleService {
    // ... methods
}

// Service Implementation
@Service
@RequiredArgsConstructor
public class NewModuleServiceImpl implements NewModuleService {
    // ... implementation
}
```

### 3. Update Security Configuration

Add new endpoints to `SecurityConfig.java`:

```java
.requestMatchers("/api/v1/newmodule/**").hasRole("USER")
```

## 🔍 Troubleshooting

### Common Issues

1. **Database Connection Issues**

   ```bash
   # Check if PostgreSQL is running
   docker-compose ps postgres

   # View database logs
   docker-compose logs postgres
   ```

2. **JWT Token Issues**

   - Ensure JWT secret is long enough (256 bits minimum)
   - Check token expiration time
   - Verify Bearer token format: `Authorization: Bearer <token>`

3. **Port Already in Use**

   ```bash
   # Find process using port 8080
   lsof -i :8080

   # Kill the process
   kill -9 <PID>
   ```

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/your-repo/issues)
- **Discussions**: [GitHub Discussions](https://github.com/your-repo/discussions)
- **Email**: your-email@example.com

---

**🎉 Happy Coding!** This template provides a solid foundation for building scalable Spring Boot applications with authentication.
