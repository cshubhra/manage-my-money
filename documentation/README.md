# Ruby on Rails to Angular and Spring Boot Migration Project

## Overview
This document outlines the migration plan from a Ruby on Rails application to a modern single-page application architecture using Angular for the frontend and Spring Boot for the backend.

## Application Description
The original application is a personal finance management system that allows users to:
- Track income and expenses across multiple categories
- Manage multiple currencies and exchange rates
- Generate reports and visualize financial data
- Set financial goals and track progress
- Manage loans and debts
- Import financial data from external sources

## Migration Approach
The migration will be conducted in several phases:
1. Document the existing Ruby on Rails application
2. Design the new architecture for Angular and Spring Boot
3. Implement the Spring Boot backend APIs
4. Develop the Angular frontend components
5. Test the new application
6. Deploy and migrate data

## Repository Structure
- `/documentation` - Contains detailed documentation about both the original application and the migration process
  - `entity-relationship-diagram.md` - ERD diagram in Mermaid format
  - `api-design.md` - REST API endpoints specifications
  - `application-architecture.md` - Overall architecture of the new system
  - `sequence-diagrams.md` - Key user flows in Mermaid format
  - `database-schema.md` - Database schema for the new application
- `/frontend` - Angular application code (to be developed)
- `/backend` - Spring Boot application code (to be developed)

## Technology Stack
- **Frontend**: Angular 15+, TypeScript, RxJS, Angular Material
- **Backend**: Spring Boot 3.x, Java 17+, JPA/Hibernate, Spring Security
- **Database**: PostgreSQL (migrated from the original MySQL database)
- **Authentication**: JWT-based authentication
- **Testing**: JUnit, Mockito, Jasmine, Karma

## Timeline
See `documentation/implementation-timeline.md` for detailed implementation timeline and milestones.