package com.prophecy.testing.models;

/**
 * Enumeration representing different types of pipeline stages in Prophecy
 */
public enum StageType {
    SOURCE("Source"),
    TRANSFORMATION("Transformation"),
    FILTER("Filter"),
    JOIN("Join"),
    AGGREGATE("Aggregate"),
    SORT("Sort"),
    UNION("Union"),
    LOOKUP("Lookup"),
    PIVOT("Pivot"),
    UNPIVOT("Unpivot"),
    WINDOW("Window"),
    CUSTOM("Custom"),
    TARGET("Target"),
    VALIDATION("Validation");
    
    private final String displayName;
    
    StageType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isSource() {
        return this == SOURCE;
    }
    
    public boolean isTarget() {
        return this == TARGET;
    }
    
    public boolean isTransformation() {
        return this == TRANSFORMATION || this == FILTER || this == JOIN || 
               this == AGGREGATE || this == SORT || this == UNION || 
               this == LOOKUP || this == PIVOT || this == UNPIVOT || 
               this == WINDOW || this == CUSTOM;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}