# Filter Package

## Overview
The `com.example.authorizationApp.filter` package contains custom servlet filters that handle request processing for authentication and authorization. This package implements JWT token validation and authentication integration with Spring Security's filter chain.

## Package Structure
```
com.example.authorizationApp.filter/
└── JwtFilter.java
```

## Filter Components

### JwtFilter
A custom authentication filter that intercepts HTTP requests to validate JWT tokens and establish user authentication context.

**Inheritance:**
- Extends `OncePerRequestFilter` - Ensures filter execution only once per request
- Implements Spring Security filter chain integration

**Key Responsibilities:**
- JWT token extraction from HTTP headers
- Token validation and user authentication
- Security context establishment
- Authentication details management

## JWT Authentication Flow

### Request Processing Pipeline
1. **Header Extraction** - Retrieves `Authorization` header from incoming request
2. **Token Validation** - Checks for `Bearer` prefix and extracts JWT token
3. **Username Extraction** - Extracts username from token claims
4. **User Loading** - Retrieves user details from database
5. **Token Verification** - Validates token against user details and expiration
6. **Authentication Setup** - Creates and sets authentication in SecurityContext
7. **Filter Chain Continuation** - Passes control to next filter

### Authentication Header Format
```
Authorization: Bearer <JWT_TOKEN>
```

## Implementation Details

### Token Processing Logic
```java
// Header validation
if (authHeader != null && authHeader.startsWith("Bearer ")) {
    // Token extraction (removes "Bearer " prefix)
    String token = authHeader.substring(7);
    
    // Username extraction from token claims
    String username = jwtService.extractUsername(token);
}
```

### Security Context Management
- **Authentication Check** - Verifies no existing authentication in SecurityContext
- **User Details Loading** - Retrieves complete user information from database
- **Token Validation** - Ensures token integrity and expiration status
- **Context Setting** - Establishes authenticated user context for request

### Authentication Token Creation
```java
UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
    userDetails,           // Principal (authenticated user)
    null,                  // Credentials (not needed after authentication)
    userDetails.getAuthorities()  // User authorities/roles
);
```

## Dependencies

### Service Dependencies
- `JWTService` - JWT token operations (validation, username extraction)
- `CustomizedUserDetailsService` - User details loading from database

### Spring Security Integration
- `SecurityContextHolder` - Authentication context management
- `WebAuthenticationDetailsSource` - Request details extraction
- `UsernamePasswordAuthenticationToken` - Authentication object creation

## Filter Configuration

### Component Registration
- Annotated with `@Component` for Spring auto-detection
- Automatically registered in Spring application context
- Integrated into Security Filter Chain via SecurityConfig

### Filter Chain Position
- Positioned **before** `UsernamePasswordAuthenticationFilter`
- Executes early in the authentication process
- Allows JWT authentication to take precedence

## Security Features

### Token Security
- **Bearer Token Standard** - Follows RFC 6750 specification
- **Header-Based Authentication** - Stateless token transmission
- **Token Validation** - Comprehensive token integrity checking
- **Expiration Handling** - Automatic token expiration validation

### Authentication Security
- **Single Execution** - OncePerRequestFilter prevents duplicate processing
- **Context Isolation** - Each request gets independent authentication context
- **Authority Preservation** - User roles and permissions maintained
- **Details Enrichment** - Request-specific authentication details added

## Error Handling

### Invalid Token Scenarios
- **Missing Authorization Header** - Request proceeds without authentication
- **Invalid Bearer Format** - Request proceeds without authentication
- **Expired Token** - Authentication fails, request requires re-authentication
- **Invalid Token Signature** - Token validation fails silently

### Exception Management
- **ServletException** - Handled by filter chain
- **IOException** - Propagated to container
- **Authentication Failures** - Silent failure, allows other authentication methods

## Usage Examples

### Valid JWT Request
```bash
curl -X GET http://localhost:8080/students \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Request Without Token
```bash
curl -X GET http://localhost:8080/students
# Proceeds to other authentication methods or returns 401
```

### Invalid Token Format
```bash
curl -X GET http://localhost:8080/students \
  -H "Authorization: InvalidTokenFormat"
# Filter passes request without setting authentication
```

## Performance Considerations

### Optimization Features
- **Single Execution** - OncePerRequestFilter prevents redundant processing
- **Early Exit** - Quick validation for missing or invalid headers
- **Context Caching** - SecurityContext maintains authentication throughout request

### Database Impact
- **User Details Loading** - One database query per authenticated request
- **Caching Opportunity** - Consider implementing user details caching
- **Connection Pooling** - Ensure proper database connection management

## Development Guidelines

### Testing Considerations
```java
@Test
public void testJwtFilterWithValidToken() {
    // Mock JWT service and user details service
    // Create test request with valid Authorization header
    // Verify authentication is set in SecurityContext
}

@Test
public void testJwtFilterWithoutToken() {
    // Create request without Authorization header
    // Verify filter passes without setting authentication
}
```

### Common Issues
- **Token Prefix** - Ensure "Bearer " prefix is correctly handled
- **Case Sensitivity** - Authorization header is case-sensitive
- **Token Expiration** - Handle expired tokens gracefully
- **Database Connectivity** - Ensure user details service availability

## Integration Points

### With Security Configuration
- Registered in SecurityFilterChain before username/password filter
- Configured to process all requests except public endpoints
- Integrates with overall authentication strategy

### With JWT Service
- Relies on JWT service for token validation and username extraction
- Handles JWT service exceptions appropriately
- Maintains consistency with token generation process

### With User Details Service
- Loads complete user information for authentication context
- Handles user not found scenarios
- Maintains consistency with authentication provider

## Production Considerations

### Security Best Practices
- **Token Transmission** - Always use HTTPS in production
- **Token Storage** - Client-side secure storage recommendations
- **Token Rotation** - Implement token refresh mechanisms
- **Logging** - Avoid logging sensitive token information

### Monitoring and Debugging
- **Request Tracking** - Log authentication attempts without exposing tokens
- **Performance Monitoring** - Track filter execution time
- **Error Tracking** - Monitor authentication failures
- **Security Events** - Log suspicious authentication patterns