package com.prophecy.testing.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Model class representing a data target/destination in Prophecy
 */
public class DataTarget {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("type")
    private DataSourceType type;
    
    @JsonProperty("connectionString")
    private String connectionString;
    
    @JsonProperty("schema")
    private DataSchema schema;
    
    @JsonProperty("configuration")
    private Map<String, Object> configuration;
    
    @JsonProperty("outputPath")
    private String outputPath;
    
    @JsonProperty("writeMode")
    private WriteMode writeMode;
    
    // Constructors
    public DataTarget() {}
    
    public DataTarget(String name, DataSourceType type) {
        this.name = name;
        this.type = type;
        this.writeMode = WriteMode.OVERWRITE;
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
    
    public DataSourceType getType() {
        return type;
    }
    
    public void setType(DataSourceType type) {
        this.type = type;
    }
    
    public String getConnectionString() {
        return connectionString;
    }
    
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
    
    public DataSchema getSchema() {
        return schema;
    }
    
    public void setSchema(DataSchema schema) {
        this.schema = schema;
    }
    
    public Map<String, Object> getConfiguration() {
        return configuration;
    }
    
    public void setConfiguration(Map<String, Object> configuration) {
        this.configuration = configuration;
    }
    
    public String getOutputPath() {
        return outputPath;
    }
    
    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }
    
    public WriteMode getWriteMode() {
        return writeMode;
    }
    
    public void setWriteMode(WriteMode writeMode) {
        this.writeMode = writeMode;
    }
    
    @Override
    public String toString() {
        return "DataTarget{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", writeMode=" + writeMode +
                '}';
    }
}