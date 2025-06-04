package com.prophecy.testing.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.prophecy.testing.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Utility class for generating mock test data for pipeline testing
 */
public class MockDataGenerator {
    private static final Logger logger = LogManager.getLogger(MockDataGenerator.class);
    private final Faker faker;
    private final ObjectMapper objectMapper;
    
    public MockDataGenerator() {
        this.faker = new Faker();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Generate mock data based on schema definition
     */
    public List<Map<String, Object>> generateMockData(DataSchema schema, int recordCount) {
        List<Map<String, Object>> data = new ArrayList<>();
        
        for (int i = 0; i < recordCount; i++) {
            Map<String, Object> record = new HashMap<>();
            
            for (SchemaField field : schema.getFields()) {
                Object value = generateValueForField(field);
                record.put(field.getName(), value);
            }
            
            data.add(record);
        }
        
        logger.info("Generated {} mock records for schema: {}", recordCount, schema.getName());
        return data;
    }
    
    /**
     * Generate value for a specific field based on its data type
     */
    private Object generateValueForField(SchemaField field) {
        if (field.isNullable() && faker.random().nextDouble() < 0.1) {
            return null; // 10% chance of null values for nullable fields
        }
        
        switch (field.getDataType()) {
            case STRING:
                return generateStringValue(field.getName());
            case INTEGER:
                return faker.number().numberBetween(1, 10000);
            case LONG:
                return faker.number().numberBetween(1L, 1000000L);
            case DOUBLE:
                return faker.number().randomDouble(2, 1, 10000);
            case FLOAT:
                return (float) faker.number().randomDouble(2, 1, 1000);
            case BOOLEAN:
                return faker.bool().bool();
            case DATE:
                return LocalDate.now().minusDays(faker.number().numberBetween(0, 365))
                        .format(DateTimeFormatter.ISO_LOCAL_DATE);
            case TIMESTAMP:
                return LocalDateTime.now().minusDays(faker.number().numberBetween(0, 365))
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            case DECIMAL:
                return faker.number().randomDouble(2, 1, 10000);
            default:
                return faker.lorem().word();
        }
    }
    
    /**
     * Generate contextual string values based on field name
     */
    private String generateStringValue(String fieldName) {
        String lowerFieldName = fieldName.toLowerCase();
        
        if (lowerFieldName.contains("name")) {
            return faker.name().fullName();
        } else if (lowerFieldName.contains("email")) {
            return faker.internet().emailAddress();
        } else if (lowerFieldName.contains("phone")) {
            return faker.phoneNumber().phoneNumber();
        } else if (lowerFieldName.contains("address")) {
            return faker.address().fullAddress();
        } else if (lowerFieldName.contains("city")) {
            return faker.address().city();
        } else if (lowerFieldName.contains("country")) {
            return faker.address().country();
        } else if (lowerFieldName.contains("company")) {
            return faker.company().name();
        } else if (lowerFieldName.contains("product")) {
            return faker.commerce().productName();
        } else if (lowerFieldName.contains("description")) {
            return faker.lorem().sentence();
        } else if (lowerFieldName.contains("id")) {
            return UUID.randomUUID().toString();
        } else {
            return faker.lorem().word();
        }
    }
    
    /**
     * Save mock data to CSV file
     */
    public void saveMockDataToCsv(List<Map<String, Object>> data, DataSchema schema, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            List<String> headers = new ArrayList<>();
            for (SchemaField field : schema.getFields()) {
                headers.add(field.getName());
            }
            writer.write(String.join(",", headers) + "\n");
            
            // Write data
            for (Map<String, Object> record : data) {
                List<String> values = new ArrayList<>();
                for (SchemaField field : schema.getFields()) {
                    Object value = record.get(field.getName());
                    values.add(value != null ? value.toString() : "");
                }
                writer.write(String.join(",", values) + "\n");
            }
            
            logger.info("Mock data saved to CSV file: {}", filePath);
        } catch (IOException e) {
            logger.error("Error saving mock data to CSV: {}", e.getMessage());
            throw new RuntimeException("Failed to save mock data to CSV", e);
        }
    }
    
    /**
     * Save mock data to JSON file
     */
    public void saveMockDataToJson(List<Map<String, Object>> data, String filePath) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
            logger.info("Mock data saved to JSON file: {}", filePath);
        } catch (IOException e) {
            logger.error("Error saving mock data to JSON: {}", e.getMessage());
            throw new RuntimeException("Failed to save mock data to JSON", e);
        }
    }
    
    /**
     * Generate a sample pipeline configuration
     */
    public Pipeline generateSamplePipeline(String name, String project) {
        Pipeline pipeline = new Pipeline(name, "Sample pipeline for testing", project);
        pipeline.setId(UUID.randomUUID().toString());
        pipeline.setVersion("1.0.0");
        pipeline.setCreatedBy("test-user");
        pipeline.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        // Create sample stages
        List<PipelineStage> stages = new ArrayList<>();
        
        // Source stage
        PipelineStage sourceStage = new PipelineStage("Source_Data", StageType.SOURCE, 1);
        sourceStage.setId(UUID.randomUUID().toString());
        sourceStage.setDescription("Read data from source");
        stages.add(sourceStage);
        
        // Transformation stage
        PipelineStage transformStage = new PipelineStage("Transform_Data", StageType.TRANSFORMATION, 2);
        transformStage.setId(UUID.randomUUID().toString());
        transformStage.setDescription("Apply data transformations");
        stages.add(transformStage);
        
        // Target stage
        PipelineStage targetStage = new PipelineStage("Target_Data", StageType.TARGET, 3);
        targetStage.setId(UUID.randomUUID().toString());
        targetStage.setDescription("Write data to target");
        stages.add(targetStage);
        
        pipeline.setStages(stages);
        
        // Create sample data sources
        List<DataSource> inputSources = new ArrayList<>();
        DataSource source = new DataSource("sample_source", DataSourceType.CSV);
        source.setId(UUID.randomUUID().toString());
        inputSources.add(source);
        pipeline.setInputSources(inputSources);
        
        // Create sample data targets
        List<DataTarget> outputTargets = new ArrayList<>();
        DataTarget target = new DataTarget("sample_target", DataSourceType.CSV);
        target.setId(UUID.randomUUID().toString());
        outputTargets.add(target);
        pipeline.setOutputTargets(outputTargets);
        
        return pipeline;
    }
    
    /**
     * Generate a sample data schema
     */
    public DataSchema generateSampleSchema(String schemaName) {
        List<SchemaField> fields = Arrays.asList(
            new SchemaField("id", DataType.STRING, false),
            new SchemaField("name", DataType.STRING, false),
            new SchemaField("email", DataType.STRING, true),
            new SchemaField("age", DataType.INTEGER, true),
            new SchemaField("salary", DataType.DOUBLE, true),
            new SchemaField("is_active", DataType.BOOLEAN, true),
            new SchemaField("created_date", DataType.DATE, false),
            new SchemaField("last_updated", DataType.TIMESTAMP, false)
        );
        
        return new DataSchema(schemaName, fields);
    }
}