# Controllers Package

## Overview
The `com.example.authorizationApp.controller` package contains REST controllers that handle HTTP requests for the authorization application. This package serves as the presentation layer, providing endpoints for user authentication, student management, and basic session handling.

## Package Structure
```
com.example.authorizationApp.controller/
├── HelloController.java
├── StudentController.java
└── UsersController.java
```

## Controllers

### HelloController
A simple REST controller for testing session management and basic connectivity.

**Endpoints:**
- `GET /hello` - Returns a greeting message with the current session ID

**Features:**
- Session ID retrieval for testing purposes
- Basic HTTP request handling demonstration

### StudentController
Manages student-related operations with CRUD functionality and security features.

**Endpoints:**
- `GET /students` - Retrieves all students from the system
- `POST /students` - Creates a new student record
- `GET /csrf-token` - Retrieves CSRF token for secure form submissions

**Features:**
- In-memory student storage (List-based)
- CSRF token management for security
- RESTful API design for student operations
- Complete CRUD operations for student entities

**Dependencies:**
- `Student` model class
- Spring Security (for CSRF token handling)

### UsersController
Handles user authentication and registration operations.

**Endpoints:**
- `POST /register` - Registers a new user in the system
- `POST /login` - Authenticates user credentials and returns verification result

**Features:**
- User registration processing
- User authentication and verification
- Integration with service layer for business logic

**Dependencies:**
- `Users` model class
- `UsersService` for business logic operations

## Security Considerations

### CSRF Protection
The `StudentController` includes CSRF token retrieval functionality to protect against Cross-Site Request Forgery attacks. The `/csrf-token` endpoint provides the necessary token for secure form submissions.

### Session Management
The `HelloController` demonstrates basic session handling, useful for testing session-based authentication mechanisms.

## Data Storage
- **StudentController**: Uses in-memory storage (`ArrayList`) for student records
- **UsersController**: Delegates data operations to the service layer

## HTTP Methods Used
- **GET**: For retrieving data (students, session info, CSRF tokens)
- **POST**: For creating resources (students, user registration/login)

## Response Types
- JSON objects for student and user data
- Plain text for session information
- CSRF token objects for security

## Usage Examples

### Testing Session
```bash
curl -X GET http://localhost:8080/hello
```

### Retrieving Students
```bash
curl -X GET http://localhost:8080/students
```

### Creating a Student
```bash
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com"}'
```

### User Registration
```bash
curl -X POST http://localhost:8080/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user123","password":"password"}'
```

## Notes
- The StudentController uses in-memory storage, which means data will be lost on application restart
- Proper error handling and validation should be implemented for production use
- CSRF protection is implemented for enhanced security
- Controllers follow RESTful design principles