# Financial Management Application

This project is a migration of a Ruby on Rails financial management application to a modern stack using Angular for the frontend and Spring Boot for the backend.

## Project Overview

The application helps users manage their personal finances, track expenses and income, set financial goals, and generate reports. Key features include:

- User authentication and authorization
- Category management with hierarchical structure
- Transaction tracking with multi-currency support
- Exchange rate management
- Financial reporting and data visualization
- Goal setting and tracking
- Data import from bank statements

## Technology Stack

### Frontend
- Angular 15+
- Angular Material
- Chart.js for data visualization
- NgRx for state management (where needed)

### Backend
- Spring Boot 3.x
- Spring Data JPA
- Spring Security with JWT
- PostgreSQL/MySQL database
- OpenAPI/Swagger for API documentation

## Project Structure

The project is organized into two main components:

1. **frontend** - Angular application
2. **backend** - Spring Boot application

## Documentation

Comprehensive documentation is available in the `/documentation` folder:

- [Entity-Relationship Diagram](documentation/entity-relationship-diagram.md)
- [Application Architecture](documentation/application-architecture.md)
- [Migration Strategy](documentation/migration-strategy.md)
- [API Design](documentation/api-design.md)
- [Angular Frontend Architecture](documentation/angular-frontend-architecture.md)
- [Spring Boot Backend Architecture](documentation/spring-boot-backend-architecture.md)
- [Database Schema](documentation/database-schema.md)
- [Implementation Timeline](documentation/implementation-timeline.md)
- [Testing Strategy](documentation/testing-strategy.md)
- [Sequence Diagrams](documentation/sequence-diagrams.md)

## Getting Started

### Prerequisites

- Node.js 16+ and npm
- Java 17+
- Maven or Gradle
- PostgreSQL or MySQL database

### Backend Setup

1. Clone the repository
2. Navigate to the backend directory
3. Configure database connection in `application.yml`
4. Run `./mvnw spring-boot:run` (Maven) or `./gradlew bootRun` (Gradle)

The API will be available at http://localhost:8080

### Frontend Setup

1. Navigate to the frontend directory
2. Install dependencies with `npm install`
3. Start the development server with `ng serve`

The application will be available at http://localhost:4200

## Development

### Backend Development

- Use Java code style consistent with Google Java Style Guide
- Follow RESTful API design principles
- Write tests for all business logic
- Document all public APIs

### Frontend Development

- Follow Angular style guide
- Use feature modules with lazy loading
- Write unit tests for services and components
- Use proper state management patterns

## Testing

See the [Testing Strategy](documentation/testing-strategy.md) for detailed information on our testing approach.

- Backend: JUnit, Mockito
- Frontend: Jasmine, Karma
- E2E: Cypress

## Contributing

1. Create a feature branch from `develop`
2. Implement your changes
3. Add tests for your code
4. Ensure all tests pass
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.