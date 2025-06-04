package com.prophecy.testing.models;

/**
 * Enumeration representing the status of a pipeline stage
 */
public enum StageStatus {
    PENDING("Pending"),
    RUNNING("Running"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    SKIPPED("Skipped");
    
    private final String displayName;
    
    StageStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isTerminal() {
        return this == COMPLETED || this == FAILED || this == SKIPPED;
    }
    
    public boolean isRunning() {
        return this == RUNNING;
    }
    
    public boolean isSuccessful() {
        return this == COMPLETED;
    }
    
    public boolean isFailed() {
        return this == FAILED;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}