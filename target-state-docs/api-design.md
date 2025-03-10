# API Design Document

This document outlines the API design for the Node.js backend that will serve the Angular frontend application. The API follows RESTful principles and uses JSON for data exchange.

## API Overview

- Base URL: `/api/v1`
- Authentication: JWT (JSON Web Token) based
- Response format: JSON
- Error handling: HTTP status codes with consistent error response structure
- Versioning: API version in the URL path

## Authentication and Authorization

### Endpoints

#### POST /api/v1/auth/login
Authenticate a user and receive a JWT token.

**Request:**
```json
{
  "login": "username",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600,
  "userId": 123,
  "name": "User Name"
}
```

#### POST /api/v1/auth/register
Register a new user.

**Request:**
```json
{
  "login": "newuser",
  "email": "user@example.com",
  "password": "securePassword123",
  "name": "New User"
}
```

**Response:**
```json
{
  "id": 124,
  "login": "newuser",
  "email": "user@example.com",
  "name": "New User",
  "createdAt": "2023-06-15T10:00:00Z"
}
```

#### POST /api/v1/auth/refresh
Refresh an existing JWT token.

**Request:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600
}
```

#### POST /api/v1/auth/logout
Invalidate the current JWT token.

**Request:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response:**
```json
{
  "message": "Successfully logged out"
}
```

## User Management

### Endpoints

#### GET /api/v1/users/me
Get the current authenticated user's details.

**Response:**
```json
{
  "id": 123,
  "login": "username",
  "email": "user@example.com",
  "name": "User Name",
  "defaultCurrencyId": 1,
  "settings": {
    "transactionAmountLimitType": 2,
    "transactionAmountLimitValue": 50,
    "includeTransactionsFromSubcategories": false,
    "multiCurrencyBalanceCalculatingAlgorithm": 0,
    "invertSaldoForIncome": true
  }
}
```

#### PUT /api/v1/users/me
Update the current user's details.

**Request:**
```json
{
  "name": "Updated Name",
  "email": "new.email@example.com",
  "defaultCurrencyId": 2,
  "settings": {
    "transactionAmountLimitType": 1,
    "transactionAmountLimitValue": 100,
    "includeTransactionsFromSubcategories": true,
    "multiCurrencyBalanceCalculatingAlgorithm": 1
  }
}
```

**Response:**
```json
{
  "id": 123,
  "login": "username",
  "email": "new.email@example.com",
  "name": "Updated Name",
  "defaultCurrencyId": 2,
  "settings": {
    "transactionAmountLimitType": 1,
    "transactionAmountLimitValue": 100,
    "includeTransactionsFromSubcategories": true,
    "multiCurrencyBalanceCalculatingAlgorithm": 1,
    "invertSaldoForIncome": true
  }
}
```

#### PUT /api/v1/users/me/password
Update the current user's password.

**Request:**
```json
{
  "currentPassword": "oldPassword123",
  "newPassword": "newPassword456"
}
```

**Response:**
```json
{
  "message": "Password updated successfully"
}
```

## Category Management

### Endpoints

#### GET /api/v1/categories
Get all categories for the authenticated user.

**Query Parameters:**
- `includeSystem` (boolean): Whether to include system categories
- `flat` (boolean): Return flat list instead of hierarchy
- `withBalance` (boolean): Include category balance in the response
- `period` (string): Period for balance calculation (THIS_MONTH, LAST_MONTH, etc.)

**Response:**
```json
{
  "categories": [
    {
      "id": 1,
      "name": "Income",
      "description": "Income category",
      "categoryType": "INCOME",
      "lft": 1,
      "rgt": 10,
      "children": [
        {
          "id": 2,
          "name": "Salary",
          "description": "Regular income",
          "categoryType": "INCOME",
          "lft": 2,
          "rgt": 3,
          "children": []
        },
        {
          "id": 3,
          "name": "Investments",
          "description": "Investment returns",
          "categoryType": "INCOME",
          "lft": 4,
          "rgt": 9,
          "children": [
            {
              "id": 4,
              "name": "Dividends",
              "description": "Dividend income",
              "categoryType": "INCOME",
              "lft": 5,
              "rgt": 6,
              "children": []
            },
            {
              "id": 5,
              "name": "Interest",
              "description": "Interest income",
              "categoryType": "INCOME",
              "lft": 7,
              "rgt": 8,
              "children": []
            }
          ]
        }
      ]
    }
  ]
}
```

#### POST /api/v1/categories
Create a new category.

**Request:**
```json
{
  "name": "New Category",
  "description": "Description of the category",
  "categoryType": "EXPENSE",
  "parentId": 10
}
```

**Response:**
```json
{
  "id": 20,
  "name": "New Category",
  "description": "Description of the category",
  "categoryType": "EXPENSE",
  "lft": 45,
  "rgt": 46,
  "parentId": 10
}
```

#### GET /api/v1/categories/{id}
Get a specific category by ID.

**Response:**
```json
{
  "id": 20,
  "name": "New Category",
  "description": "Description of the category",
  "categoryType": "EXPENSE",
  "lft": 45,
  "rgt": 46,
  "parentId": 10,
  "balance": {
    "amount": -500.75,
    "currency": {
      "id": 1,
      "symbol": "$",
      "longSymbol": "USD"
    }
  }
}
```

#### PUT /api/v1/categories/{id}
Update a specific category.

**Request:**
```json
{
  "name": "Updated Category Name",
  "description": "Updated description"
}
```

**Response:**
```json
{
  "id": 20,
  "name": "Updated Category Name",
  "description": "Updated description",
  "categoryType": "EXPENSE",
  "lft": 45,
  "rgt": 46,
  "parentId": 10
}
```

#### DELETE /api/v1/categories/{id}
Delete a specific category.

**Query Parameters:**
- `transferItems` (string): 'reassign' or 'delete' (what to do with associated transfer items)
- `reassignTo` (number): Category ID to reassign transfer items to (if transferItems=reassign)

**Response:**
```json
{
  "message": "Category deleted successfully"
}
```

#### PUT /api/v1/categories/{id}/move
Move a category to a new parent or position.

**Request:**
```json
{
  "newParentId": 15,
  "position": 2
}
```

**Response:**
```json
{
  "id": 20,
  "name": "Updated Category Name",
  "description": "Updated description",
  "categoryType": "EXPENSE",
  "lft": 67,
  "rgt": 68,
  "parentId": 15
}
```

## Transfer Management

### Endpoints

#### GET /api/v1/transfers
Get transfers for the authenticated user.

**Query Parameters:**
- `page` (number): Page number for pagination
- `limit` (number): Items per page
- `startDate` (string): Filter by start date (ISO format)
- `endDate` (string): Filter by end date (ISO format)
- `categoryId` (number): Filter by category ID
- `currencyId` (number): Filter by currency ID
- `minAmount` (number): Filter by minimum amount
- `maxAmount` (number): Filter by maximum amount
- `search` (string): Search in descriptions

**Response:**
```json
{
  "transfers": [
    {
      "id": 101,
      "description": "Grocery shopping",
      "day": "2023-06-10",
      "transferItems": [
        {
          "id": 201,
          "value": -75.50,
          "description": "Groceries",
          "category": {
            "id": 25,
            "name": "Food"
          },
          "currency": {
            "id": 1,
            "symbol": "$",
            "longSymbol": "USD"
          }
        },
        {
          "id": 202,
          "value": 75.50,
          "description": "Credit Card payment",
          "category": {
            "id": 30,
            "name": "Credit Card"
          },
          "currency": {
            "id": 1,
            "symbol": "$",
            "longSymbol": "USD"
          }
        }
      ]
    }
  ],
  "pagination": {
    "total": 157,
    "page": 1,
    "limit": 20,
    "pages": 8
  }
}
```

#### POST /api/v1/transfers
Create a new transfer.

**Request:**
```json
{
  "description": "Monthly rent payment",
  "day": "2023-06-15",
  "transferItems": [
    {
      "value": -1200,
      "description": "Rent payment",
      "categoryId": 25,
      "currencyId": 1
    },
    {
      "value": 1200,
      "description": "From bank account",
      "categoryId": 40,
      "currencyId": 1
    }
  ],
  "conversions": []
}
```

**Response:**
```json
{
  "id": 102,
  "description": "Monthly rent payment",
  "day": "2023-06-15",
  "transferItems": [
    {
      "id": 203,
      "value": -1200,
      "description": "Rent payment",
      "category": {
        "id": 25,
        "name": "Housing"
      },
      "currency": {
        "id": 1,
        "symbol": "$",
        "longSymbol": "USD"
      }
    },
    {
      "id": 204,
      "value": 1200,
      "description": "From bank account",
      "category": {
        "id": 40,
        "name": "Bank Account"
      },
      "currency": {
        "id": 1,
        "symbol": "$",
        "longSymbol": "USD"
      }
    }
  ],
  "conversions": []
}
```

#### GET /api/v1/transfers/{id}
Get a specific transfer by ID.

**Response:**
```json
{
  "id": 102,
  "description": "Monthly rent payment",
  "day": "2023-06-15",
  "transferItems": [
    {
      "id": 203,
      "value": -1200,
      "description": "Rent payment",
      "category": {
        "id": 25,
        "name": "Housing"
      },
      "currency": {
        "id": 1,
        "symbol": "$",
        "longSymbol": "USD"
      }
    },
    {
      "id": 204,
      "value": 1200,
      "description": "From bank account",
      "category": {
        "id": 40,
        "name": "Bank Account"
      },
      "currency": {
        "id": 1,
        "symbol": "$",
        "longSymbol": "USD"
      }
    }
  ],
  "conversions": []
}
```

#### PUT /api/v1/transfers/{id}
Update a specific transfer.

**Request:**
```json
{
  "description": "Updated rent payment",
  "day": "2023-06-16",
  "transferItems": [
    {
      "id": 203,
      "value": -1250,
      "description": "Rent payment with fees",
      "categoryId": 25,
      "currencyId": 1
    },
    {
      "id": 204,
      "value": 1250,
      "description": "From bank account",
      "categoryId": 40,
      "currencyId": 1
    }
  ]
}
```

**Response:**
```json
{
  "id": 102,
  "description": "Updated rent payment",
  "day": "2023-06-16",
  "transferItems": [
    {
      "id": 203,
      "value": -1250,
      "description": "Rent payment with fees",
      "category": {
        "id": 25,
        "name": "Housing"
      },
      "currency": {
        "id": 1,
        "symbol": "$",
        "longSymbol": "USD"
      }
    },
    {
      "id": 204,
      "value": 1250,
      "description": "From bank account",
      "category": {
        "id": 40,
        "name": "Bank Account"
      },
      "currency": {
        "id": 1,
        "symbol": "$",
        "longSymbol": "USD"
      }
    }
  ],
  "conversions": []
}
```

#### DELETE /api/v1/transfers/{id}
Delete a specific transfer.

**Response:**
```json
{
  "message": "Transfer deleted successfully"
}
```

## Currency Management

### Endpoints

#### GET /api/v1/currencies
Get all currencies available to the authenticated user.

**Query Parameters:**
- `includeSystem` (boolean): Whether to include system currencies

**Response:**
```json
{
  "currencies": [
    {
      "id": 1,
      "symbol": "$",
      "longSymbol": "USD",
      "name": "Dollar",
      "longName": "United States Dollar",
      "system": true
    },
    {
      "id": 2,
      "symbol": "€",
      "longSymbol": "EUR",
      "name": "Euro",
      "longName": "Euro",
      "system": true
    },
    {
      "id": 3,
      "symbol": "₹",
      "longSymbol": "INR",
      "name": "Rupee",
      "longName": "Indian Rupee",
      "system": false
    }
  ]
}
```

#### POST /api/v1/currencies
Create a new currency.

**Request:**
```json
{
  "symbol": "£",
  "longSymbol": "GBP",
  "name": "Pound",
  "longName": "British Pound Sterling"
}
```

**Response:**
```json
{
  "id": 4,
  "symbol": "£",
  "longSymbol": "GBP",
  "name": "Pound",
  "longName": "British Pound Sterling",
  "system": false
}
```

#### GET /api/v1/currencies/{id}
Get a specific currency by ID.

**Response:**
```json
{
  "id": 4,
  "symbol": "£",
  "longSymbol": "GBP",
  "name": "Pound",
  "longName": "British Pound Sterling",
  "system": false
}
```

#### PUT /api/v1/currencies/{id}
Update a specific currency.

**Request:**
```json
{
  "symbol": "£",
  "name": "GBP",
  "longName": "British Pound"
}
```

**Response:**
```json
{
  "id": 4,
  "symbol": "£",
  "longSymbol": "GBP",
  "name": "GBP",
  "longName": "British Pound",
  "system": false
}
```

#### DELETE /api/v1/currencies/{id}
Delete a specific currency.

**Response:**
```json
{
  "message": "Currency deleted successfully"
}
```

## Exchange Rate Management

### Endpoints

#### GET /api/v1/exchanges
Get exchange rates.

**Query Parameters:**
- `leftCurrencyId` (number): Left currency ID
- `rightCurrencyId` (number): Right currency ID
- `startDate` (string): Filter by start date (ISO format)
- `endDate` (string): Filter by end date (ISO format)
- `page` (number): Page number for pagination
- `limit` (number): Items per page

**Response:**
```json
{
  "exchanges": [
    {
      "id": 301,
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
      "leftToRight": 0.92,
      "rightToLeft": 1.09,
      "day": "2023-06-15",
      "description": "Official rate"
    }
  ],
  "pagination": {
    "total": 57,
    "page": 1,
    "limit": 20,
    "pages": 3
  }
}
```

#### POST /api/v1/exchanges
Create a new exchange rate.

**Request:**
```json
{
  "leftCurrencyId": 1,
  "rightCurrencyId": 4,
  "leftToRight": 0.79,
  "rightToLeft": 1.27,
  "day": "2023-06-15",
  "description": "Market rate"
}
```

**Response:**
```json
{
  "id": 302,
  "leftCurrency": {
    "id": 1,
    "symbol": "$",
    "longSymbol": "USD"
  },
  "rightCurrency": {
    "id": 4,
    "symbol": "£",
    "longSymbol": "GBP"
  },
  "leftToRight": 0.79,
  "rightToLeft": 1.27,
  "day": "2023-06-15",
  "description": "Market rate"
}
```

#### GET /api/v1/exchanges/{id}
Get a specific exchange rate by ID.

**Response:**
```json
{
  "id": 302,
  "leftCurrency": {
    "id": 1,
    "symbol": "$",
    "longSymbol": "USD"
  },
  "rightCurrency": {
    "id": 4,
    "symbol": "£",
    "longSymbol": "GBP"
  },
  "leftToRight": 0.79,
  "rightToLeft": 1.27,
  "day": "2023-06-15",
  "description": "Market rate"
}
```

#### PUT /api/v1/exchanges/{id}
Update a specific exchange rate.

**Request:**
```json
{
  "leftToRight": 0.78,
  "rightToLeft": 1.28,
  "description": "Updated market rate"
}
```

**Response:**
```json
{
  "id": 302,
  "leftCurrency": {
    "id": 1,
    "symbol": "$",
    "longSymbol": "USD"
  },
  "rightCurrency": {
    "id": 4,
    "symbol": "£",
    "longSymbol": "GBP"
  },
  "leftToRight": 0.78,
  "rightToLeft": 1.28,
  "day": "2023-06-15",
  "description": "Updated market rate"
}
```

#### DELETE /api/v1/exchanges/{id}
Delete a specific exchange rate.

**Response:**
```json
{
  "message": "Exchange rate deleted successfully"
}
```

#### GET /api/v1/exchanges/convert
Convert an amount between currencies.

**Query Parameters:**
- `amount` (number): Amount to convert
- `fromCurrencyId` (number): Source currency ID
- `toCurrencyId` (number): Target currency ID
- `date` (string): Date for exchange rate (ISO format)

**Response:**
```json
{
  "fromCurrency": {
    "id": 1,
    "symbol": "$",
    "longSymbol": "USD"
  },
  "toCurrency": {
    "id": 4,
    "symbol": "£",
    "longSymbol": "GBP"
  },
  "sourceAmount": 100,
  "convertedAmount": 78.00,
  "exchangeRate": 0.78,
  "date": "2023-06-15"
}
```

## Goal Management

### Endpoints

#### GET /api/v1/goals
Get all goals for the authenticated user.

**Query Parameters:**
- `status` (string): Filter by status (active, completed)
- `categoryId` (number): Filter by category ID

**Response:**
```json
{
  "goals": [
    {
      "id": 501,
      "name": "Vacation Fund",
      "value": 3000,
      "description": "Savings for summer vacation",
      "deadline": "2023-08-01",
      "category": {
        "id": 45,
        "name": "Vacation Savings"
      },
      "currency": {
        "id": 1,
        "symbol": "$",
        "longSymbol": "USD"
      },
      "progress": {
        "current": 1500,
        "percentage": 50
      }
    }
  ]
}
```

#### POST /api/v1/goals
Create a new goal.

**Request:**
```json
{
  "name": "New Car",
  "value": 20000,
  "description": "Saving for a new vehicle",
  "deadline": "2024-06-15",
  "categoryId": 46,
  "currencyId": 1
}
```

**Response:**
```json
{
  "id": 502,
  "name": "New Car",
  "value": 20000,
  "description": "Saving for a new vehicle",
  "deadline": "2024-06-15",
  "category": {
    "id": 46,
    "name": "Car Savings"
  },
  "currency": {
    "id": 1,
    "symbol": "$",
    "longSymbol": "USD"
  },
  "progress": {
    "current": 0,
    "percentage": 0
  }
}
```

#### GET /api/v1/goals/{id}
Get a specific goal by ID.

**Response:**
```json
{
  "id": 502,
  "name": "New Car",
  "value": 20000,
  "description": "Saving for a new vehicle",
  "deadline": "2024-06-15",
  "category": {
    "id": 46,
    "name": "Car Savings"
  },
  "currency": {
    "id": 1,
    "symbol": "$",
    "longSymbol": "USD"
  },
  "progress": {
    "current": 0,
    "percentage": 0
  }
}
```

#### PUT /api/v1/goals/{id}
Update a specific goal.

**Request:**
```json
{
  "name": "New SUV",
  "value": 25000,
  "description": "Saving for a new SUV",
  "deadline": "2024-09-01"
}
```

**Response:**
```json
{
  "id": 502,
  "name": "New SUV",
  "value": 25000,
  "description": "Saving for a new SUV",
  "deadline": "2024-09-01",
  "category": {
    "id": 46,
    "name": "Car Savings"
  },
  "currency": {
    "id": 1,
    "symbol": "$",
    "longSymbol": "USD"
  },
  "progress": {
    "current": 0,
    "percentage": 0
  }
}
```

#### DELETE /api/v1/goals/{id}
Delete a specific goal.

**Response:**
```json
{
  "message": "Goal deleted successfully"
}
```

#### GET /api/v1/goals/{id}/progress
Get detailed progress information for a goal.

**Query Parameters:**
- `periodType` (string): Period type for progress tracking (THIS_MONTH, THIS_YEAR, etc.)

**Response:**
```json
{
  "goal": {
    "id": 502,
    "name": "New SUV",
    "value": 25000
  },
  "currentAmount": 2500,
  "percentage": 10,
  "timeline": [
    {
      "date": "2023-01-15",
      "amount": 0
    },
    {
      "date": "2023-02-15",
      "amount": 500
    },
    {
      "date": "2023-03-15",
      "amount": 1000
    },
    {
      "date": "2023-04-15",
      "amount": 1500
    },
    {
      "date": "2023-05-15",
      "amount": 2000
    },
    {
      "date": "2023-06-15",
      "amount": 2500
    }
  ],
  "projectedCompletion": "2024-06-15",
  "monthlyContribution": 1000,
  "requiredMonthlyContribution": 1500
}
```

## Report Management

### Endpoints

#### GET /api/v1/reports
Get all reports for the authenticated user.

**Query Parameters:**
- `type` (string): Filter by report type (ShareReport, FlowReport, ValueReport)

**Response:**
```json
{
  "reports": [
    {
      "id": 701,
      "name": "Monthly Expenses",
      "type": "ShareReport",
      "periodType": "THIS_MONTH",
      "periodStart": null,
      "periodEnd": null,
      "reportViewType": "PIE",
      "relativePeriod": true,
      "createdAt": "2023-05-01T12:00:00Z",
      "updatedAt": "2023-05-01T12:00:00Z"
    },
    {
      "id": 702,
      "name": "Income vs Expenses",
      "type": "ValueReport",
      "periodType": "THIS_YEAR",
      "periodStart": null,
      "periodEnd": null,
      "reportViewType": "BAR",
      "relativePeriod": true,
      "createdAt": "2023-05-02T14:30:00Z",
      "updatedAt": "2023-05-02T14:30:00Z"
    }
  ]
}
```

#### POST /api/v1/reports
Create a new report.

**Request:**
```json
{
  "name": "Quarterly Expenses",
  "type": "ShareReport",
  "periodType": "SELECTED",
  "periodStart": "2023-01-01",
  "periodEnd": "2023-03-31",
  "reportViewType": "PIE",
  "relativePeriod": false,
  "depth": 2,
  "maxCategoriesValuesCount": 15,
  "categoryId": 10,
  "categoryReportOptions": []
}
```

**Response:**
```json
{
  "id": 703,
  "name": "Quarterly Expenses",
  "type": "ShareReport",
  "periodType": "SELECTED",
  "periodStart": "2023-01-01",
  "periodEnd": "2023-03-31",
  "reportViewType": "PIE",
  "relativePeriod": false,
  "createdAt": "2023-06-15T10:00:00Z",
  "updatedAt": "2023-06-15T10:00:00Z",
  "depth": 2,
  "maxCategoriesValuesCount": 15,
  "category": {
    "id": 10,
    "name": "Expenses"
  },
  "categoryReportOptions": []
}
```

#### GET /api/v1/reports/{id}
Get a specific report by ID.

**Response:**
```json
{
  "id": 703,
  "name": "Quarterly Expenses",
  "type": "ShareReport",
  "periodType": "SELECTED",
  "periodStart": "2023-01-01",
  "periodEnd": "2023-03-31",
  "reportViewType": "PIE",
  "relativePeriod": false,
  "createdAt": "2023-06-15T10:00:00Z",
  "updatedAt": "2023-06-15T10:00:00Z",
  "depth": 2,
  "maxCategoriesValuesCount": 15,
  "category": {
    "id": 10,
    "name": "Expenses"
  },
  "categoryReportOptions": []
}
```

#### PUT /api/v1/reports/{id}
Update a specific report.

**Request:**
```json
{
  "name": "Q1 Expenses",
  "reportViewType": "BAR",
  "depth": 3
}
```

**Response:**
```json
{
  "id": 703,
  "name": "Q1 Expenses",
  "type": "ShareReport",
  "periodType": "SELECTED",
  "periodStart": "2023-01-01",
  "periodEnd": "2023-03-31",
  "reportViewType": "BAR",
  "relativePeriod": false,
  "createdAt": "2023-06-15T10:00:00Z",
  "updatedAt": "2023-06-15T11:30:00Z",
  "depth": 3,
  "maxCategoriesValuesCount": 15,
  "category": {
    "id": 10,
    "name": "Expenses"
  },
  "categoryReportOptions": []
}
```

#### DELETE /api/v1/reports/{id}
Delete a specific report.

**Response:**
```json
{
  "message": "Report deleted successfully"
}
```

#### GET /api/v1/reports/{id}/data
Get the data for a specific report.

**Response for ShareReport:**
```json
{
  "report": {
    "id": 703,
    "name": "Q1 Expenses",
    "type": "ShareReport"
  },
  "data": {
    "categories": [
      {
        "id": 15,
        "name": "Groceries",
        "value": 1200,
        "percentage": 30,
        "currency": {
          "id": 1,
          "symbol": "$",
          "longSymbol": "USD"
        }
      },
      {
        "id": 16,
        "name": "Rent",
        "value": 2400,
        "percentage": 60,
        "currency": {
          "id": 1,
          "symbol": "$",
          "longSymbol": "USD"
        }
      },
      {
        "id": 17,
        "name": "Entertainment",
        "value": 400,
        "percentage": 10,
        "currency": {
          "id": 1,
          "symbol": "$",
          "longSymbol": "USD"
        }
      }
    ],
    "total": {
      "value": 4000,
      "currency": {
        "id": 1,
        "symbol": "$",
        "longSymbol": "USD"
      }
    }
  }
}
```

**Response for FlowReport:**
```json
{
  "report": {
    "id": 704,
    "name": "Cash Flow Q1",
    "type": "FlowReport"
  },
  "data": {
    "income": {
      "total": 10000,
      "currency": {
        "id": 1,
        "symbol": "$",
        "longSymbol": "USD"
      }
    },
    "expense": {
      "total": 8000,
      "currency": {
        "id": 1,
        "symbol": "$",
        "longSymbol": "USD"
      }
    },
    "balance": {
      "total": 2000,
      "currency": {
        "id": 1,
        "symbol": "$",
        "longSymbol": "USD"
      }
    },
    "details": [
      {
        "category": {
          "id": 5,
          "name": "Salary"
        },
        "amount": 9000,
        "type": "income"
      },
      {
        "category": {
          "id": 6,
          "name": "Investments"
        },
        "amount": 1000,
        "type": "income"
      },
      {
        "category": {
          "id": 15,
          "name": "Groceries"
        },
        "amount": 1200,
        "type": "expense"
      },
      {
        "category": {
          "id": 16,
          "name": "Rent"
        },
        "amount": 6000,
        "type": "expense"
      },
      {
        "category": {
          "id": 17,
          "name": "Entertainment"
        },
        "amount": 800,
        "type": "expense"
      }
    ]
  }
}
```

**Response for ValueReport:**
```json
{
  "report": {
    "id": 705,
    "name": "Income vs Expenses Q1",
    "type": "ValueReport"
  },
  "data": {
    "periods": [
      {
        "label": "January",
        "values": [
          {
            "category": {
              "id": 1,
              "name": "Income"
            },
            "value": 3500
          },
          {
            "category": {
              "id": 10,
              "name": "Expenses"
            },
            "value": 2800
          },
          {
            "category": {
              "id": 20,
              "name": "Assets"
            },
            "value": 700
          }
        ]
      },
      {
        "label": "February",
        "values": [
          {
            "category": {
              "id": 1,
              "name": "Income"
            },
            "value": 3200
          },
          {
            "category": {
              "id": 10,
              "name": "Expenses"
            },
            "value": 2500
          },
          {
            "category": {
              "id": 20,
              "name": "Assets"
            },
            "value": 700
          }
        ]
      },
      {
        "label": "March",
        "values": [
          {
            "category": {
              "id": 1,
              "name": "Income"
            },
            "value": 3300
          },
          {
            "category": {
              "id": 10,
              "name": "Expenses"
            },
            "value": 2700
          },
          {
            "category": {
              "id": 20,
              "name": "Assets"
            },
            "value": 600
          }
        ]
      }
    ],
    "categories": [
      {
        "id": 1,
        "name": "Income"
      },
      {
        "id": 10,
        "name": "Expenses"
      },
      {
        "id": 20,
        "name": "Assets"
      }
    ],
    "currency": {
      "id": 1,
      "symbol": "$",
      "longSymbol": "USD"
    }
  }
}
```

#### GET /api/v1/reports/{id}/export
Export a report in the specified format.

**Query Parameters:**
- `format` (string): Export format (pdf, csv, excel)

**Response:**
Binary file download

## Error Handling

All API endpoints will return appropriate HTTP status codes and consistent error response structures:

```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid input data",
    "details": [
      {
        "field": "name",
        "message": "Name is required"
      },
      {
        "field": "amount",
        "message": "Amount must be a positive number"
      }
    ]
  }
}
```

### Common Error Codes

- `AUTHENTICATION_ERROR`: Authentication failed or token expired
- `AUTHORIZATION_ERROR`: User doesn't have permission for the action
- `VALIDATION_ERROR`: Input validation failed
- `NOT_FOUND`: Requested resource not found
- `CONFLICT`: Request conflicts with current state
- `SERVER_ERROR`: Unexpected server error

## API Versioning

The API uses a versioning strategy in the URL path (`/api/v1/`). When significant changes are made to the API, a new version will be released (e.g., `/api/v2/`), and the older versions will be maintained for backward compatibility for a defined period.