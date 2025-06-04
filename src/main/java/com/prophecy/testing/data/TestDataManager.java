package com.prophecy.testing.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.prophecy.testing.config.ConfigManager;
import com.prophecy.testing.models.DataSchema;
import com.prophecy.testing.models.DataType;
import com.prophecy.testing.models.Pipeline;
import com.prophecy.testing.models.SchemaField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manager class for handling test data operations
 */
public class TestDataManager {
    private static final Logger logger = LogManager.getLogger(TestDataManager.class);
    private final ConfigManager config;
    private final ObjectMapper jsonMapper;
    private final ObjectMapper yamlMapper;
    private final MockDataGenerator mockDataGenerator;
    
    public TestDataManager() {
        this.config = ConfigManager.getInstance();
        this.jsonMapper = new ObjectMapper();
        this.yamlMapper = new ObjectMapper(new YAMLFactory());
        this.mockDataGenerator = new MockDataGenerator();
    }
    
    /**
     * Load pipeline configuration from file
     */
    public Pipeline loadPipelineConfig(String fileName) {
        try {
            String filePath = config.getPipelineConfigPath() + "/" + fileName;
            File file = new File(filePath);
            
            if (!file.exists()) {
                logger.warn("Pipeline config file not found: {}", filePath);
                return null;
            }
            
            Pipeline pipeline;
            if (fileName.endsWith(".json")) {
                pipeline = jsonMapper.readValue(file, Pipeline.class);
            } else if (fileName.endsWith(".yaml") || fileName.endsWith(".yml")) {
                pipeline = yamlMapper.readValue(file, Pipeline.class);
            } else {
                throw new IllegalArgumentException("Unsupported file format: " + fileName);
            }
            
            logger.info("Loaded pipeline configuration: {}", pipeline.getName());
            return pipeline;
        } catch (IOException e) {
            logger.error("Error loading pipeline config: {}", e.getMessage());
            throw new RuntimeException("Failed to load pipeline configuration", e);
        }
    }
    
    /**
     * Save pipeline configuration to file
     */
    public void savePipelineConfig(Pipeline pipeline, String fileName) {
        try {
            String filePath = config.getPipelineConfigPath() + "/" + fileName;
            ensureDirectoryExists(filePath);
            
            File file = new File(filePath);
            
            if (fileName.endsWith(".json")) {
                jsonMapper.writerWithDefaultPrettyPrinter().writeValue(file, pipeline);
            } else if (fileName.endsWith(".yaml") || fileName.endsWith(".yml")) {
                yamlMapper.writerWithDefaultPrettyPrinter().writeValue(file, pipeline);
            } else {
                throw new IllegalArgumentException("Unsupported file format: " + fileName);
            }
            
            logger.info("Saved pipeline configuration: {}", fileName);
        } catch (IOException e) {
            logger.error("Error saving pipeline config: {}", e.getMessage());
            throw new RuntimeException("Failed to save pipeline configuration", e);
        }
    }
    
    /**
     * Load data schema from file
     */
    public DataSchema loadDataSchema(String fileName) {
        try {
            String filePath = config.getTestDataPath() + "/schemas/" + fileName;
            File file = new File(filePath);
            
            if (!file.exists()) {
                logger.warn("Schema file not found: {}", filePath);
                return null;
            }
            
            DataSchema schema;
            if (fileName.endsWith(".json")) {
                schema = jsonMapper.readValue(file, DataSchema.class);
            } else if (fileName.endsWith(".yaml") || fileName.endsWith(".yml")) {
                schema = yamlMapper.readValue(file, DataSchema.class);
            } else {
                throw new IllegalArgumentException("Unsupported file format: " + fileName);
            }
            
            logger.info("Loaded data schema: {}", schema.getName());
            return schema;
        } catch (IOException e) {
            logger.error("Error loading data schema: {}", e.getMessage());
            throw new RuntimeException("Failed to load data schema", e);
        }
    }
    
    /**
     * Save data schema to file
     */
    public void saveDataSchema(DataSchema schema, String fileName) {
        try {
            String filePath = config.getTestDataPath() + "/schemas/" + fileName;
            ensureDirectoryExists(filePath);
            
            File file = new File(filePath);
            
            if (fileName.endsWith(".json")) {
                jsonMapper.writerWithDefaultPrettyPrinter().writeValue(file, schema);
            } else if (fileName.endsWith(".yaml") || fileName.endsWith(".yml")) {
                yamlMapper.writerWithDefaultPrettyPrinter().writeValue(file, schema);
            } else {
                throw new IllegalArgumentException("Unsupported file format: " + fileName);
            }
            
            logger.info("Saved data schema: {}", fileName);
        } catch (IOException e) {
            logger.error("Error saving data schema: {}", e.getMessage());
            throw new RuntimeException("Failed to save data schema", e);
        }
    }
    
    /**
     * Generate and save mock data for testing
     */
    public void generateMockData(String schemaFileName, String outputFileName, int recordCount) {
        DataSchema schema = loadDataSchema(schemaFileName);
        if (schema == null) {
            throw new RuntimeException("Schema not found: " + schemaFileName);
        }
        
        List<Map<String, Object>> mockData = mockDataGenerator.generateMockData(schema, recordCount);
        
        String outputPath = config.getMockDataPath() + "/" + outputFileName;
        ensureDirectoryExists(outputPath);
        
        if (outputFileName.endsWith(".csv")) {
            mockDataGenerator.saveMockDataToCsv(mockData, schema, outputPath);
        } else if (outputFileName.endsWith(".json")) {
            mockDataGenerator.saveMockDataToJson(mockData, outputPath);
        } else {
            throw new IllegalArgumentException("Unsupported output format: " + outputFileName);
        }
        
        logger.info("Generated {} mock records and saved to: {}", recordCount, outputPath);
    }
    
    /**
     * Load test data from file
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> loadTestData(String fileName) {
        try {
            String filePath = config.getTestDataPath() + "/" + fileName;
            File file = new File(filePath);
            
            if (!file.exists()) {
                logger.warn("Test data file not found: {}", filePath);
                return null;
            }
            
            if (fileName.endsWith(".json")) {
                return jsonMapper.readValue(file, List.class);
            } else {
                throw new IllegalArgumentException("Unsupported file format for test data: " + fileName);
            }
        } catch (IOException e) {
            logger.error("Error loading test data: {}", e.getMessage());
            throw new RuntimeException("Failed to load test data", e);
        }
    }
    
    /**
     * Get test data for a specific test scenario
     */
    public Map<String, Object> getTestDataForScenario(String scenarioName) {
        try {
            String filePath = config.getTestDataPath() + "/scenarios.json";
            File file = new File(filePath);
            
            if (!file.exists()) {
                logger.warn("Test scenarios file not found: {}", filePath);
                return new HashMap<>();
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Object> scenarios = jsonMapper.readValue(file, Map.class);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> scenarioData = (Map<String, Object>) scenarios.get(scenarioName);
            
            if (scenarioData == null) {
                logger.warn("Test scenario not found: {}", scenarioName);
                return new HashMap<>();
            }
            
            return scenarioData;
        } catch (IOException e) {
            logger.error("Error loading test scenario data: {}", e.getMessage());
            return new HashMap<>();
        }
    }
    
    /**
     * Ensure directory exists for the given file path
     */
    private void ensureDirectoryExists(String filePath) {
        Path path = Paths.get(filePath).getParent();
        if (path != null && !Files.exists(path)) {
            try {
                Files.createDirectories(path);
                logger.debug("Created directory: {}", path);
            } catch (IOException e) {
                logger.error("Error creating directory: {}", e.getMessage());
                throw new RuntimeException("Failed to create directory", e);
            }
        }
    }
    
    /**
     * Clean up test data files
     */
    public void cleanupTestData() {
        try {
            String mockDataPath = config.getMockDataPath();
            Path path = Paths.get(mockDataPath);
            
            if (Files.exists(path)) {
                Files.walk(path)
                        .filter(Files::isRegularFile)
                        .filter(p -> p.toString().contains("temp_") || p.toString().contains("test_"))
                        .forEach(p -> {
                            try {
                                Files.delete(p);
                                logger.debug("Deleted temporary test file: {}", p);
                            } catch (IOException e) {
                                logger.warn("Failed to delete test file: {}", p);
                            }
                        });
            }
        } catch (IOException e) {
            logger.error("Error during test data cleanup: {}", e.getMessage());
        }
    }
    
    /**
     * Create a default schema for testing
     */
    public DataSchema createDefaultSchema() {
        logger.info("Creating default schema");
        
        DataSchema schema = new DataSchema();
        schema.setName("default_schema");
        schema.setDescription("Default schema for testing");
        
        // Add common fields
        schema.addField(new SchemaField("id", DataType.STRING, false, "Unique identifier"));
        schema.addField(new SchemaField("name", DataType.STRING, false, "Name field"));
        schema.addField(new SchemaField("email", DataType.STRING, true, "Email address"));
        schema.addField(new SchemaField("age", DataType.INTEGER, true, "Age in years"));
        schema.addField(new SchemaField("created_date", DataType.DATE, false, "Creation date"));
        
        return schema;
    }
    
    /**
     * Generate test data based on schema
     */
    public List<Map<String, Object>> generateTestData(DataSchema schema, int recordCount) {
        logger.info("Generating {} test records for schema: {}", recordCount, schema.getName());
        
        MockDataGenerator generator = new MockDataGenerator();
        return generator.generateMockData(schema, recordCount);
    }
    
    /**
     * Validate schema structure
     */
    public boolean validateSchema(DataSchema schema) {
        logger.info("Validating schema: {}", schema.getName());
        
        if (schema == null || schema.getFields() == null || schema.getFields().isEmpty()) {
            logger.error("Schema is null or has no fields");
            return false;
        }
        
        // Check for required fields
        boolean hasIdField = schema.getFields().stream()
            .anyMatch(field -> "id".equals(field.getName()));
        
        if (!hasIdField) {
            logger.warn("Schema does not have an 'id' field");
        }
        
        // Validate field names are unique
        long uniqueFieldCount = schema.getFields().stream()
            .map(SchemaField::getName)
            .distinct()
            .count();
        
        if (uniqueFieldCount != schema.getFields().size()) {
            logger.error("Schema has duplicate field names");
            return false;
        }
        
        logger.info("Schema validation passed");
        return true;
    }
    
    /**
     * Validate data quality
     */
    public Map<String, Object> validateDataQuality(List<Map<String, Object>> data, DataSchema schema) {
        logger.info("Validating data quality for {} records", data.size());
        
        Map<String, Object> results = new HashMap<>();
        
        // Calculate completeness
        Map<String, Double> completeness = checkDataCompleteness(data);
        results.put("completeness", completeness);
        
        // Calculate overall quality score
        double qualityScore = completeness.values().stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0) / 100.0;
        
        results.put("quality_score", qualityScore);
        results.put("record_count", data.size());
        results.put("validation_timestamp", System.currentTimeMillis());
        
        logger.info("Data quality validation completed with score: {}", qualityScore);
        return results;
    }
    
    /**
     * Check data completeness
     */
    public Map<String, Double> checkDataCompleteness(List<Map<String, Object>> data) {
        logger.info("Checking data completeness for {} records", data.size());
        
        Map<String, Double> completeness = new HashMap<>();
        
        if (data.isEmpty()) {
            return completeness;
        }
        
        // Get all field names from the first record
        Set<String> fieldNames = data.get(0).keySet();
        
        for (String fieldName : fieldNames) {
            long nonNullCount = data.stream()
                .mapToLong(record -> {
                    Object value = record.get(fieldName);
                    return (value != null && !value.toString().trim().isEmpty()) ? 1 : 0;
                })
                .sum();
            
            double completenessPercentage = (double) nonNullCount / data.size() * 100.0;
            completeness.put(fieldName, completenessPercentage);
            
            logger.debug("Field '{}' completeness: {}%", fieldName, completenessPercentage);
        }
        
        return completeness;
    }
    
    /**
     * Check data uniqueness for a specific field
     */
    public double checkDataUniqueness(List<Map<String, Object>> data, String fieldName) {
        logger.info("Checking uniqueness for field: {}", fieldName);
        
        if (data.isEmpty()) {
            return 100.0;
        }
        
        Set<Object> uniqueValues = data.stream()
            .map(record -> record.get(fieldName))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        
        long nonNullCount = data.stream()
            .map(record -> record.get(fieldName))
            .filter(Objects::nonNull)
            .count();
        
        double uniquenessPercentage = nonNullCount > 0 ? 
            (double) uniqueValues.size() / nonNullCount * 100.0 : 100.0;
        
        logger.info("Field '{}' uniqueness: {}%", fieldName, uniquenessPercentage);
        return uniquenessPercentage;
    }
    
    /**
     * Validate data formats against schema
     */
    public Map<String, Object> validateDataFormats(List<Map<String, Object>> data, DataSchema schema) {
        logger.info("Validating data formats against schema");
        
        Map<String, Object> results = new HashMap<>();
        Map<String, Boolean> formatValidation = new HashMap<>();
        
        for (SchemaField field : schema.getFields()) {
            String fieldName = field.getName();
            DataType expectedType = field.getDataType();
            
            boolean isValid = data.stream()
                .allMatch(record -> {
                    Object value = record.get(fieldName);
                    if (value == null) {
                        return field.isNullable();
                    }
                    return isValidDataType(value, expectedType);
                });
            
            formatValidation.put(fieldName, isValid);
            logger.debug("Field '{}' format validation: {}", fieldName, isValid ? "PASSED" : "FAILED");
        }
        
        results.put("format_validation", formatValidation);
        return results;
    }
    
    /**
     * Check if value matches expected data type
     */
    private boolean isValidDataType(Object value, DataType expectedType) {
        if (value == null) {
            return true;
        }
        
        String stringValue = value.toString();
        
        switch (expectedType) {
            case STRING:
                return true; // Any value can be a string
            case INTEGER:
                try {
                    Integer.parseInt(stringValue);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            case DOUBLE:
                try {
                    Double.parseDouble(stringValue);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            case BOOLEAN:
                return "true".equalsIgnoreCase(stringValue) || "false".equalsIgnoreCase(stringValue);
            case DATE:
                // Simple date format validation
                return stringValue.matches("\\d{4}-\\d{2}-\\d{2}");
            case TIMESTAMP:
                // Simple timestamp format validation
                return stringValue.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
            default:
                return false;
        }
    }
    
    /**
     * Save validation results to file
     */
    public void saveValidationResults(Map<String, Object> results, String filePath) throws IOException {
        logger.info("Saving validation results to: {}", filePath);
        
        ensureDirectoryExists(filePath);
        jsonMapper.writeValue(new File(filePath), results);
        
        logger.info("Validation results saved successfully");
    }
    
    /**
     * Load test data from file
     */
    public List<Map<String, Object>> loadTestDataFromFile(String filePath) throws IOException {
        logger.info("Loading test data from: {}", filePath);
        
        List<Map<String, Object>> data = new ArrayList<>();
        
        if (filePath.endsWith(".csv")) {
            data = loadDataFromCsv(filePath);
        } else if (filePath.endsWith(".json")) {
            data = loadDataFromJson(filePath);
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + filePath);
        }
        
        logger.info("Loaded {} records from: {}", data.size(), filePath);
        return data;
    }
    
    /**
     * Compare two datasets
     */
    public boolean compareDatasets(List<Map<String, Object>> actual, List<Map<String, Object>> expected) {
        logger.info("Comparing datasets - Actual: {} records, Expected: {} records", 
                   actual.size(), expected.size());
        
        if (actual.size() != expected.size()) {
            logger.error("Dataset sizes don't match");
            return false;
        }
        
        // Sort both datasets by a common key (assuming 'id' field exists)
        actual.sort((a, b) -> String.valueOf(a.get("id")).compareTo(String.valueOf(b.get("id"))));
        expected.sort((a, b) -> String.valueOf(a.get("id")).compareTo(String.valueOf(b.get("id"))));
        
        for (int i = 0; i < actual.size(); i++) {
            Map<String, Object> actualRecord = actual.get(i);
            Map<String, Object> expectedRecord = expected.get(i);
            
            if (!actualRecord.equals(expectedRecord)) {
                logger.error("Record mismatch at index {}: {} vs {}", i, actualRecord, expectedRecord);
                return false;
            }
        }
        
        logger.info("Datasets match successfully");
        return true;
    }
    
    /**
     * Generate data quality report
     */
    public String generateDataQualityReport(List<Map<String, Object>> data, DataSchema schema, 
                                          Map<String, Object> validationResults) throws IOException {
        logger.info("Generating data quality report");
        
        String reportPath = "target/reports/data_quality_report_" + System.currentTimeMillis() + ".html";
        
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Data Quality Report</title></head><body>");
        html.append("<h1>Data Quality Report</h1>");
        html.append("<h2>Schema: ").append(schema.getName()).append("</h2>");
        html.append("<p>Generated: ").append(new Date()).append("</p>");
        html.append("<p>Record Count: ").append(data.size()).append("</p>");
        
        if (validationResults != null) {
            html.append("<h3>Validation Results</h3>");
            html.append("<pre>").append(validationResults.toString()).append("</pre>");
        }
        
        html.append("</body></html>");
        
        // Ensure directory exists
        ensureDirectoryExists(reportPath);
        Files.write(Paths.get(reportPath), html.toString().getBytes());
        
        logger.info("Data quality report generated: {}", reportPath);
        return reportPath;
    }
    
    /**
     * Load data from CSV file
     */
    private List<Map<String, Object>> loadDataFromCsv(String filePath) throws IOException {
        List<Map<String, Object>> data = new ArrayList<>();
        
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return data;
            }
            
            String[] headers = headerLine.split(",");
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, Object> record = new HashMap<>();
                
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    record.put(headers[i].trim(), values[i].trim());
                }
                
                data.add(record);
            }
        }
        
        return data;
    }
    
    /**
     * Load data from JSON file
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> loadDataFromJson(String filePath) throws IOException {
        return jsonMapper.readValue(new File(filePath), List.class);
    }
}