package com.prophecy.testing.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Test runner for stage-by-stage pipeline testing
 */
@CucumberOptions(
        features = "src/test/resources/features/stage_by_stage_testing.feature",
        glue = {"com.prophecy.testing.stepdefinitions", "com.prophecy.testing.hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/stage-by-stage-report",
                "json:target/cucumber-reports/stage-by-stage.json",
                "junit:target/cucumber-reports/stage-by-stage.xml"
        },
        monochrome = true,
        publish = true,
        tags = "@stage-testing"
)
public class StageByStageTestRunner extends AbstractTestNGCucumberTests {
    // This runner executes stage-by-stage testing scenarios
}