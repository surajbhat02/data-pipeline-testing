@ProphecyAdvanced
Feature: Prophecy Advanced Testing Scenarios
  As a data engineer
  I want to test complex Prophecy pipeline scenarios
  So that I can ensure robust pipeline execution with various data types and edge cases

  Background:
    Given I navigate to Prophecy login page "https://app.prophecy.io"
    When I login with username "test@example.com" and password "password123"
    Then I should be logged in successfully

  @WebTest @ComplexData @StageByStage
  Scenario: Execute pipeline with complex nested JSON data
    When I navigate to Pipelines section
    And I open pipeline "Complex Data Pipeline"
    And I have custom JSON data:
      """
      [
        {
          "customer": {
            "id": 1,
            "profile": {
              "name": "John Doe",
              "contact": {
                "email": "john@example.com",
                "phone": "+1-555-0123"
              },
              "preferences": ["email", "sms"]
            }
          },
          "orders": [
            {"id": 101, "amount": 250.00, "items": ["laptop", "mouse"]},
            {"id": 102, "amount": 75.50, "items": ["keyboard"]}
          ],
          "metadata": {
            "created_at": "2024-01-15T10:30:00Z",
            "source": "web"
          }
        },
        {
          "customer": {
            "id": 2,
            "profile": {
              "name": "Jane Smith",
              "contact": {
                "email": "jane@example.com",
                "phone": "+1-555-0456"
              },
              "preferences": ["email"]
            }
          },
          "orders": [
            {"id": 103, "amount": 150.00, "items": ["monitor"]}
          ],
          "metadata": {
            "created_at": "2024-01-16T14:20:00Z",
            "source": "mobile"
          }
        }
      ]
      """
    When I execute the pipeline stage by stage
    Then the pipeline execution should be successful
    And stage "JSON Parser" should complete successfully
    And stage "Customer Extractor" should process 2 records
    And stage "Order Flattener" should process 3 records
    And stage "Contact Normalizer" should complete successfully
    And stage "Final Aggregation" should complete successfully

  @WebTest @DataTypes @Validation
  Scenario: Test pipeline with various data types and null values
    When I navigate to Pipelines section
    And I open pipeline "Data Type Validation Pipeline"
    And I have custom tabular data:
      | id | name    | age | salary  | is_active | join_date  | score | notes        |
      | 1  | Alice   | 28  | 75000.5 | true      | 2023-01-15 | 8.5   | Good performer |
      | 2  | Bob     |     | 65000   | false     | 2022-06-10 | 7.2   |              |
      | 3  |         | 35  |         | true      |            | 9.1   | Excellent    |
      | 4  | Charlie | 42  | 85000.0 | true      | 2021-03-20 |       | Team lead    |
    When I execute the pipeline stage by stage
    Then the pipeline execution should be successful
    And stage "Data Type Validator" should complete successfully
    And stage "Null Handler" should process 4 records
    And stage "Type Converter" should complete successfully
    And stage "Data Cleaner" should complete successfully

  @WebTest @LargeDataset @Performance
  Scenario: Test pipeline performance with large dataset
    When I navigate to Pipelines section
    And I open pipeline "Performance Test Pipeline"
    And I have custom JSON data:
      """
      [
        {"batch_id": 1, "records": 10000, "data_type": "customer"},
        {"batch_id": 2, "records": 15000, "data_type": "transaction"},
        {"batch_id": 3, "records": 5000, "data_type": "product"}
      ]
      """
    When I execute the pipeline stage by stage
    Then the pipeline execution should be successful
    And stage "Batch Processor" should complete successfully
    And stage "Data Aggregator" should process 3 records
    And stage "Performance Monitor" should complete successfully
    And the execution should complete within 120 seconds

  @WebTest @ErrorRecovery @StageByStage
  Scenario: Test pipeline error recovery and retry mechanisms
    When I navigate to Pipelines section
    And I open pipeline "Error Recovery Pipeline"
    And I have custom tabular data:
      | record_id | data_quality | processing_flag | retry_count |
      | 1         | good         | process         | 0           |
      | 2         | corrupted    | skip           | 3           |
      | 3         | good         | process         | 0           |
      | 4         | invalid      | retry          | 1           |
    When I execute the pipeline stage by stage
    Then stage "Data Quality Check" should complete successfully
    And stage "Error Handler" should process 4 records
    And stage "Retry Logic" should complete successfully
    And stage "Final Processor" should process at least 2 records

  @WebTest @ConditionalLogic @StageByStage
  Scenario: Test pipeline with conditional logic and branching
    When I navigate to Pipelines section
    And I open pipeline "Conditional Logic Pipeline"
    And I have custom tabular data:
      | user_type | subscription | region | priority |
      | premium   | active       | US     | high     |
      | basic     | active       | EU     | medium   |
      | premium   | expired      | US     | low      |
      | trial     | active       | APAC   | medium   |
    When I execute the pipeline stage by stage
    Then stage "User Classifier" should complete successfully
    And stage "Region Router" should process 4 records
    And stage "Premium Branch" should process at least 1 records
    And stage "Basic Branch" should process at least 1 records
    And stage "Priority Merger" should complete successfully

  @WebTest @DataTransformation @StageByStage
  Scenario: Test complex data transformations
    When I navigate to Pipelines section
    And I open pipeline "Data Transformation Pipeline"
    And I have custom JSON data:
      """
      [
        {
          "raw_data": "2024-01-15|John Doe|john@email.com|New York,NY,10001|Software Engineer",
          "format": "pipe_delimited",
          "encoding": "utf8"
        },
        {
          "raw_data": "2024-01-16;Jane Smith;jane@email.com;Los Angeles,CA,90210;Data Scientist",
          "format": "semicolon_delimited",
          "encoding": "utf8"
        },
        {
          "raw_data": "2024-01-17,Bob Wilson,bob@email.com,Chicago,IL,60601,Product Manager",
          "format": "comma_delimited",
          "encoding": "utf8"
        }
      ]
      """
    When I execute the pipeline stage by stage
    Then stage "Format Detector" should complete successfully
    And stage "Dynamic Parser" should process 3 records
    And stage "Address Splitter" should complete successfully
    And stage "Data Normalizer" should complete successfully
    And stage "Schema Validator" should complete successfully

  @WebTest @RealTimeData @StageByStage
  Scenario: Test pipeline with streaming/real-time data simulation
    When I navigate to Pipelines section
    And I open pipeline "Real Time Pipeline"
    And I have custom tabular data:
      | timestamp           | event_type | user_id | session_id | action     | value |
      | 2024-01-15T10:00:00 | click      | 1001    | sess_001   | button     | login |
      | 2024-01-15T10:00:05 | view       | 1001    | sess_001   | page       | home  |
      | 2024-01-15T10:00:10 | click      | 1002    | sess_002   | link       | about |
      | 2024-01-15T10:00:15 | purchase   | 1001    | sess_001   | product    | 123   |
    When I execute the pipeline stage by stage
    Then stage "Event Ingester" should complete successfully
    And stage "Session Tracker" should process 4 records
    And stage "Event Aggregator" should complete successfully
    And stage "Real Time Analytics" should complete successfully

  @WebTest @DataQuality @Validation
  Scenario: Test comprehensive data quality validation
    When I navigate to Pipelines section
    And I open pipeline "Data Quality Pipeline"
    And I have custom tabular data:
      | email              | phone        | age | income | postal_code |
      | valid@email.com    | 555-123-4567 | 25  | 50000  | 12345       |
      | invalid-email      | 555-999-0000 | 150 | -1000  | INVALID     |
      | test@domain.co.uk  | +44-20-1234  | 30  | 75000  | SW1A 1AA    |
      |                    | 555-000-0000 | 0   | 0      | 00000       |
    When I execute the pipeline stage by stage
    Then stage "Email Validator" should complete successfully
    And stage "Phone Validator" should complete successfully
    And stage "Range Validator" should complete successfully
    And stage "Format Validator" should complete successfully
    And stage "Quality Reporter" should process 4 records

  @WebTest @MultiSource @Integration
  Scenario: Test pipeline with multiple data sources
    When I navigate to Pipelines section
    And I open pipeline "Multi Source Pipeline"
    And I have custom JSON data:
      """
      {
        "customers": [
          {"id": 1, "name": "Alice", "segment": "premium"},
          {"id": 2, "name": "Bob", "segment": "standard"}
        ],
        "transactions": [
          {"id": 101, "customer_id": 1, "amount": 500},
          {"id": 102, "customer_id": 2, "amount": 200},
          {"id": 103, "customer_id": 1, "amount": 300}
        ],
        "products": [
          {"id": "P1", "name": "Laptop", "category": "Electronics"},
          {"id": "P2", "name": "Book", "category": "Education"}
        ]
      }
      """
    When I execute the pipeline stage by stage
    Then stage "Customer Source" should complete successfully
    And stage "Transaction Source" should complete successfully
    And stage "Product Source" should complete successfully
    And stage "Data Joiner" should complete successfully
    And stage "Enrichment Engine" should complete successfully

  @WebTest @CustomFunctions @StageByStage
  Scenario: Test pipeline with custom functions and expressions
    When I navigate to Pipelines section
    And I open pipeline "Custom Functions Pipeline"
    And I have custom tabular data:
      | input_text           | calculation_base | date_string  | json_field                    |
      | Hello World          | 100              | 2024-01-15   | {"key": "value", "num": 42}   |
      | Data Engineering     | 250              | 2024-02-20   | {"items": [1, 2, 3]}          |
      | Pipeline Testing     | 75               | 2024-03-10   | {"nested": {"deep": "data"}}  |
    When I execute the pipeline stage by stage
    Then stage "Text Processor" should complete successfully
    And stage "Math Calculator" should process 3 records
    And stage "Date Parser" should complete successfully
    And stage "JSON Extractor" should complete successfully
    And stage "Custom Aggregator" should complete successfully