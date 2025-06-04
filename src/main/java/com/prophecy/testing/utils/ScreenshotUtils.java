package com.prophecy.testing.utils;

import com.prophecy.testing.config.ConfigManager;
import com.prophecy.testing.config.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for taking and managing screenshots
 */
public class ScreenshotUtils {
    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    /**
     * Take screenshot and save to file
     */
    public static String takeScreenshot(String testName) {
        try {
            WebDriver driver = WebDriverManager.getDriver();
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = testName + "_" + timestamp + ".png";
            String screenshotPath = config.getScreenshotsPath() + "/" + fileName;
            
            // Ensure directory exists
            File screenshotDir = new File(config.getScreenshotsPath());
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            
            File destFile = new File(screenshotPath);
            FileUtils.copyFile(sourceFile, destFile);
            
            logger.info("Screenshot saved: {}", screenshotPath);
            return screenshotPath;
        } catch (IOException e) {
            logger.error("Error taking screenshot: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Take screenshot with custom file name
     */
    public static String takeScreenshot(String testName, String customFileName) {
        try {
            WebDriver driver = WebDriverManager.getDriver();
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            String screenshotPath = config.getScreenshotsPath() + "/" + customFileName + ".png";
            
            // Ensure directory exists
            File screenshotDir = new File(config.getScreenshotsPath());
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            
            File destFile = new File(screenshotPath);
            FileUtils.copyFile(sourceFile, destFile);
            
            logger.info("Screenshot saved: {}", screenshotPath);
            return screenshotPath;
        } catch (IOException e) {
            logger.error("Error taking screenshot: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Take screenshot on test failure
     */
    public static String takeFailureScreenshot(String testName, String errorMessage) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String fileName = "FAILED_" + testName + "_" + timestamp;
        String screenshotPath = takeScreenshot(testName, fileName);
        
        if (screenshotPath != null) {
            logger.error("Test failed: {} - Screenshot saved: {}", errorMessage, screenshotPath);
        }
        
        return screenshotPath;
    }
    
    /**
     * Clean up old screenshots
     */
    public static void cleanupOldScreenshots(int daysToKeep) {
        try {
            File screenshotDir = new File(config.getScreenshotsPath());
            if (!screenshotDir.exists()) {
                return;
            }
            
            File[] files = screenshotDir.listFiles();
            if (files == null) {
                return;
            }
            
            long cutoffTime = System.currentTimeMillis() - (daysToKeep * 24L * 60 * 60 * 1000);
            
            for (File file : files) {
                if (file.isFile() && file.lastModified() < cutoffTime) {
                    if (file.delete()) {
                        logger.debug("Deleted old screenshot: {}", file.getName());
                    }
                }
            }
            
            logger.info("Cleaned up screenshots older than {} days", daysToKeep);
        } catch (Exception e) {
            logger.error("Error cleaning up screenshots: {}", e.getMessage());
        }
    }
    
    /**
     * Get screenshot as byte array for reporting
     */
    public static byte[] getScreenshotAsBytes() {
        try {
            WebDriver driver = WebDriverManager.getDriver();
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            return takesScreenshot.getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.error("Error getting screenshot as bytes: {}", e.getMessage());
            return new byte[0];
        }
    }
}