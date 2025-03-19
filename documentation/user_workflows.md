# User Workflows Documentation

This document outlines the key user workflows and interactions within the financial management application. Understanding these workflows is crucial for reengineering the application to an Angular and Node.js stack while preserving the existing functionality.

## Authentication Flows

### User Registration and Activation

```mermaid
sequenceDiagram
    actor User
    participant SignupForm
    participant UsersController
    participant UserModel
    participant Mailer
    participant ActivationPage
    
    User->>SignupForm: Fill in registration form
    SignupForm->>UsersController: Submit registration data
    UsersController->>UserModel: Create new user
    UserModel->>UserModel: Generate activation code
    UserModel->>Mailer: Send activation email
    Mailer-->>User: Deliver activation email
    User->>ActivationPage: Click activation link
    ActivationPage->>UsersController: Submit activation code
    UsersController->>UserModel: Activate account
    UsersController-->>User: Redirect to login page
```

### User Login

```mermaid
sequenceDiagram
    actor User
    participant LoginForm
    participant SessionsController
    participant UserModel
    participant Dashboard
    
    User->>LoginForm: Enter login credentials
    LoginForm->>SessionsController: Submit login attempt
    SessionsController->>UserModel: Authenticate credentials
    alt Authentication successful
        UserModel-->>SessionsController: Authentication success
        SessionsController->>SessionsController: Create session
        SessionsController-->>User: Redirect to dashboard
    else Authentication failed
        UserModel-->>SessionsController: Authentication failure
        SessionsController-->>LoginForm: Display error message
        LoginForm-->>User: Show login failure
    end
```

## Core Financial Workflows

### Managing Categories

```mermaid
flowchart TD
    A[View Categories List] --> B{Create New Category?}
    B -->|Yes| C[Fill Category Form]
    C --> D[Select Parent Category]
    D --> E[Select Category Type]
    E --> F[Add Opening Balance?]
    F -->|Yes| G[Enter Opening Balance]
    F -->|No| H[Save Category]
    G --> H
    H --> A
    
    B -->|No| I{Edit Existing Category?}
    I -->|Yes| J[Modify Category Details]
    J --> H
    
    I -->|No| K{Delete Category?}
    K -->|Yes| L[Confirm Deletion]
    L --> A
    
    K -->|No| M{View Category Details?}
    M -->|Yes| N[See Category Transfers]
    N --> O[View Category Balance]
```

### Creating Transfers (Transactions)

```mermaid
sequenceDiagram
    actor User
    participant TransferForm
    participant TransfersController
    participant TransferModel
    participant TransferItemModel
    participant CategoryModel
    
    User->>TransferForm: Fill transfer details (date, description)
    User->>TransferForm: Add first transfer item (category, amount)
    User->>TransferForm: Add second transfer item (category, amount)
    User->>TransferForm: Optional: Add more transfer items
    User->>TransferForm: Submit transfer
    
    TransferForm->>TransfersController: Submit transfer data
    TransfersController->>TransferModel: Create new transfer
    TransferModel->>TransferItemModel: Create transfer items
    TransferItemModel->>CategoryModel: Update category balances
    
    alt Transfer is valid (sums to zero)
        TransferModel-->>TransfersController: Transfer created
        TransfersController-->>User: Show success message
    else Transfer is invalid
        TransferModel-->>TransfersController: Return validation errors
        TransfersController-->>TransferForm: Display errors
        TransferForm-->>User: Show validation issues
    end
```

### Quick Transfer Workflow

```mermaid
flowchart TD
    A[Open Quick Transfer Form] --> B[Select From Category]
    B --> C[Select To Category]
    C --> D[Enter Amount]
    D --> E[Select Currency]
    E --> F[Enter Description]
    F --> G[Select Date]
    G --> H[Submit Quick Transfer]
    H --> I{Transfer Valid?}
    I -->|Yes| J[Transfer Created]
    I -->|No| K[Display Errors]
    K --> A
```

### Multi-Currency Transfer

```mermaid
sequenceDiagram
    actor User
    participant TransferForm
    participant TransfersController
    participant CurrencyModel
    participant ExchangeModel
    participant TransferModel
    
    User->>TransferForm: Create transfer with different currencies
    User->>TransferForm: Select first item currency A
    User->>TransferForm: Select second item currency B
    User->>TransferForm: Select exchange rate
    User->>TransferForm: Submit transfer
    
    TransferForm->>TransfersController: Submit multi-currency data
    TransfersController->>CurrencyModel: Verify currencies
    TransfersController->>ExchangeModel: Apply exchange rate
    TransfersController->>TransferModel: Create transfer with conversion
    
    alt Conversion is valid
        TransferModel-->>TransfersController: Transfer created
        TransfersController-->>User: Show success message
    else Conversion issues
        TransferModel-->>TransfersController: Return validation errors
        TransfersController-->>TransferForm: Display conversion errors
        TransferForm-->>User: Show validation issues
    end
```

## Reporting Workflows

### Creating and Viewing Reports

```mermaid
flowchart TD
    A[Open Reports List] --> B{Create New Report?}
    B -->|Yes| C[Select Report Type]
    C --> D[Configure Report Parameters]
    D --> E[Select Categories]
    E --> F[Select Time Period]
    F --> G[Choose Visualization Type]
    G --> H[Save Report]
    H --> I[View Generated Report]
    
    B -->|No| J{View Existing Report?}
    J -->|Yes| I
    
    J -->|No| K{Edit Report?}
    K -->|Yes| L[Modify Report Settings]
    L --> H
    
    K -->|No| M{Delete Report?}
    M -->|Yes| N[Confirm Deletion]
    N --> A
```

### Report Types and Visualization Flows

```mermaid
flowchart TD
    A[Select Report Type] --> B{Report Type?}
    
    B -->|Share Report| C[Select Main Category]
    C --> D[Set Depth Level]
    D --> E[Choose max categories]
    
    B -->|Flow Report| F[Select Categories]
    F --> G[Configure Filters]
    
    B -->|Value Report| H[Select Multiple Categories]
    H --> I[Configure Period Division]
    
    E --> J[Choose Visualization]
    G --> J
    I --> J
    
    J -->|Pie Chart| K[Generate Pie Visualization]
    J -->|Linear Graph| L[Generate Line Chart]
    J -->|Bar Chart| M[Generate Bar Chart]
    J -->|Text| N[Generate Text Report]
```

## Goal Management Workflows

### Creating and Tracking Goals

```mermaid
sequenceDiagram
    actor User
    participant GoalForm
    participant GoalsController
    participant GoalModel
    participant CategoryModel
    
    User->>GoalForm: Fill goal details (name, description)
    User->>GoalForm: Select associated category
    User->>GoalForm: Set target amount and currency
    User->>GoalForm: Set target date
    User->>GoalForm: Configure as cyclic goal (optional)
    User->>GoalForm: Submit goal
    
    GoalForm->>GoalsController: Submit goal data
    GoalsController->>GoalModel: Create new goal
    GoalModel->>CategoryModel: Associate with category
    
    GoalsController-->>User: Redirect to goals list
    
    User->>GoalsController: View goal progress
    GoalsController->>GoalModel: Calculate progress
    GoalModel->>CategoryModel: Get category balance
    GoalsController-->>User: Display progress visualization
```

## Currency and Exchange Rate Management

### Managing Currencies

```mermaid
flowchart TD
    A[View Currencies List] --> B{Add New Currency?}
    B -->|Yes| C[Fill Currency Details]
    C --> D[Set Symbol and Name]
    D --> E[Save Currency]
    E --> A
    
    B -->|No| F{Edit Currency?}
    F -->|Yes| G[Modify Currency Details]
    G --> E
    
    F -->|No| H{Delete Currency?}
    H -->|Yes| I{Currency in use?}
    I -->|Yes| J[Show Cannot Delete Message]
    I -->|No| K[Confirm Deletion]
    K --> A
    J --> A
```

### Managing Exchange Rates

```mermaid
sequenceDiagram
    actor User
    participant ExchangesList
    participant ExchangesController
    participant ExchangeModel
    
    User->>ExchangesList: View exchange rates
    User->>ExchangesList: Select currencies pair
    
    alt Add new exchange rate
        User->>ExchangesList: Click Add New Rate
        ExchangesList->>User: Show exchange rate form
        User->>ExchangesController: Submit rate and date
        ExchangesController->>ExchangeModel: Create new exchange rate
        ExchangeModel-->>ExchangesController: Rate created
        ExchangesController-->>User: Update exchange rates list
    else Edit existing rate
        User->>ExchangesList: Select rate to edit
        ExchangesList->>User: Show edit form
        User->>ExchangesController: Submit updated rate
        ExchangesController->>ExchangeModel: Update exchange rate
        ExchangeModel-->>ExchangesController: Rate updated
        ExchangesController-->>User: Update exchange rates list
    end
```

## Import/Export Workflows

### Importing Transactions

```mermaid
flowchart TD
    A[Open Import Page] --> B[Select File Format]
    B --> C[Upload File]
    C --> D[Preview Imported Data]
    D --> E{Data Looks Correct?}
    E -->|Yes| F[Map Categories]
    F --> G[Confirm Import]
    G --> H[View Imported Transactions]
    
    E -->|No| I[Adjust Import Settings]
    I --> C
```

## User Experience and Navigation

### Main Application Navigation

```mermaid
flowchart TD
    A[Login] --> B[Dashboard]
    
    B --> C[Categories Management]
    B --> D[Transfers Management]
    B --> E[Reports]
    B --> F[Goals]
    B --> G[Currencies and Exchange Rates]
    
    C --> B
    D --> B
    E --> B
    F --> B
    G --> B
```

This documentation captures the essential user workflows and interactions within the application, providing a blueprint for recreating these flows in the new Angular and Node.js architecture.