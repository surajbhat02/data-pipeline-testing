package com.prophecy.testing.models;

/**
 * Enumeration representing different types of data sources in Prophecy
 */
public enum DataSourceType {
    CSV("CSV File"),
    JSON("JSON File"),
    PARQUET("Parquet File"),
    AVRO("Avro File"),
    EXCEL("Excel File"),
    DATABASE("Database"),
    SNOWFLAKE("Snowflake"),
    REDSHIFT("Amazon Redshift"),
    BIGQUERY("Google BigQuery"),
    DATABRICKS("Databricks"),
    S3("Amazon S3"),
    AZURE_BLOB("Azure Blob Storage"),
    GCS("Google Cloud Storage"),
    KAFKA("Apache Kafka"),
    KINESIS("Amazon Kinesis"),
    API("REST API"),
    JDBC("JDBC Connection"),
    MOCK("Mock Data");
    
    private final String displayName;
    
    DataSourceType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isFileSource() {
        return this == CSV || this == JSON || this == PARQUET || 
               this == AVRO || this == EXCEL;
    }
    
    public boolean isCloudStorage() {
        return this == S3 || this == AZURE_BLOB || this == GCS;
    }
    
    public boolean isDatabase() {
        return this == DATABASE || this == SNOWFLAKE || this == REDSHIFT || 
               this == BIGQUERY || this == DATABRICKS || this == JDBC;
    }
    
    public boolean isStreaming() {
        return this == KAFKA || this == KINESIS;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}