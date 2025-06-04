@smoke @positive @end-to-end
Feature: End-to-End Pipeline Testing Example
  As a data engineer
  I want to test a complete data pipeline workflow
  So that I can ensure data flows correctly from source to target

  Background:
    Given I am logged into the Prophecy platform
    And I have access to the test project

  @data-setup
  Scenario: Complete Pipeline Testing Workflow
    Given I have prepared mock customer data with the following schema:
      | Field Name | Data Type | Nullable | Description           |
      | id         | INTEGER   | false    | Customer ID           |
      | name       | STRING    | false    | Customer Name         |
      | email      | STRING    | false    | Customer Email        |
      | age        | INTEGER   | true     | Customer Age          |
      | city       | STRING    | true     | Customer City         |
    And I have generated 1000 records of test data
    
    When I create a new pipeline called "Customer Data Processing Pipeline"
    And I configure the source as "CSV File" with the customer schema
    And I add a transformation to "Filter customers by age > 18"
    And I add a transformation to "Validate email format"
    And I configure the target as "Parquet File" with the same schema
    And I save the pipeline configuration
    
    Then the pipeline should be created successfully
    And the pipeline should appear in the pipelines list
    
    When I execute the pipeline with the mock data
    Then the pipeline execution should start successfully
    And I should be able to monitor the pipeline progress
    And the pipeline should complete within 5 minutes
    
    When I validate the output data
    Then the output should contain only customers with age > 18
    And all email addresses should be in valid format
    And the data count should match the filtered input count
    And the output schema should match the expected schema
    
    When I check the pipeline execution logs
    Then there should be no error messages
    And the execution metrics should be within acceptable ranges

  @negative
  Scenario: Pipeline Validation with Invalid Data
    Given I have prepared invalid customer data with missing required fields
    
    When I create a pipeline for the invalid data
    And I execute the pipeline
    
    Then the pipeline execution should fail with validation errors
    And the error messages should clearly indicate the data quality issues
    And no output data should be generated

  @performance
  Scenario: Pipeline Performance Testing
    Given I have prepared a large dataset with 100000 customer records
    
    When I execute the customer processing pipeline
    
    Then the pipeline should complete within 10 minutes
    And the memory usage should not exceed 2GB
    And the CPU usage should not exceed 80%
    And the throughput should be at least 1000 records per second