package com.prophecy.testing.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for Prophecy Login Page with Okta/ADFS Support
 */
@Slf4j
public class ProphecyLoginPage extends BasePage {
    
    // Okta/ADFS SSO elements
    @FindBy(xpath = "//button[contains(text(), 'Sign in with Okta') or contains(text(), 'Sign in with ADFS') or contains(@class, 'okta') or contains(@class, 'sso')]")
    private WebElement ssoLoginButton;
    
    @FindBy(xpath = "//a[contains(text(), 'Sign in with Okta') or contains(text(), 'Sign in with ADFS') or contains(@href, 'okta') or contains(@href, 'adfs')]")
    private WebElement ssoLoginLink;
    
    // Okta login form elements
    @FindBy(id = "okta-signin-username")
    private WebElement oktaUsernameField;
    
    @FindBy(id = "okta-signin-password")
    private WebElement oktaPasswordField;
    
    @FindBy(id = "okta-signin-submit")
    private WebElement oktaSignInButton;
    
    // ADFS login form elements
    @FindBy(id = "userNameInput")
    private WebElement adfsUsernameField;
    
    @FindBy(id = "passwordInput")
    private WebElement adfsPasswordField;
    
    @FindBy(id = "submitButton")
    private WebElement adfsSubmitButton;
    
    // Generic SSO elements
    @FindBy(xpath = "//input[@type='text' or @type='email'][@name='username' or @name='email' or @name='user' or @id='username' or @id='email']")
    private WebElement genericUsernameField;
    
    @FindBy(xpath = "//input[@type='password'][@name='password' or @id='password']")
    private WebElement genericPasswordField;
    
    @FindBy(xpath = "//button[@type='submit'] | //input[@type='submit'] | //button[contains(text(), 'Sign') or contains(text(), 'Login')]")
    private WebElement genericSubmitButton;
    
    // Traditional login elements (fallback)
    @FindBy(id = "email")
    private WebElement emailField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(xpath = "//button[@type='submit' or contains(text(), 'Sign In') or contains(text(), 'Login')]")
    private WebElement loginButton;
    
    @FindBy(xpath = "//div[contains(@class, 'error') or contains(@class, 'alert')]")
    private WebElement errorMessage;
    
    @FindBy(xpath = "//a[contains(text(), 'Forgot') or contains(text(), 'forgot')]")
    private WebElement forgotPasswordLink;
    
    @FindBy(xpath = "//input[@type='checkbox' and contains(@name, 'remember')]")
    private WebElement rememberMeCheckbox;
    
    // Alternative locators for different login page layouts
    private final By ssoButtonLocators = By.xpath("//button[contains(text(), 'SSO') or contains(text(), 'Single Sign') or contains(@class, 'sso')] | //a[contains(text(), 'SSO') or contains(text(), 'Single Sign') or contains(@class, 'sso')]");
    private final By oktaLocators = By.xpath("//*[contains(@class, 'okta') or contains(text(), 'Okta') or contains(@href, 'okta')]");
    private final By adfsLocators = By.xpath("//*[contains(@class, 'adfs') or contains(text(), 'ADFS') or contains(@href, 'adfs')]");
    private final By emailFieldAlt = By.name("username");
    private final By passwordFieldAlt = By.name("password");
    private final By loginButtonAlt = By.cssSelector("button[type='submit'], input[type='submit']");
    
    /**
     * Navigate to Prophecy login page
     */
    public ProphecyLoginPage navigateToLoginPage(String baseUrl) {
        String loginUrl = baseUrl.endsWith("/") ? baseUrl + "login" : baseUrl + "/login";
        navigateToUrl(loginUrl);
        waitForPageToLoad();
        log.info("Navigated to Prophecy login page: {}", loginUrl);
        return this;
    }
    
    /**
     * Click SSO login button/link to initiate Okta/ADFS authentication
     */
    public ProphecyLoginPage clickSSOLogin() {
        try {
            if (isElementPresent(ssoLoginButton)) {
                clickElement(ssoLoginButton);
                log.info("Clicked SSO login button");
            } else if (isElementPresent(ssoLoginLink)) {
                clickElement(ssoLoginLink);
                log.info("Clicked SSO login link");
            } else {
                // Try to find any SSO-related button/link
                WebElement ssoElement = findElement(ssoButtonLocators);
                if (ssoElement != null) {
                    clickElement(ssoElement);
                    log.info("Clicked SSO element");
                } else {
                    log.warn("No SSO login button/link found");
                }
            }
        } catch (Exception e) {
            log.error("Failed to click SSO login: {}", e.getMessage());
            throw new RuntimeException("SSO login button not found or not clickable", e);
        }
        return this;
    }
    
    /**
     * Enter username in Okta login form
     */
    public ProphecyLoginPage enterOktaUsername(String username) {
        try {
            if (isElementPresent(oktaUsernameField)) {
                enterText(oktaUsernameField, username);
                log.info("Entered Okta username: {}", username);
            } else if (isElementPresent(genericUsernameField)) {
                enterText(genericUsernameField, username);
                log.info("Entered username in generic field: {}", username);
            } else {
                throw new RuntimeException("Okta username field not found");
            }
        } catch (Exception e) {
            log.error("Failed to enter Okta username: {}", e.getMessage());
            throw e;
        }
        return this;
    }
    
    /**
     * Enter password in Okta login form
     */
    public ProphecyLoginPage enterOktaPassword(String password) {
        try {
            if (isElementPresent(oktaPasswordField)) {
                enterText(oktaPasswordField, password);
                log.info("Entered Okta password");
            } else if (isElementPresent(genericPasswordField)) {
                enterText(genericPasswordField, password);
                log.info("Entered password in generic field");
            } else {
                throw new RuntimeException("Okta password field not found");
            }
        } catch (Exception e) {
            log.error("Failed to enter Okta password: {}", e.getMessage());
            throw e;
        }
        return this;
    }
    
    /**
     * Enter username in ADFS login form
     */
    public ProphecyLoginPage enterADFSUsername(String username) {
        try {
            if (isElementPresent(adfsUsernameField)) {
                enterText(adfsUsernameField, username);
                log.info("Entered ADFS username: {}", username);
            } else if (isElementPresent(genericUsernameField)) {
                enterText(genericUsernameField, username);
                log.info("Entered username in generic field: {}", username);
            } else {
                throw new RuntimeException("ADFS username field not found");
            }
        } catch (Exception e) {
            log.error("Failed to enter ADFS username: {}", e.getMessage());
            throw e;
        }
        return this;
    }
    
    /**
     * Enter password in ADFS login form
     */
    public ProphecyLoginPage enterADFSPassword(String password) {
        try {
            if (isElementPresent(adfsPasswordField)) {
                enterText(adfsPasswordField, password);
                log.info("Entered ADFS password");
            } else if (isElementPresent(genericPasswordField)) {
                enterText(genericPasswordField, password);
                log.info("Entered password in generic field");
            } else {
                throw new RuntimeException("ADFS password field not found");
            }
        } catch (Exception e) {
            log.error("Failed to enter ADFS password: {}", e.getMessage());
            throw e;
        }
        return this;
    }
    
    /**
     * Enter email/username (fallback for traditional login)
     */
    public ProphecyLoginPage enterEmail(String email) {
        try {
            if (isElementPresent(emailField)) {
                enterText(emailField, email);
            } else if (isElementPresent(genericUsernameField)) {
                enterText(genericUsernameField, email);
            } else {
                throw new RuntimeException("Email/username field not found");
            }
        } catch (Exception e) {
            log.error("Failed to enter email: {}", e.getMessage());
            throw e;
        }
        log.info("Entered email: {}", email);
        return this;
    }
    
    /**
     * Enter password (fallback for traditional login)
     */
    public ProphecyLoginPage enterPassword(String password) {
        try {
            if (isElementPresent(passwordField)) {
                enterText(passwordField, password);
            } else if (isElementPresent(genericPasswordField)) {
                enterText(genericPasswordField, password);
            } else {
                throw new RuntimeException("Password field not found");
            }
        } catch (Exception e) {
            log.error("Failed to enter password: {}", e.getMessage());
            throw e;
        }
        log.info("Entered password");
        return this;
    }
    
    /**
     * Click Okta sign-in button
     */
    public ProphecyDashboardPage clickOktaSignIn() {
        try {
            if (isElementPresent(oktaSignInButton)) {
                clickElement(oktaSignInButton);
                log.info("Clicked Okta sign-in button");
            } else if (isElementPresent(genericSubmitButton)) {
                clickElement(genericSubmitButton);
                log.info("Clicked generic submit button");
            } else {
                throw new RuntimeException("Okta sign-in button not found");
            }
        } catch (Exception e) {
            log.error("Failed to click Okta sign-in button: {}", e.getMessage());
            throw e;
        }
        waitForPageToLoad();
        return new ProphecyDashboardPage();
    }
    
    /**
     * Click ADFS submit button
     */
    public ProphecyDashboardPage clickADFSSubmit() {
        try {
            if (isElementPresent(adfsSubmitButton)) {
                clickElement(adfsSubmitButton);
                log.info("Clicked ADFS submit button");
            } else if (isElementPresent(genericSubmitButton)) {
                clickElement(genericSubmitButton);
                log.info("Clicked generic submit button");
            } else {
                throw new RuntimeException("ADFS submit button not found");
            }
        } catch (Exception e) {
            log.error("Failed to click ADFS submit button: {}", e.getMessage());
            throw e;
        }
        waitForPageToLoad();
        return new ProphecyDashboardPage();
    }
    
    /**
     * Click login button (fallback for traditional login)
     */
    public ProphecyDashboardPage clickLoginButton() {
        try {
            if (isElementPresent(loginButton)) {
                clickElement(loginButton);
            } else if (isElementPresent(genericSubmitButton)) {
                clickElement(genericSubmitButton);
            } else {
                throw new RuntimeException("Login button not found");
            }
        } catch (Exception e) {
            log.error("Failed to click login button: {}", e.getMessage());
            throw e;
        }
        log.info("Clicked login button");
        waitForPageToLoad();
        return new ProphecyDashboardPage();
    }
    
    /**
     * Perform complete SSO login with Okta
     */
    public ProphecyDashboardPage loginWithOkta(String username, String password) {
        log.info("Starting Okta SSO login for user: {}", username);
        clickSSOLogin();
        waitForPageToLoad();
        enterOktaUsername(username);
        enterOktaPassword(password);
        return clickOktaSignIn();
    }
    
    /**
     * Perform complete SSO login with ADFS
     */
    public ProphecyDashboardPage loginWithADFS(String username, String password) {
        log.info("Starting ADFS SSO login for user: {}", username);
        clickSSOLogin();
        waitForPageToLoad();
        enterADFSUsername(username);
        enterADFSPassword(password);
        return clickADFSSubmit();
    }
    
    /**
     * Perform complete login (fallback for traditional login)
     */
    public ProphecyDashboardPage login(String email, String password) {
        log.info("Starting traditional login for user: {}", email);
        enterEmail(email);
        enterPassword(password);
        return clickLoginButton();
    }
    
    /**
     * SSO-only login method - just clicks SSO and waits for automatic authentication
     */
    public ProphecyDashboardPage loginWithSSO() {
        log.info("Starting SSO-only login (no credentials required)");
        
        // Click SSO login button
        clickSSOLogin();
        
        // Wait for SSO redirect and automatic authentication
        waitForSSOAuthentication();
        
        log.info("SSO authentication completed");
        return new ProphecyDashboardPage();
    }
    
    /**
     * Wait for SSO authentication to complete
     */
    private void waitForSSOAuthentication() {
        try {
            // Wait up to 30 seconds for SSO authentication to complete
            int maxWaitTime = 30;
            int waitTime = 0;
            
            while (waitTime < maxWaitTime) {
                String currentUrl = getCurrentUrl();
                
                // Check if we're back to the main application (not on login/auth pages)
                if (!currentUrl.contains("login") && 
                    !currentUrl.contains("signin") && 
                    !currentUrl.contains("auth") &&
                    !currentUrl.contains("okta") &&
                    !currentUrl.contains("adfs") &&
                    !currentUrl.contains("sso")) {
                    log.info("SSO authentication successful, redirected to: {}", currentUrl);
                    return;
                }
                
                // Wait 1 second before checking again
                Thread.sleep(1000);
                waitTime++;
                
                if (waitTime % 5 == 0) {
                    log.info("Waiting for SSO authentication... ({}/{}s)", waitTime, maxWaitTime);
                }
            }
            
            log.warn("SSO authentication may have timed out after {}s", maxWaitTime);
        } catch (Exception e) {
            log.error("Error waiting for SSO authentication: {}", e.getMessage());
        }
    }
    
    /**
     * Smart login method that detects authentication type and performs appropriate login
     */
    public ProphecyDashboardPage smartLogin(String username, String password) {
        log.info("Starting smart login");
        
        // Check if SSO login is available
        if (isElementPresent(ssoLoginButton) || isElementPresent(ssoLoginLink) || 
            findElement(ssoButtonLocators) != null) {
            log.info("SSO login detected, attempting SSO-only authentication");
            return loginWithSSO();
        } else {
            log.info("Traditional login detected");
            return login(username, password);
        }
    }
    
    /**
     * Smart login method without credentials (SSO-only)
     */
    public ProphecyDashboardPage smartLogin() {
        log.info("Starting smart login (SSO-only)");
        
        // Check if SSO login is available
        if (isElementPresent(ssoLoginButton) || isElementPresent(ssoLoginLink) || 
            findElement(ssoButtonLocators) != null) {
            log.info("SSO login detected, attempting SSO-only authentication");
            return loginWithSSO();
        } else {
            log.warn("No SSO login option found, cannot proceed without credentials");
            throw new RuntimeException("SSO login not available and no credentials provided");
        }
    }
    
    /**
     * Check if login was successful by verifying we're no longer on login page
     */
    public boolean isLoginSuccessful() {
        try {
            // Wait a bit for redirect
            Thread.sleep(2000);
            String currentUrl = getCurrentUrl();
            boolean success = !currentUrl.contains("login") && !currentUrl.contains("signin");
            log.info("Login successful: {}", success);
            return success;
        } catch (Exception e) {
            log.error("Error checking login status", e);
            return false;
        }
    }
    
    /**
     * Get error message if login failed
     */
    public String getErrorMessage() {
        try {
            if (errorMessage.isDisplayed()) {
                String error = errorMessage.getText();
                log.warn("Login error message: {}", error);
                return error;
            }
        } catch (Exception e) {
            log.debug("No error message found");
        }
        return "";
    }
    
    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Click forgot password link
     */
    public ProphecyLoginPage clickForgotPassword() {
        try {
            if (forgotPasswordLink.isDisplayed()) {
                clickElement(forgotPasswordLink);
                log.info("Clicked forgot password link");
            }
        } catch (Exception e) {
            log.warn("Forgot password link not found");
        }
        return this;
    }
    
    /**
     * Toggle remember me checkbox
     */
    public ProphecyLoginPage toggleRememberMe() {
        try {
            if (rememberMeCheckbox.isDisplayed()) {
                clickElement(rememberMeCheckbox);
                log.info("Toggled remember me checkbox");
            }
        } catch (Exception e) {
            log.debug("Remember me checkbox not found");
        }
        return this;
    }
    
    /**
     * Verify login page is loaded
     */
    public boolean isLoginPageLoaded() {
        try {
            return (emailField.isDisplayed() || isElementDisplayed(emailFieldAlt)) &&
                   (passwordField.isDisplayed() || isElementDisplayed(passwordFieldAlt)) &&
                   (loginButton.isDisplayed() || isElementDisplayed(loginButtonAlt));
        } catch (Exception e) {
            return false;
        }
    }
    
    // Helper methods removed - using BasePage methods instead
}