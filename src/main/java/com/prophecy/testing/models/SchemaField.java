package com.prophecy.testing.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class representing a field in a data schema
 */
public class SchemaField {
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("dataType")
    private DataType dataType;
    
    @JsonProperty("nullable")
    private boolean nullable;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("defaultValue")
    private Object defaultValue;
    
    @JsonProperty("constraints")
    private String constraints;
    
    // Constructors
    public SchemaField() {}
    
    public SchemaField(String name, DataType dataType) {
        this.name = name;
        this.dataType = dataType;
        this.nullable = true;
    }
    
    public SchemaField(String name, DataType dataType, boolean nullable) {
        this.name = name;
        this.dataType = dataType;
        this.nullable = nullable;
    }
    
    public SchemaField(String name, DataType dataType, boolean nullable, String description) {
        this.name = name;
        this.dataType = dataType;
        this.nullable = nullable;
        this.description = description;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public DataType getDataType() {
        return dataType;
    }
    
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
    
    public boolean isNullable() {
        return nullable;
    }
    
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Object getDefaultValue() {
        return defaultValue;
    }
    
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    public String getConstraints() {
        return constraints;
    }
    
    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }
    
    @Override
    public String toString() {
        return "SchemaField{" +
                "name='" + name + '\'' +
                ", dataType=" + dataType +
                ", nullable=" + nullable +
                '}';
    }
}