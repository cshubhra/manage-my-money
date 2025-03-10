# Code Architecture Documentation

## Overview
This document describes the code architecture of the Ruby on Rails financial management application. It outlines the key components, patterns, and design principles used in the application.

## Application Structure

The application follows the standard Ruby on Rails Model-View-Controller (MVC) architecture:

```mermaid
graph TD
    A[Browser/Client] -->|HTTP Request| B[Rails Router]
    B -->|Routes to| C[Controller]
    C -->|Uses| D[Model]
    D -->|Queries| E[Database]
    C -->|Renders| F[View]
    F -->|HTML Response| A
```

## Models Layer

The models layer implements the business logic and data access. Key patterns used include:

1. **Active Record Pattern** - Models represent database tables and provide object-oriented access
2. **Nested Set Pattern** - Used for the hierarchical category structure
3. **Single Table Inheritance** - Used for report types
4. **Hash Enums** - Custom implementation for enumeration values
5. **Validations** - Extensive use of ActiveRecord validations for data integrity
6. **Callbacks** - Lifecycle hooks for data processing
7. **Named Scopes** - Reusable query definitions

```mermaid
classDiagram
    class ApplicationRecord {
        <<ActiveRecord::Base>>
    }
    
    ApplicationRecord <|-- User
    ApplicationRecord <|-- Category
    ApplicationRecord <|-- Transfer
    ApplicationRecord <|-- TransferItem
    ApplicationRecord <|-- Currency
    ApplicationRecord <|-- Exchange
    ApplicationRecord <|-- Conversion
    ApplicationRecord <|-- Goal
    
    ApplicationRecord <|-- Report
    Report <|-- ShareReport
    Report <|-- FlowReport
    Report <|-- ValueReport
    Report <|-- MultipleCategoryReport
```

## Key Design Patterns

### Nested Set Pattern (for Categories)
The application uses the nested set pattern for efficient tree operations on categories:

```mermaid
graph TD
    A[Root Category] -->|lft:1, rgt:12| B[Child 1]
    A -->|lft:13, rgt:20| C[Child 2]
    B -->|lft:2, rgt:5| D[Grandchild 1.1]
    B -->|lft:6, rgt:11| E[Grandchild 1.2]
    E -->|lft:7, rgt:10| F[Great-Grandchild 1.2.1]
    C -->|lft:14, rgt:19| G[Grandchild 2.1]
    G -->|lft:15, rgt:16| H[Great-Grandchild 2.1.1]
    G -->|lft:17, rgt:18| I[Great-Grandchild 2.1.2]
```

### Money Pattern
The application implements a money pattern to handle multi-currency operations:

```mermaid
graph TD
    A[Money Object] -->|Contains| B[Amount Values]
    A -->|Contains| C[Currency References]
    A -->|Methods| D[Currency Conversion]
    A -->|Methods| E[Mathematical Operations]
    A -->|Methods| F[Formatting]
```

### Hash Enums
The application uses a custom hash enum implementation for type fields:

```mermaid
graph LR
    A[Integer DB Field] -->|Mapped via| B[Hash Enum Definition]
    B -->|Provides| C[Symbolic Access]
    B -->|Provides| D[Validation]
    B -->|Provides| E[Helper Methods]
```

## Controllers Layer

The controllers handle the HTTP request/response cycle and orchestrate application logic:

1. **RESTful Design** - Controllers follow RESTful principles
2. **Filters** - Authentication and authorization filters
3. **Thin Controllers** - Business logic delegated to models
4. **Flash Messages** - User feedback via flash messages

## Views Layer

The views layer renders the user interface:

1. **ERB Templates** - Ruby embedded in HTML
2. **Partials** - Reusable view components
3. **Helpers** - View helper methods for formatting and display logic
4. **Form Builders** - Custom form helpers

## Authentication and Authorization

User authentication and authorization are implemented using:

1. **Custom Authentication System** - Email/password authentication
2. **Account Activation** - Email verification flow
3. **Remember Me** - Persistent sessions
4. **User-based Data Isolation** - Data scoped to current user

## Data Validation

The application implements extensive data validation:

1. **Model Validations** - Field-level validations
2. **Custom Validators** - Complex business rule validation
3. **Transfer Balance Validation** - Ensures financial transfers balance
4. **Currency Conversion Validation** - Validates currency exchange operations

## Multi-Currency Support

The application has sophisticated multi-currency handling:

```mermaid
graph TD
    A[Transfer] -->|Contains| B[Transfer Item 1]
    A -->|Contains| C[Transfer Item 2]
    B -->|Has| D[Currency 1]
    C -->|Has| E[Currency 2]
    A -->|Uses| F[Conversion]
    F -->|References| G[Exchange Rate]
    G -->|Between| D
    G -->|And| E
```

## Reporting System

The reporting system uses polymorphism and composition:

```mermaid
graph TD
    A[Report Base Class] -->|Types| B[Share Report]
    A -->|Types| C[Flow Report]
    A -->|Types| D[Value Report]
    A -->|Types| E[Multiple Category Report]
    E -->|Has Many| F[Category Report Options]
    F -->|References| G[Category]
```

## Database Schema Management

The application uses Rails migrations for schema management:

1. **Versioned Migrations** - Incremental database changes
2. **Foreign Keys** - Referential integrity
3. **Indexes** - Performance optimization

## Testing Approach

The application includes various types of tests:

1. **Unit Tests** - Model validation and business logic
2. **Functional Tests** - Controller actions
3. **Integration Tests** - End-to-end workflows

## Technical Debt and Future Improvements

Several areas have been identified for potential improvement:

1. **Search Functionality** - Currently using a mix of SQL and Ruby code
2. **Delta Indexing** - Commented out, could be enabled for search
3. **Currency Handling** - Some methods marked with TODO comments
4. **Performance Optimization** - Some queries could be optimized

This document provides a high-level overview of the code architecture. It outlines the key patterns, components, and design decisions in the application, serving as a guide for understanding how the codebase is structured and why certain approaches were chosen.