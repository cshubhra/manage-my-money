# Financial Management Application

This project is a reengineering of a Ruby on Rails financial management application into a modern Angular/Spring Boot stack.

## Project Overview

The Financial Management Application is a personal finance tracking system that allows users to:

- Manage income and expense categories
- Track financial transactions
- Support multiple currencies with exchange rates
- Generate financial reports
- Set and track financial goals

## Technology Stack

### Backend
- **Spring Boot**: Java-based framework for creating the RESTful API
- **Spring Data JPA**: For database access and management
- **Spring Security**: For authentication and authorization
- **JWT**: For secure token-based authentication
- **JUnit/Mockito**: For testing

### Frontend
- **Angular**: Frontend framework for building the single-page application
- **Angular Material**: UI component library
- **NgRx** (optional): For state management
- **Chart.js**: For data visualization
- **Jasmine/Karma**: For testing

## Project Structure

The project is divided into two main parts:

1. **Spring Boot Backend**: Located in the `/backend` directory
2. **Angular Frontend**: Located in the `/frontend` directory

## Documentation

Comprehensive documentation is available in the `/documentation` directory:

- [System Overview](documentation/system_overview.md): High-level overview of the system
- [Entity Relationship Diagram](documentation/entity_relationship_diagram.md): Data model visualization
- [Sequence Diagrams](documentation/sequence_diagrams.md): Workflow visualizations
- [API Design](documentation/api_design.md): REST API endpoints and structures
- [Spring Boot Structure](documentation/spring_boot_structure.md): Backend code organization
- [Angular Structure](documentation/angular_structure.md): Frontend code organization
- [Migration Plan](documentation/migration_plan.md): Step-by-step migration approach

## Getting Started

### Prerequisites
- JDK 11+
- Node.js 14+
- npm 6+
- Maven 3+
- PostgreSQL/MySQL

### Backend Setup
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### Frontend Setup
```bash
cd frontend
npm install
ng serve
```

The application will be available at `http://localhost:4200`.

## Development Status

This project is currently under active development. See the [Migration Plan](documentation/migration_plan.md) for details on the current status and upcoming tasks.

## Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
ng test
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgements

- Original Ruby on Rails application developers
- The Spring Boot and Angular communities for their excellent documentation