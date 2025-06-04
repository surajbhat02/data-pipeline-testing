package com.prophecy.testing.pages;

import com.prophecy.testing.config.ConfigManager;
import com.prophecy.testing.config.WebDriverManager;
import com.prophecy.testing.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Base page class containing common functionality for all page objects
 */
public abstract class BasePage {
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ConfigManager config;
    
    public BasePage() {
        this.driver = WebDriverManager.getDriver();
        this.config = ConfigManager.getInstance();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeout()));
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Wait for element to be visible
     */
    protected WebElement waitForElementToBeVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be clickable
     */
    protected WebElement waitForElementToBeClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Wait for element to be present
     */
    protected WebElement waitForElementToBePresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Wait for elements to be present
     */
    protected List<WebElement> waitForElementsToBePresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }
    
    /**
     * Wait for element to disappear
     */
    protected boolean waitForElementToDisappear(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    /**
     * Click element with wait
     */
    protected void clickElement(By locator) {
        WebElement element = waitForElementToBeClickable(locator);
        element.click();
        logger.debug("Clicked element: {}", locator);
    }
    
    /**
     * Click element using JavaScript
     */
    protected void clickElementWithJS(By locator) {
        WebElement element = waitForElementToBePresent(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
        logger.debug("Clicked element with JS: {}", locator);
    }
    
    /**
     * Enter text in input field
     */
    protected void enterText(By locator, String text) {
        WebElement element = waitForElementToBeVisible(locator);
        element.clear();
        element.sendKeys(text);
        logger.debug("Entered text '{}' in element: {}", text, locator);
    }
    
    /**
     * Get text from element
     */
    protected String getText(By locator) {
        WebElement element = waitForElementToBeVisible(locator);
        String text = element.getText();
        logger.debug("Got text '{}' from element: {}", text, locator);
        return text;
    }
    
    /**
     * Get attribute value from element
     */
    protected String getAttribute(By locator, String attributeName) {
        WebElement element = waitForElementToBePresent(locator);
        String value = element.getAttribute(attributeName);
        logger.debug("Got attribute '{}' value '{}' from element: {}", attributeName, value, locator);
        return value;
    }
    
    /**
     * Check if element is displayed
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if element is enabled
     */
    protected boolean isElementEnabled(By locator) {
        try {
            WebElement element = waitForElementToBePresent(locator);
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Scroll to element
     */
    protected void scrollToElement(By locator) {
        WebElement element = waitForElementToBePresent(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
        logger.debug("Scrolled to element: {}", locator);
    }
    
    /**
     * Wait for page to load completely
     */
    protected void waitForPageToLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        logger.debug("Page loaded completely");
    }
    
    /**
     * Refresh the page
     */
    protected void refreshPage() {
        driver.navigate().refresh();
        waitForPageToLoad();
        logger.debug("Page refreshed");
    }
    
    /**
     * Navigate to URL
     */
    protected void navigateToUrl(String url) {
        driver.get(url);
        waitForPageToLoad();
        logger.info("Navigated to URL: {}", url);
    }
    
    /**
     * Get current page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Get current page URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Take screenshot
     */
    protected String takeScreenshot(String testName) {
        return ScreenshotUtils.takeScreenshot(testName);
    }
    
    /**
     * Wait for specific time
     */
    protected void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Wait interrupted: {}", e.getMessage());
        }
    }
    
    /**
     * Execute JavaScript
     */
    protected Object executeJavaScript(String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }
    
    /**
     * Switch to frame
     */
    protected void switchToFrame(By frameLocator) {
        WebElement frame = waitForElementToBePresent(frameLocator);
        driver.switchTo().frame(frame);
        logger.debug("Switched to frame: {}", frameLocator);
    }
    
    /**
     * Switch to default content
     */
    protected void switchToDefaultContent() {
        driver.switchTo().defaultContent();
        logger.debug("Switched to default content");
    }
    
    /**
     * Accept alert
     */
    protected void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        logger.debug("Alert accepted");
    }
    
    /**
     * Dismiss alert
     */
    protected void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().dismiss();
        logger.debug("Alert dismissed");
    }
    
    /**
     * Get alert text
     */
    protected String getAlertText() {
        wait.until(ExpectedConditions.alertIsPresent());
        String alertText = driver.switchTo().alert().getText();
        logger.debug("Alert text: {}", alertText);
        return alertText;
    }
}