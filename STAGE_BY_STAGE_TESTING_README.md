# Stage-by-Stage Pipeline Testing

This document provides comprehensive guidance on using the stage-by-stage testing capabilities for Prophecy data pipelines.

## Overview

The stage-by-stage testing framework allows you to:
- Test individual pipeline stages in isolation
- Inject custom test data at any stage
- Validate data transformations step-by-step
- Monitor performance at each stage
- Debug pipeline issues more effectively

## Quick Start

### 1. Configure Prophecy Credentials

Update `src/test/resources/config/application.properties`:

```properties
# Prophecy Configuration
prophecy.base.url=https://app.prophecy.io
prophecy.username=your_username
prophecy.password=your_password
prophecy.api.token=your_api_token
```

### 2. Run Stage-by-Stage Tests

```bash
# Run all stage-by-stage tests
mvn test -Dtest=StageByStageTestRunner

# Run specific scenarios
mvn test -Dtest=StageByStageTestRunner -Dcucumber.filter.tags="@stage-testing"
```

## Test Scenarios

### Basic Pipeline Testing

```gherkin
Scenario: Test pipeline with generated data
  Given I have a Prophecy pipeline named "customer_data_pipeline"
  And I have prepared test data with 1000 records matching pipeline input schema
  When I execute the pipeline stage by stage
  Then each stage should complete successfully
  And the final output should contain 1000 records
```

### Custom Data Testing

```gherkin
Scenario: Test pipeline with custom CSV data
  Given I have a Prophecy pipeline named "sales_pipeline"
  And I have test data from file "sales_data.csv"
  When I execute the pipeline stage by stage
  Then each stage should complete successfully
  And the data quality should meet expectations
```

### Schema-Based Testing

```gherkin
Scenario: Test pipeline with custom schema
  Given I have a Prophecy pipeline named "product_pipeline"
  And I have test data with custom schema:
    | Field Name | Data Type | Nullable | Description |
    | product_id | INTEGER   | false    | Product ID  |
    | name       | STRING    | false    | Product name|
    | price      | DOUBLE    | false    | Product price|
  When I execute the pipeline stage by stage
  Then each stage should complete successfully
```

## API Client Usage

### Initialize API Client

```java
ProphecyApiClient apiClient = new ProphecyApiClient();
```

### Get Pipeline Information

```java
// Get pipeline by name
Pipeline pipeline = apiClient.getPipelineByName("my_pipeline");

// Get pipeline stages
List<PipelineStage> stages = apiClient.getPipelineStages(pipeline.getId());
```

### Execute Individual Stages

```java
// Execute a specific stage
Map<String, Object> stageResult = apiClient.executeStage(
    pipeline.getId(), 
    stage.getId(), 
    testData
);

// Validate stage output
boolean isValid = apiClient.validateStageOutput(
    stage.getId(), 
    stageResult, 
    expectedSchema
);
```

## Test Data Management

### Generate Mock Data

```java
MockDataGenerator generator = new MockDataGenerator();

// Generate data from schema
DataSchema schema = generator.generateSampleSchema("customer_schema");
List<Map<String, Object>> data = generator.generateMockData(schema, 1000);
```

### Load Data from Files

```java
TestDataManager dataManager = new TestDataManager();

// Load CSV data
List<Map<String, Object>> csvData = dataManager.loadTestData("customers.csv");

// Load JSON data
List<Map<String, Object>> jsonData = dataManager.loadTestData("products.json");
```

### Custom Schema Definition

```java
DataSchema schema = new DataSchema("custom_schema", new ArrayList<>());
schema.addField(new SchemaField("id", DataType.INTEGER, false, "Record ID"));
schema.addField(new SchemaField("name", DataType.STRING, false, "Customer name"));
schema.addField(new SchemaField("email", DataType.STRING, true, "Email address"));
```

## Performance Testing

### Large Dataset Testing

```gherkin
Scenario: Test pipeline performance with large dataset
  Given I have a Prophecy pipeline named "big_data_pipeline"
  And I have prepared test data with 1000000 records matching pipeline input schema
  When I execute the pipeline stage by stage
  Then each stage should complete within performance thresholds
  And memory usage should remain within acceptable limits
```

### Stage Performance Monitoring

```java
// Monitor stage execution time
long startTime = System.currentTimeMillis();
Map<String, Object> result = apiClient.executeStage(pipelineId, stageId, data);
long executionTime = System.currentTimeMillis() - startTime;

// Assert performance
assertThat(executionTime).isLessThan(30000); // 30 seconds max
```

## Error Handling and Validation

### Data Quality Validation

```java
// Validate data completeness
assertThat(outputData).isNotEmpty();
assertThat(outputData.size()).isEqualTo(expectedRecordCount);

// Validate data schema
for (Map<String, Object> record : outputData) {
    assertThat(record).containsKeys("id", "name", "processed_date");
    assertThat(record.get("id")).isNotNull();
}
```

### Error Scenario Testing

```gherkin
Scenario: Handle invalid data gracefully
  Given I have a Prophecy pipeline named "validation_pipeline"
  And I have test data with invalid records:
    | id | name | email |
    | 1  | John | invalid-email |
    | 2  |      | john@example.com |
  When I execute the pipeline stage by stage
  Then the pipeline should handle errors gracefully
  And error records should be logged appropriately
```

## Configuration Options

### Pipeline Configuration

```properties
# Stage execution timeout (milliseconds)
stage.execution.timeout=60000

# Maximum retries for failed stages
stage.retry.count=3

# Data validation strictness
data.validation.strict=true

# Performance monitoring
performance.monitoring.enabled=true
performance.threshold.warning=10000
performance.threshold.error=30000
```

### Test Data Configuration

```properties
# Test data generation
mock.data.seed=12345
mock.data.locale=en_US

# File data loading
test.data.encoding=UTF-8
test.data.delimiter=,
test.data.quote.char="
```

## Best Practices

### 1. Test Data Preparation

- Use realistic data volumes for performance testing
- Include edge cases and boundary conditions
- Test with both valid and invalid data
- Use consistent data formats across tests

### 2. Stage Isolation

- Test each stage independently when possible
- Use mock data for upstream dependencies
- Validate stage inputs and outputs separately
- Monitor resource usage per stage

### 3. Error Handling

- Test error scenarios explicitly
- Validate error messages and codes
- Ensure graceful degradation
- Test recovery mechanisms

### 4. Performance Testing

- Establish baseline performance metrics
- Test with various data volumes
- Monitor memory and CPU usage
- Set appropriate timeouts

## Troubleshooting

### Common Issues

1. **Authentication Failures**
   - Verify Prophecy credentials in configuration
   - Check API token validity
   - Ensure proper permissions

2. **Stage Execution Timeouts**
   - Increase timeout values in configuration
   - Check data volume and complexity
   - Monitor system resources

3. **Data Validation Failures**
   - Verify schema definitions
   - Check data type compatibility
   - Validate field mappings

4. **Performance Issues**
   - Reduce test data volume
   - Optimize stage configurations
   - Check system resources

### Debug Mode

Enable debug logging for detailed information:

```properties
log.level=DEBUG
```

Run tests with verbose output:

```bash
mvn test -Dtest=StageByStageTestRunner -X
```

## Integration with CI/CD

### Jenkins Pipeline

```groovy
pipeline {
    agent any
    stages {
        stage('Stage-by-Stage Tests') {
            steps {
                sh 'mvn test -Dtest=StageByStageTestRunner'
            }
            post {
                always {
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/reports',
                        reportFiles: 'extent-report.html',
                        reportName: 'Stage Testing Report'
                    ])
                }
            }
        }
    }
}
```

### GitHub Actions

```yaml
name: Stage-by-Stage Pipeline Tests
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 11
        uses: actions/setup-java@v2
        with:
          java-version: '11'
          distribution: 'adopt'
      - name: Run stage-by-stage tests
        run: mvn test -Dtest=StageByStageTestRunner
        env:
          PROPHECY_USERNAME: ${{ secrets.PROPHECY_USERNAME }}
          PROPHECY_PASSWORD: ${{ secrets.PROPHECY_PASSWORD }}
          PROPHECY_API_TOKEN: ${{ secrets.PROPHECY_API_TOKEN }}
```

## Advanced Features

### Custom Stage Validators

```java
public class CustomStageValidator implements StageValidator {
    @Override
    public boolean validate(Map<String, Object> stageOutput, DataSchema expectedSchema) {
        // Custom validation logic
        return true;
    }
}
```

### Data Transformation Testing

```java
// Test specific transformations
Map<String, Object> input = Map.of("amount", "100.50", "currency", "USD");
Map<String, Object> output = apiClient.executeStage(pipelineId, "currency_conversion", input);
assertThat(output.get("amount_usd")).isEqualTo(100.50);
```

### Parallel Stage Execution

```java
// Execute independent stages in parallel
CompletableFuture<Map<String, Object>> stage1 = CompletableFuture.supplyAsync(() -> 
    apiClient.executeStage(pipelineId, "stage1", data1));
CompletableFuture<Map<String, Object>> stage2 = CompletableFuture.supplyAsync(() -> 
    apiClient.executeStage(pipelineId, "stage2", data2));

CompletableFuture.allOf(stage1, stage2).join();
```

## Support and Resources

- **Documentation**: See `STAGE_BY_STAGE_TESTING_GUIDE.md` for detailed API documentation
- **Examples**: Check `src/test/resources/features/stage_by_stage_testing.feature` for more examples
- **Configuration**: Review `src/test/resources/config/application.properties` for all options
- **Troubleshooting**: Enable debug logging and check test execution logs

For additional support, please refer to the main project documentation or contact the development team.