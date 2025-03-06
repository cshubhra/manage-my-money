# API Design for Spring Boot Backend

This document outlines the RESTful API design for the Spring Boot backend that will replace the Ruby on Rails application.

## API Conventions

- All endpoints return JSON responses
- HTTP status codes are used appropriately (200, 201, 204, 400, 401, 403, 404, 500)
- Authentication uses JWT tokens
- API versioning through URL path (`/api/v1/...`)
- Consistent error response format
- Pagination for collection endpoints

## Error Response Format

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "timestamp": "2023-07-14T12:34:56.789Z",
  "errors": [
    {
      "field": "description",
      "message": "must not be blank"
    }
  ]
}
```

## Authentication Endpoints

### Login

- **Endpoint**: `POST /api/v1/auth/login`
- **Request Body**:
  ```json
  {
    "username": "user@example.com",
    "password": "password123"
  }
  ```
- **Response**:
  ```json
  {
    "id": 1,
    "username": "user@example.com",
    "name": "John Doe",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresAt": "2023-07-14T14:34:56.789Z"
  }
  ```

### Register

- **Endpoint**: `POST /api/v1/auth/register`
- **Request Body**:
  ```json
  {
    "username": "user@example.com",
    "name": "John Doe",
    "password": "password123",
    "defaultCurrency": "USD"
  }
  ```
- **Response**:
  ```json
  {
    "id": 1,
    "username": "user@example.com",
    "name": "John Doe",
    "message": "User registered successfully. Please check email for activation."
  }
  ```

### User Activation

- **Endpoint**: `GET /api/v1/auth/activate/{activationCode}`
- **Response**:
  ```json
  {
    "message": "Account activated successfully"
  }
  ```

## User Management

### Get Current User

- **Endpoint**: `GET /api/v1/users/me`
- **Response**:
  ```json
  {
    "id": 1,
    "username": "user@example.com",
    "name": "John Doe",
    "defaultCurrency": {
      "id": 1,
      "symbol": "$",
      "longSymbol": "USD",
      "name": "Dollar",
      "longName": "US Dollar"
    },
    "preferences": {
      "includeTransactionsFromSubcategories": true,
      "multiCurrencyBalanceCalculatingAlgorithm": "SHOW_ALL_CURRENCIES",
      "transactionAmountLimitType": "THIS_MONTH",
      "transactionAmountLimitValue": null,
      "invertSaldoForIncome": true
    }
  }
  ```

### Update User

- **Endpoint**: `PUT /api/v1/users/me`
- **Request Body**:
  ```json
  {
    "name": "John Smith",
    "defaultCurrencyId": 2,
    "preferences": {
      "includeTransactionsFromSubcategories": false,
      "multiCurrencyBalanceCalculatingAlgorithm": "CALCULATE_WITH_NEWEST_EXCHANGES",
      "transactionAmountLimitType": "WEEK_COUNT",
      "transactionAmountLimitValue": 2,
      "invertSaldoForIncome": true
    }
  }
  ```
- **Response**: `204 No Content`

### Change Password

- **Endpoint**: `PUT /api/v1/users/me/password`
- **Request Body**:
  ```json
  {
    "currentPassword": "oldPassword123",
    "newPassword": "newPassword456"
  }
  ```
- **Response**: `204 No Content`

## Categories

### Get Categories

- **Endpoint**: `GET /api/v1/categories`
- **Query Parameters**:
  - `type`: Filter by category type (ASSET, INCOME, EXPENSE, LOAN, BALANCE)
  - `includeBalance`: Boolean flag to include calculated balance (default: false)
  - `parentId`: Filter by parent category
- **Response**:
  ```json
  {
    "content": [
      {
        "id": 1,
        "name": "Assets",
        "description": "All Assets",
        "categoryType": "ASSET",
        "parentId": null,
        "hasChildren": true,
        "level": 0,
        "balance": {
          "USD": 1000.00,
          "EUR": 800.00
        }
      },
      {
        "id": 2,
        "name": "Cash",
        "description": "Cash on hand",
        "categoryType": "ASSET",
        "parentId": 1,
        "hasChildren": false,
        "level": 1,
        "balance": {
          "USD": 100.00
        }
      }
    ],
    "totalElements": 2,
    "totalPages": 1,
    "size": 20,
    "number": 0
  }
  ```

### Get Category

- **Endpoint**: `GET /api/v1/categories/{id}`
- **Response**:
  ```json
  {
    "id": 1,
    "name": "Assets",
    "description": "All Assets",
    "categoryType": "ASSET",
    "parentId": null,
    "path": "Assets",
    "level": 0,
    "children": [
      {
        "id": 2,
        "name": "Cash",
        "description": "Cash on hand",
        "categoryType": "ASSET",
        "parentId": 1,
        "path": "Assets > Cash",
        "level": 1,
        "children": []
      }
    ]
  }
  ```

### Create Category

- **Endpoint**: `POST /api/v1/categories`
- **Request Body**:
  ```json
  {
    "name": "Groceries",
    "description": "Food and household items",
    "categoryType": "EXPENSE",
    "parentId": 5,
    "systemCategoryId": 23,
    "openingBalance": {
      "amount": 100.00,
      "currencyId": 1
    }
  }
  ```
- **Response**:
  ```json
  {
    "id": 10,
    "name": "Groceries",
    "description": "Food and household items",
    "categoryType": "EXPENSE",
    "parentId": 5,
    "path": "Expenses > Groceries",
    "level": 1,
    "children": []
  }
  ```

### Update Category

- **Endpoint**: `PUT /api/v1/categories/{id}`
- **Request Body**:
  ```json
  {
    "name": "Food & Groceries",
    "description": "Updated description",
    "parentId": 5
  }
  ```
- **Response**: `204 No Content`

### Delete Category

- **Endpoint**: `DELETE /api/v1/categories/{id}`
- **Response**: `204 No Content`

### Get Category Balance

- **Endpoint**: `GET /api/v1/categories/{id}/balance`
- **Query Parameters**:
  - `startDate`: Filter by start date (ISO format)
  - `endDate`: Filter by end date (ISO format)
  - `includeSubcategories`: Boolean flag (default: false)
  - `algorithm`: Balance algorithm (default: user preference)
- **Response**:
  ```json
  {
    "balance": {
      "USD": -250.50,
      "EUR": -120.00
    },
    "balanceInDefaultCurrency": -400.00,
    "defaultCurrency": "USD"
  }
  ```

## Transfers

### Get Transfers

- **Endpoint**: `GET /api/v1/transfers`
- **Query Parameters**:
  - `page`: Page number (default: 0)
  - `size`: Page size (default: 20)
  - `startDate`: Filter by start date (ISO format)
  - `endDate`: Filter by end date (ISO format)
  - `categoryId`: Filter by category
  - `sort`: Sort field (default: day,desc)
- **Response**:
  ```json
  {
    "content": [
      {
        "id": 1,
        "description": "Grocery shopping",
        "day": "2023-07-10",
        "items": [
          {
            "id": 1,
            "description": "Purchase",
            "value": -50.00,
            "categoryId": 10,
            "categoryName": "Groceries",
            "currencyId": 1,
            "currencySymbol": "$"
          },
          {
            "id": 2,
            "description": "Payment",
            "value": 50.00,
            "categoryId": 3,
            "categoryName": "Bank Account",
            "currencyId": 1,
            "currencySymbol": "$"
          }
        ]
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "size": 20,
    "number": 0
  }
  ```

### Get Transfer

- **Endpoint**: `GET /api/v1/transfers/{id}`
- **Response**:
  ```json
  {
    "id": 1,
    "description": "Grocery shopping",
    "day": "2023-07-10",
    "items": [
      {
        "id": 1,
        "description": "Purchase",
        "value": -50.00,
        "categoryId": 10,
        "categoryName": "Groceries",
        "currencyId": 1,
        "currencySymbol": "$"
      },
      {
        "id": 2,
        "description": "Payment",
        "value": 50.00,
        "categoryId": 3,
        "categoryName": "Bank Account",
        "currencyId": 1,
        "currencySymbol": "$"
      }
    ],
    "conversions": []
  }
  ```

### Create Transfer

- **Endpoint**: `POST /api/v1/transfers`
- **Request Body**:
  ```json
  {
    "description": "Grocery shopping",
    "day": "2023-07-10",
    "items": [
      {
        "description": "Purchase",
        "transferItemType": "OUTCOME",
        "value": 50.00,
        "categoryId": 10,
        "currencyId": 1
      },
      {
        "description": "Payment",
        "transferItemType": "INCOME",
        "value": 50.00,
        "categoryId": 3,
        "currencyId": 1
      }
    ],
    "conversions": []
  }
  ```
- **Response**:
  ```json
  {
    "id": 1,
    "description": "Grocery shopping",
    "day": "2023-07-10",
    "items": [
      {
        "id": 1,
        "description": "Purchase",
        "value": -50.00,
        "categoryId": 10,
        "categoryName": "Groceries",
        "currencyId": 1,
        "currencySymbol": "$"
      },
      {
        "id": 2,
        "description": "Payment",
        "value": 50.00,
        "categoryId": 3,
        "categoryName": "Bank Account",
        "currencyId": 1,
        "currencySymbol": "$"
      }
    ],
    "conversions": []
  }
  ```

### Update Transfer

- **Endpoint**: `PUT /api/v1/transfers/{id}`
- **Request Body**: Same as create
- **Response**: `204 No Content`

### Delete Transfer

- **Endpoint**: `DELETE /api/v1/transfers/{id}`
- **Response**: `204 No Content`

## Currencies

### Get Currencies

- **Endpoint**: `GET /api/v1/currencies`
- **Response**:
  ```json
  [
    {
      "id": 1,
      "symbol": "$",
      "longSymbol": "USD",
      "name": "Dollar",
      "longName": "US Dollar",
      "isSystem": true
    },
    {
      "id": 2,
      "symbol": "€",
      "longSymbol": "EUR",
      "name": "Euro",
      "longName": "European Euro",
      "isSystem": true
    }
  ]
  ```

### Create Currency

- **Endpoint**: `POST /api/v1/currencies`
- **Request Body**:
  ```json
  {
    "symbol": "£",
    "longSymbol": "GBP",
    "name": "Pound",
    "longName": "British Pound"
  }
  ```
- **Response**:
  ```json
  {
    "id": 3,
    "symbol": "£",
    "longSymbol": "GBP",
    "name": "Pound",
    "longName": "British Pound",
    "isSystem": false
  }
  ```

### Update Currency

- **Endpoint**: `PUT /api/v1/currencies/{id}`
- **Request Body**: Same as create
- **Response**: `204 No Content`

### Delete Currency

- **Endpoint**: `DELETE /api/v1/currencies/{id}`
- **Response**: `204 No Content`

## Exchanges

### Get Exchanges

- **Endpoint**: `GET /api/v1/exchanges`
- **Query Parameters**:
  - `leftCurrencyId`: Filter by left currency
  - `rightCurrencyId`: Filter by right currency
  - `date`: Filter by date (ISO format)
- **Response**:
  ```json
  [
    {
      "id": 1,
      "day": "2023-07-10",
      "leftCurrency": {
        "id": 1,
        "symbol": "$",
        "longSymbol": "USD"
      },
      "rightCurrency": {
        "id": 2,
        "symbol": "€",
        "longSymbol": "EUR" 
      },
      "leftToRight": 0.9200,
      "rightToLeft": 1.0870
    }
  ]
  ```

### Create Exchange

- **Endpoint**: `POST /api/v1/exchanges`
- **Request Body**:
  ```json
  {
    "day": "2023-07-10",
    "leftCurrencyId": 1,
    "rightCurrencyId": 2,
    "leftToRight": 0.9200,
    "rightToLeft": 1.0870
  }
  ```
- **Response**: Same as in Get Exchanges

### Update Exchange

- **Endpoint**: `PUT /api/v1/exchanges/{id}`
- **Request Body**: Same as create
- **Response**: `204 No Content`

### Delete Exchange

- **Endpoint**: `DELETE /api/v1/exchanges/{id}`
- **Response**: `204 No Content`

## Reports

### Get Reports

- **Endpoint**: `GET /api/v1/reports`
- **Response**:
  ```json
  [
    {
      "id": 1,
      "name": "Monthly Expenses",
      "type": "FLOW_REPORT",
      "periodType": "THIS_MONTH",
      "reportViewType": "TEXT",
      "temporary": false
    },
    {
      "id": 2,
      "name": "Spending By Category",
      "type": "SHARE_REPORT",
      "periodType": "LAST_MONTH",
      "reportViewType": "PIE",
      "temporary": false
    }
  ]
  ```

### Get Report

- **Endpoint**: `GET /api/v1/reports/{id}`
- **Response** (depends on report type):
  ```json
  {
    "id": 1,
    "name": "Monthly Expenses",
    "type": "FLOW_REPORT",
    "periodType": "THIS_MONTH",
    "periodStart": "2023-07-01",
    "periodEnd": "2023-07-31",
    "reportViewType": "TEXT",
    "temporary": false,
    "categories": [
      {
        "id": 10,
        "name": "Groceries"
      },
      {
        "id": 11,
        "name": "Utilities"
      }
    ],
    "data": {
      "in": [
        {
          "category": {
            "id": 3,
            "name": "Salary"
          },
          "currency": {
            "id": 1,
            "symbol": "$",
            "longSymbol": "USD"
          },
          "value": 3000.00
        }
      ],
      "out": [
        {
          "category": {
            "id": 10,
            "name": "Groceries"
          },
          "currency": {
            "id": 1,
            "symbol": "$",
            "longSymbol": "USD" 
          },
          "value": 500.00
        },
        {
          "category": {
            "id": 11,
            "name": "Utilities"
          },
          "currency": {
            "id": 1,
            "symbol": "$",
            "longSymbol": "USD" 
          },
          "value": 200.00
        }
      ],
      "summary": {
        "in": {
          "USD": 3000.00
        },
        "out": {
          "USD": 700.00
        },
        "balance": {
          "USD": 2300.00
        }
      }
    }
  }
  ```

### Create Report

- **Endpoint**: `POST /api/v1/reports`
- **Request Body** (depends on report type):
  ```json
  {
    "name": "Monthly Expenses",
    "type": "FLOW_REPORT",
    "periodType": "THIS_MONTH",
    "reportViewType": "TEXT",
    "temporary": false,
    "categoryOptions": [
      {
        "categoryId": 10,
        "inclusionType": "CATEGORY_AND_SUBCATEGORIES"
      },
      {
        "categoryId": 11,
        "inclusionType": "CATEGORY_ONLY"
      }
    ]
  }
  ```
- **Response**: Same as in Get Report

### Update Report

- **Endpoint**: `PUT /api/v1/reports/{id}`
- **Request Body**: Same as create
- **Response**: `204 No Content`

### Delete Report

- **Endpoint**: `DELETE /api/v1/reports/{id}`
- **Response**: `204 No Content`

## Goals

### Get Goals

- **Endpoint**: `GET /api/v1/goals`
- **Query Parameters**:
  - `status`: Filter by status (ACTIVE, FINISHED, ALL)
- **Response**:
  ```json
  [
    {
      "id": 1,
      "description": "Save for vacation",
      "includeSubcategories": true,
      "periodType": "THIS_YEAR",
      "periodStart": "2023-01-01",
      "periodEnd": "2023-12-31",
      "goalType": "VALUE",
      "goalCompletionCondition": "AT_LEAST",
      "value": 5000.00,
      "currency": {
        "id": 1,
        "symbol": "$",
        "longSymbol": "USD"
      },
      "category": {
        "id": 5,
        "name": "Savings",
        "categoryType": "ASSET"
      },
      "isCyclic": false,
      "isFinished": false,
      "actualValue": 3000.00,
      "progress": 60.0
    }
  ]
  ```

### Create Goal

- **Endpoint**: `POST /api/v1/goals`
- **Request Body**:
  ```json
  {
    "description": "Save for vacation",
    "includeSubcategories": true,
    "periodType": "THIS_YEAR",
    "goalType": "VALUE",
    "goalCompletionCondition": "AT_LEAST",
    "value": 5000.00,
    "currencyId": 1,
    "categoryId": 5,
    "isCyclic": false
  }
  ```
- **Response**: Same as in Get Goals single item

### Update Goal

- **Endpoint**: `PUT /api/v1/goals/{id}`
- **Request Body**: Same as create
- **Response**: `204 No Content`

### Delete Goal

- **Endpoint**: `DELETE /api/v1/goals/{id}`
- **Response**: `204 No Content`

### Complete Goal

- **Endpoint**: `PUT /api/v1/goals/{id}/complete`
- **Response**: `204 No Content`

## System Categories

### Get System Categories

- **Endpoint**: `GET /api/v1/system-categories`
- **Query Parameters**:
  - `type`: Filter by category type
- **Response**:
  ```json
  [
    {
      "id": 1,
      "name": "Housing",
      "description": "Housing expenses",
      "categoryType": "EXPENSE",
      "parentId": null,
      "level": 0,
      "children": [
        {
          "id": 2,
          "name": "Rent",
          "description": "Monthly rent",
          "categoryType": "EXPENSE",
          "parentId": 1,
          "level": 1,
          "children": []
        },
        {
          "id": 3,
          "name": "Utilities",
          "description": "Utility bills",
          "categoryType": "EXPENSE",
          "parentId": 1,
          "level": 1,
          "children": []
        }
      ]
    }
  ]
  ```