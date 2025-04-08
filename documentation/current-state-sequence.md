# Current State Sequence Diagram

This document outlines the key interactions in the current Ruby on Rails application.

## User Authentication Flow

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant SessionsController
    participant UserModel
    participant Database

    User->>Browser: Navigate to Login
    Browser->>SessionsController: GET /login
    SessionsController->>Browser: Render login form
    User->>Browser: Enter credentials
    Browser->>SessionsController: POST /sessions
    SessionsController->>UserModel: authenticate(email, password)
    UserModel->>Database: find_by(email)
    Database-->>UserModel: user record
    UserModel-->>SessionsController: authentication result
    SessionsController->>Browser: Redirect to dashboard
    Browser->>User: Show dashboard
```

## Transfer Creation Flow

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant TransfersController
    participant TransferModel
    participant CategoryModel
    participant Database

    User->>Browser: Navigate to New Transfer
    Browser->>TransfersController: GET /transfers/new
    TransfersController->>CategoryModel: get_categories()
    CategoryModel->>Database: fetch categories
    Database-->>CategoryModel: categories list
    TransfersController->>Browser: Render transfer form
    User->>Browser: Fill transfer details
    Browser->>TransfersController: POST /transfers
    TransfersController->>TransferModel: create(transfer_params)
    TransferModel->>Database: save transfer
    Database-->>TransferModel: confirmation
    TransferModel-->>TransfersController: transfer object
    TransfersController->>Browser: Redirect to transfer list
    Browser->>User: Show success message
```

## Report Generation Flow

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant ReportsController
    participant ReportModel
    participant TransferModel
    participant Database

    User->>Browser: Request Report
    Browser->>ReportsController: GET /reports
    ReportsController->>ReportModel: generate_report(params)
    ReportModel->>TransferModel: get_transfers(date_range)
    TransferModel->>Database: query transfers
    Database-->>TransferModel: transfer data
    TransferModel-->>ReportModel: transfers
    ReportModel->>ReportModel: process_data()
    ReportModel-->>ReportsController: report data
    ReportsController->>Browser: Render report
    Browser->>User: Display report
```

## Currency Exchange Flow

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant ExchangesController
    participant ExchangeModel
    participant CurrencyModel
    participant Database

    User->>Browser: Create Exchange
    Browser->>ExchangesController: GET /exchanges/new
    ExchangesController->>CurrencyModel: get_currencies()
    CurrencyModel->>Database: fetch currencies
    Database-->>CurrencyModel: currencies list
    ExchangesController->>Browser: Render exchange form
    User->>Browser: Enter exchange details
    Browser->>ExchangesController: POST /exchanges
    ExchangesController->>ExchangeModel: create(exchange_params)
    ExchangeModel->>Database: save exchange
    Database-->>ExchangeModel: confirmation
    ExchangeModel-->>ExchangesController: exchange object
    ExchangesController->>Browser: Redirect to exchanges list
    Browser->>User: Show success message
```

## Goal Management Flow

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant GoalsController
    participant GoalModel
    participant Database

    User->>Browser: Create Goal
    Browser->>GoalsController: GET /goals/new
    GoalsController->>Browser: Render goal form
    User->>Browser: Enter goal details
    Browser->>GoalsController: POST /goals
    GoalsController->>GoalModel: create(goal_params)
    GoalModel->>Database: save goal
    Database-->>GoalModel: confirmation
    GoalModel-->>GoalsController: goal object
    GoalsController->>Browser: Redirect to goals list
    Browser->>User: Show success message
```

These sequence diagrams represent the main interactions in the current Ruby on Rails application. They will serve as a reference for implementing similar functionality in the new Angular and Spring Boot architecture.