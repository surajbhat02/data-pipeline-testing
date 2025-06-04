# Stage-by-Stage Pipeline Testing Guide

## Overview

This guide explains how to test your existing Prophecy pipelines stage by stage using the testing framework. You can plug in your own test data and validate each transformation step individually.

## Prophecy API Integration

### Available APIs

Prophecy exposes several REST APIs that this framework leverages:

1. **Authentication API**: `/api/auth/login`
2. **Pipeline Management API**: `/api/pipelines/{id}`
3. **Stage Execution API**: `/api/pipelines/{id}/stages/{stageId}/execute`
4. **Pipeline Execution API**: `/api/pipelines/{id}/execute`
5. **Data Validation API**: `/api/pipelines/{id}/validate`

### API Configuration

Update your `application.properties`:

```properties
# Prophecy API Configuration
prophecy.base.url=https://your-prophecy-instance.com
prophecy.api.version=v1
prophecy.username=your-api-username
prophecy.password=your-api-password

# Or use API token
prophecy.api.token=your-api-token
```

## Testing Your Existing Pipeline

### Step 1: Identify Your Pipeline

First, get your pipeline ID from Prophecy UI or API:

```java
ProphecyApiClient apiClient = new ProphecyApiClient();
Pipeline pipeline = apiClient.getPipeline("your-pipeline-id");
List<PipelineStage> stages = apiClient.getPipelineStages("your-pipeline-id");
```

### Step 2: Prepare Your Test Data

#### Option A: Use Your Own Dataset

```java
// Load your existing dataset
TestDataManager dataManager = new TestDataManager();
List<Map<String, Object>> yourData = dataManager.loadCsvData("your_dataset.csv");

// Convert to API format
Map<String, Object> testData = Map.of(
    "records", yourData,
    "schema", "your_schema_name",
    "format", "csv"
);
```

#### Option B: Generate Mock Data Matching Your Schema

```java
// Define your pipeline's input schema
DataSchema yourSchema = new DataSchema("your_pipeline_schema", "Your pipeline input schema");
yourSchema.addField(new SchemaField("customer_id", DataType.INTEGER, false, "Customer ID"));
yourSchema.addField(new SchemaField("name", DataType.STRING, false, "Customer Name"));
yourSchema.addField(new SchemaField("email", DataType.STRING, false, "Email Address"));
// ... add more fields

// Generate test data
MockDataGenerator generator = new MockDataGenerator();
List<Map<String, Object>> mockData = generator.generateDataFromSchema(yourSchema, 1000);
```

### Step 3: Execute Stage-by-Stage Testing

#### Test All Stages Sequentially

```java
ProphecyApiClient apiClient = new ProphecyApiClient();
ProphecyApiClient.PipelineTestResult result = apiClient.testPipelineStageByStage(
    "your-pipeline-id", 
    testData
);

// Check results
if (result.isOverallSuccess()) {
    System.out.println("All stages passed!");
    
    // Examine each stage result
    for (ProphecyApiClient.StageTestResult stageResult : result.getStageResults()) {
        System.out.println("Stage: " + stageResult.getStageName());
        System.out.println("Success: " + stageResult.isSuccessful());
        System.out.println("Output records: " + stageResult.getOutputData().size());
    }
} else {
    System.out.println("Pipeline failed: " + result.getError());
}
```

#### Test Individual Stages

```java
// Test a specific stage
ProphecyApiClient.StageExecutionResult stageResult = apiClient.executeStage(
    "your-pipeline-id",
    "your-stage-id",
    testData
);

if (stageResult.isSuccessful()) {
    Map<String, Object> outputData = stageResult.getOutputData();
    // Validate output data
} else {
    System.out.println("Stage failed: " + stageResult.getErrorMessage());
}
```

## BDD Testing with Cucumber

### Create Feature File for Your Pipeline

```gherkin
@your-pipeline
Feature: Your Pipeline Stage Testing
  As a data engineer
  I want to test my specific pipeline stages
  So that I can ensure data quality at each step

  Background:
    Given I have an existing pipeline with ID "your-actual-pipeline-id"

  @smoke
  Scenario: Test your pipeline end-to-end
    Given I have prepared test data from file "your_test_data.csv"
    When I execute the pipeline stage by stage with the test data
    Then all pipeline stages should execute successfully
    And the output data from stage "Your Final Stage" should contain 950 records

  @individual-stage
  Scenario: Test your data cleaning stage
    Given I have prepared test data with 1000 records matching the pipeline input schema
    When I test stage "Your Data Cleaning Stage" individually with the test data
    Then stage "Your Data Cleaning Stage" should execute successfully
    And the output data from stage "Your Data Cleaning Stage" should match the expected schema
```

### Run Your Tests

```bash
# Test your specific pipeline
mvn test -Dtest=StageByStageTestRunner -Dcucumber.filter.tags="@your-pipeline"

# Test individual stages only
mvn test -Dtest=StageByStageTestRunner -Dcucumber.filter.tags="@individual-stage"

# Test with performance monitoring
mvn test -Dtest=StageByStageTestRunner -Dcucumber.filter.tags="@performance"
```

## Real-World Example

### Example: E-commerce Customer Pipeline

Let's say you have a pipeline with these stages:

1. **Raw Data Ingestion** - Reads customer data from CSV
2. **Data Validation** - Validates email formats, required fields
3. **Data Cleansing** - Removes duplicates, standardizes formats
4. **Age Filtering** - Filters customers by age > 18
5. **Geo Enrichment** - Adds geographic data based on postal codes
6. **Final Output** - Writes to Parquet format

#### Test Configuration

```java
// Your pipeline stages
String pipelineId = "ecommerce_customer_pipeline_v2";
String[] stageNames = {
    "Raw Data Ingestion",
    "Data Validation", 
    "Data Cleansing",
    "Age Filtering",
    "Geo Enrichment",
    "Final Output"
};

// Your test data
Map<String, Object> testData = Map.of(
    "records", loadYourCustomerData(),
    "schema", "ecommerce_customer_schema",
    "format", "csv"
);
```

#### Expected Results at Each Stage

```java
ProphecyApiClient.PipelineTestResult result = apiClient.testPipelineStageByStage(pipelineId, testData);

// Validate expected data flow
assertThat(getRecordCount(result, "Raw Data Ingestion")).isEqualTo(10000);      // All records
assertThat(getRecordCount(result, "Data Validation")).isEqualTo(9500);         // 95% pass validation
assertThat(getRecordCount(result, "Data Cleansing")).isEqualTo(9200);          // Remove duplicates
assertThat(getRecordCount(result, "Age Filtering")).isEqualTo(8500);           // Filter by age
assertThat(getRecordCount(result, "Geo Enrichment")).isEqualTo(8500);          // Same count, enriched data
assertThat(getRecordCount(result, "Final Output")).isEqualTo(8500);            // Final output
```

## Advanced Testing Scenarios

### 1. Data Quality Validation

```java
// Test data quality at each stage
for (ProphecyApiClient.StageTestResult stageResult : result.getStageResults()) {
    Map<String, Object> outputData = stageResult.getOutputData();
    
    // Validate data quality
    TestDataManager.ValidationResult validation = dataManager.validateDataQuality(
        (List<Map<String, Object>>) outputData.get("records"),
        expectedSchema
    );
    
    assertThat(validation.isValid()).isTrue();
    assertThat(validation.getDataQualityScore()).isGreaterThan(0.95); // 95% quality
}
```

### 2. Performance Testing

```java
// Measure stage execution times
PerformanceTestUtils.PerformanceMetrics metrics = PerformanceTestUtils.measureExecutionTime(() -> {
    return apiClient.executeStage(pipelineId, stageId, testData);
});

// Assert performance requirements
PerformanceTestUtils.assertExecutionTimeWithinLimit(metrics, 30000); // 30 seconds max
```

### 3. Data Volume Testing

```java
// Test with different data volumes
int[] testVolumes = {100, 1000, 10000, 100000};

for (int volume : testVolumes) {
    List<Map<String, Object>> testRecords = generator.generateCustomerData(volume);
    Map<String, Object> volumeTestData = Map.of("records", testRecords);
    
    ProphecyApiClient.PipelineTestResult result = apiClient.testPipelineStageByStage(pipelineId, volumeTestData);
    assertThat(result.isOverallSuccess()).isTrue();
}
```

### 4. Error Handling Testing

```java
// Test with invalid data
Map<String, Object> invalidData = Map.of(
    "records", generateInvalidData(),
    "schema", "invalid_schema"
);

ProphecyApiClient.PipelineTestResult result = apiClient.testPipelineStageByStage(pipelineId, invalidData);

// Expect specific stage to fail
ProphecyApiClient.StageTestResult validationStage = findStageByName(result, "Data Validation");
assertThat(validationStage.isSuccessful()).isFalse();
assertThat(validationStage.getError()).contains("schema validation failed");
```

## Integration with CI/CD

### GitHub Actions Example

```yaml
name: Pipeline Stage Testing

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  stage-testing:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'temurin'
    
    - name: Run Stage-by-Stage Tests
      run: |
        mvn test -Dtest=StageByStageTestRunner
      env:
        PROPHECY_BASE_URL: ${{ secrets.PROPHECY_BASE_URL }}
        PROPHECY_USERNAME: ${{ secrets.PROPHECY_USERNAME }}
        PROPHECY_PASSWORD: ${{ secrets.PROPHECY_PASSWORD }}
    
    - name: Upload Test Reports
      uses: actions/upload-artifact@v3
      if: always()
      with:
        name: stage-test-reports
        path: target/cucumber-reports/
```

## Best Practices

### 1. Test Data Management
- Use realistic data that represents your production scenarios
- Test with various data volumes (small, medium, large)
- Include edge cases (empty data, single record, malformed data)
- Version control your test datasets

### 2. Stage Testing Strategy
- Test stages individually first, then end-to-end
- Validate data schema at each stage
- Check data quality metrics (completeness, accuracy, consistency)
- Monitor performance at each stage

### 3. Error Handling
- Test with invalid input data
- Verify error messages are meaningful
- Ensure pipeline fails gracefully
- Test recovery scenarios

### 4. Continuous Testing
- Integrate with your CI/CD pipeline
- Run tests on every code change
- Set up alerts for test failures
- Monitor test execution trends

## Troubleshooting

### Common Issues

1. **Authentication Failures**
   ```bash
   # Check API credentials
   curl -X POST https://your-prophecy-instance.com/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"your-username","password":"your-password"}'
   ```

2. **Pipeline Not Found**
   ```bash
   # List available pipelines
   curl -X GET https://your-prophecy-instance.com/api/pipelines \
     -H "Authorization: Bearer your-token"
   ```

3. **Stage Execution Failures**
   ```bash
   # Check stage configuration
   curl -X GET https://your-prophecy-instance.com/api/pipelines/your-pipeline-id/stages \
     -H "Authorization: Bearer your-token"
   ```

### Debug Mode

```bash
# Run with debug logging
mvn test -Dtest=StageByStageTestRunner -Dlog.level=DEBUG

# Run single scenario
mvn test -Dtest=StageByStageTestRunner -Dcucumber.filter.tags="@your-specific-test"
```

This framework provides comprehensive stage-by-stage testing capabilities for your Prophecy pipelines, allowing you to validate each transformation step with your own test data and ensure data quality throughout the entire pipeline.