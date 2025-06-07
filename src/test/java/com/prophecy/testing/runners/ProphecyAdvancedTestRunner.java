package com.prophecy.testing.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Test runner for Prophecy advanced testing scenarios
 */
@CucumberOptions(
        features = "src/test/resources/features/prophecy_advanced_scenarios.feature",
        glue = {"com.prophecy.testing.stepdefinitions", "com.prophecy.testing.hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/prophecy-advanced-report",
                "json:target/cucumber-reports/prophecy-advanced.json",
                "junit:target/cucumber-reports/prophecy-advanced.xml"
        },
        monochrome = true,
        publish = false,
        tags = "@ProphecyAdvanced"
)
public class ProphecyAdvancedTestRunner extends AbstractTestNGCucumberTests {
    // Test runner for advanced Prophecy testing scenarios
}