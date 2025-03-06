# Developer Guide

This document provides guidelines and instructions for developers working on the modernization project to convert the Ruby on Rails application to Angular frontend and Spring Boot backend.

## Table of Contents

1. [Development Environment Setup](#development-environment-setup)
2. [Project Structure](#project-structure)
3. [Coding Standards](#coding-standards)
4. [Git Workflow](#git-workflow)
5. [Building and Running](#building-and-running)
6. [Testing](#testing)
7. [Deployment](#deployment)
8. [Troubleshooting](#troubleshooting)

## Development Environment Setup

### Prerequisites

- **JDK 11+** - For Spring Boot development
- **Node.js 16+** - For Angular development
- **npm 7+** - Package manager for Angular
- **Maven 3.6+** - Build tool for Spring Boot
- **Git** - Version control
- **IDE** - IntelliJ IDEA or Visual Studio Code recommended
- **Docker** - For containerization and local database

### Setting Up Backend (Spring Boot)

1. **Clone the repository:**
   
   ```bash
   git clone <repository-url>
   cd <repository-directory>/backend
   ```

2. **Configure application.properties:**
   
   Create a copy of `application-example.properties` as `application-dev.properties` and configure database connection:
   
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/financeapp
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Run the application:**
   
   ```bash
   mvn spring-boot:run
   ```

4. **Access API documentation:**
   
   Open your browser and navigate to: `http://localhost:8080/swagger-ui.html`

### Setting Up Frontend (Angular)

1. **Navigate to frontend directory:**
   
   ```bash
   cd <repository-directory>/frontend
   ```

2. **Install dependencies:**
   
   ```bash
   npm install
   ```

3. **Configure environment:**
   
   Create a copy of `environment.example.ts` as `environment.ts` and update API URL:
   
   ```typescript
   export const environment = {
     production: false,
     apiUrl: 'http://localhost:8080/api'
   };
   ```

4. **Run development server:**
   
   ```bash
   ng serve
   ```

5. **Access frontend:**
   
   Open your browser and navigate to: `http://localhost:4200`

## Project Structure

### Backend Structure (Spring Boot)

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── financeapp/
│   │   │           ├── config/           # Configuration classes
│   │   │           ├── controller/       # REST controllers
│   │   │           ├── dto/              # Data Transfer Objects
│   │   │           ├── exception/        # Custom exceptions
│   │   │           ├── mapper/           # DTO-Entity mappers
│   │   │           ├── model/            # Domain entities
│   │   │           ├── repository/       # JPA repositories
│   │   │           ├── security/         # Security config
│   │   │           ├── service/          # Business logic
│   │   │           └── util/             # Utility classes
│   │   └── resources/
│   │       ├── application.properties    # Main config
│   │       ├── application-dev.properties  # Dev config
│   │       └── db/
│   │           └── migration/            # Flyway migrations
│   └── test/                             # Test classes
├── pom.xml                               # Maven config
└── README.md                             # Backend documentation
```

### Frontend Structure (Angular)

```
frontend/
├── src/
│   ├── app/
│   │   ├── core/                         # Core module
│   │   │   ├── auth/                     # Authentication
│   │   │   ├── interceptors/             # HTTP interceptors
│   │   │   └── services/                 # Core services
│   │   ├── shared/                       # Shared module
│   │   │   ├── components/               # Common components
│   │   │   ├── directives/               # Shared directives
│   │   │   └── pipes/                    # Shared pipes
│   │   ├── features/                     # Feature modules
│   │   │   ├── categories/               # Categories feature
│   │   │   ├── transfers/                # Transfers feature
│   │   │   └── ...                       # Other features
│   │   ├── store/                        # NgRx store
│   │   │   ├── actions/                  # Store actions
│   │   │   ├── effects/                  # Store effects
│   │   │   ├── reducers/                 # Store reducers
│   │   │   └── selectors/                # Store selectors
│   │   ├── app.component.ts              # Root component
│   │   └── app.module.ts                 # Root module
│   ├── assets/                           # Static assets
│   ├── environments/                     # Environment config
│   └── styles/                           # Global styles
├── angular.json                          # Angular config
├── package.json                          # npm dependencies
└── README.md                             # Frontend documentation
```

## Coding Standards

### Java Code Style

- **Naming Conventions**:
  - Classes: PascalCase
  - Methods/Variables: camelCase
  - Constants: UPPER_SNAKE_CASE
- **Documentation**: Javadoc for public APIs
- **Code Formatting**: Google Java Style Guide
- **Line Length**: 120 characters maximum
- **Imports**: No wildcard imports
- **Best Practices**: Prefer immutability, use functional constructs where appropriate

### TypeScript/Angular Code Style

- **Naming Conventions**:
  - Components/Services: PascalCase
  - Methods/Variables: camelCase
  - Constants: UPPER_SNAKE_CASE
  - Files: kebab-case.type.ts (e.g., user-profile.component.ts)
- **Documentation**: JSDoc for public APIs
- **Code Formatting**: Angular Style Guide
- **Line Length**: 100 characters maximum
- **Imports**: Group and sort imports
- **Best Practices**: Follow Angular best practices, prefer reactive approach

## Git Workflow

### Branching Strategy

- **main**: Production-ready code
- **develop**: Integration branch for feature work
- **feature/xxx**: Feature branches
- **bugfix/xxx**: Bug fix branches
- **release/xxx**: Release preparation branches

### Commit Guidelines

- Use descriptive commit messages with a summary line and detailed description
- Reference issue numbers in commits (e.g., "Fix #123: Add validation to transfer form")
- Keep commits focused on single logical changes

### Pull Request Process

1. Create a feature branch from `develop`
2. Develop and test your changes
3. Create a pull request to merge back to `develop`
4. Ensure CI/CD checks pass
5. Get at least one code review
6. Merge after approval

## Building and Running

### Backend (Spring Boot)

#### Development

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Production Build

```bash
mvn clean package
java -jar target/financeapp-backend-1.0.0.jar
```

### Frontend (Angular)

#### Development

```bash
ng serve
```

#### Production Build

```bash
ng build --prod
```

## Testing

### Backend Testing

- **Unit Tests**: Use JUnit 5 and Mockito
  
  ```bash
  mvn test
  ```

- **Integration Tests**: Tests with database using TestContainers
  
  ```bash
  mvn verify
  ```

- **API Tests**: Postman or REST Assured

### Frontend Testing

- **Unit Tests**: Use Jasmine and Karma
  
  ```bash
  ng test
  ```

- **E2E Tests**: Use Cypress
  
  ```bash
  ng e2e
  ```

## Deployment

### Docker Deployment

#### Building Docker Images

```bash
# Backend
cd backend
mvn clean package
docker build -t financeapp-backend .

# Frontend
cd frontend
ng build --prod
docker build -t financeapp-frontend .
```

#### Running with Docker Compose

Create a `docker-compose.yml` file:

```yaml
version: '3.8'
services:
  database:
    image: postgres:13
    environment:
      POSTGRES_USER: financeapp
      POSTGRES_PASSWORD: password
      POSTGRES_DB: financeapp
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
      
  backend:
    image: financeapp-backend
    depends_on:
      - database
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://database:5432/financeapp
      SPRING_DATASOURCE_USERNAME: financeapp
      SPRING_DATASOURCE_PASSWORD: password
    ports:
      - "8080:8080"
      
  frontend:
    image: financeapp-frontend
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  postgres_data:
```

Run with:

```bash
docker-compose up -d
```

## Troubleshooting

### Common Backend Issues

- **Database Connection Issues**:
  - Verify connection settings in application properties
  - Ensure database is running and accessible
  - Check for proper credentials

- **Hibernate/JPA Issues**:
  - Check entity mappings
  - Review SQL logs for query problems
  - Ensure proper transaction management

- **Spring Security Issues**:
  - Verify security configuration
  - Check JWT token configuration
  - Review CORS settings

### Common Frontend Issues

- **API Communication Issues**:
  - Verify API URL in environment configuration
  - Check network requests in browser dev tools
  - Ensure CORS is properly configured on backend

- **NgRx Store Issues**:
  - Use Redux DevTools to inspect store state
  - Verify action dispatch and reducer logic
  - Check effect implementation for API calls

- **Build Issues**:
  - Clear npm cache: `npm cache clean --force`
  - Delete node_modules and reinstall: `rm -rf node_modules && npm install`
  - Verify Angular CLI version compatibility