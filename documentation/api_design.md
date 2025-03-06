# API Design

This document outlines the RESTful API design for the Spring Boot backend of our financial management application.

## Authentication Endpoints

### POST /api/auth/login
- **Description**: Authenticate user and retrieve JWT token
- **Request Body**:
  ```json
  {
    "login": "username",
    "password": "password"
  }
  ```
- **Response**:
  ```json
  {
    "token": "jwt-token-string",
    "user": {
      "id": 1,
      "login": "username",
      "name": "User Name",
      "email": "user@example.com",
      "preferences": { ... }
    }
  }
  ```

### POST /api/auth/signup
- **Description**: Register a new user
- **Request Body**:
  ```json
  {
    "login": "newuser",
    "name": "New User",
    "email": "newuser@example.com",
    "password": "password"
  }
  ```
- **Response**:
  ```json
  {
    "id": 1,
    "login": "newuser",
    "name": "New User",
    "email": "newuser@example.com",
    "message": "Verification email sent."
  }
  ```

### POST /api/auth/activate
- **Description**: Activate a user account
- **Request Parameters**:
  - `code`: Activation code from email
- **Response**:
  ```json
  {
    "success": true,
    "message": "Account activated successfully"
  }
  ```

## Category Endpoints

### GET /api/categories
- **Description**: Get all categories for the current user
- **Query Parameters**:
  - `type`: Optional filter by category type (INCOME, EXPENSE, ASSET, LOAN, BALANCE)
- **Response**:
  ```json
  [
    {
      "id": 1,
      "name": "Income",
      "description": "Income category",
      "categoryType": "INCOME",
      "parent": null,
      "children": [
        {
          "id": 2,
          "name": "Salary",
          "description": "Monthly salary",
          "categoryType": "INCOME",
          "parent": 1,
          "children": []
        }
      ]
    }
  ]
  ```

### GET /api/categories/{id}
- **Description**: Get a specific category
- **Response**:
  ```json
  {
    "id": 1,
    "name": "Income",
    "description": "Income category",
    "categoryType": "INCOME",
    "parent": null,
    "path": "Income",
    "transferItems": [ ... ]
  }
  ```

### POST /api/categories
- **Description**: Create a new category
- **Request Body**:
  ```json
  {
    "name": "New Category",
    "description": "Description",
    "categoryType": "EXPENSE",
    "parentId": 5,
    "openingBalance": 100.00,
    "openingBalanceCurrencyId": 1
  }
  ```
- **Response**: Created category object

### PUT /api/categories/{id}
- **Description**: Update a category
- **Request Body**:
  ```json
  {
    "name": "Updated Category",
    "description": "New description",
    "parentId": 6
  }
  ```
- **Response**: Updated category object

### DELETE /api/categories/{id}
- **Description**: Delete a category
- **Response**: Success message

## Transfer Endpoints

### GET /api/transfers
- **Description**: Get user's transfers
- **Query Parameters**:
  - `limit`: Max number of transfers to return
  - `offset`: Pagination offset
  - `startDate`: Filter by start date
  - `endDate`: Filter by end date
  - `categoryId`: Filter by category
- **Response**:
  ```json
  {
    "content": [
      {
        "id": 1,
        "description": "Grocery shopping",
        "day": "2023-04-15",
        "transferItems": [
          {
            "id": 1,
            "description": "Grocery expense",
            "value": -50.00,
            "categoryId": 5,
            "currencyId": 1,
            "categoryName": "Groceries",
            "currencySymbol": "USD"
          },
          {
            "id": 2,
            "description": "From checking account",
            "value": 50.00,
            "categoryId": 3,
            "currencyId": 1,
            "categoryName": "Checking Account",
            "currencySymbol": "USD"
          }
        ]
      }
    ],
    "totalElements": 120,
    "totalPages": 12,
    "number": 0,
    "size": 10
  }
  ```

### GET /api/transfers/{id}
- **Description**: Get a specific transfer
- **Response**: Detailed transfer object

### POST /api/transfers
- **Description**: Create a new transfer
- **Request Body**:
  ```json
  {
    "description": "New transfer",
    "day": "2023-05-01",
    "transferItems": [
      {
        "description": "Expense item",
        "value": -100.00,
        "categoryId": 5,
        "currencyId": 1
      },
      {
        "description": "From account",
        "value": 100.00,
        "categoryId": 3,
        "currencyId": 1
      }
    ]
  }
  ```
- **Response**: Created transfer object

### PUT /api/transfers/{id}
- **Description**: Update a transfer
- **Request Body**: Updated transfer object
- **Response**: Updated transfer object

### DELETE /api/transfers/{id}
- **Description**: Delete a transfer
- **Response**: Success message

## Currency Endpoints

### GET /api/currencies
- **Description**: Get all available currencies for the user
- **Response**:
  ```json
  [
    {
      "id": 1,
      "symbol": "$",
      "longSymbol": "USD",
      "name": "Dollar",
      "longName": "US Dollar"
    }
  ]
  ```

### POST /api/currencies
- **Description**: Create a custom currency
- **Request Body**:
  ```json
  {
    "symbol": "£",
    "longSymbol": "GBP",
    "name": "Pound",
    "longName": "British Pound"
  }
  ```
- **Response**: Created currency object

### PUT /api/currencies/{id}
- **Description**: Update a currency
- **Request Body**: Updated currency information
- **Response**: Updated currency object

### DELETE /api/currencies/{id}
- **Description**: Delete a custom currency
- **Response**: Success message

## Exchange Endpoints

### GET /api/exchanges
- **Description**: Get all exchanges
- **Query Parameters**:
  - `leftCurrencyId`: Filter by left currency
  - `rightCurrencyId`: Filter by right currency
  - `date`: Get exchange rates for specific date
- **Response**:
  ```json
  [
    {
      "id": 1,
      "leftCurrencyId": 1,
      "rightCurrencyId": 2,
      "leftToRight": 0.9182,
      "rightToLeft": 1.0891,
      "day": "2023-05-01",
      "leftCurrencySymbol": "USD",
      "rightCurrencySymbol": "EUR"
    }
  ]
  ```

### POST /api/exchanges
- **Description**: Create a new exchange rate
- **Request Body**:
  ```json
  {
    "leftCurrencyId": 1,
    "rightCurrencyId": 2,
    "leftToRight": 0.9182,
    "rightToLeft": 1.0891,
    "day": "2023-05-01"
  }
  ```
- **Response**: Created exchange object

## Report Endpoints

### GET /api/reports
- **Description**: Get user's saved reports
- **Response**: List of saved reports

### POST /api/reports/generate
- **Description**: Generate a new report
- **Request Body**:
  ```json
  {
    "type": "FLOW", // or "SHARE", "VALUE"
    "name": "Monthly Flow Report",
    "startDate": "2023-01-01",
    "endDate": "2023-01-31",
    "categoryIds": [1, 2, 3],
    "includeSubcategories": true
  }
  ```
- **Response**: Generated report data

### POST /api/reports/save
- **Description**: Save a report configuration
- **Request Body**: Report configuration
- **Response**: Saved report object

## User Settings Endpoints

### GET /api/users/settings
- **Description**: Get current user settings
- **Response**: User settings object

### PUT /api/users/settings
- **Description**: Update user settings
- **Request Body**:
  ```json
  {
    "defaultCurrencyId": 1,
    "includeTransactionsFromSubcategories": true,
    "transactionAmountLimitType": "TRANSACTION_COUNT",
    "transactionAmountLimitValue": 50,
    "multiCurrencyBalanceCalculatingAlgorithm": "SHOW_ALL_CURRENCIES",
    "invertSaldoForIncome": true
  }
  ```
- **Response**: Updated user settings