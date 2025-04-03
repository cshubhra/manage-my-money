# Current State Architecture

## Overview

The current application is a financial management system built with Ruby on Rails. It allows users to manage their finances, track expenses and income, create reports, set financial goals, and handle multiple currencies.

## Architecture

The application follows the standard Ruby on Rails MVC (Model-View-Controller) architecture:

```mermaid
graph TD
    subgraph Client
        Browser
    end
    
    subgraph "Ruby on Rails Application"
        subgraph "View Layer"
            V[ERB Templates]
            L[Layouts]
            P[Partials]
            H[Helpers]
        end
        
        subgraph "Controller Layer"
            AC[ApplicationController]
            C1[TransfersController]
            C2[CategoriesController]
            C3[ReportsController]
            C4[GoalsController]
            C5[CurrenciesController]
            C6[ExchangesController]
            C7[UsersController]
        end
        
        subgraph "Model Layer"
            M1[User]
            M2[Transfer]
            M3[TransferItem]
            M4[Category]
            M5[Currency]
            M6[Exchange]
            M7[Report]
            M8[Goal]
            M9[Conversion]
        end
    end
    
    subgraph "Data Layer"
        DB[(Database)]
    end
    
    Browser -->|HTTP Request| AC
    AC --> C1
    AC --> C2
    AC --> C3
    AC --> C4
    AC --> C5
    AC --> C6
    AC --> C7
    
    C1 -->|Uses| M1
    C1 -->|Uses| M2
    C1 -->|Uses| M3
    
    C2 -->|Uses| M1
    C2 -->|Uses| M4
    
    C3 -->|Uses| M1
    C3 -->|Uses| M7
    
    C4 -->|Uses| M1
    C4 -->|Uses| M8
    
    C5 -->|Uses| M1
    C5 -->|Uses| M5
    
    C6 -->|Uses| M1
    C6 -->|Uses| M6
    
    C7 -->|Uses| M1
    
    M1 --> DB
    M2 --> DB
    M3 --> DB
    M4 --> DB
    M5 --> DB
    M6 --> DB
    M7 --> DB
    M8 --> DB
    M9 --> DB
    
    C1 --> V
    C2 --> V
    C3 --> V
    C4 --> V
    C5 --> V
    C6 --> V
    C7 --> V
    
    V --> L
    V --> P
    V --> H
```

## Components

### Models
- **User**: Core model representing system users with authentication
- **Category**: Represents financial categories in a hierarchical structure (income, expense, asset, loan, balance)
- **Transfer**: Financial transactions
- **TransferItem**: Individual entries within a transfer, linked to categories and currencies
- **Currency**: Represents different currencies in the system
- **Exchange**: Currency exchange rates
- **Conversion**: Currency conversion records for transfers
- **Goal**: Financial goals linked to categories
- **Report**: Various financial reports (share, value, flow)

### Controllers
- **ApplicationController**: Base controller with shared functionality
- **TransfersController**: Manages financial transactions
- **CategoriesController**: Manages financial categories
- **ReportsController**: Handles report generation and display
- **GoalsController**: Manages financial goals
- **CurrenciesController**: Handles currency management
- **ExchangesController**: Manages exchange rates
- **UsersController**: User management
- **SessionsController**: Authentication

### Plugins and Libraries
The application uses several Ruby on Rails plugins:
- **restful-authentication**: Handles user authentication
- **awesome_nested_set**: Manages hierarchical structures for categories
- **backgroundrb**: Background job processing
- **open_flash_chart**: Generates charts for reports
- **thinking-sphinx**: Search functionality

## Technology Stack

- **Framework**: Ruby on Rails
- **Database**: SQL (based on configuration, likely PostgreSQL or MySQL)
- **Frontend**: 
  - ERB templates with embedded Ruby
  - JavaScript (Prototype framework)
  - CSS
- **Authentication**: Custom mechanism using the restful-authentication plugin
- **Background Processing**: BackgroundRB for report generation

## Key Features

1. User authentication and management
2. Hierarchical categorization of financial entries
3. Transaction management with multi-currency support
4. Currency exchange rate tracking
5. Financial goal setting and tracking
6. Report generation (share reports, value reports, flow reports)
7. Debtor and creditor tracking
8. Data import/export functionality