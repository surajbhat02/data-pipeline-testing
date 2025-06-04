package com.prophecy.testing.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Main test runner for Cucumber tests
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.prophecy.testing.stepdefinitions", "com.prophecy.testing.hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-html-report",
                "json:target/cucumber-reports/cucumber.json",
                "junit:target/cucumber-reports/cucumber.xml",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true,
        publish = true,
        tags = "not @ignore"
)
public class TestRunner extends AbstractTestNGCucumberTests {
    // This class serves as the main entry point for running Cucumber tests
}