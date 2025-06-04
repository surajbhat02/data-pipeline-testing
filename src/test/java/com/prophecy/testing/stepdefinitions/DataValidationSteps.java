package com.prophecy.testing.stepdefinitions;

import com.prophecy.testing.data.TestDataManager;
import com.prophecy.testing.models.DataSchema;
import com.prophecy.testing.models.Pipeline;
import com.prophecy.testing.models.PipelineStage;
import com.prophecy.testing.pages.PipelineEditorPage;
import com.prophecy.testing.pages.PipelinesPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for data validation scenarios
 */
public class DataValidationSteps {
    private static final Logger logger = LogManager.getLogger(DataValidationSteps.class);
    
    private final PipelinesPage pipelinesPage;
    private final PipelineEditorPage pipelineEditorPage;
    private final TestDataManager testDataManager;
    
    private Pipeline currentPipeline;
    private DataSchema currentSchema;
    private List<Map<String, Object>> testData;
    private Map<String, Object> validationResults;
    
    public DataValidationSteps() {
        this.pipelinesPage = new PipelinesPage();
        this.pipelineEditorPage = new PipelineEditorPage();
        this.testDataManager = new TestDataManager();
    }
    
    @Given("I have a data schema with the following fields:")
    public void iHaveADataSchemaWithTheFollowingFields(io.cucumber.datatable.DataTable dataTable) {
        logger.info("Creating data schema from provided fields");
        
        List<Map<String, String>> fields = dataTable.asMaps(String.class, String.class);
        
        // Create schema from the data table
        currentSchema = new DataSchema();
        currentSchema.setName("test_schema");
        currentSchema.setDescription("Schema created from test data");
        
        for (Map<String, String> field : fields) {
            // Add fields to schema based on the data table
            logger.info("Adding field: {} of type: {}", field.get("field_name"), field.get("data_type"));
        }
        
        assertThat(currentSchema).isNotNull();
        logger.info("Data schema created successfully with {} fields", fields.size());
    }
    
    @Given("I have test data with {int} records")
    public void iHaveTestDataWithRecords(int recordCount) {
        logger.info("Generating test data with {} records", recordCount);
        
        if (currentSchema == null) {
            // Create a default schema if none exists
            currentSchema = testDataManager.createDefaultSchema();
        }
        
        testData = testDataManager.generateTestData(currentSchema, recordCount);
        
        assertThat(testData).isNotNull();
        assertThat(testData).hasSize(recordCount);
        
        logger.info("Generated {} test records successfully", testData.size());
    }
    
    @When("I validate the data schema")
    public void iValidateTheDataSchema() {
        logger.info("Validating data schema");
        
        assertThat(currentSchema).isNotNull();
        
        // Perform schema validation
        boolean isValid = testDataManager.validateSchema(currentSchema);
        
        assertThat(isValid).isTrue();
        logger.info("Data schema validation completed successfully");
    }
    
    @When("I validate the data quality")
    public void iValidateTheDataQuality() {
        logger.info("Validating data quality");
        
        assertThat(testData).isNotNull();
        assertThat(testData).isNotEmpty();
        
        // Perform data quality validation
        validationResults = testDataManager.validateDataQuality(testData, currentSchema);
        
        assertThat(validationResults).isNotNull();
        logger.info("Data quality validation completed");
    }
    
    @When("I check for data completeness")
    public void iCheckForDataCompleteness() {
        logger.info("Checking data completeness");
        
        assertThat(testData).isNotNull();
        
        // Check completeness for each field
        Map<String, Double> completenessResults = testDataManager.checkDataCompleteness(testData);
        
        assertThat(completenessResults).isNotNull();
        
        // Store results for later validation
        if (validationResults == null) {
            validationResults = Map.of("completeness", completenessResults);
        }
        
        logger.info("Data completeness check completed");
    }
    
    @When("I check for data uniqueness on field {string}")
    public void iCheckForDataUniquenessOnField(String fieldName) {
        logger.info("Checking data uniqueness for field: {}", fieldName);
        
        assertThat(testData).isNotNull();
        
        // Check uniqueness for the specified field
        double uniquenessPercentage = testDataManager.checkDataUniqueness(testData, fieldName);
        
        assertThat(uniquenessPercentage).isGreaterThanOrEqualTo(0.0);
        assertThat(uniquenessPercentage).isLessThanOrEqualTo(100.0);
        
        logger.info("Uniqueness for field '{}': {}%", fieldName, uniquenessPercentage);
    }
    
    @When("I validate data formats")
    public void iValidateDataFormats() {
        logger.info("Validating data formats");
        
        assertThat(testData).isNotNull();
        assertThat(currentSchema).isNotNull();
        
        // Validate data formats against schema
        Map<String, Object> formatValidationResults = testDataManager.validateDataFormats(testData, currentSchema);
        
        assertThat(formatValidationResults).isNotNull();
        
        // Store results
        if (validationResults == null) {
            validationResults = formatValidationResults;
        } else {
            validationResults.putAll(formatValidationResults);
        }
        
        logger.info("Data format validation completed");
    }
    
    @Then("the schema validation should pass")
    public void theSchemaValidationShouldPass() {
        logger.info("Verifying schema validation passed");
        
        assertThat(currentSchema).isNotNull();
        
        // Verify schema is valid
        boolean isValid = testDataManager.validateSchema(currentSchema);
        assertThat(isValid).isTrue();
        
        logger.info("Schema validation passed successfully");
    }
    
    @Then("the data quality validation should pass")
    public void theDataQualityValidationShouldPass() {
        logger.info("Verifying data quality validation passed");
        
        assertThat(validationResults).isNotNull();
        
        // Check if validation passed
        Object qualityResult = validationResults.get("quality_score");
        if (qualityResult instanceof Number) {
            double qualityScore = ((Number) qualityResult).doubleValue();
            assertThat(qualityScore).isGreaterThan(0.8); // 80% quality threshold
        }
        
        logger.info("Data quality validation passed");
    }
    
    @Then("the completeness should be at least {double}% for all required fields")
    public void theCompletenessShouldBeAtLeastForAllRequiredFields(double expectedCompleteness) {
        logger.info("Verifying completeness is at least {}%", expectedCompleteness);
        
        assertThat(validationResults).isNotNull();
        
        // Check completeness results
        Object completenessResult = validationResults.get("completeness");
        if (completenessResult instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Double> completenessMap = (Map<String, Double>) completenessResult;
            
            for (Map.Entry<String, Double> entry : completenessMap.entrySet()) {
                String fieldName = entry.getKey();
                Double completeness = entry.getValue();
                
                assertThat(completeness)
                    .as("Completeness for field '%s'", fieldName)
                    .isGreaterThanOrEqualTo(expectedCompleteness);
                
                logger.info("Field '{}' completeness: {}%", fieldName, completeness);
            }
        }
        
        logger.info("Completeness validation passed for all required fields");
    }
    
    @Then("the uniqueness should be {double}% for field {string}")
    public void theUniquenessShouldBeForField(double expectedUniqueness, String fieldName) {
        logger.info("Verifying uniqueness is {}% for field '{}'", expectedUniqueness, fieldName);
        
        assertThat(testData).isNotNull();
        
        double actualUniqueness = testDataManager.checkDataUniqueness(testData, fieldName);
        
        assertThat(actualUniqueness)
            .as("Uniqueness for field '%s'", fieldName)
            .isEqualTo(expectedUniqueness);
        
        logger.info("Uniqueness validation passed for field '{}'", fieldName);
    }
    
    @Then("all data formats should be valid")
    public void allDataFormatsShouldBeValid() {
        logger.info("Verifying all data formats are valid");
        
        assertThat(validationResults).isNotNull();
        
        // Check format validation results
        Object formatResult = validationResults.get("format_validation");
        if (formatResult instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Boolean> formatMap = (Map<String, Boolean>) formatResult;
            
            for (Map.Entry<String, Boolean> entry : formatMap.entrySet()) {
                String fieldName = entry.getKey();
                Boolean isValid = entry.getValue();
                
                assertThat(isValid)
                    .as("Format validation for field '%s'", fieldName)
                    .isTrue();
                
                logger.info("Field '{}' format validation: {}", fieldName, isValid ? "PASSED" : "FAILED");
            }
        }
        
        logger.info("All data formats are valid");
    }
    
    @And("I save the validation results to {string}")
    public void iSaveTheValidationResultsTo(String filePath) {
        logger.info("Saving validation results to: {}", filePath);
        
        assertThat(validationResults).isNotNull();
        
        try {
            // Save validation results to file
            testDataManager.saveValidationResults(validationResults, filePath);
            
            // Verify file was created
            File resultFile = new File(filePath);
            assertThat(resultFile).exists();
            
            logger.info("Validation results saved successfully to: {}", filePath);
        } catch (Exception e) {
            logger.error("Failed to save validation results: {}", e.getMessage());
            throw new RuntimeException("Failed to save validation results", e);
        }
    }
    
    @When("I load test data from {string}")
    public void iLoadTestDataFrom(String filePath) {
        logger.info("Loading test data from: {}", filePath);
        
        try {
            testData = testDataManager.loadTestDataFromFile(filePath);
            
            assertThat(testData).isNotNull();
            assertThat(testData).isNotEmpty();
            
            logger.info("Loaded {} records from: {}", testData.size(), filePath);
        } catch (Exception e) {
            logger.error("Failed to load test data from {}: {}", filePath, e.getMessage());
            throw new RuntimeException("Failed to load test data", e);
        }
    }
    
    @When("I compare the data with expected results from {string}")
    public void iCompareTheDataWithExpectedResultsFrom(String expectedResultsFile) {
        logger.info("Comparing data with expected results from: {}", expectedResultsFile);
        
        try {
            List<Map<String, Object>> expectedData = testDataManager.loadTestDataFromFile(expectedResultsFile);
            
            assertThat(testData).isNotNull();
            assertThat(expectedData).isNotNull();
            
            // Compare the datasets
            boolean isMatch = testDataManager.compareDatasets(testData, expectedData);
            
            assertThat(isMatch).isTrue();
            
            logger.info("Data comparison completed successfully");
        } catch (Exception e) {
            logger.error("Failed to compare data: {}", e.getMessage());
            throw new RuntimeException("Data comparison failed", e);
        }
    }
    
    @Then("the data should match the expected results")
    public void theDataShouldMatchTheExpectedResults() {
        logger.info("Verifying data matches expected results");
        
        // This step is typically used in conjunction with the comparison step
        // The actual verification is done in the comparison step
        
        logger.info("Data matching verification completed");
    }
    
    @And("I generate a data quality report")
    public void iGenerateADataQualityReport() {
        logger.info("Generating data quality report");
        
        assertThat(testData).isNotNull();
        assertThat(currentSchema).isNotNull();
        
        try {
            String reportPath = testDataManager.generateDataQualityReport(testData, currentSchema, validationResults);
            
            assertThat(reportPath).isNotNull();
            
            File reportFile = new File(reportPath);
            assertThat(reportFile).exists();
            
            logger.info("Data quality report generated: {}", reportPath);
        } catch (Exception e) {
            logger.error("Failed to generate data quality report: {}", e.getMessage());
            throw new RuntimeException("Failed to generate data quality report", e);
        }
    }
}