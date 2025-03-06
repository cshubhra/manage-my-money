# System Overview - Ruby on Rails to Angular/Spring Boot Migration

## Original Ruby on Rails Application

The original application is a personal finance management system built in Ruby on Rails, allowing users to:

1. Create and manage categories for income and expenses
2. Track transfers (transactions) between categories
3. Support multiple currencies with exchange rates
4. Generate reports on financial data
5. Set financial goals

### Core Domain Models

- **User**: Represents the system user with authentication and preferences
- **Category**: Hierarchical categories for organizing finances (income, expenses, assets, etc.)
- **Transfer**: Financial transactions consisting of multiple transfer items
- **TransferItem**: Individual entries in a transfer, connecting to categories and currencies 
- **Currency**: Monetary units used in transfers
- **Exchange**: Currency exchange rates between two currencies
- **Conversion**: Links transfers to exchanges for calculating values in different currencies

### Key Relationships

- Users have many categories, transfers, and currencies
- Categories form a hierarchical tree structure (nested set)
- Transfers contain multiple transfer items
- Transfer items belong to categories and have specific currencies
- Exchanges define conversion rates between currencies

## Migration Goals

This project aims to migrate the Ruby on Rails application to a modern stack with:

1. Angular frontend for a responsive single-page application
2. Spring Boot backend providing RESTful APIs
3. Improved architecture with clear separation of concerns
4. Maintaining all existing functionality while enhancing the user experience

### Architecture Overview

The new system will follow a clear client-server architecture:

- **Frontend**: Angular application handling UI rendering and user interactions
- **Backend**: Spring Boot RESTful API services for business logic and data access
- **Communication**: HTTP/JSON between frontend and backend components

This document serves as the starting point for our migration project. Additional technical specifications and diagrams will be created to guide the implementation process.