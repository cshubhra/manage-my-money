# API Design

This document describes the RESTful API design for the Spring Boot backend of the Financial Management System.

## API Overview

The API follows REST principles with the following characteristics:

- Resource-based URLs
- Standard HTTP methods (GET, POST, PUT, DELETE)
- JSON request and response bodies
- JWT-based authentication
- Consistent error handling
- Pagination for collection endpoints
- Versioning through accept headers

## Authentication

All API endpoints except for authentication-related ones require a valid JWT token.

### Authentication Endpoints

```
POST /api/auth/login
POST /api/auth/register
POST /api/auth/refresh-token
POST /api/auth/logout
```

Example login request:
```json
{
  "username": "user@example.com",
  "password": "securePassword123"
}
```

Example login response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600,
  "userId": "123",
  "username": "user@example.com"
}
```

## Core Resources

### User Resource

```
GET    /api/users/me          # Get current user details
PUT    /api/users/me          # Update current user details
PUT    /api/users/password    # Change password
GET    /api/users/preferences # Get user preferences
PUT    /api/users/preferences # Update user preferences
DELETE /api/users/me          # Delete user account
```

### Category Resource

```
GET    /api/categories              # List categories (with optional filters)
POST   /api/categories              # Create new category
GET    /api/categories/{id}         # Get category details
PUT    /api/categories/{id}         # Update category
DELETE /api/categories/{id}         # Delete category
GET    /api/categories/{id}/balance # Get category balance
GET    /api/categories/top          # Get top-level categories
GET    /api/categories/tree         # Get full category tree
```

### Transfer Resource

```
GET    /api/transfers                  # List transfers (with filters)
POST   /api/transfers                  # Create new transfer
GET    /api/transfers/{id}             # Get transfer details
PUT    /api/transfers/{id}             # Update transfer
DELETE /api/transfers/{id}             # Delete transfer
GET    /api/transfers/recent           # Get recent transfers 
POST   /api/transfers/quick            # Create quick transfer
```

### Currency Resource

```
GET    /api/currencies           # List all currencies
POST   /api/currencies           # Create custom currency
GET    /api/currencies/{id}      # Get currency details
PUT    /api/currencies/{id}      # Update custom currency
DELETE /api/currencies/{id}      # Delete custom currency
GET    /api/currencies/system    # List system currencies
```

### Exchange Resource

```
GET    /api/exchanges                               # List exchanges
POST   /api/exchanges                               # Create exchange rate
GET    /api/exchanges/{id}                          # Get exchange details
PUT    /api/exchanges/{id}                          # Update exchange rate
DELETE /api/exchanges/{id}                          # Delete exchange rate
GET    /api/exchanges/currency/{leftId}/{rightId}   # Get rates for currency pair
GET    /api/exchanges/latest/{leftId}/{rightId}     # Get latest rate for currency pair
```

### Goal Resource

```
GET    /api/goals              # List goals
POST   /api/goals              # Create goal
GET    /api/goals/{id}         # Get goal details  
PUT    /api/goals/{id}         # Update goal
DELETE /api/goals/{id}         # Delete goal
GET    /api/goals/active       # Get active goals
GET    /api/goals/completed    # Get completed goals
POST   /api/goals/{id}/finish  # Mark goal as finished
```

### Report Resource

```
GET    /api/reports                # List reports
POST   /api/reports                # Create report
GET    /api/reports/{id}           # Get report details
PUT    /api/reports/{id}           # Update report
DELETE /api/reports/{id}           # Delete report
GET    /api/reports/{id}/execute   # Execute report and get results
```

## Request and Response Examples

### Category Example

Request to create a category:
```json
POST /api/categories
{
  "name": "Groceries",
  "description": "Food and household items",
  "categoryType": "EXPENSE",
  "parentId": 5,
  "attributes": {
    "color": "#4CAF50"
  }
}
```

Response:
```json
{
  "id": 42,
  "name": "Groceries",
  "description": "Food and household items",
  "categoryType": "EXPENSE",
  "parentId": 5,
  "path": "Expenses:Food:Groceries",
  "attributes": {
    "color": "#4CAF50"
  },
  "created": "2023-04-15T10:30:00Z",
  "updated": "2023-04-15T10:30:00Z"
}
```

### Transfer Example

Request to create a transfer:
```json
POST /api/transfers
{
  "description": "Weekly grocery shopping",
  "day": "2023-04-15",
  "items": [
    {
      "description": "Groceries at Walmart",
      "categoryId": 42,
      "currencyId": 1,
      "value": -120.50
    },
    {
      "description": "Paid from checking account",
      "categoryId": 15,
      "currencyId": 1,
      "value": 120.50
    }
  ],
  "conversions": []
}
```

Response:
```json
{
  "id": 1001,
  "description": "Weekly grocery shopping",
  "day": "2023-04-15",
  "items": [
    {
      "id": 2001,
      "description": "Groceries at Walmart",
      "categoryId": 42,
      "category": {
        "id": 42,
        "name": "Groceries"
      },
      "currencyId": 1,
      "currency": {
        "id": 1,
        "symbol": "$", 
        "longSymbol": "USD"
      },
      "value": -120.50
    },
    {
      "id": 2002,
      "description": "Paid from checking account",
      "categoryId": 15,
      "category": {
        "id": 15,
        "name": "Checking Account"
      },
      "currencyId": 1,
      "currency": {
        "id": 1,
        "symbol": "$",
        "longSymbol": "USD"
      },
      "value": 120.50
    }
  ],
  "conversions": [],
  "created": "2023-04-15T14:30:00Z",
  "updated": "2023-04-15T14:30:00Z"
}
```

## Query Parameters

Many GET endpoints support the following query parameters:

- `page` - Page number for paginated results (default: 0)
- `size` - Number of items per page (default: 20)
- `sort` - Field to sort by (e.g., `sort=name,asc`)
- `search` - Text search query
- `from` - Start date for filtering (ISO format: YYYY-MM-DD)
- `to` - End date for filtering (ISO format: YYYY-MM-DD)
- `type` - Filter by type (e.g., category type: EXPENSE, INCOME)
- `includeSubcategories` - Whether to include subcategories (true/false)

## Error Handling

All errors follow a consistent format:

```json
{
  "timestamp": "2023-04-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid input data",
  "details": [
    "Category name cannot be empty",
    "Parent category does not exist"
  ],
  "path": "/api/categories"
}
```

Common HTTP status codes:

- `200 OK` - Successful operation
- `201 Created` - Resource successfully created
- `204 No Content` - Successful operation with no response body
- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Missing or invalid authentication
- `403 Forbidden` - Authentication provided but insufficient permissions
- `404 Not Found` - Resource not found
- `409 Conflict` - Business rule violation or conflict
- `500 Internal Server Error` - Unexpected server error

## API Versioning

API versioning is handled through accept headers:

```
Accept: application/vnd.financialmanager.v1+json
```

If no version is specified, the latest version will be used.