# API Design

This document outlines the proposed API design for the new Node.js backend that will replace the Ruby on Rails application. The API follows RESTful principles and is designed to support the Angular frontend.

## Authentication API

### POST /api/auth/register

Create a new user account.

**Request:**
```json
{
  "login": "username",
  "email": "user@example.com",
  "password": "securepassword",
  "name": "User Name"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "login": "username",
  "email": "user@example.com",
  "name": "User Name",
  "activationRequired": true,
  "message": "Please check your email for the activation link."
}
```

### POST /api/auth/activate

Activate a user account.

**Request:**
```json
{
  "activationCode": "activation-token"
}
```

**Response (200 OK):**
```json
{
  "activated": true,
  "message": "Your account has been activated successfully."
}
```

### POST /api/auth/login

Authenticate a user and get JWT token.

**Request:**
```json
{
  "login": "username",
  "password": "securepassword"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "login": "username",
    "name": "User Name",
    "preferences": {
      "defaultCurrencyId": 1,
      "transactionAmountLimitType": "this_month",
      "includeTransactionsFromSubcategories": true
    }
  }
}
```

### GET /api/auth/me

Get current authenticated user information.

**Response (200 OK):**
```json
{
  "id": 1,
  "login": "username",
  "email": "user@example.com",
  "name": "User Name",
  "preferences": {
    "defaultCurrencyId": 1,
    "transactionAmountLimitType": "this_month",
    "transactionAmountLimitValue": null,
    "includeTransactionsFromSubcategories": true,
    "multiCurrencyBalanceCalculatingAlgorithm": "calculate_with_newest_exchanges",
    "invertSaldoForIncome": true
  }
}
```

### PUT /api/auth/preferences

Update user preferences.

**Request:**
```json
{
  "defaultCurrencyId": 2,
  "transactionAmountLimitType": "transaction_count",
  "transactionAmountLimitValue": 20,
  "includeTransactionsFromSubcategories": false,
  "multiCurrencyBalanceCalculatingAlgorithm": "show_all_currencies"
}
```

**Response (200 OK):**
```json
{
  "preferences": {
    "defaultCurrencyId": 2,
    "transactionAmountLimitType": "transaction_count",
    "transactionAmountLimitValue": 20,
    "includeTransactionsFromSubcategories": false,
    "multiCurrencyBalanceCalculatingAlgorithm": "show_all_currencies",
    "invertSaldoForIncome": true
  },
  "message": "Preferences updated successfully."
}
```

## Categories API

### GET /api/categories

Get all categories for the authenticated user.

**Query parameters:**
- `type`: Filter by category type (ASSET, INCOME, EXPENSE, LOAN, BALANCE)
- `includeRoot`: Include top-level categories (default: true)
- `flat`: Return flat structure vs. hierarchical (default: false)

**Response (200 OK):**
```json
{
  "categories": [
    {
      "id": 1,
      "name": "Assets",
      "categoryType": "ASSET",
      "level": 0,
      "children": [
        {
          "id": 5,
          "name": "Bank Accounts",
          "categoryType": "ASSET",
          "level": 1,
          "children": []
        }
      ]
    },
    {
      "id": 2,
      "name": "Income",
      "categoryType": "INCOME",
      "level": 0,
      "children": []
    }
  ]
}
```

### GET /api/categories/:id

Get a specific category.

**Response (200 OK):**
```json
{
  "id": 5,
  "name": "Bank Accounts",
  "description": "All bank accounts",
  "categoryType": "ASSET",
  "level": 1,
  "parentId": 1,
  "path": [1, 5],
  "hasChildren": false,
  "balance": {
    "USD": 1250.45,
    "EUR": 350.00
  }
}
```

### POST /api/categories

Create a new category.

**Request:**
```json
{
  "name": "Credit Cards",
  "description": "Credit card expenses",
  "categoryType": "EXPENSE",
  "parentId": 3
}
```

**Response (201 Created):**
```json
{
  "id": 12,
  "name": "Credit Cards",
  "description": "Credit card expenses",
  "categoryType": "EXPENSE",
  "level": 1,
  "parentId": 3,
  "path": [3, 12]
}
```

### PUT /api/categories/:id

Update a category.

**Request:**
```json
{
  "name": "Credit Card Expenses",
  "description": "Updated description"
}
```

**Response (200 OK):**
```json
{
  "id": 12,
  "name": "Credit Card Expenses",
  "description": "Updated description",
  "categoryType": "EXPENSE",
  "level": 1,
  "parentId": 3,
  "path": [3, 12]
}
```

### DELETE /api/categories/:id

Delete a category.

**Response (200 OK):**
```json
{
  "message": "Category deleted successfully."
}
```

### GET /api/categories/:id/balance

Get category balance with optional date range.

**Query parameters:**
- `startDate`: Start date (format: YYYY-MM-DD)
- `endDate`: End date (format: YYYY-MM-DD)
- `includeSubcategories`: Include subcategories in calculation (default: true)
- `currencyConversion`: Conversion strategy for multi-currency balances

**Response (200 OK):**
```json
{
  "categoryId": 5,
  "categoryName": "Bank Accounts",
  "balances": {
    "USD": 1250.45,
    "EUR": 350.00
  },
  "convertedBalance": {
    "USD": 1657.45
  },
  "period": {
    "start": "2023-01-01",
    "end": "2023-12-31"
  }
}
```

## Transfers API

### GET /api/transfers

Get transfers with pagination and filtering.

**Query parameters:**
- `page`: Page number (default: 1)
- `limit`: Items per page (default: 20)
- `startDate`: Filter by start date (format: YYYY-MM-DD)
- `endDate`: Filter by end date (format: YYYY-MM-DD)
- `categoryId`: Filter by category
- `search`: Search in description

**Response (200 OK):**
```json
{
  "transfers": [
    {
      "id": 123,
      "description": "Salary payment",
      "day": "2023-05-15",
      "items": [
        {
          "id": 456,
          "categoryId": 2,
          "categoryName": "Income",
          "value": 3000.00,
          "currencyId": 1,
          "currencySymbol": "USD"
        },
        {
          "id": 457,
          "categoryId": 5,
          "categoryName": "Bank Accounts",
          "value": -3000.00,
          "currencyId": 1,
          "currencySymbol": "USD"
        }
      ]
    }
  ],
  "pagination": {
    "total": 150,
    "page": 1,
    "limit": 20,
    "pages": 8
  }
}
```

### GET /api/transfers/:id

Get a specific transfer.

**Response (200 OK):**
```json
{
  "id": 123,
  "description": "Salary payment",
  "day": "2023-05-15",
  "items": [
    {
      "id": 456,
      "categoryId": 2,
      "categoryName": "Income",
      "value": 3000.00,
      "currencyId": 1,
      "currencySymbol": "USD"
    },
    {
      "id": 457,
      "categoryId": 5,
      "categoryName": "Bank Accounts",
      "value": -3000.00,
      "currencyId": 1,
      "currencySymbol": "USD"
    }
  ],
  "conversions": []
}
```

### POST /api/transfers

Create a new transfer.

**Request:**
```json
{
  "description": "Grocery shopping",
  "day": "2023-05-20",
  "items": [
    {
      "categoryId": 8,
      "value": 150.75,
      "currencyId": 1
    },
    {
      "categoryId": 5,
      "value": -150.75,
      "currencyId": 1
    }
  ],
  "conversions": []
}
```

**Response (201 Created):**
```json
{
  "id": 124,
  "description": "Grocery shopping",
  "day": "2023-05-20",
  "items": [
    {
      "id": 458,
      "categoryId": 8,
      "categoryName": "Groceries",
      "value": 150.75,
      "currencyId": 1,
      "currencySymbol": "USD"
    },
    {
      "id": 459,
      "categoryId": 5,
      "categoryName": "Bank Accounts",
      "value": -150.75,
      "currencyId": 1,
      "currencySymbol": "USD"
    }
  ],
  "conversions": []
}
```

### PUT /api/transfers/:id

Update a transfer.

**Request:**
```json
{
  "description": "Updated grocery shopping",
  "day": "2023-05-21",
  "items": [
    {
      "id": 458,
      "categoryId": 8,
      "value": 160.75,
      "currencyId": 1
    },
    {
      "id": 459,
      "categoryId": 5,
      "value": -160.75,
      "currencyId": 1
    }
  ]
}
```

**Response (200 OK):**
```json
{
  "id": 124,
  "description": "Updated grocery shopping",
  "day": "2023-05-21",
  "items": [
    {
      "id": 458,
      "categoryId": 8,
      "value": 160.75,
      "currencyId": 1
    },
    {
      "id": 459,
      "categoryId": 5,
      "value": -160.75,
      "currencyId": 1
    }
  ],
  "conversions": []
}
```

### DELETE /api/transfers/:id

Delete a transfer.

**Response (200 OK):**
```json
{
  "message": "Transfer deleted successfully."
}
```

## Currencies API

### GET /api/currencies

Get all available currencies.

**Response (200 OK):**
```json
{
  "currencies": [
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
      "longName": "Euro",
      "isSystem": true
    }
  ]
}
```

### POST /api/currencies

Create a new currency.

**Request:**
```json
{
  "symbol": "£",
  "longSymbol": "GBP",
  "name": "Pound",
  "longName": "British Pound"
}
```

**Response (201 Created):**
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

### PUT /api/currencies/:id

Update a currency.

**Request:**
```json
{
  "name": "British Pound",
  "longName": "Great British Pound"
}
```

**Response (200 OK):**
```json
{
  "id": 3,
  "symbol": "£",
  "longSymbol": "GBP",
  "name": "British Pound",
  "longName": "Great British Pound",
  "isSystem": false
}
```

### DELETE /api/currencies/:id

Delete a currency.

**Response (200 OK):**
```json
{
  "message": "Currency deleted successfully."
}
```

## Exchange Rates API

### GET /api/exchanges

Get exchange rates with optional filtering.

**Query parameters:**
- `leftCurrencyId`: Filter by left currency ID
- `rightCurrencyId`: Filter by right currency ID
- `date`: Get rates for specific date (format: YYYY-MM-DD)

**Response (200 OK):**
```json
{
  "exchanges": [
    {
      "id": 1,
      "leftCurrencyId": 1,
      "leftCurrencySymbol": "USD",
      "rightCurrencyId": 2,
      "rightCurrencySymbol": "EUR",
      "leftToRight": 0.85,
      "rightToLeft": 1.18,
      "day": "2023-05-20"
    }
  ]
}
```

### POST /api/exchanges

Create a new exchange rate.

**Request:**
```json
{
  "leftCurrencyId": 1,
  "rightCurrencyId": 3,
  "leftToRight": 0.78,
  "rightToLeft": 1.28,
  "day": "2023-05-20"
}
```

**Response (201 Created):**
```json
{
  "id": 2,
  "leftCurrencyId": 1,
  "leftCurrencySymbol": "USD",
  "rightCurrencyId": 3,
  "rightCurrencySymbol": "GBP",
  "leftToRight": 0.78,
  "rightToLeft": 1.28,
  "day": "2023-05-20"
}
```

### PUT /api/exchanges/:id

Update an exchange rate.

**Request:**
```json
{
  "leftToRight": 0.79,
  "rightToLeft": 1.27
}
```

**Response (200 OK):**
```json
{
  "id": 2,
  "leftCurrencyId": 1,
  "leftCurrencySymbol": "USD",
  "rightCurrencyId": 3,
  "rightCurrencySymbol": "GBP",
  "leftToRight": 0.79,
  "rightToLeft": 1.27,
  "day": "2023-05-20"
}
```

### DELETE /api/exchanges/:id

Delete an exchange rate.

**Response (200 OK):**
```json
{
  "message": "Exchange rate deleted successfully."
}
```

## Goals API

### GET /api/goals

Get all goals with optional filtering.

**Query parameters:**
- `status`: Filter by status (active, finished, all)
- `categoryId`: Filter by category ID

**Response (200 OK):**
```json
{
  "goals": [
    {
      "id": 1,
      "description": "Save for vacation",
      "categoryId": 5,
      "categoryName": "Savings",
      "goalType": "value",
      "value": 5000,
      "currencyId": 1,
      "currencySymbol": "USD",
      "goalCompletionCondition": "at_least",
      "periodStart": "2023-01-01",
      "periodEnd": "2023-12-31",
      "periodType": "THIS_YEAR",
      "includeSubcategories": true,
      "isCyclic": false,
      "isFinished": false,
      "actualValue": 2500,
      "progress": 50
    }
  ]
}
```

### GET /api/goals/:id

Get a specific goal.

**Response (200 OK):**
```json
{
  "id": 1,
  "description": "Save for vacation",
  "categoryId": 5,
  "categoryName": "Savings",
  "goalType": "value",
  "value": 5000,
  "currencyId": 1,
  "currencySymbol": "USD",
  "goalCompletionCondition": "at_least",
  "periodStart": "2023-01-01",
  "periodEnd": "2023-12-31",
  "periodType": "THIS_YEAR",
  "includeSubcategories": true,
  "isCyclic": false,
  "isFinished": false,
  "actualValue": 2500,
  "progress": 50
}
```

### POST /api/goals

Create a new goal.

**Request:**
```json
{
  "description": "Reduce dining out",
  "categoryId": 9,
  "goalType": "percent",
  "value": 10,
  "goalCompletionCondition": "at_most",
  "periodType": "THIS_MONTH",
  "includeSubcategories": true,
  "isCyclic": true
}
```

**Response (201 Created):**
```json
{
  "id": 2,
  "description": "Reduce dining out",
  "categoryId": 9,
  "categoryName": "Restaurants",
  "goalType": "percent",
  "value": 10,
  "goalCompletionCondition": "at_most",
  "periodStart": "2023-05-01",
  "periodEnd": "2023-05-31",
  "periodType": "THIS_MONTH",
  "includeSubcategories": true,
  "isCyclic": true,
  "isFinished": false,
  "actualValue": 15,
  "progress": -50
}
```

### PUT /api/goals/:id

Update a goal.

**Request:**
```json
{
  "description": "Reduce dining out expenses",
  "value": 8
}
```

**Response (200 OK):**
```json
{
  "id": 2,
  "description": "Reduce dining out expenses",
  "value": 8,
  "actualValue": 15,
  "progress": -87.5
}
```

### DELETE /api/goals/:id

Delete a goal.

**Response (200 OK):**
```json
{
  "message": "Goal deleted successfully."
}
```

### POST /api/goals/:id/finish

Mark a goal as finished.

**Response (200 OK):**
```json
{
  "id": 2,
  "isFinished": true,
  "message": "Goal marked as finished."
}
```

## Reports API

### GET /api/reports

Get all user reports.

**Response (200 OK):**
```json
{
  "reports": [
    {
      "id": 1,
      "name": "Monthly Expenses",
      "type": "share",
      "reportViewType": "pie",
      "periodType": "THIS_MONTH",
      "periodStart": "2023-05-01",
      "periodEnd": "2023-05-31",
      "relativePeriod": true,
      "depth": 1
    }
  ]
}
```

### GET /api/reports/:id

Get a specific report with data.

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Monthly Expenses",
  "type": "share",
  "reportViewType": "pie",
  "periodType": "THIS_MONTH",
  "periodStart": "2023-05-01",
  "periodEnd": "2023-05-31",
  "relativePeriod": true,
  "depth": 1,
  "categoryId": 3,
  "categoryName": "Expenses",
  "data": [
    {
      "categoryId": 8,
      "categoryName": "Groceries",
      "value": 450.75,
      "percentage": 60
    },
    {
      "categoryId": 9,
      "categoryName": "Restaurants",
      "value": 300.50,
      "percentage": 40
    }
  ]
}
```

### POST /api/reports

Create a new report.

**Request:**
```json
{
  "name": "Annual Income",
  "type": "value",
  "reportViewType": "bar",
  "periodType": "THIS_YEAR",
  "relativePeriod": true,
  "depth": 1,
  "periodDivision": "month",
  "categories": [
    {
      "categoryId": 2,
      "inclusionType": "category_and_subcategories"
    }
  ]
}
```

**Response (201 Created):**
```json
{
  "id": 2,
  "name": "Annual Income",
  "type": "value",
  "reportViewType": "bar",
  "periodType": "THIS_YEAR",
  "periodStart": "2023-01-01",
  "periodEnd": "2023-12-31",
  "relativePeriod": true,
  "depth": 1,
  "periodDivision": "month",
  "categories": [
    {
      "categoryId": 2,
      "categoryName": "Income",
      "inclusionType": "category_and_subcategories"
    }
  ]
}
```

### PUT /api/reports/:id

Update a report.

**Request:**
```json
{
  "name": "Annual Income Analysis",
  "reportViewType": "linear"
}
```

**Response (200 OK):**
```json
{
  "id": 2,
  "name": "Annual Income Analysis",
  "reportViewType": "linear"
}
```

### DELETE /api/reports/:id

Delete a report.

**Response (200 OK):**
```json
{
  "message": "Report deleted successfully."
}
```

### GET /api/reports/:id/data

Get report data only.

**Query parameters:**
- `refresh`: Force data recalculation (default: false)

**Response (200 OK):**
```json
{
  "reportId": 2,
  "reportName": "Annual Income Analysis",
  "dataPoints": [
    {
      "label": "January",
      "value": 3500.00
    },
    {
      "label": "February",
      "value": 3500.00
    },
    {
      "label": "March",
      "value": 4000.00
    }
  ],
  "total": 11000.00,
  "average": 3666.67
}
```