package com.prophecy.testing.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Example test runner to demonstrate the framework capabilities
 */
@CucumberOptions(
        features = "src/test/resources/features/example_end_to_end.feature",
        glue = {"com.prophecy.testing.stepdefinitions", "com.prophecy.testing.hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/example-test-report",
                "json:target/cucumber-reports/example-test.json",
                "junit:target/cucumber-reports/example-test.xml"
        },
        monochrome = true,
        publish = true,
        tags = "@smoke"
)
public class ExampleTestRunner extends AbstractTestNGCucumberTests {
    // This class demonstrates how to run specific feature files
}