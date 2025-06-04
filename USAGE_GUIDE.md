# Prophecy Data Pipeline Testing Framework - Usage Guide

## Quick Start

### 1. Prerequisites
- Java 11 or higher
- Maven 3.6+
- Chrome browser (for UI testing)
- Access to Prophecy platform

### 2. Configuration Setup

Update `src/main/resources/application.properties`:
```properties
# Prophecy Platform Configuration
prophecy.base.url=https://your-prophecy-instance.com
prophecy.login.url=https://your-prophecy-instance.com/login
prophecy.username=your-username
prophecy.password=your-password

# Browser Configuration
browser.name=chrome
browser.headless=false
browser.maximize=true

# Test Data Paths
test.data.path=src/test/resources/testdata
mock.data.path=src/test/resources/mockdata
pipeline.config.path=src/test/resources/configs
```

### 3. Running Tests

#### Run All Tests
```bash
mvn clean test
```

#### Run Smoke Tests Only
```bash
mvn clean test -Dtest=SmokeTestRunner
```

#### Run Regression Tests
```bash
mvn clean test -Dtest=RegressionTestRunner
```

#### Run Example End-to-End Test
```bash
mvn clean test -Dtest=ExampleTestRunner
```

#### Run Tests with Specific Tags
```bash
mvn clean test -Dcucumber.filter.tags="@smoke"
mvn clean test -Dcucumber.filter.tags="@positive and not @performance"
```

## Framework Components

### 1. Data Model Classes

#### Pipeline
```java
Pipeline pipeline = new Pipeline("Customer Processing Pipeline", "Process customer data");
pipeline.addStage(new PipelineStage("source", "CSV Reader", sourceConfig));
pipeline.addStage(new PipelineStage("transform", "Data Filter", transformConfig));
pipeline.addStage(new PipelineStage("target", "Parquet Writer", targetConfig));
```

#### Data Schema
```java
DataSchema schema = new DataSchema("customer_schema", "Customer data schema");
schema.addField(new SchemaField("id", DataType.INTEGER, false, "Customer ID"));
schema.addField(new SchemaField("name", DataType.STRING, false, "Customer Name"));
schema.addField(new SchemaField("email", DataType.STRING, false, "Customer Email"));
```

### 2. Mock Data Generation

#### Generate Test Data
```java
MockDataGenerator generator = new MockDataGenerator();

// Generate customer data
List<Map<String, Object>> customers = generator.generateCustomerData(1000);

// Generate with custom schema
DataSchema schema = loadSchemaFromFile("customer_schema.json");
List<Map<String, Object>> data = generator.generateDataFromSchema(schema, 500);

// Save to CSV
generator.saveDataToCsv(customers, "test_customers.csv");
```

#### Using TestDataManager
```java
TestDataManager dataManager = new TestDataManager();

// Load and validate data
List<Map<String, Object>> data = dataManager.loadCsvData("customers.csv");
ValidationResult result = dataManager.validateDataQuality(data, schema);

// Compare datasets
ComparisonResult comparison = dataManager.compareDatasets(expectedData, actualData);
```

### 3. Page Object Model

#### Login and Navigation
```java
LoginPage loginPage = new LoginPage(driver);
loginPage.login("username", "password");

DashboardPage dashboard = new DashboardPage(driver);
dashboard.navigateToPipelines();

PipelinesPage pipelinesPage = new PipelinesPage(driver);
pipelinesPage.createNewPipeline("My Pipeline");
```

#### Pipeline Editor
```java
PipelineEditorPage editor = new PipelineEditorPage(driver);
editor.addSourceComponent("CSV File");
editor.configureComponent("source", sourceConfig);
editor.addTransformComponent("Filter");
editor.addTargetComponent("Parquet File");
editor.savePipeline();
```

### 4. Step Definitions

#### Common Steps
```gherkin
Given I am logged into the Prophecy platform
When I navigate to the pipelines page
Then I should see the pipelines list
```

#### Pipeline Creation Steps
```gherkin
When I create a new pipeline called "Customer Pipeline"
And I configure the source as "CSV File" with schema "customer_schema"
And I add a transformation to "Filter by age > 18"
Then the pipeline should be created successfully
```

#### Data Validation Steps
```gherkin
Given I have prepared mock data with 1000 records
When I execute the pipeline with the mock data
Then the output should contain only valid records
And the data count should match the expected count
```

### 5. Performance Testing

#### Measure Execution Time
```java
PerformanceMetrics metrics = PerformanceTestUtils.measureExecutionTime(() -> {
    // Your operation here
    pipelineExecutor.runPipeline(pipeline);
});

// Assert performance requirements
PerformanceTestUtils.assertExecutionTimeWithinLimit(metrics, 30000); // 30 seconds
```

#### Load Testing
```java
LoadTestResults results = PerformanceTestUtils.performLoadTest(
    () -> {
        apiClient.triggerPipeline(pipelineId);
        return null;
    },
    10, // concurrent users
    100 // iterations
);

// Assert performance requirements
PerformanceTestUtils.assertSuccessRate(results, 95.0); // 95% success rate
PerformanceTestUtils.assertThroughput(results, 5.0); // 5 ops/sec
```

### 6. API Testing

#### REST API Calls
```java
// GET request
HttpResponse<String> response = ApiTestUtils.sendGetRequest(
    "https://api.prophecy.com/pipelines",
    Map.of("Authorization", "Bearer " + token)
);

// POST request
String jsonBody = "{\"name\":\"Test Pipeline\",\"type\":\"batch\"}";
HttpResponse<String> response = ApiTestUtils.sendPostRequest(
    "https://api.prophecy.com/pipelines",
    jsonBody,
    Map.of("Authorization", "Bearer " + token)
);

// Parse and validate response
JsonNode jsonResponse = ApiTestUtils.parseJsonResponse(response.body());
String pipelineId = ApiTestUtils.extractJsonValue(jsonResponse, "data.id");
```

## Writing Custom Tests

### 1. Create Feature File
```gherkin
@smoke @positive
Feature: Custom Pipeline Testing
  Scenario: Test custom data transformation
    Given I have prepared test data
    When I execute the transformation pipeline
    Then the output should meet quality standards
```

### 2. Implement Step Definitions
```java
@Given("I have prepared test data")
public void prepareTestData() {
    // Implementation
}

@When("I execute the transformation pipeline")
public void executeTransformationPipeline() {
    // Implementation
}

@Then("the output should meet quality standards")
public void validateOutputQuality() {
    // Implementation
}
```

### 3. Create Test Runner
```java
@CucumberOptions(
    features = "src/test/resources/features/custom_pipeline.feature",
    glue = {"com.prophecy.testing.stepdefinitions"},
    tags = "@smoke"
)
public class CustomTestRunner extends AbstractTestNGCucumberTests {
}
```

## Best Practices

### 1. Test Data Management
- Use realistic test data that represents production scenarios
- Implement data cleanup after test execution
- Version control your test data schemas
- Use parameterized data for different test scenarios

### 2. Page Object Model
- Keep page objects focused on a single page or component
- Use explicit waits instead of implicit waits
- Implement proper error handling and logging
- Make page objects reusable across different test scenarios

### 3. Test Organization
- Group related tests using tags (@smoke, @regression, @performance)
- Use descriptive scenario names and step definitions
- Implement proper test setup and teardown
- Keep tests independent and idempotent

### 4. Performance Testing
- Set realistic performance benchmarks
- Test with production-like data volumes
- Monitor resource usage during tests
- Implement proper load testing scenarios

### 5. Reporting and Monitoring
- Use ExtentReports for detailed test reporting
- Implement screenshot capture for failed tests
- Log important test steps and data
- Set up CI/CD integration for automated testing

## Troubleshooting

### Common Issues

#### WebDriver Issues
```bash
# Update WebDriver
mvn clean compile -U

# Run in headless mode
browser.headless=true
```

#### Test Data Issues
```bash
# Validate test data format
mvn test -Dtest=TestDataValidationTest

# Regenerate mock data
mvn test -Dtest=MockDataGenerationTest
```

#### Performance Issues
```bash
# Run with increased timeouts
prophecy.timeout.explicit=60
prophecy.timeout.page.load=120
```

### Debug Mode
```bash
# Run with debug logging
mvn test -Dlog4j.configurationFile=src/test/resources/log4j2-debug.xml

# Run single test with verbose output
mvn test -Dtest=ExampleTestRunner -X
```

## Integration with CI/CD

The framework includes GitHub Actions configuration for automated testing:

```yaml
# .github/workflows/ci.yml
- name: Run Smoke Tests
  run: mvn clean test -Dtest=SmokeTestRunner

- name: Run Regression Tests
  run: mvn clean test -Dtest=RegressionTestRunner

- name: Generate Reports
  run: mvn site
```

## Support and Documentation

- Framework Documentation: See README.md
- Cucumber Documentation: https://cucumber.io/docs
- Selenium Documentation: https://selenium.dev/documentation
- TestNG Documentation: https://testng.org/doc/documentation-main.html