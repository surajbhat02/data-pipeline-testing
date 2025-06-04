@pipeline @execution
Feature: Pipeline Execution and Monitoring
  As a data engineer
  I want to execute data pipelines and monitor their progress
  So that I can ensure data processing completes successfully

  Background:
    Given I am logged into Prophecy application
    And I have a configured pipeline ready for execution

  @smoke @positive
  Scenario: Execute a simple pipeline successfully
    Given I have a pipeline named "Simple_Test_Pipeline" with mock data
    When I execute the pipeline
    Then the pipeline execution should start
    And the pipeline status should change to "Running"
    And I should be able to monitor the execution progress
    And the pipeline should complete successfully
    And the pipeline status should be "Completed"
    And the output data should be generated

  @positive
  Scenario: Execute pipeline with stage-by-stage monitoring
    Given I have a pipeline named "Multi_Stage_Pipeline" with the following stages:
      | stage_name          | stage_type      | order |
      | Source_Data         | Source          | 1     |
      | Clean_Data          | Transformation  | 2     |
      | Validate_Data       | Validation      | 3     |
      | Target_Data         | Target          | 4     |
    When I execute the pipeline
    Then each stage should execute in the correct order
    And I should see the status of each stage:
      | stage_name          | expected_status |
      | Source_Data         | Completed       |
      | Clean_Data          | Completed       |
      | Validate_Data       | Completed       |
      | Target_Data         | Completed       |
    And the overall pipeline status should be "Completed"

  @positive
  Scenario: Execute pipeline with data validation
    Given I have a pipeline named "Validation_Pipeline" with validation rules
    And the input data contains both valid and invalid records
    When I execute the pipeline
    Then the validation stage should process all records
    And valid records should be routed to the success output
    And invalid records should be routed to the error output
    And I should see validation statistics:
      | metric              | expected_value |
      | total_records       | 1000          |
      | valid_records       | 850           |
      | invalid_records     | 150           |
    And the pipeline should complete successfully

  @positive
  Scenario: Execute pipeline with large dataset
    Given I have a pipeline named "Large_Data_Pipeline"
    And the input data contains 100000 records
    When I execute the pipeline
    Then the pipeline should handle the large dataset
    And the execution should complete within acceptable time limits
    And memory usage should remain within acceptable bounds
    And all records should be processed correctly

  @positive
  Scenario: Monitor pipeline execution logs
    Given I have a pipeline named "Logging_Test_Pipeline"
    When I execute the pipeline
    Then I should be able to view execution logs
    And the logs should contain stage-level information
    And the logs should show data processing statistics
    And the logs should include any warnings or errors
    And I should be able to download the complete log file

  @positive
  Scenario: Execute pipeline with different data formats
    Given I have a pipeline that processes multiple data formats:
      | input_format | output_format | test_data_file    |
      | CSV          | JSON          | test_data.csv     |
      | JSON         | CSV           | test_data.json    |
      | Excel        | CSV           | test_data.xlsx    |
      | Parquet      | JSON          | test_data.parquet |
    When I execute the pipeline with each data format
    Then each execution should complete successfully
    And the output should be in the correct format
    And data integrity should be maintained

  @negative
  Scenario: Handle pipeline execution with missing input data
    Given I have a pipeline named "Missing_Data_Pipeline"
    And the input data source is not available
    When I execute the pipeline
    Then the pipeline execution should fail
    And the error message should indicate "Input data source not found"
    And the pipeline status should be "Failed"
    And no output data should be generated

  @negative
  Scenario: Handle pipeline execution with invalid data schema
    Given I have a pipeline named "Schema_Mismatch_Pipeline"
    And the input data has a different schema than expected
    When I execute the pipeline
    Then the pipeline should detect the schema mismatch
    And the execution should fail at the source stage
    And the error message should describe the schema difference
    And the pipeline status should be "Failed"

  @negative
  Scenario: Handle pipeline execution timeout
    Given I have a pipeline named "Timeout_Test_Pipeline"
    And the pipeline is configured with a 30-second timeout
    And the pipeline processing takes longer than the timeout
    When I execute the pipeline
    Then the pipeline execution should be terminated
    And the pipeline status should be "Cancelled"
    And I should see a timeout error message

  @recovery
  Scenario: Resume failed pipeline execution
    Given I have a pipeline named "Recovery_Test_Pipeline"
    And the pipeline failed during execution at stage "Data_Transformation"
    When I fix the issue causing the failure
    And I resume the pipeline execution from the failed stage
    Then the pipeline should continue from where it left off
    And the remaining stages should execute successfully
    And the pipeline status should be "Completed"

  @performance
  Scenario: Execute pipeline with performance monitoring
    Given I have a pipeline named "Performance_Test_Pipeline"
    When I execute the pipeline with performance monitoring enabled
    Then I should be able to track execution metrics:
      | metric                | threshold     |
      | total_execution_time  | < 300 seconds |
      | memory_usage          | < 2 GB        |
      | cpu_usage             | < 80%         |
      | records_per_second    | > 1000        |
    And the performance should meet the defined thresholds

  @concurrent
  Scenario: Execute multiple pipelines concurrently
    Given I have multiple pipelines ready for execution:
      | pipeline_name           | expected_duration |
      | Pipeline_A              | 60 seconds        |
      | Pipeline_B              | 90 seconds        |
      | Pipeline_C              | 45 seconds        |
    When I execute all pipelines concurrently
    Then all pipelines should execute without interference
    And each pipeline should complete within its expected duration
    And system resources should be properly managed
    And all pipelines should complete successfully

  @data-quality
  Scenario: Execute pipeline with data quality checks
    Given I have a pipeline named "Data_Quality_Pipeline" with quality rules:
      | rule_type           | rule_description              | threshold |
      | completeness        | No null values in key fields | 100%      |
      | uniqueness          | Customer ID must be unique    | 100%      |
      | validity            | Email format validation       | 95%       |
      | consistency         | Date format consistency       | 100%      |
    When I execute the pipeline
    Then all data quality rules should be evaluated
    And the quality metrics should meet the defined thresholds
    And a data quality report should be generated
    And the pipeline should complete successfully if all rules pass