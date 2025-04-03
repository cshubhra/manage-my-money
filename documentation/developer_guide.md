# Developer Guide

## Overview

This document provides guidelines and instructions for developers working on the modernization project to convert the Ruby on Rails application to an Angular/Spring Boot application.

## Project Structure

The project will consist of two main components:

1. **Frontend**: Angular application
2. **Backend**: Spring Boot application

### Frontend Structure (Angular)

```
/frontend
├── src/
│   ├── app/
│   │   ├── core/                 # Core functionality, services, guards
│   │   ├── shared/               # Shared components, directives, pipes
│   │   ├── features/             # Feature modules
│   │   │   ├── auth/             # Authentication
│   │   │   ├── categories/       # Categories management
│   │   │   ├── transfers/        # Transfers management
│   │   │   ├── reports/          # Reports generation and display
│   │   │   ├── goals/            # Financial goals
│   │   │   ├── currencies/       # Currencies management
│   │   │   └── exchanges/        # Exchange rates
│   │   ├── store/                # NgRx state management
│   │   ├── app-routing.module.ts # Main routing
│   │   └── app.module.ts         # Main module
│   ├── assets/                   # Static assets
│   └── environments/             # Environment configurations
├── angular.json                  # Angular CLI config
└── package.json                  # Dependencies
```

### Backend Structure (Spring Boot)

```
/backend
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/finance/app/
│   │   │   │   ├── config/             # Configuration classes
│   │   │   │   ├── controller/         # REST controllers
│   │   │   │   ├── dto/                # Data Transfer Objects
│   │   │   │   ├── exception/          # Custom exceptions
│   │   │   │   ├── model/              # Entity models
│   │   │   │   ├── repository/         # JPA repositories
│   │   │   │   ├── security/           # Security config, JWT
│   │   │   │   ├── service/            # Business logic
│   │   │   │   └── util/               # Utility classes
│   │   ├── resources/
│   │   │   ├── application.properties  # App configuration
│   │   │   ├── db/migration/           # Database migrations
│   │   │   └── static/                 # Static resources
│   ├── test/                           # Test files
├── pom.xml                             # Maven dependencies
└── build.gradle                        # Gradle configuration (alternative)
```

## Development Environment Setup

### Prerequisites

- Java JDK 17+
- Node.js 16+ and npm 8+
- Maven 3.8+ or Gradle 7.5+
- IDE (IntelliJ IDEA, VS Code, or Eclipse)
- Git
- Docker (for containerization and database)

### Frontend Setup

1. Clone the repository:
   ```bash
   git clone [repository-url]
   cd [repository-name]/frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Run development server:
   ```bash
   npm start
   ```
   The application will be available at `http://localhost:4200/`

4. Run tests:
   ```bash
   npm test
   ```

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd [repository-name]/backend
   ```

2. Build the project:
   ```bash
   # Using Maven
   mvn clean install
   
   # Using Gradle
   ./gradlew build
   ```

3. Run the application:
   ```bash
   # Using Maven
   mvn spring-boot:run
   
   # Using Gradle
   ./gradlew bootRun
   ```
   The API will be available at `http://localhost:8080/`

4. Run tests:
   ```bash
   # Using Maven
   mvn test
   
   # Using Gradle
   ./gradlew test
   ```

## Database Migration

The database migration process will use Flyway or Liquibase to handle schema changes and data migration.

### Migration Steps

1. **Schema Migration**: Convert the Rails schema to Spring Boot entities
2. **Data Migration**: Create scripts to migrate data to the new schema
3. **Verification**: Validate data integrity after migration

## Development Workflow

1. **Branch Strategy**:
   - `master`: Production-ready code
   - `develop`: Integration branch
   - Feature branches: `feature/feature-name`
   - Bug fixes: `fix/bug-description`

2. **Development Process**:
   - Create a new branch from `develop`
   - Implement changes
   - Write tests
   - Submit pull request to `develop`
   - Review and approval
   - Merge to `develop`

3. **Code Reviews**:
   - All code changes must be reviewed before merging
   - Focus on maintainability, performance, and security
   - Ensure test coverage

## API Development Guidelines

### REST API Design

1. **Resource Naming**:
   - Use nouns for resources (`/users`, `/transfers`)
   - Use plural for collections
   - Use sub-resources for relationships (`/categories/{id}/subcategories`)

2. **HTTP Methods**:
   - GET: Retrieve resources
   - POST: Create resources
   - PUT/PATCH: Update resources
   - DELETE: Remove resources

3. **Status Codes**:
   - 200: Success
   - 201: Created
   - 204: No Content
   - 400: Bad Request
   - 401: Unauthorized
   - 403: Forbidden
   - 404: Not Found
   - 500: Server Error

4. **Versioning**:
   - API versioning via URL path: `/api/v1/resource`

5. **Response Format**:
   ```json
   {
     "data": {
       "id": 1,
       "attribute": "value"
     },
     "meta": {
       "timestamp": "2023-12-01T12:00:00Z"
     }
   }
   ```

6. **Error Format**:
   ```json
   {
     "error": {
       "code": "VALIDATION_ERROR",
       "message": "Invalid input",
       "details": [
         {
           "field": "name",
           "message": "Name cannot be empty"
         }
       ]
     }
   }
   ```

## Frontend Development Guidelines

1. **Component Structure**:
   - Smart components (containers) handle logic and state
   - Presentation components receive data via inputs
   - Follow the single responsibility principle

2. **State Management**:
   - Use NgRx for global state
   - Use component state for local state
   - Implement selectors for data access
   - Use effects for side effects

3. **Styling**:
   - Use Angular Material for UI components
   - Follow BEM methodology for custom CSS
   - Ensure responsive design

4. **Testing**:
   - Unit test components and services
   - Test state management with NgRx testing utilities
   - Write end-to-end tests for critical flows

## Backend Development Guidelines

1. **Architecture**:
   - Follow clean architecture principles
   - Use DTOs for API contracts
   - Implement service layer for business logic
   - Use repositories for data access

2. **Security**:
   - Implement JWT authentication
   - Define role-based access control
   - Validate inputs
   - Protect against common vulnerabilities (OWASP Top 10)

3. **Testing**:
   - Write unit tests for services
   - Write integration tests for repositories
   - Write API tests with MockMvc or RestAssured
   - Use TestContainers for database tests

## Build and Deployment

### CI/CD Pipeline

1. **Build**:
   - Compile code
   - Run tests
   - Static code analysis
   - Build Docker images

2. **Deploy**:
   - Deploy to development environment
   - Run smoke tests
   - Deploy to staging/production

### Docker Setup

1. **Frontend Dockerfile**:
   ```dockerfile
   FROM node:16-alpine as build
   WORKDIR /app
   COPY package*.json ./
   RUN npm install
   COPY . .
   RUN npm run build --prod
   
   FROM nginx:alpine
   COPY --from=build /app/dist/* /usr/share/nginx/html/
   EXPOSE 80
   ```

2. **Backend Dockerfile**:
   ```dockerfile
   FROM openjdk:17-slim
   WORKDIR /app
   COPY target/*.jar app.jar
   EXPOSE 8080
   ENTRYPOINT ["java", "-jar", "app.jar"]
   ```

3. **Docker Compose**:
   ```yaml
   version: '3'
   services:
     frontend:
       build: ./frontend
       ports:
         - "80:80"
       depends_on:
         - backend
     backend:
       build: ./backend
       ports:
         - "8080:8080"
       environment:
         - SPRING_PROFILES_ACTIVE=prod
       depends_on:
         - database
     database:
       image: postgres:14
       ports:
         - "5432:5432"
       environment:
         - POSTGRES_USER=app
         - POSTGRES_PASSWORD=secret
         - POSTGRES_DB=finance
       volumes:
         - pgdata:/var/lib/postgresql/data
   volumes:
     pgdata:
   ```

## Monitoring and Logging

1. **Logging**:
   - Use SLF4J with Logback on the backend
   - Configure log levels
   - Implement structured logging

2. **Monitoring**:
   - Spring Boot Actuator for health checks
   - Micrometer for metrics
   - Prometheus for metrics collection
   - Grafana for visualization

## Additional Resources

- [Angular Documentation](https://angular.io/docs)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [NgRx Documentation](https://ngrx.io/docs)
- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)

## Troubleshooting

### Common Issues

1. **JWT Token Expired**:
   - Check token expiration time
   - Implement token refresh

2. **CORS Issues**:
   - Configure CORS in Spring Security
   - Allow specific origins

3. **Database Connection Issues**:
   - Check database credentials
   - Verify database service is running
   - Check connection pool settings