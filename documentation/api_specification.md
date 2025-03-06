# API Specification

This document outlines the REST API endpoints for the Spring Boot backend that will support the Angular frontend.

## Authentication

### POST /api/auth/login
Authenticates a user and returns a JWT token.

**Request:**
```json
{
  "username": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "user@example.com",
    "name": "John Doe",
    "defaultCurrencyId": 1
  }
}
```

### POST /api/auth/register
Registers a new user.

**Request:**
```json
{
  "username": "user@example.com",
  "password": "password123",
  "name": "John Doe",
  "email": "user@example.com"
}
```

**Response:**
```json
{
  "message": "User registered successfully. Please check your email for activation instructions."
}
```

### GET /api/auth/activate/{activationCode}
Activates a user account.

**Response:**
```json
{
  "message": "Account activated successfully. You can now log in."
}
```

## Users

### GET /api/users/current
Gets the current user's profile.

**Response:**
```json
{
  "id": 1,
  "username": "user@example.com",
  "name": "John Doe",
  "email": "user@example.com",
  "transactionAmountLimitType": "THIS_MONTH",
  "transactionAmountLimitValue": 10,
  "includeTransactionsFromSubcategories": true,
  "multiCurrencyBalanceCalculatingAlgorithm": "SHOW_ALL_CURRENCIES",
  "defaultCurrencyId": 1,
  "invertSaldoForIncome": true
}
```

### PUT /api/users/current
Updates the current user's profile.

**Request:**
```json
{
  "name": "John Smith",
  "transactionAmountLimitType": "WEEK_COUNT",
  "transactionAmountLimitValue": 4,
  "includeTransactionsFromSubcategories": false,
  "multiCurrencyBalanceCalculatingAlgorithm": "CALCULATE_WITH_NEWEST_EXCHANGES",
  "defaultCurrencyId": 2,
  "invertSaldoForIncome": false
}
```

**Response:**
```json
{
  "id": 1,
  "username": "user@example.com",
  "name": "John Smith",
  "email": "user@example.com",
  "transactionAmountLimitType": "WEEK_COUNT",
  "transactionAmountLimitValue": 4,
  "includeTransactionsFromSubcategories": false,
  "multiCurrencyBalanceCalculatingAlgorithm": "CALCULATE_WITH_NEWEST_EXCHANGES",
  "defaultCurrencyId": 2,
  "invertSaldoForIncome": false
}
```

### DELETE /api/users/current
Deletes the current user's account.

**Request:**
```json
{
  "password": "password123"
}
```

**Response:**
```json
{
  "message": "Account deleted successfully"
}
```

## Categories

### GET /api/categories
Gets all categories for the current user.

**Response:**
```json
[
  {
    "id": 1,
    "name": "Assets",
    "categoryType": "ASSET",
    "parentId": null,
    "level": 0,
    "children": [
      {
        "id": 5,
        "name": "Bank Accounts",
        "categoryType": "ASSET",
        "parentId": 1,
        "level": 1,
        "children": []
      }
    ]
  },
  {
    "id": 2,
    "name": "Income",
    "categoryType": "INCOME",
    "parentId": null,
    "level": 0,
    "children": []
  }
]
```

### GET /api/categories/{id}
Gets a specific category with its transfers.

**Response:**
```json
{
  "id": 5,
  "name": "Bank Accounts",
  "categoryType": "ASSET",
  "parentId": 1,
  "level": 1,
  "children": [],
  "transfers": [
    {
      "id": 101,
      "description": "Initial deposit",
      "day": "2023-06-15",
      "items": [
        {
          "id": 201,
          "description": "Initial deposit",
          "value": 1000.00,
          "categoryId": 5,
          "currencyId": 1
        }
      ]
    }
  ]
}
```

### POST /api/categories
Creates a new category.

**Request:**
```json
{
  "name": "Savings",
  "categoryType": "ASSET",
  "parentId": 1,
  "description": "Long-term savings"
}
```

**Response:**
```json
{
  "id": 10,
  "name": "Savings",
  "categoryType": "ASSET",
  "parentId": 1,
  "level": 1,
  "children": []
}
```

### PUT /api/categories/{id}
Updates a category.

**Request:**
```json
{
  "name": "Savings Account",
  "description": "Long-term savings account"
}
```

**Response:**
```json
{
  "id": 10,
  "name": "Savings Account",
  "categoryType": "ASSET",
  "parentId": 1,
  "level": 1,
  "description": "Long-term savings account",
  "children": []
}
```

### DELETE /api/categories/{id}
Deletes a category.

**Response:**
```json
{
  "message": "Category deleted successfully"
}
```

## Transfers

### GET /api/transfers
Gets transfers based on current user's settings.

**Response:**
```json
[
  {
    "id": 101,
    "description": "Salary payment",
    "day": "2023-06-15",
    "items": [
      {
        "id": 201,
        "description": "Monthly salary",
        "value": 5000.00,
        "categoryId": 2,
        "currencyId": 1
      },
      {
        "id": 202,
        "description": "Salary to bank account",
        "value": -5000.00,
        "categoryId": 5,
        "currencyId": 1
      }
    ]
  }
]
```

### GET /api/transfers/search
Searches for transfers within a date range.

**Request Query Parameters:**
- startDate: YYYY-MM-DD
- endDate: YYYY-MM-DD
- categoryId (optional): ID of category to filter by

**Response:**
```json
[
  {
    "id": 103,
    "description": "Grocery shopping",
    "day": "2023-06-18",
    "items": [
      {
        "id": 205,
        "description": "Weekly groceries",
        "value": -125.50,
        "categoryId": 8,
        "currencyId": 1
      },
      {
        "id": 206,
        "description": "Payment from bank account",
        "value": 125.50,
        "categoryId": 5,
        "currencyId": 1
      }
    ]
  }
]
```

### POST /api/transfers
Creates a new transfer.

**Request:**
```json
{
  "description": "Restaurant dinner",
  "day": "2023-06-20",
  "items": [
    {
      "description": "Dinner payment",
      "value": -85.00,
      "categoryId": 9,
      "currencyId": 1
    },
    {
      "description": "Payment from card",
      "value": 85.00,
      "categoryId": 5,
      "currencyId": 1
    }
  ]
}
```

**Response:**
```json
{
  "id": 105,
  "description": "Restaurant dinner",
  "day": "2023-06-20",
  "items": [
    {
      "id": 210,
      "description": "Dinner payment",
      "value": -85.00,
      "categoryId": 9,
      "currencyId": 1
    },
    {
      "id": 211,
      "description": "Payment from card",
      "value": 85.00,
      "categoryId": 5,
      "currencyId": 1
    }
  ]
}
```

### PUT /api/transfers/{id}
Updates a transfer.

**Request:**
```json
{
  "description": "Restaurant dinner with friends",
  "day": "2023-06-20",
  "items": [
    {
      "id": 210,
      "description": "Dinner payment",
      "value": -95.00,
      "categoryId": 9,
      "currencyId": 1
    },
    {
      "id": 211,
      "description": "Payment from card",
      "value": 95.00,
      "categoryId": 5,
      "currencyId": 1
    }
  ]
}
```

**Response:**
```json
{
  "id": 105,
  "description": "Restaurant dinner with friends",
  "day": "2023-06-20",
  "items": [
    {
      "id": 210,
      "description": "Dinner payment",
      "value": -95.00,
      "categoryId": 9,
      "currencyId": 1
    },
    {
      "id": 211,
      "description": "Payment from card",
      "value": 95.00,
      "categoryId": 5,
      "currencyId": 1
    }
  ]
}
```

### DELETE /api/transfers/{id}
Deletes a transfer.

**Response:**
```json
{
  "message": "Transfer deleted successfully"
}
```

## Currencies

### GET /api/currencies
Gets all currencies accessible to the user.

**Response:**
```json
[
  {
    "id": 1,
    "symbol": "$",
    "longSymbol": "USD",
    "name": "Dollar",
    "longName": "US Dollar"
  },
  {
    "id": 2,
    "symbol": "€",
    "longSymbol": "EUR",
    "name": "Euro",
    "longName": "Euro"
  }
]
```

### POST /api/currencies
Creates a new currency.

**Request:**
```json
{
  "symbol": "£",
  "longSymbol": "GBP",
  "name": "Pound",
  "longName": "British Pound"
}
```

**Response:**
```json
{
  "id": 3,
  "symbol": "£",
  "longSymbol": "GBP",
  "name": "Pound",
  "longName": "British Pound"
}
```

## Exchanges

### GET /api/exchanges
Gets all exchanges for the user.

**Response:**
```json
[
  {
    "id": 1,
    "leftCurrencyId": 1,
    "rightCurrencyId": 2,
    "rate": 0.85,
    "day": "2023-06-15"
  }
]
```

### GET /api/exchanges/{leftCurrencyId}/{rightCurrencyId}
Gets exchanges between two currencies.

**Response:**
```json
[
  {
    "id": 1,
    "leftCurrencyId": 1,
    "rightCurrencyId": 2,
    "rate": 0.85,
    "day": "2023-06-15"
  },
  {
    "id": 2,
    "leftCurrencyId": 1,
    "rightCurrencyId": 2,
    "rate": 0.84,
    "day": "2023-06-10"
  }
]
```

### POST /api/exchanges
Creates a new exchange rate.

**Request:**
```json
{
  "leftCurrencyId": 1,
  "rightCurrencyId": 3,
  "rate": 0.78,
  "day": "2023-06-20"
}
```

**Response:**
```json
{
  "id": 3,
  "leftCurrencyId": 1,
  "rightCurrencyId": 3,
  "rate": 0.78,
  "day": "2023-06-20"
}
```