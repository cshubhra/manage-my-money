# Current State Sequence Diagrams

This document outlines the key interaction flows in the current Ruby on Rails application through sequence diagrams.

## 1. User Authentication Flow

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant SessionsController
    participant UserModel
    participant Database
    participant UserMailer

    User->>Browser: Access Login Page
    Browser->>SessionsController: GET /login
    SessionsController->>Browser: Render Login Form
    
    User->>Browser: Submit Credentials
    Browser->>SessionsController: POST /sessions
    SessionsController->>UserModel: authenticate(email, password)
    UserModel->>Database: find_by_email
    Database-->>UserModel: User Record
    UserModel-->>SessionsController: Authentication Result
    
    alt Authentication Successful
        SessionsController->>Browser: Redirect to Dashboard
        SessionsController->>UserMailer: Send Login Notification
        UserMailer->>User: Login Alert Email
    else Authentication Failed
        SessionsController->>Browser: Show Error Message
    end
```

## 2. Financial Transfer Flow

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant TransfersController
    participant TransferModel
    participant MoneyModel
    participant CurrencyModel
    participant Database

    User->>Browser: Create Transfer
    Browser->>TransfersController: POST /transfers
    
    TransfersController->>CurrencyModel: get_exchange_rate
    CurrencyModel-->>TransfersController: Current Rate
    
    TransfersController->>MoneyModel: convert_amount
    MoneyModel-->>TransfersController: Converted Amount
    
    TransfersController->>TransferModel: create_transfer
    TransferModel->>Database: Save Transfer
    Database-->>TransferModel: Confirmation
    
    alt Transfer Successful
        TransferModel-->>TransfersController: Success
        TransfersController->>Browser: Show Success Message
    else Transfer Failed
        TransferModel-->>TransfersController: Error
        TransfersController->>Browser: Show Error Message
    end
```

## 3. Report Generation Flow

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant ReportsController
    participant ReportModel
    participant GraphBuilder
    participant Database

    User->>Browser: Request Report
    Browser->>ReportsController: GET /reports
    
    ReportsController->>ReportModel: generate_report(params)
    ReportModel->>Database: fetch_data
    Database-->>ReportModel: Raw Data
    
    ReportModel->>GraphBuilder: build_visualizations
    GraphBuilder-->>ReportModel: Graph Data
    
    ReportModel-->>ReportsController: Processed Report
    ReportsController->>Browser: Render Report View
```

## 4. Category Management Flow

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant CategoriesController
    participant CategoryModel
    participant Database
    participant SystemCategory

    User->>Browser: Manage Categories
    Browser->>CategoriesController: GET /categories
    
    CategoriesController->>CategoryModel: list_categories
    CategoryModel->>Database: fetch_categories
    Database-->>CategoryModel: Category Data
    
    alt Create New Category
        User->>Browser: Create Category
        Browser->>CategoriesController: POST /categories
        CategoriesController->>CategoryModel: create
        CategoryModel->>SystemCategory: validate_category
        SystemCategory-->>CategoryModel: Validation Result
        CategoryModel->>Database: Save Category
    else Update Category
        User->>Browser: Update Category
        Browser->>CategoriesController: PUT /categories/:id
        CategoriesController->>CategoryModel: update
        CategoryModel->>Database: Update Record
    end
    
    Database-->>CategoriesController: Operation Result
    CategoriesController->>Browser: Response
```

## 5. Goal Tracking Flow

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant GoalsController
    participant GoalModel
    participant MoneyModel
    participant Database

    User->>Browser: Set Financial Goal
    Browser->>GoalsController: POST /goals
    
    GoalsController->>GoalModel: create_goal
    GoalModel->>MoneyModel: validate_amount
    MoneyModel-->>GoalModel: Validated Amount
    
    GoalModel->>Database: Save Goal
    Database-->>GoalModel: Confirmation
    
    alt Goal Created
        GoalModel-->>GoalsController: Success
        GoalsController->>Browser: Show Success Message
    else Goal Creation Failed
        GoalModel-->>GoalsController: Error
        GoalsController->>Browser: Show Error Message
    end
```

These sequence diagrams illustrate the main interaction flows in the current Ruby on Rails application. They show how different components interact with each other to handle user requests and process business logic. Understanding these flows is crucial for the migration to Angular and Spring Boot architecture.