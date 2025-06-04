@pipeline @creation
Feature: Pipeline Creation and Configuration
  As a data engineer
  I want to create and configure data pipelines in Prophecy
  So that I can process data according to business requirements

  Background:
    Given I am logged into Prophecy application
    And I am on the pipelines page

  @smoke @positive
  Scenario: Create a simple data pipeline with source and target
    When I create a new pipeline named "Simple_Data_Pipeline"
    And I add a CSV source stage named "Customer_Data_Source"
    And I configure the source stage with test data "customer_data.csv"
    And I add a CSV target stage named "Processed_Customer_Data"
    And I connect "Customer_Data_Source" to "Processed_Customer_Data"
    And I save the pipeline
    Then the pipeline should be created successfully
    And the pipeline should contain 2 stages
    And the pipeline status should be "Ready"

  @positive
  Scenario: Create a pipeline with data transformation
    When I create a new pipeline named "Customer_Transformation_Pipeline"
    And I add a CSV source stage named "Raw_Customer_Data"
    And I configure the source stage with test data "raw_customer_data.csv"
    And I add a transformation stage named "Clean_Customer_Data" of type "Filter"
    And I configure the transformation stage to filter out null values
    And I add a transformation stage named "Enrich_Customer_Data" of type "Join"
    And I configure the join stage with lookup data "customer_lookup.csv"
    And I add a CSV target stage named "Clean_Customer_Output"
    And I connect the stages in sequence
    And I save the pipeline
    Then the pipeline should be created successfully
    And the pipeline should contain 4 stages
    And all stages should be properly connected

  @positive
  Scenario: Create a pipeline with multiple data sources
    When I create a new pipeline named "Multi_Source_Pipeline"
    And I add a CSV source stage named "Customer_Source" with data "customers.csv"
    And I add a JSON source stage named "Orders_Source" with data "orders.json"
    And I add a transformation stage named "Join_Customer_Orders" of type "Join"
    And I configure the join stage to join on "customer_id"
    And I add a CSV target stage named "Customer_Orders_Output"
    And I connect "Customer_Source" to "Join_Customer_Orders"
    And I connect "Orders_Source" to "Join_Customer_Orders"
    And I connect "Join_Customer_Orders" to "Customer_Orders_Output"
    And I save the pipeline
    Then the pipeline should be created successfully
    And the pipeline should contain 4 stages
    And the join stage should have 2 input connections

  @positive
  Scenario: Create a pipeline with data validation
    When I create a new pipeline named "Data_Validation_Pipeline"
    And I add a CSV source stage named "Input_Data" with data "validation_test_data.csv"
    And I add a validation stage named "Data_Quality_Check"
    And I configure validation rules for required fields
    And I configure validation rules for data types
    And I configure validation rules for value ranges
    And I add a CSV target stage named "Valid_Data_Output"
    And I add a CSV target stage named "Invalid_Data_Output"
    And I connect the stages with conditional routing
    And I save the pipeline
    Then the pipeline should be created successfully
    And the validation stage should have proper rules configured

  @negative
  Scenario: Attempt to create pipeline without required stages
    When I create a new pipeline named "Incomplete_Pipeline"
    And I try to save the pipeline without adding any stages
    Then I should see a validation error
    And the error message should indicate "Pipeline must contain at least one source and one target stage"

  @negative
  Scenario: Attempt to create pipeline with invalid connections
    When I create a new pipeline named "Invalid_Connection_Pipeline"
    And I add a CSV source stage named "Source_Stage"
    And I add a CSV target stage named "Target_Stage"
    And I try to connect stages in an invalid way
    Then I should see a connection error
    And the pipeline should not be saved

  @edge-case
  Scenario: Create pipeline with maximum allowed stages
    When I create a new pipeline named "Large_Pipeline"
    And I add the maximum number of allowed stages
    And I connect all stages in a valid sequence
    And I save the pipeline
    Then the pipeline should be created successfully
    And all stages should be properly connected
    And the pipeline should be performant

  @data-driven
  Scenario Outline: Create pipelines with different data formats
    When I create a new pipeline named "<pipeline_name>"
    And I add a <source_format> source stage with data "<source_data>"
    And I add a transformation stage of type "<transformation_type>"
    And I add a <target_format> target stage
    And I connect all stages
    And I save the pipeline
    Then the pipeline should be created successfully
    And the pipeline should handle <source_format> to <target_format> conversion

    Examples:
      | pipeline_name        | source_format | source_data    | transformation_type | target_format |
      | CSV_to_JSON_Pipeline | CSV           | sample.csv     | Transform           | JSON          |
      | JSON_to_CSV_Pipeline | JSON          | sample.json    | Transform           | CSV           |
      | Excel_to_CSV_Pipeline| Excel         | sample.xlsx    | Transform           | CSV           |
      | CSV_to_Parquet_Pipeline| CSV         | sample.csv     | Transform           | Parquet       |