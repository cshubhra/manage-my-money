# API Design

## Overview

This document outlines the REST API design for the Spring Boot backend that will support the Angular frontend. The API adheres to REST principles and follows the OpenAPI specification.

## API Base URL

```
/api/v1
```

## Authentication Endpoints

### Login

```
POST /api/v1/auth/login
```

**Request:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Response:**
```json
{
  "token": "string",
  "refreshToken": "string",
  "user": {
    "id": "integer",
    "username": "string",
    "email": "string",
    "name": "string",
    "defaultCurrencyId": "integer",
    "preferences": {
      "multiCurrencyBalanceCalculatingAlgorithm": "string",
      "includeTransactionsFromSubcategories": "boolean",
      "invertSaldoForIncome": "boolean",
      "transactionAmountLimitType": "string",
      "transactionAmountLimitValue": "integer"
    }
  }
}
```

### Register

```
POST /api/v1/auth/register
```

**Request:**
```json
{
  "username": "string",
  "email": "string",
  "name": "string",
  "password": "string"
}
```

**Response:**
```json
{
  "id": "integer",
  "username": "string",
  "email": "string",
  "name": "string",
  "active": "boolean",
  "message": "string"
}
```

### Logout

```
POST /api/v1/auth/logout
```

**Request:**
```json
{
  "token": "string"
}
```

**Response:**
```json
{
  "message": "string"
}
```

### Refresh Token

```
POST /api/v1/auth/token
```

**Request:**
```json
{
  "refreshToken": "string"
}
```

**Response:**
```json
{
  "token": "string",
  "refreshToken": "string"
}
```

## User Endpoints

### Get Current User

```
GET /api/v1/users/current
```

**Response:**
```json
{
  "id": "integer",
  "username": "string",
  "email": "string",
  "name": "string",
  "defaultCurrencyId": "integer",
  "preferences": {
    "multiCurrencyBalanceCalculatingAlgorithm": "string",
    "includeTransactionsFromSubcategories": "boolean",
    "invertSaldoForIncome": "boolean",
    "transactionAmountLimitType": "string",
    "transactionAmountLimitValue": "integer"
  }
}
```

### Update User

```
PUT /api/v1/users/current
```

**Request:**
```json
{
  "email": "string",
  "name": "string",
  "defaultCurrencyId": "integer",
  "preferences": {
    "multiCurrencyBalanceCalculatingAlgorithm": "string",
    "includeTransactionsFromSubcategories": "boolean",
    "invertSaldoForIncome": "boolean",
    "transactionAmountLimitType": "string",
    "transactionAmountLimitValue": "integer"
  }
}
```

**Response:**
```json
{
  "id": "integer",
  "username": "string",
  "email": "string",
  "name": "string",
  "defaultCurrencyId": "integer",
  "preferences": {
    "multiCurrencyBalanceCalculatingAlgorithm": "string",
    "includeTransactionsFromSubcategories": "boolean",
    "invertSaldoForIncome": "boolean",
    "transactionAmountLimitType": "string",
    "transactionAmountLimitValue": "integer"
  }
}
```

### Change Password

```
PUT /api/v1/users/current/password
```

**Request:**
```json
{
  "currentPassword": "string",
  "newPassword": "string"
}
```

**Response:**
```json
{
  "message": "string"
}
```

## Categories Endpoints

### Get All Categories

```
GET /api/v1/categories
```

**Query Parameters:**
- `type` (optional): Filter by category type (ASSET, INCOME, EXPENSE, LOAN, BALANCE)
- `parentId` (optional): Filter by parent category ID
- `includeSystem` (optional): Include system categories

**Response:**
```json
{
  "data": [
    {
      "id": "integer",
      "name": "string",
      "description": "string",
      "categoryType": "string",
      "parentId": "integer",
      "level": "integer",
      "lft": "integer",
      "rgt": "integer",
      "bankAccountNumber": "string",
      "email": "string",
      "loanCategory": "boolean",
      "systemCategoryIds": ["integer"],
      "isTop": "boolean"
    }
  ],
  "meta": {
    "total": "integer"
  }
}
```

### Get Category

```
GET /api/v1/categories/{id}
```

**Path Parameters:**
- `id`: Category ID

**Response:**
```json
{
  "id": "integer",
  "name": "string",
  "description": "string",
  "categoryType": "string",
  "parentId": "integer",
  "level": "integer",
  "lft": "integer",
  "rgt": "integer",
  "bankAccountNumber": "string",
  "email": "string",
  "loanCategory": "boolean",
  "systemCategoryIds": ["integer"],
  "isTop": "boolean",
  "children": [
    {
      "id": "integer",
      "name": "string",
      "description": "string",
      "categoryType": "string"
    }
  ]
}
```

### Create Category

```
POST /api/v1/categories
```

**Request:**
```json
{
  "name": "string",
  "description": "string",
  "categoryType": "string",
  "parentId": "integer",
  "bankAccountNumber": "string",
  "email": "string",
  "loanCategory": "boolean",
  "systemCategoryId": "integer",
  "openingBalance": "number",
  "openingBalanceCurrencyId": "integer",
  "newSubcategories": ["integer"]
}
```

**Response:**
```json
{
  "id": "integer",
  "name": "string",
  "description": "string",
  "categoryType": "string",
  "parentId": "integer",
  "level": "integer",
  "lft": "integer",
  "rgt": "integer",
  "bankAccountNumber": "string",
  "email": "string",
  "loanCategory": "boolean",
  "systemCategoryIds": ["integer"],
  "isTop": "boolean"
}
```

### Update Category

```
PUT /api/v1/categories/{id}
```

**Path Parameters:**
- `id`: Category ID

**Request:**
```json
{
  "name": "string",
  "description": "string",
  "parentId": "integer",
  "bankAccountNumber": "string",
  "email": "string",
  "loanCategory": "boolean",
  "systemCategoryId": "integer"
}
```

**Response:**
```json
{
  "id": "integer",
  "name": "string",
  "description": "string",
  "categoryType": "string",
  "parentId": "integer",
  "level": "integer",
  "lft": "integer",
  "rgt": "integer",
  "bankAccountNumber": "string",
  "email": "string",
  "loanCategory": "boolean",
  "systemCategoryIds": ["integer"],
  "isTop": "boolean"
}
```

### Delete Category

```
DELETE /api/v1/categories/{id}
```

**Path Parameters:**
- `id`: Category ID

**Response:**
```json
{
  "message": "string"
}
```

### Get Category Balance

```
GET /api/v1/categories/{id}/balance
```

**Path Parameters:**
- `id`: Category ID

**Query Parameters:**
- `algorithm` (optional): Balance calculation algorithm
- `withSubcategories` (optional): Include subcategories in calculation
- `date` (optional): Balance as of specific date
- `startDate` (optional): Start date for period balance
- `endDate` (optional): End date for period balance

**Response:**
```json
{
  "balances": [
    {
      "currencyId": "integer",
      "currencySymbol": "string",
      "value": "number"
    }
  ],
  "totalInDefaultCurrency": "number",
  "defaultCurrencySymbol": "string"
}
```

## Transfers Endpoints

### Get All Transfers

```
GET /api/v1/transfers
```

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `sort` (optional): Sort field and direction (e.g., "day,desc")
- `categoryId` (optional): Filter by category ID
- `startDate` (optional): Filter by start date
- `endDate` (optional): Filter by end date
- `description` (optional): Search in description

**Response:**
```json
{
  "data": [
    {
      "id": "integer",
      "description": "string",
      "day": "string (ISO date)",
      "items": [
        {
          "id": "integer",
          "description": "string",
          "value": "number",
          "categoryId": "integer",
          "categoryName": "string",
          "currencyId": "integer",
          "currencySymbol": "string"
        }
      ]
    }
  ],
  "meta": {
    "page": "integer",
    "size": "integer",
    "totalElements": "integer",
    "totalPages": "integer"
  }
}
```

### Get Transfer

```
GET /api/v1/transfers/{id}
```

**Path Parameters:**
- `id`: Transfer ID

**Response:**
```json
{
  "id": "integer",
  "description": "string",
  "day": "string (ISO date)",
  "items": [
    {
      "id": "integer",
      "description": "string",
      "value": "number",
      "categoryId": "integer",
      "categoryName": "string",
      "currencyId": "integer",
      "currencySymbol": "string"
    }
  ],
  "conversions": [
    {
      "exchangeId": "integer",
      "leftCurrencyId": "integer",
      "leftCurrencySymbol": "string",
      "rightCurrencyId": "integer",
      "rightCurrencySymbol": "string",
      "leftToRight": "number",
      "rightToLeft": "number"
    }
  ]
}
```

### Create Transfer

```
POST /api/v1/transfers
```

**Request:**
```json
{
  "description": "string",
  "day": "string (ISO date)",
  "items": [
    {
      "description": "string",
      "value": "number",
      "categoryId": "integer",
      "currencyId": "integer"
    }
  ],
  "conversions": [
    {
      "exchangeId": "integer"
    }
  ]
}
```

**Response:**
```json
{
  "id": "integer",
  "description": "string",
  "day": "string (ISO date)",
  "items": [
    {
      "id": "integer",
      "description": "string",
      "value": "number",
      "categoryId": "integer",
      "categoryName": "string",
      "currencyId": "integer",
      "currencySymbol": "string"
    }
  ],
  "conversions": [
    {
      "id": "integer",
      "exchangeId": "integer",
      "leftCurrencyId": "integer",
      "leftCurrencySymbol": "string",
      "rightCurrencyId": "integer",
      "rightCurrencySymbol": "string",
      "leftToRight": "number",
      "rightToLeft": "number"
    }
  ]
}
```

### Update Transfer

```
PUT /api/v1/transfers/{id}
```

**Path Parameters:**
- `id`: Transfer ID

**Request:**
```json
{
  "description": "string",
  "day": "string (ISO date)",
  "items": [
    {
      "id": "integer",
      "description": "string",
      "value": "number",
      "categoryId": "integer",
      "currencyId": "integer"
    }
  ],
  "conversions": [
    {
      "id": "integer",
      "exchangeId": "integer"
    }
  ]
}
```

**Response:**
```json
{
  "id": "integer",
  "description": "string",
  "day": "string (ISO date)",
  "items": [
    {
      "id": "integer",
      "description": "string",
      "value": "number",
      "categoryId": "integer",
      "categoryName": "string",
      "currencyId": "integer",
      "currencySymbol": "string"
    }
  ],
  "conversions": [
    {
      "id": "integer",
      "exchangeId": "integer",
      "leftCurrencyId": "integer",
      "leftCurrencySymbol": "string",
      "rightCurrencyId": "integer",
      "rightCurrencySymbol": "string",
      "leftToRight": "number",
      "rightToLeft": "number"
    }
  ]
}
```

### Delete Transfer

```
DELETE /api/v1/transfers/{id}
```

**Path Parameters:**
- `id`: Transfer ID

**Response:**
```json
{
  "message": "string"
}
```

### Quick Transfer

```
POST /api/v1/transfers/quick
```

**Request:**
```json
{
  "description": "string",
  "day": "string (ISO date)",
  "value": "number",
  "categoryId": "integer",
  "fromCategoryId": "integer",
  "currencyId": "integer"
}
```

**Response:**
```json
{
  "id": "integer",
  "description": "string",
  "day": "string (ISO date)",
  "items": [
    {
      "id": "integer",
      "description": "string",
      "value": "number",
      "categoryId": "integer",
      "categoryName": "string",
      "currencyId": "integer",
      "currencySymbol": "string"
    }
  ]
}
```

## Currencies Endpoints

### Get All Currencies

```
GET /api/v1/currencies
```

**Query Parameters:**
- `includeSystem` (optional): Include system currencies

**Response:**
```json
{
  "data": [
    {
      "id": "integer",
      "symbol": "string",
      "longSymbol": "string",
      "name": "string",
      "longName": "string",
      "isSystem": "boolean"
    }
  ],
  "meta": {
    "total": "integer"
  }
}
```

### Get Currency

```
GET /api/v1/currencies/{id}
```

**Path Parameters:**
- `id`: Currency ID

**Response:**
```json
{
  "id": "integer",
  "symbol": "string",
  "longSymbol": "string",
  "name": "string",
  "longName": "string",
  "isSystem": "boolean"
}
```

### Create Currency

```
POST /api/v1/currencies
```

**Request:**
```json
{
  "symbol": "string",
  "longSymbol": "string",
  "name": "string",
  "longName": "string"
}
```

**Response:**
```json
{
  "id": "integer",
  "symbol": "string",
  "longSymbol": "string",
  "name": "string",
  "longName": "string",
  "isSystem": "boolean"
}
```

### Update Currency

```
PUT /api/v1/currencies/{id}
```

**Path Parameters:**
- `id`: Currency ID

**Request:**
```json
{
  "symbol": "string",
  "longSymbol": "string",
  "name": "string",
  "longName": "string"
}
```

**Response:**
```json
{
  "id": "integer",
  "symbol": "string",
  "longSymbol": "string",
  "name": "string",
  "longName": "string",
  "isSystem": "boolean"
}
```

### Delete Currency

```
DELETE /api/v1/currencies/{id}
```

**Path Parameters:**
- `id`: Currency ID

**Response:**
```json
{
  "message": "string"
}
```

## Exchanges Endpoints

### Get Exchanges

```
GET /api/v1/exchanges
```

**Query Parameters:**
- `leftCurrencyId` (optional): Left currency ID
- `rightCurrencyId` (optional): Right currency ID
- `startDate` (optional): Start date
- `endDate` (optional): End date

**Response:**
```json
{
  "data": [
    {
      "id": "integer",
      "leftCurrencyId": "integer",
      "leftCurrencySymbol": "string",
      "rightCurrencyId": "integer",
      "rightCurrencySymbol": "string",
      "leftToRight": "number",
      "rightToLeft": "number",
      "day": "string (ISO date)"
    }
  ],
  "meta": {
    "total": "integer"
  }
}
```

### Get Exchange

```
GET /api/v1/exchanges/{id}
```

**Path Parameters:**
- `id`: Exchange ID

**Response:**
```json
{
  "id": "integer",
  "leftCurrencyId": "integer",
  "leftCurrencySymbol": "string",
  "rightCurrencyId": "integer",
  "rightCurrencySymbol": "string",
  "leftToRight": "number",
  "rightToLeft": "number",
  "day": "string (ISO date)"
}
```

### Create Exchange

```
POST /api/v1/exchanges
```

**Request:**
```json
{
  "leftCurrencyId": "integer",
  "rightCurrencyId": "integer",
  "leftToRight": "number",
  "rightToLeft": "number",
  "day": "string (ISO date)"
}
```

**Response:**
```json
{
  "id": "integer",
  "leftCurrencyId": "integer",
  "leftCurrencySymbol": "string",
  "rightCurrencyId": "integer",
  "rightCurrencySymbol": "string",
  "leftToRight": "number",
  "rightToLeft": "number",
  "day": "string (ISO date)"
}
```

### Update Exchange

```
PUT /api/v1/exchanges/{id}
```

**Path Parameters:**
- `id`: Exchange ID

**Request:**
```json
{
  "leftCurrencyId": "integer",
  "rightCurrencyId": "integer",
  "leftToRight": "number",
  "rightToLeft": "number",
  "day": "string (ISO date)"
}
```

**Response:**
```json
{
  "id": "integer",
  "leftCurrencyId": "integer",
  "leftCurrencySymbol": "string",
  "rightCurrencyId": "integer",
  "rightCurrencySymbol": "string",
  "leftToRight": "number",
  "rightToLeft": "number",
  "day": "string (ISO date)"
}
```

### Delete Exchange

```
DELETE /api/v1/exchanges/{id}
```

**Path Parameters:**
- `id`: Exchange ID

**Response:**
```json
{
  "message": "string"
}
```

## Reports Endpoints

### Get All Reports

```
GET /api/v1/reports
```

**Response:**
```json
{
  "data": [
    {
      "id": "integer",
      "type": "string",
      "name": "string",
      "periodType": "string",
      "periodStart": "string (ISO date)",
      "periodEnd": "string (ISO date)",
      "reportViewType": "string",
      "depth": "integer",
      "maxCategoriesValuesCount": "integer",
      "categoryId": "integer",
      "periodDivision": "string",
      "temporary": "boolean",
      "relativePeriod": "boolean"
    }
  ],
  "meta": {
    "total": "integer"
  }
}
```

### Get Report

```
GET /api/v1/reports/{id}
```

**Path Parameters:**
- `id`: Report ID

**Response:**
```json
{
  "id": "integer",
  "type": "string",
  "name": "string",
  "periodType": "string",
  "periodStart": "string (ISO date)",
  "periodEnd": "string (ISO date)",
  "reportViewType": "string",
  "depth": "integer",
  "maxCategoriesValuesCount": "integer",
  "categoryId": "integer",
  "categoryName": "string",
  "periodDivision": "string",
  "temporary": "boolean",
  "relativePeriod": "boolean",
  "categoryReportOptions": [
    {
      "categoryId": "integer",
      "categoryName": "string",
      "inclusionType": "string"
    }
  ]
}
```

### Create Report

```
POST /api/v1/reports
```

**Request:**
```json
{
  "type": "string",
  "name": "string",
  "periodType": "string",
  "periodStart": "string (ISO date)",
  "periodEnd": "string (ISO date)",
  "reportViewType": "string",
  "depth": "integer",
  "maxCategoriesValuesCount": "integer",
  "categoryId": "integer",
  "periodDivision": "string",
  "temporary": "boolean",
  "relativePeriod": "boolean",
  "categoryReportOptions": [
    {
      "categoryId": "integer",
      "inclusionType": "string"
    }
  ]
}
```

**Response:**
```json
{
  "id": "integer",
  "type": "string",
  "name": "string",
  "periodType": "string",
  "periodStart": "string (ISO date)",
  "periodEnd": "string (ISO date)",
  "reportViewType": "string",
  "depth": "integer",
  "maxCategoriesValuesCount": "integer",
  "categoryId": "integer",
  "categoryName": "string",
  "periodDivision": "string",
  "temporary": "boolean",
  "relativePeriod": "boolean",
  "categoryReportOptions": [
    {
      "categoryId": "integer",
      "categoryName": "string",
      "inclusionType": "string"
    }
  ]
}
```

### Update Report

```
PUT /api/v1/reports/{id}
```

**Path Parameters:**
- `id`: Report ID

**Request:**
```json
{
  "name": "string",
  "periodType": "string",
  "periodStart": "string (ISO date)",
  "periodEnd": "string (ISO date)",
  "reportViewType": "string",
  "depth": "integer",
  "maxCategoriesValuesCount": "integer",
  "categoryId": "integer",
  "periodDivision": "string",
  "relativePeriod": "boolean",
  "categoryReportOptions": [
    {
      "categoryId": "integer",
      "inclusionType": "string"
    }
  ]
}
```

**Response:**
```json
{
  "id": "integer",
  "type": "string",
  "name": "string",
  "periodType": "string",
  "periodStart": "string (ISO date)",
  "periodEnd": "string (ISO date)",
  "reportViewType": "string",
  "depth": "integer",
  "maxCategoriesValuesCount": "integer",
  "categoryId": "integer",
  "categoryName": "string",
  "periodDivision": "string",
  "temporary": "boolean",
  "relativePeriod": "boolean",
  "categoryReportOptions": [
    {
      "categoryId": "integer",
      "categoryName": "string",
      "inclusionType": "string"
    }
  ]
}
```

### Delete Report

```
DELETE /api/v1/reports/{id}
```

**Path Parameters:**
- `id`: Report ID

**Response:**
```json
{
  "message": "string"
}
```

### Get Report Data

```
GET /api/v1/reports/{id}/data
```

**Path Parameters:**
- `id`: Report ID

**Response:**
```json
{
  "reportType": "string",
  "name": "string",
  "periodStart": "string (ISO date)",
  "periodEnd": "string (ISO date)",
  "data": {
    // Varies based on report type
  }
}
```

## Goals Endpoints

### Get All Goals

```
GET /api/v1/goals
```

**Query Parameters:**
- `categoryId` (optional): Filter by category ID
- `includeFinished` (optional): Include finished goals

**Response:**
```json
{
  "data": [
    {
      "id": "integer",
      "description": "string",
      "includeSubcategories": "boolean",
      "periodType": "string",
      "goalType": "string",
      "goalCompletionCondition": "string",
      "value": "number",
      "categoryId": "integer",
      "categoryName": "string",
      "currencyId": "integer",
      "currencySymbol": "string",
      "periodStart": "string (ISO date)",
      "periodEnd": "string (ISO date)",
      "isCyclic": "boolean",
      "isFinished": "boolean",
      "cycleGroup": "integer",
      "progress": "number"
    }
  ],
  "meta": {
    "total": "integer"
  }
}
```

### Get Goal

```
GET /api/v1/goals/{id}
```

**Path Parameters:**
- `id`: Goal ID

**Response:**
```json
{
  "id": "integer",
  "description": "string",
  "includeSubcategories": "boolean",
  "periodType": "string",
  "goalType": "string",
  "goalCompletionCondition": "string",
  "value": "number",
  "categoryId": "integer",
  "categoryName": "string",
  "currencyId": "integer",
  "currencySymbol": "string",
  "periodStart": "string (ISO date)",
  "periodEnd": "string (ISO date)",
  "isCyclic": "boolean",
  "isFinished": "boolean",
  "cycleGroup": "integer",
  "progress": "number"
}
```

### Create Goal

```
POST /api/v1/goals
```

**Request:**
```json
{
  "description": "string",
  "includeSubcategories": "boolean",
  "periodType": "string",
  "goalType": "string",
  "goalCompletionCondition": "string",
  "value": "number",
  "categoryId": "integer",
  "currencyId": "integer",
  "periodStart": "string (ISO date)",
  "periodEnd": "string (ISO date)",
  "isCyclic": "boolean"
}
```

**Response:**
```json
{
  "id": "integer",
  "description": "string",
  "includeSubcategories": "boolean",
  "periodType": "string",
  "goalType": "string",
  "goalCompletionCondition": "string",
  "value": "number",
  "categoryId": "integer",
  "categoryName": "string",
  "currencyId": "integer",
  "currencySymbol": "string",
  "periodStart": "string (ISO date)",
  "periodEnd": "string (ISO date)",
  "isCyclic": "boolean",
  "isFinished": "boolean",
  "cycleGroup": "integer",
  "progress": "number"
}
```

### Update Goal

```
PUT /api/v1/goals/{id}
```

**Path Parameters:**
- `id`: Goal ID

**Request:**
```json
{
  "description": "string",
  "includeSubcategories": "boolean",
  "periodType": "string",
  "goalType": "string",
  "goalCompletionCondition": "string",
  "value": "number",
  "categoryId": "integer",
  "currencyId": "integer",
  "periodStart": "string (ISO date)",
  "periodEnd": "string (ISO date)",
  "isCyclic": "boolean",
  "isFinished": "boolean"
}
```

**Response:**
```json
{
  "id": "integer",
  "description": "string",
  "includeSubcategories": "boolean",
  "periodType": "string",
  "goalType": "string",
  "goalCompletionCondition": "string",
  "value": "number",
  "categoryId": "integer",
  "categoryName": "string",
  "currencyId": "integer",
  "currencySymbol": "string",
  "periodStart": "string (ISO date)",
  "periodEnd": "string (ISO date)",
  "isCyclic": "boolean",
  "isFinished": "boolean",
  "cycleGroup": "integer",
  "progress": "number"
}
```

### Delete Goal

```
DELETE /api/v1/goals/{id}
```

**Path Parameters:**
- `id`: Goal ID

**Response:**
```json
{
  "message": "string"
}
```

## System Categories Endpoints

### Get All System Categories

```
GET /api/v1/system-categories
```

**Query Parameters:**
- `type` (optional): Filter by category type
- `parentId` (optional): Filter by parent ID

**Response:**
```json
{
  "data": [
    {
      "id": "integer",
      "name": "string",
      "description": "string",
      "categoryType": "string",
      "parentId": "integer",
      "level": "integer",
      "lft": "integer",
      "rgt": "integer",
      "nameWithPath": "string"
    }
  ],
  "meta": {
    "total": "integer"
  }
}
```

## Error Responses

All endpoints may return the following error responses:

### 400 Bad Request

```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Validation failed",
    "details": [
      {
        "field": "username",
        "message": "Username cannot be empty"
      }
    ]
  }
}
```

### 401 Unauthorized

```json
{
  "error": {
    "code": "UNAUTHORIZED",
    "message": "Authentication required"
  }
}
```

### 403 Forbidden

```json
{
  "error": {
    "code": "FORBIDDEN",
    "message": "Insufficient permissions"
  }
}
```

### 404 Not Found

```json
{
  "error": {
    "code": "RESOURCE_NOT_FOUND",
    "message": "Resource not found"
  }
}
```

### 500 Internal Server Error

```json
{
  "error": {
    "code": "SERVER_ERROR",
    "message": "An unexpected error occurred"
  }
}
```

## OpenAPI Specification

The complete OpenAPI specification for this API is available at:

```
/api/v1/docs
```

The specification describes all endpoints, request/response models, and possible errors in detail.