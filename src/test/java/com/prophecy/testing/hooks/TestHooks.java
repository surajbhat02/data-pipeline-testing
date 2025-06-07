package com.prophecy.testing.hooks;

import com.prophecy.testing.config.ConfigManager;
import com.prophecy.testing.config.WebDriverManager;

import com.prophecy.testing.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Cucumber hooks for test setup and teardown
 */
public class TestHooks {
    private static final Logger logger = LogManager.getLogger(TestHooks.class);
    private static ConfigManager config;

    
    @BeforeAll
    public static void globalSetup() {
        logger.info("=== Starting Test Suite Execution ===");
        
        // Initialize configuration
        config = ConfigManager.getInstance();
        // Test data manager initialization removed for web-only testing
        
        // Clean up old screenshots
        ScreenshotUtils.cleanupOldScreenshots(7); // Keep screenshots for 7 days
        
        logger.info("Global test setup completed");
    }
    
    @Before("@WebTest")
    public void setUpWebTest(Scenario scenario) {
        logger.info("=== Starting Web Test Scenario: {} ===", scenario.getName());
        
        // Initialize WebDriver only for web testing scenarios
        WebDriverManager.initializeDriver();
        
        // Log scenario tags
        if (!scenario.getSourceTagNames().isEmpty()) {
            logger.info("Scenario tags: {}", scenario.getSourceTagNames());
        }
        
        logger.info("Web test scenario setup completed for: {}", scenario.getName());
    }
    
    @Before("not @WebTest and not @PipelineTest and not @StageByStage")
    public void setUpGenericTest(Scenario scenario) {
        logger.info("=== Starting Test Scenario: {} ===", scenario.getName());
        
        // Log scenario tags
        if (!scenario.getSourceTagNames().isEmpty()) {
            logger.info("Scenario tags: {}", scenario.getSourceTagNames());
        }
        
        logger.info("Test scenario setup completed for: {}", scenario.getName());
    }
    
    @After("@WebTest")
    public void tearDownWebTest(Scenario scenario) {
        logger.info("=== Finishing Web Test Scenario: {} ===", scenario.getName());
        
        try {
            // Take screenshot if scenario failed
            if (scenario.isFailed()) {
                logger.error("Web test scenario failed: {}", scenario.getName());
                
                String screenshotPath = ScreenshotUtils.takeFailureScreenshot(
                        scenario.getName().replaceAll(" ", "_"),
                        "Scenario failed"
                );
                
                if (screenshotPath != null) {
                    // Attach screenshot to Cucumber report
                    byte[] screenshot = ScreenshotUtils.getScreenshotAsBytes();
                    if (screenshot.length > 0) {
                        scenario.attach(screenshot, "image/png", "Screenshot");
                    }
                }
            } else {
                logger.info("Web test scenario passed: {}", scenario.getName());
            }
            
            // Log scenario status
            logger.info("Scenario status: {}", scenario.getStatus());
            
        } catch (Exception e) {
            logger.error("Error during web test scenario teardown: {}", e.getMessage());
        } finally {
            // Always quit the driver for web tests
            WebDriverManager.quitDriver();
        }
        
        logger.info("Web test scenario teardown completed for: {}", scenario.getName());
    }
    
    @After("not @WebTest and not @PipelineTest and not @StageByStage")
    public void tearDownGenericTest(Scenario scenario) {
        logger.info("=== Finishing Test Scenario: {} ===", scenario.getName());
        
        try {
            // Log scenario status
            if (scenario.isFailed()) {
                logger.error("Scenario failed: {}", scenario.getName());
            } else {
                logger.info("Scenario passed: {}", scenario.getName());
            }
            
            logger.info("Scenario status: {}", scenario.getStatus());
            
        } catch (Exception e) {
            logger.error("Error during scenario teardown: {}", e.getMessage());
        }
        
        logger.info("Test scenario teardown completed for: {}", scenario.getName());
    }
    
    @AfterAll
    public static void globalTeardown() {
        logger.info("=== Test Suite Execution Completed ===");
        
        try {
            // Test data cleanup removed for web-only testing
            
            // Final cleanup
            WebDriverManager.quitAllDrivers();
            
            logger.info("Global test teardown completed successfully");
        } catch (Exception e) {
            logger.error("Error during global teardown: {}", e.getMessage());
        }
    }
    
    /**
     * Hook for scenarios tagged with @data-setup
     */
    @Before("@data-setup")
    public void setUpTestData(Scenario scenario) {
        logger.info("Setting up test data for scenario: {}", scenario.getName());
        
        // Test data generation removed for web-only testing
        logger.info("Test data setup completed");
    }
    
    /**
     * Hook for scenarios tagged with @performance
     */
    @Before("@performance")
    public void setUpPerformanceMonitoring(Scenario scenario) {
        logger.info("Setting up performance monitoring for scenario: {}", scenario.getName());
        
        // Set up performance monitoring
        System.setProperty("performance.monitoring.enabled", "true");
        
        logger.info("Performance monitoring setup completed");
    }
    
    /**
     * Hook for cleaning up after performance tests
     */
    @After("@performance")
    public void tearDownPerformanceMonitoring(Scenario scenario) {
        logger.info("Tearing down performance monitoring for scenario: {}", scenario.getName());
        
        // Clean up performance monitoring
        System.clearProperty("performance.monitoring.enabled");
        
        logger.info("Performance monitoring teardown completed");
    }
    
    /**
     * Hook for scenarios tagged with @database
     */
    @Before("@database")
    public void setUpDatabase(Scenario scenario) {
        logger.info("Setting up database for scenario: {}", scenario.getName());
        
        // Database setup would go here
        // For now, we'll just log the setup
        
        logger.info("Database setup completed");
    }
    
    /**
     * Hook for cleaning up after database tests
     */
    @After("@database")
    public void tearDownDatabase(Scenario scenario) {
        logger.info("Tearing down database for scenario: {}", scenario.getName());
        
        // Database cleanup would go here
        // For now, we'll just log the teardown
        
        logger.info("Database teardown completed");
    }
    
    /**
     * Hook for scenarios tagged with @api
     */
    @Before("@api")
    public void setUpApiTesting(Scenario scenario) {
        logger.info("Setting up API testing for scenario: {}", scenario.getName());
        
        // API testing setup would go here
        // For now, we'll just log the setup
        
        logger.info("API testing setup completed");
    }
    
    /**
     * Hook for scenarios that require special browser configuration
     */
    @Before("@headless")
    public void setUpHeadlessBrowser(Scenario scenario) {
        logger.info("Setting up headless browser for scenario: {}", scenario.getName());
        
        // Set headless mode
        System.setProperty("browser.headless", "true");
        
        logger.info("Headless browser setup completed");
    }
    
    /**
     * Hook for cleaning up after headless browser tests
     */
    @After("@headless")
    public void tearDownHeadlessBrowser(Scenario scenario) {
        logger.info("Tearing down headless browser for scenario: {}", scenario.getName());
        
        // Clear headless mode
        System.clearProperty("browser.headless");
        
        logger.info("Headless browser teardown completed");
    }
}