package com.prophecy.testing.utils;

import com.prophecy.testing.config.ConfigManager;
import com.prophecy.testing.config.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;

/**
 * Utility class for various wait operations
 */
public class WaitUtils {
    private static final Logger logger = LogManager.getLogger(WaitUtils.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    /**
     * Wait for element to be visible
     */
    public static WebElement waitForElementVisible(By locator) {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeout()));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be visible with custom timeout
     */
    public static WebElement waitForElementVisible(By locator, int timeoutSeconds) {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be clickable
     */
    public static WebElement waitForElementClickable(By locator) {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeout()));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Wait for element to be clickable with custom timeout
     */
    public static WebElement waitForElementClickable(By locator, int timeoutSeconds) {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Wait for element to be present
     */
    public static WebElement waitForElementPresent(By locator) {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeout()));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Wait for element to disappear
     */
    public static boolean waitForElementToDisappear(By locator) {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeout()));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to disappear with custom timeout
     */
    public static boolean waitForElementToDisappear(By locator, int timeoutSeconds) {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for text to be present in element
     */
    public static boolean waitForTextInElement(By locator, String text) {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeout()));
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
    
    /**
     * Wait for attribute to contain value
     */
    public static boolean waitForAttributeContains(By locator, String attribute, String value) {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeout()));
        return wait.until(ExpectedConditions.attributeContains(locator, attribute, value));
    }
    
    /**
     * Wait for URL to contain text
     */
    public static boolean waitForUrlContains(String urlFragment) {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeout()));
        return wait.until(ExpectedConditions.urlContains(urlFragment));
    }
    
    /**
     * Wait for title to contain text
     */
    public static boolean waitForTitleContains(String title) {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeout()));
        return wait.until(ExpectedConditions.titleContains(title));
    }
    
    /**
     * Wait for custom condition
     */
    public static <T> T waitForCustomCondition(Function<WebDriver, T> condition) {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeout()));
        return wait.until(condition);
    }
    
    /**
     * Wait for custom condition with timeout
     */
    public static <T> T waitForCustomCondition(Function<WebDriver, T> condition, int timeoutSeconds) {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(condition);
    }
    
    /**
     * Wait for page to load completely
     */
    public static void waitForPageLoad() {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getPageLoadTimeout()));
        
        wait.until((ExpectedCondition<Boolean>) wd ->
                ((org.openqa.selenium.JavascriptExecutor) wd)
                        .executeScript("return document.readyState").equals("complete"));
        
        logger.debug("Page loaded completely");
    }
    
    /**
     * Wait for AJAX to complete
     */
    public static void waitForAjaxToComplete() {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeout()));
        
        wait.until((ExpectedCondition<Boolean>) wd -> {
            try {
                return (Boolean) ((org.openqa.selenium.JavascriptExecutor) wd)
                        .executeScript("return jQuery.active == 0");
            } catch (Exception e) {
                return true; // If jQuery is not present, assume AJAX is complete
            }
        });
        
        logger.debug("AJAX requests completed");
    }
    
    /**
     * Wait for element count to be
     */
    public static boolean waitForElementCount(By locator, int expectedCount) {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeout()));
        
        return wait.until((ExpectedCondition<Boolean>) wd -> {
            try {
                return wd.findElements(locator).size() == expectedCount;
            } catch (Exception e) {
                return false;
            }
        });
    }
    
    /**
     * Wait for element to be enabled
     */
    public static WebElement waitForElementEnabled(By locator) {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeout()));
        
        return wait.until((ExpectedCondition<WebElement>) wd -> {
            WebElement element = wd.findElement(locator);
            if (element != null && element.isEnabled()) {
                return element;
            }
            return null;
        });
    }
    
    /**
     * Wait for element to be selected
     */
    public static boolean waitForElementSelected(By locator) {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeout()));
        return wait.until(ExpectedConditions.elementToBeSelected(locator));
    }
    
    /**
     * Wait with polling interval
     */
    public static <T> T waitWithPolling(Function<WebDriver, T> condition, int timeoutSeconds, int pollingIntervalMillis) {
        WebDriver driver = WebDriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds), Duration.ofMillis(pollingIntervalMillis));
        return wait.until(condition);
    }
    
    /**
     * Hard wait (Thread.sleep) - use sparingly
     */
    public static void hardWait(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            logger.debug("Hard wait for {} seconds", seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Hard wait interrupted: {}", e.getMessage());
        }
    }
    
    /**
     * Wait for pipeline execution status
     */
    public static boolean waitForPipelineStatus(By statusLocator, String expectedStatus, int timeoutSeconds) {
        return waitForCustomCondition(driver -> {
            try {
                WebElement statusElement = driver.findElement(statusLocator);
                String currentStatus = statusElement.getText().toLowerCase();
                return currentStatus.contains(expectedStatus.toLowerCase());
            } catch (Exception e) {
                return false;
            }
        }, timeoutSeconds);
    }
    
    /**
     * Wait for data to load in table/grid
     */
    public static boolean waitForDataToLoad(By tableLocator, int minimumRows) {
        return waitForCustomCondition(driver -> {
            try {
                WebElement table = driver.findElement(tableLocator);
                int rowCount = table.findElements(By.tagName("tr")).size();
                return rowCount >= minimumRows;
            } catch (Exception e) {
                return false;
            }
        });
    }
}