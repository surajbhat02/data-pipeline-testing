package com.prophecy.testing.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Test runner for regression tests
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.prophecy.testing.stepdefinitions", "com.prophecy.testing.hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/regression-test-report",
                "json:target/cucumber-reports/regression-test.json",
                "junit:target/cucumber-reports/regression-test.xml",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true,
        publish = true,
        tags = "@positive or @negative"
)
public class RegressionTestRunner extends AbstractTestNGCucumberTests {
    // This class runs comprehensive regression tests
}