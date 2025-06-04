@data @validation
Feature: Data Validation and Quality Checks
  As a data engineer
  I want to validate data quality throughout the pipeline
  So that I can ensure data meets business requirements and quality standards

  Background:
    Given I am logged into Prophecy application
    And I have access to test data with various quality scenarios

  @smoke @positive
  Scenario: Validate data schema compliance
    Given I have a pipeline with expected schema:
      | field_name    | data_type | nullable | constraints           |
      | customer_id   | String    | false    | unique, not_empty     |
      | first_name    | String    | false    | not_empty, max_50     |
      | last_name     | String    | false    | not_empty, max_50     |
      | email         | String    | true     | email_format          |
      | age           | Integer   | true     | min_0, max_120        |
      | created_date  | Date      | false    | not_future            |
    And I have input data that matches the schema
    When I execute the pipeline with schema validation
    Then all records should pass schema validation
    And no schema violation errors should be reported
    And the data should proceed to the next stage

  @positive
  Scenario: Validate data completeness
    Given I have a pipeline with completeness rules:
      | field_name    | completeness_threshold |
      | customer_id   | 100%                   |
      | first_name    | 100%                   |
      | last_name     | 100%                   |
      | email         | 80%                    |
      | phone_number  | 70%                    |
    And I have input data with varying completeness levels
    When I execute the pipeline with completeness validation
    Then the completeness metrics should be calculated
    And fields meeting the threshold should pass validation
    And fields below the threshold should trigger warnings
    And a completeness report should be generated

  @positive
  Scenario: Validate data uniqueness
    Given I have a pipeline with uniqueness rules for "customer_id"
    And I have input data with some duplicate customer IDs
    When I execute the pipeline with uniqueness validation
    Then duplicate records should be identified
    And the first occurrence should be kept
    And subsequent duplicates should be flagged
    And a duplicate report should be generated with:
      | metric                | expected_value |
      | total_records         | 1000          |
      | unique_records        | 950           |
      | duplicate_records     | 50            |
      | uniqueness_percentage | 95%           |

  @positive
  Scenario: Validate data format and patterns
    Given I have a pipeline with format validation rules:
      | field_name    | format_rule                    |
      | email         | valid_email_format             |
      | phone_number  | US_phone_format                |
      | postal_code   | US_zip_code_format             |
      | ssn           | SSN_format                     |
      | credit_card   | valid_credit_card_format       |
    And I have input data with various format compliance levels
    When I execute the pipeline with format validation
    Then each field should be validated against its format rule
    And format compliance percentage should be calculated
    And invalid format records should be flagged
    And a format validation report should be generated

  @positive
  Scenario: Validate data ranges and business rules
    Given I have a pipeline with business rule validation:
      | rule_name              | rule_description                    | field_names           |
      | age_range              | Age must be between 0 and 120      | age                   |
      | salary_range           | Salary must be between 0 and 1M     | salary                |
      | date_range             | Date must not be in the future      | created_date          |
      | email_domain           | Email must be from allowed domains  | email                 |
      | state_code             | State code must be valid US state   | state_code            |
    And I have input data with various business rule scenarios
    When I execute the pipeline with business rule validation
    Then each business rule should be evaluated
    And rule compliance should be measured
    And rule violations should be documented
    And corrective actions should be suggested

  @positive
  Scenario: Validate data consistency across related fields
    Given I have a pipeline with consistency rules:
      | rule_name                | rule_description                           |
      | address_consistency      | City, State, ZIP must be consistent       |
      | date_consistency         | End date must be after start date         |
      | amount_consistency       | Total must equal sum of line items        |
      | status_consistency       | Status must match business logic          |
    And I have input data with consistency scenarios
    When I execute the pipeline with consistency validation
    Then consistency rules should be evaluated
    And inconsistent records should be identified
    And consistency metrics should be calculated
    And recommendations for data correction should be provided

  @positive
  Scenario: Validate data freshness and timeliness
    Given I have a pipeline with freshness requirements:
      | data_source        | max_age_hours | critical_threshold |
      | customer_updates   | 24            | true               |
      | transaction_data   | 4             | true               |
      | reference_data     | 168           | false              |
    And I have input data with various timestamps
    When I execute the pipeline with freshness validation
    Then data freshness should be calculated for each source
    And stale data should be identified
    And critical freshness violations should trigger alerts
    And a freshness report should be generated

  @negative
  Scenario: Handle data with multiple validation failures
    Given I have a pipeline with comprehensive validation rules
    And I have input data with multiple quality issues:
      | issue_type          | affected_records |
      | missing_values      | 50               |
      | invalid_format      | 30               |
      | duplicate_keys      | 20               |
      | range_violations    | 25               |
      | consistency_errors  | 15               |
    When I execute the pipeline with all validation rules
    Then all validation issues should be detected
    And each issue should be categorized and counted
    And a comprehensive quality report should be generated
    And the pipeline should handle the failures gracefully

  @negative
  Scenario: Handle data that completely fails validation
    Given I have a pipeline with strict validation rules
    And I have input data where 100% of records fail validation
    When I execute the pipeline
    Then the validation stage should complete
    And all records should be rejected
    And no data should proceed to subsequent stages
    And an alert should be triggered for complete data failure
    And detailed failure reasons should be provided

  @performance
  Scenario: Validate large dataset performance
    Given I have a pipeline with comprehensive validation rules
    And I have a large dataset with 1 million records
    When I execute the pipeline with validation
    Then validation should complete within acceptable time limits
    And memory usage should remain within bounds
    And validation performance metrics should be tracked:
      | metric                    | threshold        |
      | records_per_second        | > 10000         |
      | memory_usage              | < 4 GB          |
      | validation_time           | < 600 seconds   |
    And the validation results should be accurate

  @integration
  Scenario: Integrate validation with data lineage tracking
    Given I have a pipeline with validation and lineage tracking enabled
    When I execute the pipeline with validation
    Then validation results should be linked to data lineage
    And I should be able to trace validation failures to source data
    And validation metrics should be stored for historical analysis
    And data quality trends should be trackable over time

  @real-time
  Scenario: Validate streaming data in real-time
    Given I have a streaming pipeline with real-time validation
    And I have a continuous stream of incoming data
    When the pipeline processes the streaming data
    Then validation should occur in real-time
    And validation results should be immediately available
    And quality metrics should be updated continuously
    And alerts should be triggered for quality threshold breaches
    And the stream processing should not be significantly impacted

  @custom-rules
  Scenario: Apply custom validation rules
    Given I have a pipeline with custom validation logic:
      | rule_name              | rule_logic                                    |
      | customer_tier_logic    | Premium customers must have salary > 100K    |
      | geographic_logic       | Shipping address must match billing country  |
      | temporal_logic         | Order date must be within business hours     |
      | cross_reference_logic  | Product codes must exist in catalog          |
    And I have input data for custom rule testing
    When I execute the pipeline with custom validation rules
    Then custom rules should be executed correctly
    And rule results should be properly categorized
    And custom rule performance should be acceptable
    And rule logic should be easily maintainable