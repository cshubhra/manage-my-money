# Transfer Sequence Diagrams

## Ruby on Rails Transfer Creation Flow

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant TransfersController
    participant Transfer
    participant TransferItem
    participant Category
    participant Currency
    participant DB as Database

    User->>Browser: Fill transfer form
    Browser->>TransfersController: POST /transfers
    TransfersController->>TransfersController: process transfer params
    TransfersController->>Category: find source/dest categories
    Category->>DB: query categories
    DB-->>Category: return categories
    Category-->>TransfersController: categories
    
    TransfersController->>Currency: find currency
    Currency->>DB: query currency
    DB-->>Currency: return currency
    Currency-->>TransfersController: currency
    
    TransfersController->>Transfer: new(transfer_params)
    Transfer->>TransferItem: build items from params
    TransferItem-->>Transfer: items created
    Transfer->>DB: save transfer with items
    
    alt Save successful
        DB-->>Transfer: success
        Transfer-->>TransfersController: transfer saved
        TransfersController-->>Browser: Redirect to transfer show page
        Browser-->>User: Display transfer details
    else Validation errors
        DB-->>Transfer: validation errors
        Transfer-->>TransfersController: errors
        TransfersController-->>Browser: Render form with errors
        Browser-->>User: Display errors
    end
```

## Angular/Spring Boot Transfer Creation Flow

```mermaid
sequenceDiagram
    participant User
    participant AngularUI
    participant TransferService
    participant HttpInterceptor
    participant TransferController
    participant TransferService as BackendTransferService
    participant CategoryService
    participant CurrencyService
    participant DB as Database

    User->>AngularUI: Fill transfer form
    AngularUI->>TransferService: createTransfer(transferData)
    TransferService->>HttpInterceptor: POST /api/transfers
    HttpInterceptor->>TransferController: POST with JWT Auth
    
    TransferController->>CategoryService: validateCategories(sourceId, destId)
    CategoryService->>DB: query categories
    DB-->>CategoryService: return categories
    CategoryService-->>TransferController: validated categories
    
    TransferController->>CurrencyService: validateCurrency(currencyId)
    CurrencyService->>DB: query currency
    DB-->>CurrencyService: return currency
    CurrencyService-->>TransferController: validated currency
    
    TransferController->>BackendTransferService: createTransfer(transferDTO)
    BackendTransferService->>BackendTransferService: map DTO to entities
    BackendTransferService->>DB: save transfer and items
    
    alt Save successful
        DB-->>BackendTransferService: success
        BackendTransferService-->>TransferController: created transfer
        TransferController-->>HttpInterceptor: 201 Created with transfer
        HttpInterceptor-->>TransferService: transfer data
        TransferService-->>AngularUI: transfer created success
        AngularUI-->>User: Display success and transfer details
    else Validation errors
        DB-->>BackendTransferService: validation errors
        BackendTransferService-->>TransferController: error details
        TransferController-->>HttpInterceptor: 400 Bad Request with errors
        HttpInterceptor-->>TransferService: error response
        TransferService-->>AngularUI: validation errors
        AngularUI-->>User: Display form with errors
    end
```

## Ruby on Rails Transfer Listing Flow

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant TransfersController
    participant Transfer
    participant DB as Database

    User->>Browser: Navigate to transfers page
    Browser->>TransfersController: GET /transfers
    TransfersController->>Transfer: find user's transfers (with pagination)
    Transfer->>DB: query transfers
    DB-->>Transfer: return transfers
    Transfer-->>TransfersController: transfers collection
    TransfersController-->>Browser: Render transfers/index with transfers
    Browser-->>User: Display transfers list
    
    User->>Browser: Click on transfer details
    Browser->>TransfersController: GET /transfers/:id
    TransfersController->>Transfer: find(id)
    Transfer->>DB: query transfer with items
    DB-->>Transfer: return transfer with items
    Transfer-->>TransfersController: transfer with items
    TransfersController-->>Browser: Render transfers/show
    Browser-->>User: Display transfer details
```

## Angular/Spring Boot Transfer Listing Flow

```mermaid
sequenceDiagram
    participant User
    participant AngularUI
    participant TransferService
    participant HttpInterceptor
    participant TransferController
    participant BackendTransferService
    participant DB as Database

    User->>AngularUI: Navigate to transfers page
    AngularUI->>TransferService: getTransfers(page, size)
    TransferService->>HttpInterceptor: GET /api/transfers?page=0&size=10
    HttpInterceptor->>TransferController: GET with JWT Auth
    TransferController->>BackendTransferService: findTransfers(pageable)
    BackendTransferService->>DB: query transfers
    DB-->>BackendTransferService: return transfers
    BackendTransferService-->>TransferController: page of transfers
    TransferController-->>HttpInterceptor: 200 OK with transfers page
    HttpInterceptor-->>TransferService: transfers data
    TransferService-->>AngularUI: transfers list
    AngularUI-->>User: Display transfers table
    
    User->>AngularUI: Click on transfer details
    AngularUI->>TransferService: getTransfer(id)
    TransferService->>HttpInterceptor: GET /api/transfers/{id}
    HttpInterceptor->>TransferController: GET with JWT Auth
    TransferController->>BackendTransferService: findTransferById(id)
    BackendTransferService->>DB: query transfer with items
    DB-->>BackendTransferService: return transfer with items
    BackendTransferService-->>TransferController: transfer details
    TransferController-->>HttpInterceptor: 200 OK with transfer
    HttpInterceptor-->>TransferService: transfer data
    TransferService-->>AngularUI: transfer details
    AngularUI-->>User: Display transfer details view
```

These sequence diagrams illustrate the differences between how transfers are created and retrieved in the Ruby on Rails application versus the new Angular/Spring Boot architecture. The key differences include:

1. Clear separation between frontend (Angular) and backend (Spring Boot) in the new architecture
2. RESTful API communication with HTTP verbs and status codes in the Angular/Spring Boot version
3. JWT authentication through HTTP interceptors in the Angular/Spring Boot flow
4. More explicit error handling in the Angular/Spring Boot architecture
5. Clearer separation of concerns with dedicated services in the Spring Boot backend