package com.prophecy.testing.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Test runner for Prophecy edge cases and boundary testing
 */
@CucumberOptions(
        features = "src/test/resources/features/prophecy_edge_cases.feature",
        glue = {"com.prophecy.testing.stepdefinitions", "com.prophecy.testing.hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/prophecy-edge-cases-report",
                "json:target/cucumber-reports/prophecy-edge-cases.json",
                "junit:target/cucumber-reports/prophecy-edge-cases.xml"
        },
        monochrome = true,
        publish = false,
        tags = "@ProphecyEdgeCases"
)
public class ProphecyEdgeCasesTestRunner extends AbstractTestNGCucumberTests {
    // Test runner for Prophecy edge cases and boundary testing
}