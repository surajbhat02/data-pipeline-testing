@ProphecyEdgeCases
Feature: Prophecy Edge Cases and Boundary Testing
  As a data engineer
  I want to test edge cases and boundary conditions in Prophecy pipelines
  So that I can ensure robust handling of unusual data scenarios

  Background:
    Given I navigate to Prophecy login page "https://app.prophecy.io"
    When I login with username "test@example.com" and password "password123"
    Then I should be logged in successfully

  @WebTest @EdgeCase @EmptyData
  Scenario: Test pipeline with empty dataset
    When I navigate to Pipelines section
    And I open pipeline "Empty Data Handler Pipeline"
    And I have custom JSON data:
      """
      []
      """
    When I execute the pipeline stage by stage
    Then stage "Empty Data Detector" should complete successfully
    And stage "Default Value Generator" should complete successfully
    And stage "Empty Handler" should process 0 records

  @WebTest @EdgeCase @SingleRecord
  Scenario: Test pipeline with single record
    When I navigate to Pipelines section
    And I open pipeline "Single Record Pipeline"
    And I have custom tabular data:
      | id | name | value |
      | 1  | Test | 100   |
    When I execute the pipeline stage by stage
    Then stage "Single Record Processor" should process 1 records
    And stage "Aggregation Handler" should complete successfully
    And stage "Output Generator" should process 1 records

  @WebTest @EdgeCase @SpecialCharacters
  Scenario: Test pipeline with special characters and unicode
    When I navigate to Pipelines section
    And I open pipeline "Special Characters Pipeline"
    And I have custom tabular data:
      | name                | description                    | symbols        |
      | José María          | Café & Résumé                 | @#$%^&*()      |
      | 北京                | 中文测试数据                    | ¥€£¢           |
      | Müller & Søn        | Spëcîál chäracters tëst       | ±×÷≠≤≥         |
      | O'Connor-Smith      | "Quoted" & 'Apostrophe' test  | <>{}[]         |
    When I execute the pipeline stage by stage
    Then stage "Unicode Handler" should complete successfully
    And stage "Special Char Processor" should process 4 records
    And stage "Encoding Validator" should complete successfully

  @WebTest @EdgeCase @ExtremeValues
  Scenario: Test pipeline with extreme numerical values
    When I navigate to Pipelines section
    And I open pipeline "Extreme Values Pipeline"
    And I have custom tabular data:
      | id | tiny_number        | huge_number           | precision_number      | negative_extreme    |
      | 1  | 0.000000001        | 999999999999999999    | 3.141592653589793     | -999999999999999    |
      | 2  | 0                  | 1                     | 0.000000000000001     | -1                  |
      | 3  | -0.000000001       | 1000000000000000000   | 2.718281828459045     | -1000000000000000   |
    When I execute the pipeline stage by stage
    Then stage "Number Validator" should complete successfully
    And stage "Precision Handler" should process 3 records
    And stage "Range Checker" should complete successfully
    And stage "Math Operations" should complete successfully

  @WebTest @EdgeCase @DateTimeBoundaries
  Scenario: Test pipeline with edge case dates and times
    When I navigate to Pipelines section
    And I open pipeline "DateTime Edge Cases Pipeline"
    And I have custom tabular data:
      | event_date | event_time           | timezone    | epoch_time    |
      | 1900-01-01 | 00:00:00.000         | UTC         | 0             |
      | 2099-12-31 | 23:59:59.999         | PST         | 2147483647    |
      | 2000-02-29 | 12:00:00.000         | EST         | 951825600     |
      | 1970-01-01 | 00:00:00.001         | GMT         | 1             |
    When I execute the pipeline stage by stage
    Then stage "Date Validator" should complete successfully
    And stage "Timezone Converter" should process 4 records
    And stage "Epoch Handler" should complete successfully
    And stage "Leap Year Processor" should complete successfully

  @WebTest @EdgeCase @MalformedData
  Scenario: Test pipeline with malformed and corrupted data
    When I navigate to Pipelines section
    And I open pipeline "Malformed Data Pipeline"
    And I have custom JSON data:
      """
      [
        {"id": 1, "data": "valid_record"},
        {"id": "not_a_number", "data": null},
        {"incomplete": "missing_id"},
        {"id": 3, "data": "valid_record", "extra_field": "unexpected"},
        {"id": 4, "data": {"nested": "should_be_string"}},
        {"id": 5}
      ]
      """
    When I execute the pipeline stage by stage
    Then stage "Data Validator" should complete successfully
    And stage "Error Classifier" should process 6 records
    And stage "Data Repair" should complete successfully
    And stage "Quality Filter" should process at least 2 records

  @WebTest @EdgeCase @MemoryIntensive
  Scenario: Test pipeline with memory-intensive operations
    When I navigate to Pipelines section
    And I open pipeline "Memory Intensive Pipeline"
    And I have custom tabular data:
      | operation_type | data_size | complexity | iterations |
      | sort           | large     | high       | 1000       |
      | aggregate      | medium    | medium     | 500        |
      | join           | large     | high       | 2000       |
      | transform      | small     | low        | 100        |
    When I execute the pipeline stage by stage
    Then stage "Memory Monitor" should complete successfully
    And stage "Resource Manager" should process 4 records
    And stage "Optimization Engine" should complete successfully

  @WebTest @EdgeCase @ConcurrentAccess
  Scenario: Test pipeline with concurrent data access patterns
    When I navigate to Pipelines section
    And I open pipeline "Concurrent Access Pipeline"
    And I have custom tabular data:
      | session_id | user_id | action_type | timestamp           | resource_id |
      | sess_001   | user_1  | read        | 2024-01-15T10:00:00 | res_001     |
      | sess_002   | user_2  | write       | 2024-01-15T10:00:01 | res_001     |
      | sess_003   | user_1  | read        | 2024-01-15T10:00:02 | res_002     |
      | sess_004   | user_3  | write       | 2024-01-15T10:00:03 | res_001     |
    When I execute the pipeline stage by stage
    Then stage "Concurrency Detector" should complete successfully
    And stage "Lock Manager" should process 4 records
    And stage "Conflict Resolver" should complete successfully

  @WebTest @EdgeCase @NetworkTimeout
  Scenario: Test pipeline resilience to network timeouts
    When I navigate to Pipelines section
    And I open pipeline "Network Resilience Pipeline"
    And I have custom tabular data:
      | request_id | endpoint        | timeout_ms | retry_count |
      | req_001    | /api/data/fast  | 1000       | 0           |
      | req_002    | /api/data/slow  | 30000      | 3           |
      | req_003    | /api/data/error | 5000       | 5           |
    When I execute the pipeline stage by stage
    Then stage "Network Monitor" should complete successfully
    And stage "Timeout Handler" should process 3 records
    And stage "Retry Logic" should complete successfully

  @WebTest @EdgeCase @CircularReferences
  Scenario: Test pipeline with circular reference detection
    When I navigate to Pipelines section
    And I open pipeline "Circular Reference Pipeline"
    And I have custom JSON data:
      """
      [
        {"id": "A", "depends_on": ["B", "C"]},
        {"id": "B", "depends_on": ["C"]},
        {"id": "C", "depends_on": ["A"]},
        {"id": "D", "depends_on": ["E"]},
        {"id": "E", "depends_on": []}
      ]
      """
    When I execute the pipeline stage by stage
    Then stage "Dependency Analyzer" should complete successfully
    And stage "Circular Detector" should process 5 records
    And stage "Graph Resolver" should complete successfully

  @WebTest @EdgeCase @DataTypeConflicts
  Scenario: Test pipeline with data type conflicts and coercion
    When I navigate to Pipelines section
    And I open pipeline "Type Conflict Pipeline"
    And I have custom tabular data:
      | field_name | value_1 | value_2 | value_3 | expected_type |
      | age        | 25      | "30"    | 35.0    | integer       |
      | price      | "19.99" | 25      | 30.50   | decimal       |
      | active     | true    | "yes"   | 1       | boolean       |
      | date       | "2024-01-15" | 1705276800 | "Jan 15, 2024" | date |
    When I execute the pipeline stage by stage
    Then stage "Type Detector" should complete successfully
    And stage "Conflict Resolver" should process 4 records
    And stage "Type Coercion" should complete successfully
    And stage "Validation Engine" should complete successfully

  @WebTest @EdgeCase @InfiniteLoops
  Scenario: Test pipeline protection against infinite loops
    When I navigate to Pipelines section
    And I open pipeline "Loop Protection Pipeline"
    And I have custom tabular data:
      | process_id | max_iterations | condition_type | break_condition |
      | proc_001   | 1000          | counter        | 100             |
      | proc_002   | 5000          | condition      | value > 50      |
      | proc_003   | 10000         | timeout        | 30 seconds      |
    When I execute the pipeline stage by stage
    Then stage "Loop Monitor" should complete successfully
    And stage "Iteration Counter" should process 3 records
    And stage "Break Condition Checker" should complete successfully
    And stage "Safety Validator" should complete successfully

  @WebTest @EdgeCase @ResourceExhaustion
  Scenario: Test pipeline behavior under resource exhaustion
    When I navigate to Pipelines section
    And I open pipeline "Resource Exhaustion Pipeline"
    And I have custom tabular data:
      | resource_type | usage_percent | threshold | action      |
      | memory        | 95           | 90        | throttle    |
      | cpu           | 85           | 80        | queue       |
      | disk          | 98           | 95        | cleanup     |
      | network       | 75           | 70        | continue    |
    When I execute the pipeline stage by stage
    Then stage "Resource Monitor" should complete successfully
    And stage "Threshold Checker" should process 4 records
    And stage "Action Executor" should complete successfully
    And stage "Recovery Manager" should complete successfully