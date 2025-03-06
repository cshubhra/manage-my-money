# Project Overview

## Introduction

This project involves reengineering a Ruby on Rails application into a modern architecture with an Angular frontend and a Spring Boot backend. The original application appears to be a personal finance management system with features such as:

- User authentication and management
- Categories for organizing financial data
- Currency management
- Financial exchanges
- Goal tracking
- Report generation
- Transfer management

## Original Application Structure

The original Ruby on Rails application follows the standard MVC architecture:

- **Models**: Define data structures and business logic
- **Views**: Handle presentation logic with ERB templates
- **Controllers**: Process requests and coordinate between models and views

## New Architecture

The reengineered application will follow a modern client-server architecture:

### Frontend (Angular)
- Single Page Application
- Component-based UI
- TypeScript for robust type checking
- State management
- Responsive design
- Material Design components

### Backend (Spring Boot)
- RESTful API endpoints
- JWT authentication
- Spring Data JPA for database operations
- Service layer for business logic
- Repository pattern for data access
- Comprehensive unit and integration tests

## Migration Strategy

1. Document and understand the existing RoR application
2. Map out data models and relationships
3. Design API endpoints
4. Implement the Spring Boot backend
5. Implement the Angular frontend
6. Migrate and validate data
7. Deploy the new application