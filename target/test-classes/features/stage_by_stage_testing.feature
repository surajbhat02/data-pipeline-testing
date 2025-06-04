@stage-testing @api
Feature: Stage-by-Stage Pipeline Testing
  As a data engineer
  I want to test each stage of my existing pipeline individually
  So that I can identify issues at specific transformation points

  Background:
    Given I have an existing pipeline with ID "customer_processing_pipeline_123"

  @smoke @positive
  Scenario: Test complete pipeline stage by stage with generated data
    Given I have prepared test data with 1000 records matching the pipeline input schema
    When I execute the pipeline stage by stage with the test data
    Then all pipeline stages should execute successfully
    And I should be able to trace data flow through all stages

  @positive
  Scenario: Test pipeline with custom test data schema
    Given I have prepared test data with custom schema:
      | Field Name | Data Type | Nullable | Description           |
      | customer_id| INTEGER   | false    | Unique customer ID    |
      | full_name  | STRING    | false    | Customer full name    |
      | email      | STRING    | false    | Customer email        |
      | age        | INTEGER   | true     | Customer age          |
      | city       | STRING    | true     | Customer city         |
      | country    | STRING    | false    | Customer country      |
    When I execute the pipeline stage by stage with the test data
    Then all pipeline stages should execute successfully

  @positive
  Scenario: Test pipeline with file-based test data
    Given I have prepared test data from file "sample_customers.csv"
    When I execute the pipeline stage by stage with the test data
    Then all pipeline stages should execute successfully

  @individual-stage
  Scenario: Test individual data source stage
    Given I have prepared test data with 500 records matching the pipeline input schema
    When I test stage "Data Source" individually with the test data
    Then stage "Data Source" should execute successfully
    And the output data from stage "Data Source" should contain 500 records
    And the output data from stage "Data Source" should match the expected schema

  @individual-stage
  Scenario: Test individual data transformation stage
    Given I have prepared test data with 1000 records matching the pipeline input schema
    When I test stage "Data Cleansing" individually with the test data
    Then stage "Data Cleansing" should execute successfully
    And the output data from stage "Data Cleansing" should match the expected schema

  @individual-stage
  Scenario: Test individual data filter stage
    Given I have prepared test data with 1000 records matching the pipeline input schema
    When I test stage "Age Filter" individually with the test data
    Then stage "Age Filter" should execute successfully
    And the output data from stage "Age Filter" should contain 800 records

  @individual-stage
  Scenario: Test individual data aggregation stage
    Given I have prepared test data with 1000 records matching the pipeline input schema
    When I test stage "City Aggregation" individually with the test data
    Then stage "City Aggregation" should execute successfully
    And the output data from stage "City Aggregation" should match the expected schema

  @individual-stage
  Scenario: Test individual data join stage
    Given I have prepared test data with 1000 records matching the pipeline input schema
    When I test stage "Customer Enrichment" individually with the test data
    Then stage "Customer Enrichment" should execute successfully
    And the output data from stage "Customer Enrichment" should match the expected schema

  @negative
  Scenario: Test pipeline with invalid data format
    Given I have prepared test data with custom schema:
      | Field Name | Data Type | Nullable | Description           |
      | invalid_id | STRING    | false    | Invalid ID format     |
      | bad_email  | INTEGER   | false    | Wrong email type      |
    When I execute the pipeline stage by stage with the test data
    Then stage "Data Validation" should fail with error containing "schema mismatch"

  @negative
  Scenario: Test pipeline with missing required fields
    Given I have prepared test data with custom schema:
      | Field Name | Data Type | Nullable | Description           |
      | partial_id | INTEGER   | true     | Incomplete data       |
    When I execute the pipeline stage by stage with the test data
    Then stage "Data Validation" should fail with error containing "missing required fields"

  @performance
  Scenario: Test pipeline performance with large dataset
    Given I have prepared test data with 100000 records matching the pipeline input schema
    When I execute the pipeline stage by stage with the test data
    Then all pipeline stages should execute successfully
    And the pipeline should process data within 300 seconds
    And I should be able to trace data flow through all stages

  @data-quality
  Scenario: Test data quality at each stage
    Given I have prepared test data with 1000 records matching the pipeline input schema
    When I execute the pipeline stage by stage with the test data
    Then all pipeline stages should execute successfully
    And the output data from stage "Data Source" should contain 1000 records
    And the output data from stage "Data Cleansing" should contain 950 records
    And the output data from stage "Age Filter" should contain 800 records
    And the output data from stage "Final Output" should contain 800 records

  @regression
  Scenario Outline: Test pipeline with different data volumes
    Given I have prepared test data with <record_count> records matching the pipeline input schema
    When I execute the pipeline stage by stage with the test data
    Then all pipeline stages should execute successfully
    And I should be able to trace data flow through all stages

    Examples:
      | record_count |
      | 10           |
      | 100          |
      | 1000         |
      | 10000        |

  @edge-cases
  Scenario: Test pipeline with empty dataset
    Given I have prepared test data with 0 records matching the pipeline input schema
    When I execute the pipeline stage by stage with the test data
    Then all pipeline stages should execute successfully
    And the output data from stage "Final Output" should contain 0 records

  @edge-cases
  Scenario: Test pipeline with single record
    Given I have prepared test data with 1 records matching the pipeline input schema
    When I execute the pipeline stage by stage with the test data
    Then all pipeline stages should execute successfully
    And the output data from stage "Final Output" should contain 1 records