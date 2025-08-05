# Service Package

## Overview
The `com.example.authorizationApp.service` package contains business logic services for the authorization application. This package implements core functionality for user authentication, JWT token management, and integration with Spring Security framework.

## Package Structure
```
com.example.authorizationApp.service/
├── CustomizedUserDetailsService.java
├── JWTService.java
└── UsersService.java
```

## Services

### CustomizedUserDetailsService
Custom implementation of Spring Security's `UserDetailsService` interface for loading user-specific data during authentication.

**Key Features:**
- Implements Spring Security's authentication mechanism
- Loads user details from database during login process
- Converts domain user objects to Spring Security UserDetails
- Handles user not found exceptions

**Methods:**
- `loadUserByUsername(String username)` - Retrieves user by username and wraps in UserPrincipal

**Dependencies:**
- `UserRepo` - Repository for database user operations
- `Users` - Domain model for user entities
- `UserPrincipal` - Custom UserDetails implementation

**Integration Points:**
- Spring Security authentication process
- Database user lookup via repository layer

### JWTService
Comprehensive service for JSON Web Token (JWT) operations including generation, validation, and claim extraction.

**Key Features:**
- JWT token generation with configurable expiration (10 hours default)
- Token validation against user details and expiration
- Secure token signing using HMAC-SHA algorithm
- Claim extraction utilities

**Configuration:**
- `jwt.secret` - Secret key from application properties for token signing

**Methods:**
- `generateToken(String username)` - Creates JWT token for authenticated user
- `validateToken(String token, UserDetails userDetails)` - Validates token authenticity and expiration
- `extractUsername(String token)` - Extracts username from token claims
- `isTokenExpired(String token)` - Checks token expiration status

**Security Features:**
- HMAC-SHA256 signing algorithm
- Configurable token expiration
- Secure key generation from secret
- Comprehensive token validation

### UsersService
Main business logic service for user operations including registration and authentication.

**Key Features:**
- User registration with BCrypt password encryption
- User authentication with JWT token generation
- Integration with Spring Security authentication manager
- Secure password hashing with strength 12

**Methods:**
- `registerUser(Users user)` - Registers new user with encrypted password
- `verify(Users user)` - Authenticates user and returns JWT token

**Security Implementation:**
- BCrypt password encoder with strength 12
- Spring Security AuthenticationManager integration
- JWT token generation upon successful authentication

**Dependencies:**
- `UserRepo` - Repository for user persistence
- `JWTService` - JWT token operations
- `AuthenticationManager` - Spring Security authentication
- `BCryptPasswordEncoder` - Password encryption

## Security Architecture

### Password Security
- **Encryption Algorithm**: BCrypt with strength 12
- **Storage**: Only encrypted passwords stored in database
- **Validation**: Automatic through Spring Security's authentication process

### JWT Token Security
- **Algorithm**: HMAC-SHA256 for token signing
- **Expiration**: 10 hours from generation
- **Claims**: Username as subject, issued at timestamp
- **Validation**: Username matching and expiration checking

### Authentication Flow
1. User credentials received via UsersService
2. Spring Security AuthenticationManager validates credentials
3. CustomizedUserDetailsService loads user details from database
4. Upon successful authentication, JWT token generated
5. Token returned to client for subsequent requests

## Configuration Requirements

### Application Properties
```properties
# JWT secret key for token signing
jwt.secret=your-secret-key-here
```

### Spring Security Integration
- CustomizedUserDetailsService must be registered with Spring Security
- AuthenticationManager bean configuration required
- JWT filter integration for token-based authentication

## Error Handling
- **UsernameNotFoundException**: Thrown when user not found during authentication
- **Authentication failures**: Handled by returning "Fail" response
- **Token validation errors**: Boolean responses for validation methods

## Usage Examples

### User Registration
```java
@Autowired
private UsersService usersService;

public void registerUser() {
    Users user = new Users();
    user.setUsername("testuser");
    user.setPassword("plainpassword");
    
    Users registered = usersService.registerUser(user);
    // Password is now encrypted and user is saved
}
```

### User Authentication
```java
public void authenticateUser() {
    Users user = new Users();
    user.setUsername("testuser");
    user.setPassword("plainpassword");
    
    String token = usersService.verify(user);
    // Returns JWT token on success, "Fail" on failure
}
```

### JWT Token Operations
```java
@Autowired
private JWTService jwtService;

public void tokenOperations() {
    // Generate token
    String token = jwtService.generateToken("testuser");
    
    // Extract username
    String username = jwtService.extractUsername(token);
    
    // Validate token
    boolean isValid = jwtService.validateToken(token, userDetails);
}
```

## Performance Considerations
- BCrypt strength 12 provides strong security but requires computational resources
- JWT tokens are stateless, reducing server-side session storage needs
- Token expiration of 10 hours balances security and user experience

## Development Notes
- Ensure secret key is properly configured and kept secure
- Consider implementing token refresh mechanism for longer sessions
- Monitor authentication performance with BCrypt strength settings
- Implement proper logging for security events