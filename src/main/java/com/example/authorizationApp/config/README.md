# Config Package

## Overview
The `com.example.authorizationApp.config` package contains configuration classes that define the security architecture and setup for the authorization application. This package is responsible for configuring Spring Security, authentication mechanisms, and security policies.

## Package Structure
```
com.example.authorizationApp.config/
└── SecurityConfig.java
```

## Configuration Classes

### SecurityConfig
Main security configuration class that orchestrates the entire security framework for the application using Spring Security.

**Annotations:**
- `@Configuration` - Marks this as a configuration class
- `@EnableWebSecurity` - Enables Spring Security web security features

**Key Features:**
- JWT-based authentication configuration
- OAuth2 login integration
- HTTP Basic authentication support
- Stateless session management
- CSRF protection management
- Custom authentication provider setup

## Security Architecture

### Authentication Methods Supported
1. **JWT Token Authentication** - Primary authentication method using JSON Web Tokens
2. **OAuth2 Login** - External OAuth2 provider integration
3. **HTTP Basic Authentication** - Traditional username/password authentication

### Security Filter Chain Configuration

#### Public Endpoints
- `/register` - User registration endpoint (no authentication required)
- `/login` - User login endpoint (no authentication required)

#### Protected Resources
- All other endpoints require authentication
- JWT token validation for API access
- Stateless session management

#### Security Policies
- **CSRF Protection**: Disabled (suitable for API-first applications)
- **Session Management**: Stateless (SessionCreationPolicy.STATELESS)
- **CORS**: Not explicitly configured (default behavior)

### Authentication Provider Configuration

**DaoAuthenticationProvider Setup:**
- Uses custom `UserDetailsService` for user loading
- BCrypt password encoding with strength 12
- Database-backed user authentication

**Password Security:**
- Algorithm: BCrypt
- Strength: 12 (high security level)
- Automatic password verification during authentication

## Bean Definitions

### SecurityFilterChain Bean
Configures the complete HTTP security policy including:
- Request authorization rules
- Authentication methods
- Session management
- Filter chain ordering

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http)
```

### AuthenticationProvider Bean
Creates and configures the authentication provider:
- Links UserDetailsService for user loading
- Sets up BCrypt password encoder
- Enables DAO-based authentication

```java
@Bean
public AuthenticationProvider authenticationProvider()
```

### AuthenticationManager Bean
Provides the authentication manager for programmatic authentication:
- Used by services for user verification
- Integrates with authentication provider
- Enables custom authentication logic

```java
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
```

## Filter Chain Integration

### JWT Filter Integration
- **Position**: Before `UsernamePasswordAuthenticationFilter`
- **Purpose**: Validates JWT tokens on incoming requests
- **Dependency**: `JwtFilter` component

### Filter Execution Order
1. JWT Filter (validates tokens)
2. Username/Password Authentication Filter
3. Other Spring Security filters

## Dependencies

### Required Components
- `UserDetailsService` - Custom user details service implementation
- `JwtFilter` - JWT token validation filter
- `AuthenticationConfiguration` - Spring Security configuration

### External Dependencies
- Spring Security Web
- Spring Security Config
- Spring Security OAuth2 Client (for OAuth2 support)

## Configuration Properties

### Security Settings
- **Session Creation Policy**: Stateless
- **CSRF Protection**: Disabled
- **Authentication Required**: All endpoints except `/register` and `/login`

### Password Encoding
- **Algorithm**: BCrypt
- **Strength**: 12
- **Purpose**: Secure password storage and verification

## Usage Examples

### Custom Security Configuration
```java
@Configuration
@EnableWebSecurity
public class CustomSecurityConfig extends SecurityConfig {
    // Additional security customizations
}
```

### Accessing Authentication Manager
```java
@Autowired
private AuthenticationManager authenticationManager;

public void authenticateUser(String username, String password) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, password)
    );
}
```

## Security Best Practices Implemented

### Password Security
- Strong BCrypt hashing with high cost factor (12)
- No plain text password storage
- Secure password comparison during authentication

### Token-Based Security
- Stateless session management reduces server-side state
- JWT tokens provide secure, self-contained authentication
- Filter-based token validation for each request

### Access Control
- Clear separation between public and protected endpoints
- Default deny policy (all requests require authentication except explicitly permitted)
- Multiple authentication methods for flexibility

## Development Considerations

### Testing
- Security configuration affects all endpoints
- Test both authenticated and unauthenticated access
- Verify JWT token validation and expiration

### Performance
- Stateless sessions reduce memory usage
- BCrypt strength 12 requires computational resources
- JWT validation on every request

### Customization Points
- Add custom filters by extending filter chain
- Modify authentication providers for different user sources
- Configure CORS policies if needed for cross-origin requests

## Production Recommendations

### Security Hardening
- Enable HTTPS in production
- Configure proper CORS policies
- Implement rate limiting for authentication endpoints
- Add security headers (HSTS, X-Frame-Options, etc.)

### Monitoring
- Log authentication events
- Monitor failed login attempts
- Track JWT token usage patterns

### Scalability
- Consider JWT token refresh mechanisms
- Implement proper logout functionality
- Monitor authentication provider performance