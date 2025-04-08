# API Design Documentation

## Overview
This document describes the REST API design for the financial application. The API follows OpenAPI 3.0 specifications and REST principles.

## Base URL
```
https://api.financial-app.com/api/v1
```

## Authentication
All API endpoints except `/auth/*` require JWT authentication token in the Authorization header:
```
Authorization: Bearer <token>
```

## API Endpoints

### Authentication

```yaml
openapi: 3.0.0
info:
  title: Financial Application API
  version: 1.0.0
paths:
  /auth/login:
    post:
      tags:
        - Authentication
      summary: User login
      requestBody:
        required: true
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
      responses:
        '200':
          description: Successful login
          content:
            application/json:
              schema:
                type: object
                properties:
                  token:
                    type: string
                  user:
                    $ref: '#/components/schemas/User'
        '401':
          description: Invalid credentials

  /auth/register:
    post:
      tags:
        - Authentication
      summary: User registration
      requestBody:
        required: true
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
                name:
                  type: string
      responses:
        '201':
          description: User created
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/User'
        '400':
          description: Invalid input
```

### Transfers

```yaml
  /transfers:
    get:
      tags:
        - Transfers
      summary: Get user transfers
      parameters:
        - in: query
          name: page
          schema:
            type: integer
            default: 0
        - in: query
          name: size
          schema:
            type: integer
            default: 20
        - in: query
          name: sort
          schema:
            type: string
            default: date,desc
      responses:
        '200':
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
                  
    post:
      tags:
        - Transfers
      summary: Create new transfer
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/TransferRequest'
      responses:
        '201':
          description: Transfer created
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Transfer'
```

### Categories

```yaml
  /categories:
    get:
      tags:
        - Categories
      summary: Get all categories
      responses:
        '200':
          description: List of categories
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/Category'
                  
    post:
      tags:
        - Categories
      summary: Create new category
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/CategoryRequest'
      responses:
        '201':
          description: Category created
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Category'
```

### Goals

```yaml
  /goals:
    get:
      tags:
        - Goals
      summary: Get user goals
      responses:
        '200':
          description: List of goals
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/Goal'
                  
    post:
      tags:
        - Goals
      summary: Create new goal
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/GoalRequest'
      responses:
        '201':
          description: Goal created
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Goal'
                
  /goals/{id}/progress:
    get:
      tags:
        - Goals
      summary: Get goal progress
      parameters:
        - in: path
          name: id
          required: true
          schema:
            type: integer
      responses:
        '200':
          description: Goal progress
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/GoalProgress'
```

### Reports

```yaml
  /reports:
    get:
      tags:
        - Reports
      summary: Generate financial report
      parameters:
        - in: query
          name: startDate
          required: true
          schema:
            type: string
            format: date
        - in: query
          name: endDate
          required: true
          schema:
            type: string
            format: date
        - in: query
          name: type
          required: true
          schema:
            type: string
            enum: [CATEGORY, MONTHLY, GOAL]
      responses:
        '200':
          description: Report data
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Report'
```

### Exchange Rates

```yaml
  /exchanges/rates:
    get:
      tags:
        - Exchanges
      summary: Get current exchange rates
      parameters:
        - in: query
          name: baseCurrency
          required: true
          schema:
            type: string
      responses:
        '200':
          description: Exchange rates
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ExchangeRates'
                
  /exchanges:
    post:
      tags:
        - Exchanges
      summary: Create currency exchange
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ExchangeRequest'
      responses:
        '201':
          description: Exchange created
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Exchange'
```

## Data Models

```yaml
components:
  schemas:
    User:
      type: object
      properties:
        id:
          type: integer
        email:
          type: string
        name:
          type: string
        createdAt:
          type: string
          format: date-time

    Transfer:
      type: object
      properties:
        id:
          type: integer
        amount:
          type: number
          format: decimal
        description:
          type: string
        date:
          type: string
          format: date-time
        category:
          $ref: '#/components/schemas/Category'
        items:
          type: array
          items:
            $ref: '#/components/schemas/TransferItem'

    TransferRequest:
      type: object
      required:
        - amount
        - categoryId
        - date
      properties:
        amount:
          type: number
          format: decimal
        description:
          type: string
        categoryId:
          type: integer
        date:
          type: string
          format: date-time
        items:
          type: array
          items:
            $ref: '#/components/schemas/TransferItemRequest'

    TransferItem:
      type: object
      properties:
        id:
          type: integer
        amount:
          type: number
          format: decimal
        description:
          type: string
        category:
          $ref: '#/components/schemas/Category'

    Category:
      type: object
      properties:
        id:
          type: integer
        name:
          type: string
        type:
          type: string
          enum: [INCOME, EXPENSE]
        system:
          type: boolean

    Goal:
      type: object
      properties:
        id:
          type: integer
        name:
          type: string
        targetAmount:
          type: number
          format: decimal
        currentAmount:
          type: number
          format: decimal
        deadline:
          type: string
          format: date

    GoalProgress:
      type: object
      properties:
        goalId:
          type: integer
        currentAmount:
          type: number
          format: decimal
        targetAmount:
          type: number
          format: decimal
        percentage:
          type: number
          format: float
        remainingAmount:
          type: number
          format: decimal
        daysRemaining:
          type: integer

    Report:
      type: object
      properties:
        startDate:
          type: string
          format: date
        endDate:
          type: string
          format: date
        type:
          type: string
          enum: [CATEGORY, MONTHLY, GOAL]
        data:
          type: object
          additionalProperties: true

    ExchangeRates:
      type: object
      properties:
        baseCurrency:
          type: string
        date:
          type: string
          format: date
        rates:
          type: object
          additionalProperties:
            type: number
            format: decimal

    Exchange:
      type: object
      properties:
        id:
          type: integer
        fromCurrency:
          type: string
        toCurrency:
          type: string
        amount:
          type: number
          format: decimal
        rate:
          type: number
          format: decimal
        date:
          type: string
          format: date-time
```

## Error Responses

All API endpoints may return the following error responses:

```yaml
components:
  responses:
    UnauthorizedError:
      description: Authentication failed
      content:
        application/json:
          schema:
            type: object
            properties:
              code:
                type: integer
                example: 401
              message:
                type: string
                example: "Unauthorized access"

    ValidationError:
      description: Invalid input data
      content:
        application/json:
          schema:
            type: object
            properties:
              code:
                type: integer
                example: 400
              message:
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

    NotFoundError:
      description: Resource not found
      content:
        application/json:
          schema:
            type: object
            properties:
              code:
                type: integer
                example: 404
              message:
                type: string
```

## Security Schemes

```yaml
components:
  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT
```

## Pagination

For endpoints that return lists, the following pagination parameters are supported:

- `page`: Zero-based page number (default: 0)
- `size`: Number of items per page (default: 20)
- `sort`: Sorting criteria in the format: property,(asc|desc)

Response format for paginated results:

```json
{
  "content": [
    // array of items
  ],
  "totalElements": 100,
  "totalPages": 5,
  "page": 0,
  "size": 20,
  "sort": "date,desc"
}
```

## Rate Limiting

The API implements rate limiting with the following defaults:

- 100 requests per minute per IP address
- 1000 requests per hour per user

Rate limit headers included in responses:
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1620000000
```

## Versioning

The API is versioned through the URL path:
- Current version: `/api/v1`
- Future versions: `/api/v2`, etc.

Major version changes will be communicated well in advance and will maintain backward compatibility for a specified period.