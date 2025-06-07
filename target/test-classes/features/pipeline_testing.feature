@ProphecyWebTest
Feature: Prophecy Web Testing - Pipeline Execution with Custom Data
  As a data engineer
  I want to test Prophecy pipelines through the web interface
  So that I can validate pipeline execution with custom data sources

  Background:
    Given I navigate to Prophecy login page "https://app.prophecy.io"
    When I login with SSO
    Then I should be logged in successfully

  @WebTest @Login @SSO
  Scenario: Successful login to Prophecy with SSO
    Given I navigate to Prophecy login page "https://app.prophecy.io"
    When I login with SSO
    Then I should be logged in successfully
    And I should see the dashboard

  @WebTest @Login @SmartLogin
  Scenario: Successful login to Prophecy with Smart Login (Auto-detect SSO)
    Given I navigate to Prophecy login page "https://app.prophecy.io"
    When I login using smart login
    Then I should be logged in successfully
    And I should see the dashboard

  @WebTest @Login @SSO @Manual
  Scenario: Manual SSO login process
    Given I navigate to Prophecy login page "https://app.prophecy.io"
    When I click SSO login button
    Then I should be redirected to SSO provider
    And I should be logged in successfully after SSO authentication

  @WebTest @Login @Negative
  Scenario: Failed login with invalid credentials
    Given I navigate to Prophecy login page "https://app.prophecy.io"
    When I login with username "invalid@user.com" and password "wrongpassword"
    Then I should see login error message

  @WebTest @Navigation
  Scenario: Navigate to pipelines section
    When I navigate to Pipelines section
    Then I should see the pipelines list
    And I should see at least 1 pipeline in the list

  @WebTest @PipelineExecution @StageByStage
  Scenario: Execute pipeline stage by stage with custom JSON data
    When I navigate to Pipelines section
    And I open pipeline "Customer Data Pipeline"
    And I have custom JSON data:
      """
      [
        {"id": 1, "name": "John Doe", "age": 30, "city": "New York"},
        {"id": 2, "name": "Jane Smith", "age": 25, "city": "Los Angeles"},
        {"id": 3, "name": "Bob Johnson", "age": 35, "city": "Chicago"}
      ]
      """
    When I execute the pipeline stage by stage
    Then the pipeline execution should be successful
    And all stages should complete successfully
    And stage "Data Source" should process 3 records

  @WebTest @PipelineExecution @CustomData
  Scenario: Execute pipeline with custom tabular data
    When I navigate to Pipelines section
    And I open pipeline "Sales Data Pipeline"
    And I have custom tabular data:
      | product_id | product_name | price | category   | quantity |
      | 1          | Laptop       | 999.99| Electronics| 10       |
      | 2          | Mouse        | 29.99 | Electronics| 50       |
      | 3          | Keyboard     | 79.99 | Electronics| 25       |
      | 4          | Monitor      | 299.99| Electronics| 15       |
    When I execute the pipeline stage by stage
    Then the pipeline execution should be successful
    And stage "Product Source" should process 4 records
    And stage "Price Calculation" should complete successfully
    And stage "Category Filter" should complete successfully
    And stage "Output Target" should complete successfully

  @WebTest @PipelineExecution @FileUpload
  Scenario: Execute pipeline with uploaded data file
    When I navigate to Pipelines section
    And I open pipeline "File Processing Pipeline"
    And I upload custom data file "/path/to/test-data.csv"
    When I execute the pipeline stage by stage
    Then the pipeline execution should be successful
    And all stages should complete successfully

  @WebTest @StageValidation
  Scenario: Validate individual stage execution and data flow
    When I navigate to Pipelines section
    And I open pipeline "Data Transformation Pipeline"
    And I have custom tabular data:
      | user_id | first_name | last_name | email           | age | status |
      | 1       | Alice      | Johnson   | alice@email.com | 28  | active |
      | 2       | Bob        | Smith     | bob@email.com   | 34  | active |
      | 3       | Carol      | Davis     | carol@email.com | 29  | inactive |
    When I execute the pipeline stage by stage
    Then stage "User Data Source" should complete successfully
    And stage "User Data Source" should process 3 records
    And stage "Name Concatenation" should complete successfully
    And stage "Name Concatenation" should process 3 records
    And stage "Email Validation" should complete successfully
    And stage "Status Filter" should complete successfully
    And stage "Status Filter" should process at least 2 records
    And stage "Final Output" should complete successfully

  @WebTest @DataValidation
  Scenario: Validate data transformation at each stage
    When I navigate to Pipelines section
    And I open pipeline "Customer Analytics Pipeline"
    And I have custom tabular data:
      | customer_id | name        | purchase_amount | purchase_date | region |
      | 1           | John Smith  | 150.00         | 2024-01-15    | North  |
      | 2           | Jane Doe    | 250.00         | 2024-01-16    | South  |
      | 3           | Bob Wilson  | 75.00          | 2024-01-17    | East   |
    When I execute the pipeline stage by stage
    Then stage "Customer Source" should contain data
    And stage "Customer Source" data should match expected format
    And stage "Amount Calculation" should contain data
    And stage "Regional Analysis" should contain data
    And stage "Final Report" should contain data

  @WebTest @Performance
  Scenario: Validate pipeline performance with large dataset
    When I navigate to Pipelines section
    And I open pipeline "High Volume Pipeline"
    And I upload custom data file "/path/to/large-dataset.json"
    When I execute the pipeline
    Then the pipeline execution should be successful
    And the execution should complete within 60 seconds

  @WebTest @ErrorHandling
  Scenario: Handle pipeline execution errors gracefully
    When I navigate to Pipelines section
    And I open pipeline "Error Prone Pipeline"
    And I have custom JSON data:
      """
      [
        {"id": "invalid", "value": null},
        {"id": 2, "value": "test"}
      ]
      """
    When I execute the pipeline stage by stage
    Then stage "Data Validation" should have status "failed"
    And I should see execution logs
    And the error should be handled gracefully

  @WebTest @PipelineManagement
  Scenario: Stop pipeline execution
    When I navigate to Pipelines section
    And I open pipeline "Long Running Pipeline"
    And I have custom tabular data:
      | id | data |
      | 1  | test |
    When I execute the pipeline
    And I stop the pipeline execution
    Then the pipeline should be stopped

  @WebTest @Search
  Scenario: Search for specific pipeline
    When I navigate to Pipelines section
    And I search for pipeline "Customer"
    Then I should see search results for "Customer"
    And I should see pipeline "Customer Data Pipeline" in the list

  @WebTest @MultipleDataFormats
  Scenario: Test pipeline with different data formats
    When I navigate to Pipelines section
    And I open pipeline "Multi Format Pipeline"
    And I have custom JSON data:
      """
      {
        "users": [
          {"id": 1, "name": "Alice", "scores": [85, 90, 78]},
          {"id": 2, "name": "Bob", "scores": [92, 88, 95]}
        ],
        "metadata": {
          "version": "1.0",
          "timestamp": "2024-01-01T00:00:00Z"
        }
      }
      """
    When I execute the pipeline stage by stage
    Then the pipeline execution should be successful
    And stage "JSON Parser" should complete successfully
    And stage "Array Processor" should complete successfully
    And stage "Data Flattener" should complete successfully

  @WebTest @Logout
  Scenario: Logout from Prophecy
    When I logout from Prophecy
    Then I should be redirected to login page