# SSO Authentication Guide

## Overview
The Prophecy Web Testing Framework now supports comprehensive Single Sign-On (SSO) authentication including Okta and ADFS, with intelligent detection and fallback capabilities.

## Authentication Methods

### 1. Smart Login (Recommended)
Automatically detects the authentication type and handles the login process accordingly.

```gherkin
When I login with username "user@company.com" and password "password123"
```

**How it works:**
1. Checks for SSO login buttons/links on the page
2. If SSO is detected, clicks the SSO button and waits for redirect
3. Detects if redirected to Okta or ADFS login page
4. Uses appropriate authentication method
5. Falls back to traditional login if no SSO is detected

### 2. Okta SSO Authentication
Direct Okta authentication for environments using Okta SSO.

```gherkin
When I login with Okta using username "user@company.com" and password "password123"
```

**Process:**
1. Clicks SSO login button
2. Waits for Okta login page
3. Enters username and password in Okta form
4. Clicks Okta sign-in button

### 3. ADFS SSO Authentication
Direct ADFS authentication for environments using ADFS SSO.

```gherkin
When I login with ADFS using username "user@company.com" and password "password123"
```

**Process:**
1. Clicks SSO login button
2. Waits for ADFS login page
3. Enters username and password in ADFS form
4. Clicks ADFS submit button

### 4. Traditional Login (Fallback)
Standard email/password authentication for non-SSO environments.

```gherkin
# This is handled automatically by smart login when no SSO is detected
```

## Element Detection Strategy

The framework uses multiple locator strategies to handle different SSO implementations:

### SSO Button Detection
```java
// Multiple patterns to detect SSO buttons
"//button[contains(text(), 'SSO') or contains(text(), 'Single Sign') or contains(@class, 'sso')]"
"//a[contains(text(), 'SSO') or contains(text(), 'Single Sign') or contains(@class, 'sso')]"
```

### Okta Form Detection
```java
// Okta-specific elements
"//input[contains(@id, 'okta-signin-username') or contains(@name, 'username')]"
"//input[contains(@id, 'okta-signin-password') or contains(@name, 'password')]"
"//input[@type='submit' and contains(@value, 'Sign In')]"
```

### ADFS Form Detection
```java
// ADFS-specific elements
"//input[contains(@id, 'userNameInput') or contains(@name, 'UserName')]"
"//input[contains(@id, 'passwordInput') or contains(@name, 'Password')]"
"//span[@id='submitButton'] | //input[@type='submit']"
```

### Generic Form Detection
```java
// Fallback for generic SSO forms
"//input[@type='text' or @type='email']"
"//input[@type='password']"
"//button[@type='submit'] | //input[@type='submit']"
```

## Test Scenarios

### Basic SSO Test
```gherkin
@WebTest @Login @SSO
Scenario: Successful login with smart SSO detection
  Given I navigate to Prophecy login page "https://app.prophecy.io"
  When I login with username "test.user@company.com" and password "password123"
  Then I should be logged in successfully
  And I should see the dashboard
```

### Okta-Specific Test
```gherkin
@WebTest @Login @SSO @Okta
Scenario: Successful login with Okta SSO
  Given I navigate to Prophecy login page "https://app.prophecy.io"
  When I login with Okta using username "test.user@company.com" and password "password123"
  Then I should be logged in successfully
  And I should see the dashboard
```

### ADFS-Specific Test
```gherkin
@WebTest @Login @SSO @ADFS
Scenario: Successful login with ADFS SSO
  Given I navigate to Prophecy login page "https://app.prophecy.io"
  When I login with ADFS using username "test.user@company.com" and password "password123"
  Then I should be logged in successfully
  And I should see the dashboard
```

## Configuration

### Environment Variables
```properties
# Optional: Set specific SSO provider if known
prophecy.sso.provider=okta|adfs|auto
prophecy.sso.timeout=30
prophecy.login.timeout=10
```

### Test Tags
Use tags to organize and run specific authentication tests:

```bash
# Run all SSO tests
mvn test -Dcucumber.filter.tags="@SSO"

# Run only Okta tests
mvn test -Dcucumber.filter.tags="@Okta"

# Run only ADFS tests
mvn test -Dcucumber.filter.tags="@ADFS"

# Run smart login tests
mvn test -Dcucumber.filter.tags="@Login and not @SSO"
```

## Error Handling

The framework includes comprehensive error handling:

1. **Element Not Found**: Tries multiple locator strategies
2. **Timeout Issues**: Configurable timeouts for different authentication steps
3. **Redirect Failures**: Waits for page loads and validates URLs
4. **Authentication Failures**: Captures error messages and screenshots

## Troubleshooting

### Common Issues

1. **SSO Button Not Found**
   - Check if the page has loaded completely
   - Verify the SSO button text/class matches expected patterns
   - Use browser developer tools to inspect the actual elements

2. **Authentication Timeout**
   - Increase timeout values in configuration
   - Check network connectivity to SSO provider
   - Verify credentials are correct

3. **Page Not Redirecting**
   - Ensure JavaScript is enabled
   - Check for popup blockers
   - Verify the SSO configuration on the server side

### Debug Mode
Enable debug logging to see detailed authentication flow:

```properties
logging.level.com.prophecy.testing.pages.ProphecyLoginPage=DEBUG
```

## Best Practices

1. **Use Smart Login**: Always use the smart login method unless you need to test specific SSO providers
2. **Tag Tests Appropriately**: Use proper tags for test organization and selective execution
3. **Handle Timeouts**: Set appropriate timeouts based on your SSO provider's response time
4. **Validate Success**: Always verify login success and dashboard loading
5. **Error Screenshots**: Capture screenshots on authentication failures for debugging

## API Reference

### ProphecyLoginPage Methods

```java
// Smart login (recommended)
public ProphecyDashboardPage smartLogin(String username, String password)

// Specific SSO methods
public ProphecyDashboardPage loginWithOkta(String username, String password)
public ProphecyDashboardPage loginWithADFS(String username, String password)

// Traditional login (fallback)
public ProphecyDashboardPage login(String email, String password)

// Individual steps
public ProphecyLoginPage clickSSOLogin()
public ProphecyLoginPage enterOktaUsername(String username)
public ProphecyLoginPage enterOktaPassword(String password)
public ProphecyDashboardPage clickOktaSignIn()
```

### Step Definitions

```java
@When("I login with username {string} and password {string}")
@When("I login with Okta using username {string} and password {string}")
@When("I login with ADFS using username {string} and password {string}")
@When("I click SSO login button")
```