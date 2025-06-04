package com.prophecy.testing.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager for handling application properties
 */
public class ConfigManager {
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private Properties properties;
    
    private ConfigManager() {
        loadProperties();
    }
    
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }
    
    private void loadProperties() {
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("config/application.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
                logger.info("Configuration properties loaded successfully");
            } else {
                logger.error("Unable to find application.properties file");
            }
        } catch (IOException e) {
            logger.error("Error loading configuration properties: {}", e.getMessage());
        }
    }
    
    public String getProperty(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = properties.getProperty(key);
        }
        return value;
    }
    
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }
    
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for property {}: {}", key, value);
            return defaultValue;
        }
    }
    
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    // Prophecy specific configuration getters
    public String getProphecyBaseUrl() {
        return getProperty("prophecy.base.url");
    }
    
    public String getProphecyLoginUrl() {
        return getProperty("prophecy.login.url");
    }
    
    public int getImplicitTimeout() {
        return getIntProperty("prophecy.timeout.implicit", 10);
    }
    
    public int getExplicitTimeout() {
        return getIntProperty("prophecy.timeout.explicit", 30);
    }
    
    public int getPageLoadTimeout() {
        return getIntProperty("prophecy.timeout.page.load", 60);
    }
    
    public String getBrowserName() {
        return getProperty("browser.name", "chrome");
    }
    
    public boolean isHeadless() {
        return getBooleanProperty("browser.headless", false);
    }
    
    public boolean shouldMaximizeBrowser() {
        return getBooleanProperty("browser.maximize", true);
    }
    
    public String getTestDataPath() {
        return getProperty("test.data.path");
    }
    
    public String getMockDataPath() {
        return getProperty("mock.data.path");
    }
    
    public String getPipelineConfigPath() {
        return getProperty("pipeline.config.path");
    }
    
    public String getReportsPath() {
        return getProperty("reports.path");
    }
    
    public String getScreenshotsPath() {
        return getProperty("screenshots.path");
    }
    
    public String getEnvironment() {
        return getProperty("environment", "dev");
    }
    
    public boolean isParallelExecution() {
        return getBooleanProperty("parallel.execution", false);
    }
    
    public int getThreadCount() {
        return getIntProperty("thread.count", 1);
    }
    
    public boolean shouldRetryFailedTests() {
        return getBooleanProperty("retry.failed.tests", true);
    }
    
    public int getRetryCount() {
        return getIntProperty("retry.count", 2);
    }
}