package com.prophecy.testing.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prophecy.testing.config.ConfigManager;
import com.prophecy.testing.models.Pipeline;
import com.prophecy.testing.models.PipelineStage;
import com.prophecy.testing.utils.ApiTestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpResponse;
import java.util.*;

/**
 * Prophecy API Client for pipeline management and stage-by-stage testing
 */
public class ProphecyApiClient {
    private static final Logger logger = LoggerFactory.getLogger(ProphecyApiClient.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private final String baseUrl;
    private final String authToken;
    private final Map<String, String> defaultHeaders;
    
    public ProphecyApiClient() {
        this.baseUrl = ConfigManager.getInstance().getProphecyBaseUrl();
        this.authToken = getAuthToken();
        this.defaultHeaders = createDefaultHeaders();
    }
    
    /**
     * Get authentication token from Prophecy
     */
    private String getAuthToken() {
        try {
            String loginUrl = baseUrl + "/api/auth/login";
            String loginPayload = String.format(
                "{\"username\":\"%s\",\"password\":\"%s\"}", 
                ConfigManager.getInstance().getProphecyUsername(),
                ConfigManager.getInstance().getProphecyPassword()
            );
            
            HttpResponse<String> response = ApiTestUtils.sendPostRequest(
                loginUrl, loginPayload, Map.of("Content-Type", "application/json")
            );
            
            if (response.statusCode() == 200) {
                JsonNode jsonResponse = ApiTestUtils.parseJsonResponse(response.body());
                return ApiTestUtils.extractJsonValue(jsonResponse, "token");
            } else {
                throw new RuntimeException("Authentication failed: " + response.statusCode());
            }
        } catch (Exception e) {
            logger.error("Failed to authenticate with Prophecy API: {}", e.getMessage());
            throw new RuntimeException("Authentication failed", e);
        }
    }
    
    private Map<String, String> createDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + authToken);
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return headers;
    }
    
    /**
     * Get pipeline details by ID
     */
    public Pipeline getPipeline(String pipelineId) {
        try {
            String url = baseUrl + "/api/pipelines/" + pipelineId;
            HttpResponse<String> response = ApiTestUtils.sendGetRequest(url, defaultHeaders);
            
            if (response.statusCode() == 200) {
                JsonNode jsonResponse = ApiTestUtils.parseJsonResponse(response.body());
                return parsePipelineFromJson(jsonResponse);
            } else {
                throw new RuntimeException("Failed to get pipeline: " + response.statusCode());
            }
        } catch (Exception e) {
            logger.error("Error getting pipeline {}: {}", pipelineId, e.getMessage());
            throw new RuntimeException("Failed to get pipeline", e);
        }
    }
    
    /**
     * Get all stages of a pipeline
     */
    public List<PipelineStage> getPipelineStages(String pipelineId) {
        try {
            String url = baseUrl + "/api/pipelines/" + pipelineId + "/stages";
            HttpResponse<String> response = ApiTestUtils.sendGetRequest(url, defaultHeaders);
            
            if (response.statusCode() == 200) {
                JsonNode jsonResponse = ApiTestUtils.parseJsonResponse(response.body());
                return parseStagesFromJson(jsonResponse);
            } else {
                throw new RuntimeException("Failed to get pipeline stages: " + response.statusCode());
            }
        } catch (Exception e) {
            logger.error("Error getting stages for pipeline {}: {}", pipelineId, e.getMessage());
            throw new RuntimeException("Failed to get pipeline stages", e);
        }
    }
    
    /**
     * Execute specific stage with test data
     */
    public StageExecutionResult executeStage(String pipelineId, String stageId, Map<String, Object> inputData) {
        try {
            String url = baseUrl + "/api/pipelines/" + pipelineId + "/stages/" + stageId + "/execute";
            String payload = objectMapper.writeValueAsString(Map.of(
                "inputData", inputData,
                "executionMode", "test",
                "validateOnly", false
            ));
            
            HttpResponse<String> response = ApiTestUtils.sendPostRequest(url, payload, defaultHeaders);
            
            if (response.statusCode() == 200) {
                JsonNode jsonResponse = ApiTestUtils.parseJsonResponse(response.body());
                return parseStageExecutionResult(jsonResponse);
            } else {
                throw new RuntimeException("Failed to execute stage: " + response.statusCode());
            }
        } catch (Exception e) {
            logger.error("Error executing stage {} in pipeline {}: {}", stageId, pipelineId, e.getMessage());
            throw new RuntimeException("Failed to execute stage", e);
        }
    }
    
    /**
     * Test entire pipeline stage by stage
     */
    public PipelineTestResult testPipelineStageByStage(String pipelineId, Map<String, Object> initialData) {
        logger.info("Starting stage-by-stage testing for pipeline: {}", pipelineId);
        
        PipelineTestResult result = new PipelineTestResult(pipelineId);
        
        try {
            // Get pipeline stages
            List<PipelineStage> stages = getPipelineStages(pipelineId);
            Map<String, Object> currentData = new HashMap<>(initialData);
            
            for (PipelineStage stage : stages) {
                logger.info("Testing stage: {} ({})", stage.getName(), stage.getType());
                
                StageTestResult stageResult = new StageTestResult(stage.getId(), stage.getName());
                
                try {
                    // Execute stage
                    StageExecutionResult execution = executeStage(pipelineId, stage.getId(), currentData);
                    stageResult.setExecutionResult(execution);
                    
                    if (execution.isSuccessful()) {
                        // Get output data for next stage
                        currentData = execution.getOutputData();
                        stageResult.setOutputData(currentData);
                    } else {
                        stageResult.setError("Stage execution failed: " + execution.getErrorMessage());
                        break; // Stop testing if stage fails
                    }
                    
                } catch (Exception e) {
                    stageResult.setError("Exception during stage testing: " + e.getMessage());
                    logger.error("Error testing stage {}: {}", stage.getName(), e.getMessage());
                    break;
                }
                
                result.addStageResult(stageResult);
            }
            
            result.setOverallSuccess(result.getStageResults().stream().allMatch(StageTestResult::isSuccessful));
            
        } catch (Exception e) {
            result.setOverallSuccess(false);
            result.setError("Pipeline testing failed: " + e.getMessage());
            logger.error("Error during pipeline testing: {}", e.getMessage());
        }
        
        logger.info("Stage-by-stage testing completed for pipeline: {}. Success: {}", 
                   pipelineId, result.isOverallSuccess());
        
        return result;
    }
    
    // Helper methods for parsing JSON responses
    private Pipeline parsePipelineFromJson(JsonNode json) {
        String id = json.get("id").asText();
        String name = json.get("name").asText();
        String description = json.has("description") ? json.get("description").asText() : "";
        String project = json.has("project") ? json.get("project").asText() : "default";
        
        Pipeline pipeline = new Pipeline(name, description, project);
        pipeline.setId(id);
        return pipeline;
    }
    
    private List<PipelineStage> parseStagesFromJson(JsonNode json) {
        List<PipelineStage> stages = new ArrayList<>();
        JsonNode stagesArray = json.get("stages");
        
        if (stagesArray != null && stagesArray.isArray()) {
            for (JsonNode stageNode : stagesArray) {
                String id = stageNode.get("id").asText();
                String name = stageNode.get("name").asText();
                Map<String, Object> config = objectMapper.convertValue(stageNode.get("config"), Map.class);
                
                // Add type to config if it exists
                if (stageNode.has("type")) {
                    config.put("type", stageNode.get("type").asText());
                }
                
                PipelineStage stage = new PipelineStage(id, name, config);
                stages.add(stage);
            }
        }
        
        return stages;
    }
    
    private StageExecutionResult parseStageExecutionResult(JsonNode json) {
        boolean successful = json.get("success").asBoolean();
        Map<String, Object> outputData = objectMapper.convertValue(json.get("outputData"), Map.class);
        String errorMessage = json.has("error") ? json.get("error").asText() : null;
        
        return new StageExecutionResult(successful, outputData, errorMessage);
    }
    
    // Inner classes for results
    public static class StageExecutionResult {
        private final boolean successful;
        private final Map<String, Object> outputData;
        private final String errorMessage;
        
        public StageExecutionResult(boolean successful, Map<String, Object> outputData, String errorMessage) {
            this.successful = successful;
            this.outputData = outputData;
            this.errorMessage = errorMessage;
        }
        
        public boolean isSuccessful() { return successful; }
        public Map<String, Object> getOutputData() { return outputData; }
        public String getErrorMessage() { return errorMessage; }
    }
    
    public static class StageTestResult {
        private final String stageId;
        private final String stageName;
        private StageExecutionResult executionResult;
        private Map<String, Object> outputData;
        private String error;
        
        public StageTestResult(String stageId, String stageName) {
            this.stageId = stageId;
            this.stageName = stageName;
        }
        
        public boolean isSuccessful() {
            return error == null && executionResult != null && executionResult.isSuccessful();
        }
        
        // Getters and setters
        public String getStageId() { return stageId; }
        public String getStageName() { return stageName; }
        public StageExecutionResult getExecutionResult() { return executionResult; }
        public void setExecutionResult(StageExecutionResult executionResult) { this.executionResult = executionResult; }
        public Map<String, Object> getOutputData() { return outputData; }
        public void setOutputData(Map<String, Object> outputData) { this.outputData = outputData; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
    
    public static class PipelineTestResult {
        private final String pipelineId;
        private final List<StageTestResult> stageResults;
        private boolean overallSuccess;
        private String error;
        
        public PipelineTestResult(String pipelineId) {
            this.pipelineId = pipelineId;
            this.stageResults = new ArrayList<>();
        }
        
        public void addStageResult(StageTestResult result) {
            stageResults.add(result);
        }
        
        // Getters and setters
        public String getPipelineId() { return pipelineId; }
        public List<StageTestResult> getStageResults() { return stageResults; }
        public boolean isOverallSuccess() { return overallSuccess; }
        public void setOverallSuccess(boolean overallSuccess) { this.overallSuccess = overallSuccess; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}