package com.prophecy.testing.models;

/**
 * Enumeration representing the status of a Prophecy data pipeline
 */
public enum PipelineStatus {
    DRAFT("Draft"),
    READY("Ready"),
    RUNNING("Running"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    CANCELLED("Cancelled"),
    PAUSED("Paused");
    
    private final String displayName;
    
    PipelineStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isTerminal() {
        return this == COMPLETED || this == FAILED || this == CANCELLED;
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