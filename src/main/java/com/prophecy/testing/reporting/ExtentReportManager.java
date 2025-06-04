package com.prophecy.testing.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.prophecy.testing.config.ConfigManager;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Manager class for ExtentReports configuration and operations
 */
public class ExtentReportManager {
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    
    public static void initializeExtentReports() {
        if (extent == null) {
            String reportPath = getReportPath();
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            
            // Configure the reporter
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("Prophecy Data Pipeline Test Report");
            sparkReporter.config().setReportName("Automation Test Results");
            sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
            
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            
            // Set system information
            extent.setSystemInfo("Application", "Prophecy Data Platform");
            extent.setSystemInfo("Environment", ConfigManager.getInstance().getEnvironment());
            extent.setSystemInfo("Browser", ConfigManager.getInstance().getBrowserName());
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
        }
    }
    
    public static ExtentTest createTest(String testName, String description) {
        ExtentTest extentTest = extent.createTest(testName, description);
        test.set(extentTest);
        return extentTest;
    }
    
    public static ExtentTest getTest() {
        return test.get();
    }
    
    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
    
    public static void removeTest() {
        test.remove();
    }
    
    private static String getReportPath() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String reportDir = "target/extent-reports";
        
        // Create directory if it doesn't exist
        File dir = new File(reportDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        return reportDir + "/ExtentReport_" + timestamp + ".html";
    }
}