# Authentication Sequence Diagram

## Ruby on Rails Authentication Flow

```mermaid
sequenceDiagram
    participant User as User
    participant Browser
    participant SessionsController
    participant UsersController
    participant UserModel
    participant DB as Database

    %% User Registration
    User->>Browser: Fill registration form
    Browser->>UsersController: POST /users
    UsersController->>UserModel: create(user_params)
    UserModel->>UserModel: validate user data
    UserModel->>UserModel: encrypt password
    UserModel->>DB: save user
    DB-->>UserModel: user saved
    alt New user saved successfully
        UserModel-->>UsersController: success
        UsersController-->>Browser: Redirect to login
        Browser-->>User: Show login page
    else Validation failures
        UserModel-->>UsersController: validation errors
        UsersController-->>Browser: Display registration form with errors
        Browser-->>User: Show errors
    end

    %% User Login
    User->>Browser: Fill login form
    Browser->>SessionsController: POST /sessions
    SessionsController->>UserModel: authenticate(login, password)
    UserModel->>DB: find user by login
    DB-->>UserModel: return user
    UserModel->>UserModel: verify password
    
    alt Authentication successful
        UserModel-->>SessionsController: authenticated user
        SessionsController->>SessionsController: create session
        SessionsController-->>Browser: Set session cookie & redirect
        Browser-->>User: Show dashboard
    else Authentication failed
        UserModel-->>SessionsController: authentication failure
        SessionsController-->>Browser: Display login form with error
        Browser-->>User: Show error
    end

    %% User Logout
    User->>Browser: Click logout
    Browser->>SessionsController: DELETE /sessions
    SessionsController->>SessionsController: destroy session
    SessionsController-->>Browser: Clear cookie & redirect
    Browser-->>User: Show login page
```

## Angular/Spring Boot Authentication Flow

```mermaid
sequenceDiagram
    participant User as User
    participant Angular
    participant AuthService
    participant HttpInterceptor
    participant AuthController
    participant JWTService
    participant UserService
    participant DB as Database

    %% User Registration
    User->>Angular: Fill registration form
    Angular->>AuthService: register(userData)
    AuthService->>AuthController: POST /api/auth/register
    AuthController->>UserService: createUser(userData)
    UserService->>UserService: validate user data
    UserService->>UserService: encrypt password
    UserService->>DB: save user
    DB-->>UserService: user saved
    
    alt New user saved successfully
        UserService-->>AuthController: success
        AuthController-->>AuthService: 201 Created with user details
        AuthService-->>Angular: Registration success
        Angular-->>User: Show login page or success message
    else Validation failures
        UserService-->>AuthController: validation errors
        AuthController-->>AuthService: 400 Bad Request with errors
        AuthService-->>Angular: Registration errors
        Angular-->>User: Show errors
    end

    %% User Login
    User->>Angular: Fill login form
    Angular->>AuthService: login(credentials)
    AuthService->>AuthController: POST /api/auth/login
    AuthController->>UserService: authenticate(login, password)
    UserService->>DB: find user by login
    DB-->>UserService: return user
    UserService->>UserService: verify password
    
    alt Authentication successful
        UserService-->>AuthController: authenticated user
        AuthController->>JWTService: generateToken(user)
        JWTService-->>AuthController: JWT token
        AuthController-->>AuthService: 200 OK with token and user info
        AuthService->>AuthService: store token in localStorage
        AuthService-->>Angular: Login success with user info
        Angular-->>User: Show dashboard
    else Authentication failed
        UserService-->>AuthController: authentication failure
        AuthController-->>AuthService: 401 Unauthorized
        AuthService-->>Angular: Authentication error
        Angular-->>User: Show error
    end

    %% Authenticated API Request
    User->>Angular: Request protected resource
    Angular->>HttpInterceptor: HTTP request
    HttpInterceptor->>HttpInterceptor: Add Authorization header with JWT
    HttpInterceptor->>AuthController: Request with JWT
    AuthController->>JWTService: validateToken(jwt)
    
    alt Valid token
        JWTService-->>AuthController: Token valid
        AuthController->>AuthController: Process request
        AuthController-->>HttpInterceptor: Response
        HttpInterceptor-->>Angular: Response
        Angular-->>User: Display data
    else Invalid token
        JWTService-->>AuthController: Token invalid
        AuthController-->>HttpInterceptor: 401 Unauthorized
        HttpInterceptor-->>AuthService: Token invalid notification
        AuthService->>AuthService: clear token
        AuthService-->>Angular: Logout user
        Angular-->>User: Redirect to login
    end

    %% User Logout
    User->>Angular: Click logout
    Angular->>AuthService: logout()
    AuthService->>AuthService: clear token from localStorage
    AuthService-->>Angular: Logout successful
    Angular-->>User: Redirect to login page
```

These sequence diagrams illustrate the differences in authentication flows between the Ruby on Rails application and the new Angular/Spring Boot architecture. The key differences are:

1. Token-based authentication (JWT) in Angular/Spring Boot vs session-based in Rails
2. Client-side token storage in Angular vs server-side session in Rails
3. HTTP interceptors for adding authentication headers automatically in Angular
4. More explicit error handling in the Angular/Spring Boot flow