package com.prophecy.testing.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Test runner for smoke tests only
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.prophecy.testing.stepdefinitions", "com.prophecy.testing.hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/smoke-test-report",
                "json:target/cucumber-reports/smoke-test.json",
                "junit:target/cucumber-reports/smoke-test.xml",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true,
        publish = true,
        tags = "@smoke"
)
public class SmokeTestRunner extends AbstractTestNGCucumberTests {
    // This class runs only smoke tests for quick validation
}