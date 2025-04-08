# API Design Specification

## Overview
This document defines the REST API specifications for the financial management system. The API follows OpenAPI 3.0 standards and implements RESTful principles.

## Base URL
```
https://api.finance-app.com/api/v1
```

## Authentication
All API endpoints (except /auth) require JWT authentication using Bearer token in the Authorization header:
```
Authorization: Bearer <jwt_token>
```

## API Endpoints

### Authentication API

#### Login
```yaml
POST /auth/login
description: Authenticate user and retrieve JWT token
request:
  content:
    application/json:
      schema:
        type: object
        properties:
          email:
            type: string
            format: email
          password:
            type: string
            format: password
response:
  200:
    description: Successful authentication
    content:
      application/json:
        schema:
          type: object
          properties:
            token:
              type: string
            expiresIn:
              type: integer
  401:
    description: Invalid credentials
```

### Transfer API

#### Create Transfer
```yaml
POST /transfers
description: Create a new transfer
security:
  - bearerAuth: []
request:
  content:
    application/json:
      schema:
        type: object
        properties:
          description:
            type: string
          day:
            type: string
            format: date
          items:
            type: array
            items:
              type: object
              properties:
                value:
                  type: number
                  format: decimal
                categoryId:
                  type: integer
                currencyId:
                  type: integer
response:
  201:
    description: Transfer created successfully
    content:
      application/json:
        schema:
          $ref: '#/components/schemas/Transfer'
  400:
    description: Invalid request
```

#### Get Transfers
```yaml
GET /transfers
description: Retrieve transfers with pagination
security:
  - bearerAuth: []
parameters:
  - name: page
    in: query
    schema:
      type: integer
      default: 0
  - name: size
    in: query
    schema:
      type: integer
      default: 20
  - name: sort
    in: query
    schema:
      type: string
      default: day,desc
response:
  200:
    description: List of transfers
    content:
      application/json:
        schema:
          type: object
          properties:
            content:
              type: array
              items:
                $ref: '#/components/schemas/Transfer'
            totalElements:
              type: integer
            totalPages:
              type: integer
```

### Category API

#### Create Category
```yaml
POST /categories
description: Create a new category
security:
  - bearerAuth: []
request:
  content:
    application/json:
      schema:
        type: object
        properties:
          name:
            type: string
          description:
            type: string
          parentId:
            type: integer
            nullable: true
          type:
            type: string
            enum: [INCOME, EXPENSE, TRANSFER]
response:
  201:
    description: Category created successfully
    content:
      application/json:
        schema:
          $ref: '#/components/schemas/Category'
```

#### Get Category Tree
```yaml
GET /categories/tree
description: Retrieve category hierarchy
security:
  - bearerAuth: []
response:
  200:
    description: Category tree
    content:
      application/json:
        schema:
          type: array
          items:
            $ref: '#/components/schemas/CategoryTree'
```

### Currency API

#### Get Exchange Rate
```yaml
GET /currencies/exchange-rate
description: Get exchange rate between currencies
security:
  - bearerAuth: []
parameters:
  - name: from
    in: query
    required: true
    schema:
      type: string
  - name: to
    in: query
    required: true
    schema:
      type: string
  - name: date
    in: query
    schema:
      type: string
      format: date
response:
  200:
    description: Exchange rate information
    content:
      application/json:
        schema:
          type: object
          properties:
            from:
              type: string
            to:
              type: string
            rate:
              type: number
            date:
              type: string
              format: date
```

### Report API

#### Generate Report
```yaml
POST /reports
description: Generate financial report
security:
  - bearerAuth: []
request:
  content:
    application/json:
      schema:
        type: object
        properties:
          type:
            type: string
            enum: [CATEGORY, FLOW, VALUE, SHARE]
          periodStart:
            type: string
            format: date
          periodEnd:
            type: string
            format: date
          categories:
            type: array
            items:
              type: integer
          includeSubcategories:
            type: boolean
            default: false
response:
  200:
    description: Generated report
    content:
      application/json:
        schema:
          $ref: '#/components/schemas/Report'
```

## Data Models

### Transfer
```yaml
components:
  schemas:
    Transfer:
      type: object
      properties:
        id:
          type: integer
          format: int64
        description:
          type: string
        day:
          type: string
          format: date
        items:
          type: array
          items:
            $ref: '#/components/schemas/TransferItem'
        createdAt:
          type: string
          format: date-time
```

### Category
```yaml
components:
  schemas:
    Category:
      type: object
      properties:
        id:
          type: integer
          format: int64
        name:
          type: string
        description:
          type: string
        type:
          type: string
          enum: [INCOME, EXPENSE, TRANSFER]
        parentId:
          type: integer
          format: int64
          nullable: true
```

### CategoryTree
```yaml
components:
  schemas:
    CategoryTree:
      type: object
      properties:
        id:
          type: integer
          format: int64
        name:
          type: string
        type:
          type: string
        children:
          type: array
          items:
            $ref: '#/components/schemas/CategoryTree'
```

### Report
```yaml
components:
  schemas:
    Report:
      type: object
      properties:
        id:
          type: integer
          format: int64
        type:
          type: string
        periodStart:
          type: string
          format: date
        periodEnd:
          type: string
          format: date
        data:
          type: object
        charts:
          type: array
          items:
            $ref: '#/components/schemas/Chart'
```

## Error Responses

### Standard Error Response
```yaml
components:
  schemas:
    Error:
      type: object
      properties:
        status:
          type: integer
        message:
          type: string
        timestamp:
          type: string
          format: date-time
        path:
          type: string
        errors:
          type: array
          items:
            type: object
            properties:
              field:
                type: string
              message:
                type: string
```

## Rate Limiting
- Rate limit: 100 requests per minute per user
- Headers:
  - X-RateLimit-Limit: Maximum number of requests allowed
  - X-RateLimit-Remaining: Number of requests remaining
  - X-RateLimit-Reset: Time when the rate limit resets

## Versioning
API versioning is handled through the URL path (/api/v1/). Major version changes will increment the version number.

## CORS
The API supports CORS for specified origins with the following headers:
```
Access-Control-Allow-Origin: [configured domains]
Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS
Access-Control-Allow-Headers: Authorization, Content-Type
Access-Control-Max-Age: 3600
```

## Security
1. All endpoints use HTTPS
2. JWT tokens expire after 24 hours
3. Refresh tokens are supported for seamless authentication
4. Password requirements:
   - Minimum 8 characters
   - At least one uppercase letter
   - At least one number
   - At least one special character

## Pagination
List endpoints support pagination with the following query parameters:
- page: Page number (0-based)
- size: Number of items per page
- sort: Sort field and direction (e.g., "field,asc" or "field,desc")

Response includes:
- content: Array of items
- totalElements: Total number of items
- totalPages: Total number of pages
- number: Current page number
- size: Page size