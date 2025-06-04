package com.prophecy.testing.models;

/**
 * Enumeration representing different write modes for data targets
 */
public enum WriteMode {
    OVERWRITE("Overwrite"),
    APPEND("Append"),
    MERGE("Merge"),
    UPSERT("Upsert"),
    ERROR_IF_EXISTS("Error if exists"),
    IGNORE("Ignore");
    
    private final String displayName;
    
    WriteMode(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}