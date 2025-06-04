package com.prophecy.testing.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Model class representing a stage in a Prophecy data pipeline
 */
public class PipelineStage {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("type")
    private StageType type;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("order")
    private int order;
    
    @JsonProperty("configuration")
    private Map<String, Object> configuration;
    
    @JsonProperty("inputSchema")
    private DataSchema inputSchema;
    
    @JsonProperty("outputSchema")
    private DataSchema outputSchema;
    
    @JsonProperty("status")
    private StageStatus status;
    
    @JsonProperty("executionTime")
    private long executionTime;
    
    @JsonProperty("errorMessage")
    private String errorMessage;
    
    // Constructors
    public PipelineStage() {}
    
    public PipelineStage(String name, StageType type, int order) {
        this.name = name;
        this.type = type;
        this.order = order;
        this.status = StageStatus.PENDING;
    }
    
    public PipelineStage(String id, String name, Map<String, Object> configuration) {
        this.id = id;
        this.name = name;
        this.configuration = configuration;
        this.status = StageStatus.PENDING;
        // Set type from configuration or default
        if (configuration != null && configuration.containsKey("type")) {
            try {
                this.type = StageType.valueOf(configuration.get("type").toString().toUpperCase());
            } catch (IllegalArgumentException e) {
                this.type = StageType.TRANSFORMATION; // default
            }
        } else {
            this.type = StageType.TRANSFORMATION; // default
        }
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public StageType getType() {
        return type;
    }
    
    public void setType(StageType type) {
        this.type = type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getOrder() {
        return order;
    }
    
    public void setOrder(int order) {
        this.order = order;
    }
    
    public Map<String, Object> getConfiguration() {
        return configuration;
    }
    
    public void setConfiguration(Map<String, Object> configuration) {
        this.configuration = configuration;
    }
    
    public DataSchema getInputSchema() {
        return inputSchema;
    }
    
    public void setInputSchema(DataSchema inputSchema) {
        this.inputSchema = inputSchema;
    }
    
    public DataSchema getOutputSchema() {
        return outputSchema;
    }
    
    public void setOutputSchema(DataSchema outputSchema) {
        this.outputSchema = outputSchema;
    }
    
    public StageStatus getStatus() {
        return status;
    }
    
    public void setStatus(StageStatus status) {
        this.status = status;
    }
    
    public long getExecutionTime() {
        return executionTime;
    }
    
    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    @Override
    public String toString() {
        return "PipelineStage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", order=" + order +
                ", status=" + status +
                '}';
    }
}