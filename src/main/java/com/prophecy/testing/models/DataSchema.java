package com.prophecy.testing.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a data schema in Prophecy
 */
public class DataSchema {
    @JsonProperty("fields")
    private List<SchemaField> fields;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("description")
    private String description;
    
    // Constructors
    public DataSchema() {}
    
    public DataSchema(String name, List<SchemaField> fields) {
        this.name = name;
        this.fields = fields;
    }
    
    // Getters and Setters
    public List<SchemaField> getFields() {
        return fields;
    }
    
    public void setFields(List<SchemaField> fields) {
        this.fields = fields;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public SchemaField getField(String fieldName) {
        return fields.stream()
                .filter(field -> field.getName().equals(fieldName))
                .findFirst()
                .orElse(null);
    }
    
    public boolean hasField(String fieldName) {
        return getField(fieldName) != null;
    }
    
    public int getFieldCount() {
        return fields != null ? fields.size() : 0;
    }
    
    public void addField(SchemaField field) {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        fields.add(field);
    }
    
    public void removeField(String fieldName) {
        if (fields != null) {
            fields.removeIf(field -> field.getName().equals(fieldName));
        }
    }
    
    @Override
    public String toString() {
        return "DataSchema{" +
                "name='" + name + '\'' +
                ", fieldCount=" + getFieldCount() +
                '}';
    }
}