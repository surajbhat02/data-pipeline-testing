package com.prophecy.testing.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.WebElement;

/**
 * Page Object Model for Prophecy Login Page
 */
public class LoginPage extends BasePage {
    
    // Locators
    private static final By EMAIL_INPUT = By.id("email");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON = By.xpath("//button[contains(text(), 'Sign In') or contains(text(), 'Login')]");
    private static final By FORGOT_PASSWORD_LINK = By.xpath("//a[contains(text(), 'Forgot Password')]");
    private static final By SIGNUP_LINK = By.xpath("//a[contains(text(), 'Sign Up')]");
    private static final By ERROR_MESSAGE = By.xpath("//div[contains(@class, 'error') or contains(@class, 'alert')]");
    private static final By LOADING_SPINNER = By.xpath("//div[contains(@class, 'loading') or contains(@class, 'spinner')]");
    
    // Alternative locators (in case the primary ones don't work)
    private static final By EMAIL_INPUT_ALT = By.xpath("//input[@type='email' or @name='email' or @placeholder*='email']");
    private static final By PASSWORD_INPUT_ALT = By.xpath("//input[@type='password' or @name='password']");
    private static final By LOGIN_BUTTON_ALT = By.xpath("//button[@type='submit'] | //input[@type='submit']");
    
    /**
     * Navigate to login page
     */
    public void navigateToLoginPage() {
        String loginUrl = config.getProphecyLoginUrl();
        navigateToUrl(loginUrl);
        logger.info("Navigated to login page: {}", loginUrl);
    }
    
    /**
     * Enter email address
     */
    public void enterEmail(String email) {
        try {
            enterText(EMAIL_INPUT, email);
        } catch (Exception e) {
            logger.warn("Primary email locator failed, trying alternative");
            enterText(EMAIL_INPUT_ALT, email);
        }
        logger.info("Entered email: {}", email);
    }
    
    /**
     * Enter password
     */
    public void enterPassword(String password) {
        try {
            enterText(PASSWORD_INPUT, password);
        } catch (Exception e) {
            logger.warn("Primary password locator failed, trying alternative");
            enterText(PASSWORD_INPUT_ALT, password);
        }
        logger.info("Entered password");
    }
    
    /**
     * Click login button
     */
    public void clickLoginButton() {
        try {
            clickElement(LOGIN_BUTTON);
        } catch (Exception e) {
            logger.warn("Primary login button locator failed, trying alternative");
            clickElement(LOGIN_BUTTON_ALT);
        }
        logger.info("Clicked login button");
    }
    
    /**
     * Perform complete login
     */
    public DashboardPage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
        
        // Wait for login to complete
        waitForLoginToComplete();
        
        logger.info("Login completed for user: {}", email);
        return new DashboardPage();
    }
    
    /**
     * Wait for login process to complete
     */
    private void waitForLoginToComplete() {
        // Wait for loading spinner to disappear if present
        if (isElementDisplayed(LOADING_SPINNER)) {
            waitForElementToDisappear(LOADING_SPINNER);
        }
        
        // Wait for page to load
        waitForPageToLoad();
        
        // Additional wait for any redirects
        waitFor(2);
    }
    
    /**
     * Check if login error is displayed
     */
    public boolean isLoginErrorDisplayed() {
        return isElementDisplayed(ERROR_MESSAGE);
    }
    
    /**
     * Get login error message
     */
    public String getLoginErrorMessage() {
        if (isLoginErrorDisplayed()) {
            return getText(ERROR_MESSAGE);
        }
        return "";
    }
    
    /**
     * Click forgot password link
     */
    public void clickForgotPasswordLink() {
        clickElement(FORGOT_PASSWORD_LINK);
        logger.info("Clicked forgot password link");
    }
    
    /**
     * Click sign up link
     */
    public void clickSignUpLink() {
        clickElement(SIGNUP_LINK);
        logger.info("Clicked sign up link");
    }
    
    /**
     * Check if login page is displayed
     */
    public boolean isLoginPageDisplayed() {
        try {
            return isElementDisplayed(EMAIL_INPUT) || isElementDisplayed(EMAIL_INPUT_ALT);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if login button is enabled
     */
    public boolean isLoginButtonEnabled() {
        try {
            return isElementEnabled(LOGIN_BUTTON);
        } catch (Exception e) {
            return isElementEnabled(LOGIN_BUTTON_ALT);
        }
    }
    
    /**
     * Clear login form
     */
    public void clearLoginForm() {
        try {
            WebElement emailField = driver.findElement(EMAIL_INPUT);
            emailField.clear();
        } catch (Exception e) {
            WebElement emailField = driver.findElement(EMAIL_INPUT_ALT);
            emailField.clear();
        }
        
        try {
            WebElement passwordField = driver.findElement(PASSWORD_INPUT);
            passwordField.clear();
        } catch (Exception e) {
            WebElement passwordField = driver.findElement(PASSWORD_INPUT_ALT);
            passwordField.clear();
        }
        
        logger.info("Cleared login form");
    }
    
    /**
     * Verify login page elements
     */
    public boolean verifyLoginPageElements() {
        boolean emailPresent = isElementDisplayed(EMAIL_INPUT) || isElementDisplayed(EMAIL_INPUT_ALT);
        boolean passwordPresent = isElementDisplayed(PASSWORD_INPUT) || isElementDisplayed(PASSWORD_INPUT_ALT);
        boolean loginButtonPresent = isElementDisplayed(LOGIN_BUTTON) || isElementDisplayed(LOGIN_BUTTON_ALT);
        
        logger.info("Login page elements verification - Email: {}, Password: {}, Login Button: {}", 
                   emailPresent, passwordPresent, loginButtonPresent);
        
        return emailPresent && passwordPresent && loginButtonPresent;
    }
}