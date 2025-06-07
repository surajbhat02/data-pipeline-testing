package com.prophecy.testing.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for Prophecy Login Page
 */
@Slf4j
public class ProphecyLoginPage extends BasePage {
    
    // Page elements using @FindBy annotations
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
    
    // Alternative locators for different Prophecy login page layouts
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
     * Enter email/username
     */
    public ProphecyLoginPage enterEmail(String email) {
        try {
            if (emailField.isDisplayed()) {
                emailField.clear();
                emailField.sendKeys(email);
            } else {
                enterText(emailFieldAlt, email);
            }
        } catch (Exception e) {
            enterText(emailFieldAlt, email);
        }
        log.info("Entered email: {}", email);
        return this;
    }
    
    /**
     * Enter password
     */
    public ProphecyLoginPage enterPassword(String password) {
        try {
            if (passwordField.isDisplayed()) {
                passwordField.clear();
                passwordField.sendKeys(password);
            } else {
                enterText(passwordFieldAlt, password);
            }
        } catch (Exception e) {
            enterText(passwordFieldAlt, password);
        }
        log.info("Entered password");
        return this;
    }
    
    /**
     * Click login button
     */
    public ProphecyDashboardPage clickLoginButton() {
        try {
            if (loginButton.isDisplayed()) {
                clickElement(loginButton);
            } else {
                clickElement(loginButtonAlt);
            }
        } catch (Exception e) {
            clickElement(loginButtonAlt);
        }
        log.info("Clicked login button");
        waitForPageToLoad();
        return new ProphecyDashboardPage();
    }
    
    /**
     * Perform complete login
     */
    public ProphecyDashboardPage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        return clickLoginButton();
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
    
    // Helper methods for clicking elements
    private void clickElement(WebElement element) {
        waitForElementToBeClickable(element);
        element.click();
    }
    
    private void waitForElementToBeClickable(WebElement element) {
        wait.until(driver -> element.isDisplayed() && element.isEnabled());
    }
}