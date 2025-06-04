package com.prophecy.testing.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Model class representing a Prophecy data pipeline
 */
public class Pipeline {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("project")
    private String project;
    
    @JsonProperty("version")
    private String version;
    
    @JsonProperty("stages")
    private List<PipelineStage> stages;
    
    @JsonProperty("inputSources")
    private List<DataSource> inputSources;
    
    @JsonProperty("outputTargets")
    private List<DataTarget> outputTargets;
    
    @JsonProperty("configuration")
    private Map<String, Object> configuration;
    
    @JsonProperty("status")
    private PipelineStatus status;
    
    @JsonProperty("createdBy")
    private String createdBy;
    
    @JsonProperty("createdAt")
    private String createdAt;
    
    @JsonProperty("lastModified")
    private String lastModified;
    
    // Constructors
    public Pipeline() {}
    
    public Pipeline(String name, String description, String project) {
        this.name = name;
        this.description = description;
        this.project = project;
        this.status = PipelineStatus.DRAFT;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getProject() {
        return project;
    }
    
    public void setProject(String project) {
        this.project = project;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public List<PipelineStage> getStages() {
        return stages;
    }
    
    public void setStages(List<PipelineStage> stages) {
        this.stages = stages;
    }
    
    public List<DataSource> getInputSources() {
        return inputSources;
    }
    
    public void setInputSources(List<DataSource> inputSources) {
        this.inputSources = inputSources;
    }
    
    public List<DataTarget> getOutputTargets() {
        return outputTargets;
    }
    
    public void setOutputTargets(List<DataTarget> outputTargets) {
        this.outputTargets = outputTargets;
    }
    
    public Map<String, Object> getConfiguration() {
        return configuration;
    }
    
    public void setConfiguration(Map<String, Object> configuration) {
        this.configuration = configuration;
    }
    
    public PipelineStatus getStatus() {
        return status;
    }
    
    public void setStatus(PipelineStatus status) {
        this.status = status;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getLastModified() {
        return lastModified;
    }
    
    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
    
    @Override
    public String toString() {
        return "Pipeline{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", project='" + project + '\'' +
                ", version='" + version + '\'' +
                ", status=" + status +
                '}';
    }
}