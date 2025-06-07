package com.prophecy.testing.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Test runner for Prophecy pipeline testing with stage-by-stage execution and mock data sources
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.prophecy.testing.stepdefinitions", "com.prophecy.testing.hooks"},
        tags = "@ProphecyWebTest",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/prophecy-web-test-report",
                "json:target/cucumber-reports/prophecy-web-test.json",
                "junit:target/cucumber-reports/prophecy-web-test.xml"
        },
        monochrome = true,
        publish = false
)
public class PipelineTestRunner extends AbstractTestNGCucumberTests {
    // Test runner for pipeline testing scenarios
}