# API Design Document

This document outlines the RESTful API design for the Node.js backend that will replace the current Ruby on Rails application.

## API Standards

### Base URL
`/api/v1/`

### Authentication
- JWT-based authentication
- Tokens should be included in the Authorization header: `Authorization: Bearer <token>`
- Access tokens expire after 1 hour
- Refresh tokens for obtaining new access tokens

### Response Format
All API responses will follow this general format:

```json
{
  "success": true,
  "data": {},
  "message": "",
  "errors": []
}
```

### Error Handling
Error responses use appropriate HTTP status codes and include descriptive messages:

```json
{
  "success": false,
  "message": "Error description",
  "errors": [
    {
      "field": "field_name",
      "message": "Validation error message"
    }
  ]
}
```

### Common Status Codes
- 200: Success
- 201: Created
- 400: Bad Request
- 401: Unauthorized
- 403: Forbidden
- 404: Not Found
- 422: Unprocessable Entity
- 500: Server Error

## API Endpoints

### Authentication

#### Register User
- **POST** `/api/v1/auth/register`
- **Request Body**:
  ```json
  {
    "login": "username",
    "email": "user@example.com",
    "password": "password",
    "name": "User Name"
  }
  ```
- **Response**: User object with token

#### Login
- **POST** `/api/v1/auth/login`
- **Request Body**:
  ```json
  {
    "login": "username",
    "password": "password"
  }
  ```
- **Response**: JWT tokens and user info

#### Refresh Token
- **POST** `/api/v1/auth/refresh`
- **Request Body**:
  ```json
  {
    "refreshToken": "valid_refresh_token"
  }
  ```
- **Response**: New JWT tokens

#### Logout
- **POST** `/api/v1/auth/logout`
- **Response**: Confirmation message

### Users

#### Get Current User
- **GET** `/api/v1/users/me`
- **Response**: Current user data

#### Update User
- **PUT** `/api/v1/users/me`
- **Request Body**: User data
- **Response**: Updated user data

### Categories

#### List Categories
- **GET** `/api/v1/categories`
- **Query Parameters**:
  - `type`: Filter by category type
  - `includeSubcategories`: Include subcategories (boolean)
- **Response**: Array of categories

#### Get Category
- **GET** `/api/v1/categories/:id`
- **Response**: Category details

#### Create Category
- **POST** `/api/v1/categories`
- **Request Body**:
  ```json
  {
    "name": "Category Name",
    "description": "Description",
    "categoryType": "EXPENSE",
    "parentId": null,
    "openingBalance": {
      "value": 100.00,
      "currencyId": 1
    }
  }
  ```
- **Response**: Created category

#### Update Category
- **PUT** `/api/v1/categories/:id`
- **Request Body**: Category data
- **Response**: Updated category

#### Delete Category
- **DELETE** `/api/v1/categories/:id`
- **Response**: Success confirmation

#### Get Category Balance
- **GET** `/api/v1/categories/:id/balance`
- **Query Parameters**:
  - `algorithm`: Balance calculation algorithm
  - `includeSubcategories`: Include subcategories (boolean)
  - `startDate`: Start date for period
  - `endDate`: End date for period
- **Response**: Balance information

### Transfers

#### List Transfers
- **GET** `/api/v1/transfers`
- **Query Parameters**:
  - `categoryId`: Filter by category
  - `startDate`: Start date
  - `endDate`: End date
  - `limit`: Limit results
  - `offset`: Pagination offset
- **Response**: Array of transfers with pagination

#### Get Transfer
- **GET** `/api/v1/transfers/:id`
- **Response**: Transfer details with items

#### Create Transfer
- **POST** `/api/v1/transfers`
- **Request Body**:
  ```json
  {
    "description": "Transfer description",
    "day": "2023-01-15",
    "transferItems": [
      {
        "description": "Item description",
        "categoryId": 1,
        "currencyId": 1,
        "value": 100.00
      },
      {
        "description": "Item description",
        "categoryId": 2,
        "currencyId": 1,
        "value": -100.00
      }
    ],
    "conversions": []
  }
  ```
- **Response**: Created transfer with items

#### Update Transfer
- **PUT** `/api/v1/transfers/:id`
- **Request Body**: Transfer data
- **Response**: Updated transfer

#### Delete Transfer
- **DELETE** `/api/v1/transfers/:id`
- **Response**: Success confirmation

### Currencies

#### List Currencies
- **GET** `/api/v1/currencies`
- **Response**: Array of currencies

#### Get Currency
- **GET** `/api/v1/currencies/:id`
- **Response**: Currency details

#### Create Currency
- **POST** `/api/v1/currencies`
- **Request Body**:
  ```json
  {
    "name": "Currency name",
    "longSymbol": "USD",
    "shortSymbol": "$"
  }
  ```
- **Response**: Created currency

#### Update Currency
- **PUT** `/api/v1/currencies/:id`
- **Request Body**: Currency data
- **Response**: Updated currency

#### Delete Currency
- **DELETE** `/api/v1/currencies/:id`
- **Response**: Success confirmation

### Exchanges

#### List Exchanges
- **GET** `/api/v1/exchanges`
- **Query Parameters**:
  - `leftCurrencyId`: Left currency ID
  - `rightCurrencyId`: Right currency ID
- **Response**: Array of exchanges

#### Get Exchange
- **GET** `/api/v1/exchanges/:id`
- **Response**: Exchange details

#### Create Exchange
- **POST** `/api/v1/exchanges`
- **Request Body**:
  ```json
  {
    "leftCurrencyId": 1,
    "rightCurrencyId": 2,
    "leftToRight": 0.82,
    "rightToLeft": 1.22,
    "day": "2023-01-15"
  }
  ```
- **Response**: Created exchange

#### Update Exchange
- **PUT** `/api/v1/exchanges/:id`
- **Request Body**: Exchange data
- **Response**: Updated exchange

#### Delete Exchange
- **DELETE** `/api/v1/exchanges/:id`
- **Response**: Success confirmation

### Goals

#### List Goals
- **GET** `/api/v1/goals`
- **Response**: Array of goals

#### Get Goal
- **GET** `/api/v1/goals/:id`
- **Response**: Goal details

#### Create Goal
- **POST** `/api/v1/goals`
- **Request Body**:
  ```json
  {
    "name": "Goal name",
    "description": "Goal description",
    "categoryId": 1,
    "currencyId": 1,
    "value": 1000.00,
    "startDate": "2023-01-15",
    "endDate": "2023-12-31",
    "cyclic": false,
    "periodType": "MONTHLY"
  }
  ```
- **Response**: Created goal

#### Update Goal
- **PUT** `/api/v1/goals/:id`
- **Request Body**: Goal data
- **Response**: Updated goal

#### Delete Goal
- **DELETE** `/api/v1/goals/:id`
- **Response**: Success confirmation

### Reports

#### List Reports
- **GET** `/api/v1/reports`
- **Response**: Array of reports

#### Get Report
- **GET** `/api/v1/reports/:id`
- **Response**: Report details

#### Create Report
- **POST** `/api/v1/reports`
- **Request Body**:
  ```json
  {
    "name": "Report name",
    "type": "FLOW_REPORT",
    "categoryId": 1,
    "periodType": "THIS_MONTH",
    "startDate": "2023-01-01",
    "endDate": "2023-01-31",
    "temporary": false,
    "options": {
      // Report-specific options
    }
  }
  ```
- **Response**: Created report

#### Update Report
- **PUT** `/api/v1/reports/:id`
- **Request Body**: Report data
- **Response**: Updated report

#### Delete Report
- **DELETE** `/api/v1/reports/:id`
- **Response**: Success confirmation

#### Generate Report Data
- **GET** `/api/v1/reports/:id/generate`
- **Response**: Generated report data

### Import

#### Upload Import File
- **POST** `/api/v1/import/upload`
- **Request**: Multipart form data with file
- **Response**: Upload confirmation and import ID

#### Parse Import File
- **POST** `/api/v1/import/parse/:importId`
- **Request Body**: Parse configuration
- **Response**: Parsed data

#### Execute Import
- **POST** `/api/v1/import/execute/:importId`
- **Request Body**: Import confirmation data
- **Response**: Import results

#### Get Import Status
- **GET** `/api/v1/import/:importId/status`
- **Response**: Import status information

## Data Models

### User
```json
{
  "id": 1,
  "login": "username",
  "name": "User Name",
  "email": "user@example.com",
  "transactionAmountLimitType": "THIS_MONTH",
  "transactionAmountLimitValue": 50,
  "includeTransactionsFromSubcategories": true,
  "multiCurrencyBalanceCalculatingAlgorithm": "SHOW_ALL_CURRENCIES",
  "defaultCurrencyId": 1,
  "invertSaldoForIncome": true,
  "createdAt": "2023-01-15T12:00:00Z",
  "updatedAt": "2023-01-15T12:00:00Z"
}
```

### Category
```json
{
  "id": 1,
  "name": "Category Name",
  "description": "Description",
  "categoryType": "EXPENSE",
  "parentId": null,
  "lft": 1,
  "rgt": 2,
  "level": 0,
  "bankAccountNumber": null,
  "email": null,
  "loanCategory": false,
  "createdAt": "2023-01-15T12:00:00Z",
  "updatedAt": "2023-01-15T12:00:00Z",
  "systemCategoryIds": [1, 2]
}
```

### Transfer
```json
{
  "id": 1,
  "description": "Transfer description",
  "day": "2023-01-15",
  "userId": 1,
  "importGuid": null,
  "createdAt": "2023-01-15T12:00:00Z",
  "updatedAt": "2023-01-15T12:00:00Z",
  "transferItems": [
    {
      "id": 1,
      "transferId": 1,
      "description": "Item description",
      "categoryId": 1,
      "currencyId": 1,
      "value": 100.00
    }
  ],
  "conversions": [
    {
      "id": 1,
      "transferId": 1,
      "exchangeId": 1
    }
  ]
}
```

### Currency
```json
{
  "id": 1,
  "name": "US Dollar",
  "longSymbol": "USD",
  "shortSymbol": "$",
  "userId": 1,
  "createdAt": "2023-01-15T12:00:00Z",
  "updatedAt": "2023-01-15T12:00:00Z"
}
```

### Exchange
```json
{
  "id": 1,
  "day": "2023-01-15",
  "leftCurrencyId": 1,
  "rightCurrencyId": 2,
  "leftToRight": 0.82,
  "rightToLeft": 1.22,
  "userId": 1,
  "createdAt": "2023-01-15T12:00:00Z",
  "updatedAt": "2023-01-15T12:00:00Z"
}
```

### Goal
```json
{
  "id": 1,
  "name": "Goal name",
  "description": "Goal description",
  "categoryId": 1,
  "userId": 1,
  "currencyId": 1,
  "value": 1000.00,
  "startDate": "2023-01-15",
  "endDate": "2023-12-31",
  "cyclic": false,
  "periodType": "MONTHLY",
  "createdAt": "2023-01-15T12:00:00Z",
  "updatedAt": "2023-01-15T12:00:00Z"
}
```

### Report
```json
{
  "id": 1,
  "name": "Report name",
  "type": "FLOW_REPORT",
  "userId": 1,
  "categoryId": 1,
  "periodType": "THIS_MONTH",
  "startDate": "2023-01-01",
  "endDate": "2023-01-31",
  "temporary": false,
  "relativePeriodsCount": null,
  "createdAt": "2023-01-15T12:00:00Z",
  "updatedAt": "2023-01-15T12:00:00Z",
  "categoryReportOptions": [
    {
      "id": 1,
      "reportId": 1,
      "categoryId": 2
    }
  ]
}
```

## Security Considerations

1. **Authentication**:
   - Use secure JWT implementation
   - Implement token expiration and refresh
   - Store tokens securely on client side

2. **Authorization**:
   - Implement middleware to verify resource ownership
   - Use role-based access control for admin functions
   - Apply proper validation on all incoming data

3. **Data Protection**:
   - Encrypt sensitive data in transit (HTTPS)
   - Hash passwords with strong algorithms
   - Validate and sanitize all user inputs

4. **API Rate Limiting**:
   - Implement rate limiting to prevent abuse
   - Add request throttling for sensitive operations

5. **CORS Configuration**:
   - Restrict allowed origins
   - Configure appropriate CORS headers

## Implementation Notes

1. **Node.js Framework**:
   - Consider Express.js with TypeScript for implementation
   - Use middleware architecture for cross-cutting concerns

2. **ORM Layer**:
   - Use Sequelize or TypeORM for database interactions
   - Implement proper transaction management

3. **Validation**:
   - Use Joi or class-validator for request validation
   - Implement consistent validation error responses

4. **Documentation**:
   - Generate API documentation using OpenAPI/Swagger
   - Include example requests and responses

5. **Testing**:
   - Create comprehensive test suite for all endpoints
   - Use mock objects for external dependencies
   - Implement integration tests for critical paths