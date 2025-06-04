package com.prophecy.testing.models;

/**
 * Enumeration representing different data types in Prophecy
 */
public enum DataType {
    STRING("String"),
    INTEGER("Integer"),
    LONG("Long"),
    DOUBLE("Double"),
    FLOAT("Float"),
    BOOLEAN("Boolean"),
    DATE("Date"),
    TIMESTAMP("Timestamp"),
    DECIMAL("Decimal"),
    BINARY("Binary"),
    ARRAY("Array"),
    MAP("Map"),
    STRUCT("Struct");
    
    private final String displayName;
    
    DataType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isNumeric() {
        return this == INTEGER || this == LONG || this == DOUBLE || 
               this == FLOAT || this == DECIMAL;
    }
    
    public boolean isString() {
        return this == STRING;
    }
    
    public boolean isTemporal() {
        return this == DATE || this == TIMESTAMP;
    }
    
    public boolean isComplex() {
        return this == ARRAY || this == MAP || this == STRUCT;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}