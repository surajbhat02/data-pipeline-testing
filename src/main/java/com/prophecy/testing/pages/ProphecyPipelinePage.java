package com.prophecy.testing.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Page Object for Prophecy Pipeline Page - Main page for pipeline execution and stage management
 */
@Slf4j
public class ProphecyPipelinePage extends BasePage {
    
    // Pipeline header elements
    @FindBy(xpath = "//h1[contains(@class, 'pipeline-title') or contains(@class, 'pipeline-name')]")
    private WebElement pipelineTitle;
    
    @FindBy(xpath = "//button[contains(text(), 'Run') or contains(text(), 'Execute') or contains(@class, 'run-button')]")
    private WebElement runPipelineButton;
    
    @FindBy(xpath = "//button[contains(text(), 'Stop') or contains(text(), 'Cancel') or contains(@class, 'stop-button')]")
    private WebElement stopPipelineButton;
    
    @FindBy(xpath = "//div[contains(@class, 'pipeline-status') or contains(@class, 'execution-status')]")
    private WebElement pipelineStatus;
    
    // Stage-related elements
    @FindBy(xpath = "//div[contains(@class, 'stage') or contains(@class, 'node') or contains(@class, 'component')]")
    private List<WebElement> pipelineStages;
    
    @FindBy(xpath = "//div[contains(@class, 'stage-list') or contains(@class, 'pipeline-canvas')]")
    private WebElement stagesContainer;
    
    // Data source elements
    @FindBy(xpath = "//button[contains(text(), 'Data Source') or contains(text(), 'Input') or contains(@class, 'data-source')]")
    private WebElement dataSourceButton;
    
    @FindBy(xpath = "//input[@type='file' or contains(@class, 'file-upload')]")
    private WebElement fileUploadInput;
    
    @FindBy(xpath = "//textarea[contains(@placeholder, 'JSON') or contains(@class, 'json-input')]")
    private WebElement jsonDataInput;
    
    // Execution controls
    @FindBy(xpath = "//button[contains(text(), 'Step') or contains(text(), 'Next Stage') or contains(@class, 'step-button')]")
    private WebElement stepButton;
    
    @FindBy(xpath = "//button[contains(text(), 'Debug') or contains(@class, 'debug-button')]")
    private WebElement debugButton;
    
    @FindBy(xpath = "//div[contains(@class, 'execution-log') or contains(@class, 'console')]")
    private WebElement executionLog;
    
    // Record validation elements
    @FindBy(xpath = "//div[contains(@class, 'record-count') or contains(@class, 'row-count')]")
    private List<WebElement> recordCountElements;
    
    @FindBy(xpath = "//table[contains(@class, 'data-preview') or contains(@class, 'sample-data')]")
    private WebElement dataPreviewTable;
    
    // Alternative locators
    private final By stageLocator = By.xpath("//div[contains(@class, 'stage') or contains(@class, 'node')]");
    private final By stageNameLocator = By.xpath(".//span[contains(@class, 'stage-name') or contains(@class, 'node-title')]");
    private final By stageStatusLocator = By.xpath(".//div[contains(@class, 'status') or contains(@class, 'state')]");
    private final By recordCountLocator = By.xpath(".//span[contains(text(), 'records') or contains(text(), 'rows')]");
    
    /**
     * Verify pipeline page is loaded
     */
    public boolean isPipelinePageLoaded() {
        try {
            waitForPageToLoad();
            return pipelineTitle.isDisplayed() || 
                   runPipelineButton.isDisplayed() ||
                   stagesContainer.isDisplayed();
        } catch (Exception e) {
            log.warn("Pipeline page not loaded: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Get pipeline name
     */
    public String getPipelineName() {
        try {
            if (pipelineTitle.isDisplayed()) {
                return pipelineTitle.getText().trim();
            }
        } catch (Exception e) {
            log.debug("Pipeline title not found");
        }
        return getPageTitle();
    }
    
    /**
     * Upload custom data file (JSON, CSV, etc.)
     */
    public ProphecyPipelinePage uploadCustomData(String filePath) {
        try {
            if (fileUploadInput.isDisplayed()) {
                fileUploadInput.sendKeys(filePath);
                log.info("Uploaded custom data file: {}", filePath);
            } else {
                // Try alternative approach
                clickElement(dataSourceButton);
                WebElement uploadInput = driver.findElement(By.xpath("//input[@type='file']"));
                uploadInput.sendKeys(filePath);
                log.info("Uploaded custom data file via data source: {}", filePath);
            }
        } catch (Exception e) {
            log.error("Failed to upload custom data: {}", e.getMessage());
        }
        return this;
    }
    
    /**
     * Input custom JSON data
     */
    public ProphecyPipelinePage inputJsonData(String jsonData) {
        try {
            if (jsonDataInput.isDisplayed()) {
                jsonDataInput.clear();
                jsonDataInput.sendKeys(jsonData);
                log.info("Input custom JSON data");
            } else {
                // Try to find JSON input in data source configuration
                clickElement(dataSourceButton);
                WebElement jsonInput = driver.findElement(By.xpath("//textarea[contains(@placeholder, 'JSON')]"));
                jsonInput.clear();
                jsonInput.sendKeys(jsonData);
                log.info("Input custom JSON data via data source");
            }
        } catch (Exception e) {
            log.error("Failed to input JSON data: {}", e.getMessage());
        }
        return this;
    }
    
    /**
     * Input tabular data (as JSON array)
     */
    public ProphecyPipelinePage inputTabularData(List<Map<String, Object>> tableData) {
        try {
            // Convert table data to JSON
            StringBuilder jsonBuilder = new StringBuilder("[\n");
            for (int i = 0; i < tableData.size(); i++) {
                Map<String, Object> row = tableData.get(i);
                jsonBuilder.append("  {");
                int fieldCount = 0;
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    if (fieldCount > 0) jsonBuilder.append(", ");
                    jsonBuilder.append("\"").append(entry.getKey()).append("\": ");
                    if (entry.getValue() instanceof String) {
                        jsonBuilder.append("\"").append(entry.getValue()).append("\"");
                    } else {
                        jsonBuilder.append(entry.getValue());
                    }
                    fieldCount++;
                }
                jsonBuilder.append("}");
                if (i < tableData.size() - 1) jsonBuilder.append(",");
                jsonBuilder.append("\n");
            }
            jsonBuilder.append("]");
            
            inputJsonData(jsonBuilder.toString());
            log.info("Input tabular data with {} rows", tableData.size());
        } catch (Exception e) {
            log.error("Failed to input tabular data: {}", e.getMessage());
        }
        return this;
    }
    
    /**
     * Run the entire pipeline
     */
    public ProphecyPipelinePage runPipeline() {
        try {
            if (runPipelineButton.isDisplayed() && runPipelineButton.isEnabled()) {
                clickElement(runPipelineButton);
                log.info("Started pipeline execution");
                // Wait for execution to start
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            log.error("Failed to run pipeline: {}", e.getMessage());
        }
        return this;
    }
    
    /**
     * Execute pipeline stage by stage
     */
    public ProphecyPipelinePage executeStageByStage() {
        try {
            List<WebElement> stages = getStageElements();
            log.info("Executing {} stages step by step", stages.size());
            
            for (int i = 0; i < stages.size(); i++) {
                WebElement stage = stages.get(i);
                String stageName = getStageNameFromElement(stage);
                
                log.info("Executing stage {}: {}", i + 1, stageName);
                
                // Click on the stage to select it
                clickElement(stage);
                
                // Click step/next button to execute this stage
                if (stepButton.isDisplayed()) {
                    clickElement(stepButton);
                } else {
                    // Alternative: click run button for single stage execution
                    clickElement(runPipelineButton);
                }
                
                // Wait for stage execution
                waitForStageExecution(stage);
                
                // Validate stage completion
                validateStageExecution(stageName, stage);
                
                log.info("Completed stage {}: {}", i + 1, stageName);
            }
            
            log.info("Completed stage-by-stage execution");
        } catch (Exception e) {
            log.error("Failed during stage-by-stage execution: {}", e.getMessage());
        }
        return this;
    }
    
    /**
     * Get all pipeline stages
     */
    public List<String> getAllStageNames() {
        try {
            return getStageElements().stream()
                    .map(this::getStageNameFromElement)
                    .toList();
        } catch (Exception e) {
            log.warn("Could not get stage names: {}", e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Get stage status
     */
    public String getStageStatus(String stageName) {
        try {
            WebElement stage = findStageByName(stageName);
            if (stage != null) {
                WebElement statusElement = stage.findElement(stageStatusLocator);
                return statusElement.getText().trim();
            }
        } catch (Exception e) {
            log.warn("Could not get status for stage: {}", stageName);
        }
        return "Unknown";
    }
    
    /**
     * Get record count for a stage
     */
    public int getStageRecordCount(String stageName) {
        try {
            WebElement stage = findStageByName(stageName);
            if (stage != null) {
                WebElement recordElement = stage.findElement(recordCountLocator);
                String recordText = recordElement.getText();
                // Extract number from text like "1000 records" or "1,000 rows"
                String numberStr = recordText.replaceAll("[^0-9,]", "").replace(",", "");
                return Integer.parseInt(numberStr);
            }
        } catch (Exception e) {
            log.warn("Could not get record count for stage: {}", stageName);
        }
        return 0;
    }
    
    /**
     * Validate stage execution
     */
    public boolean validateStageExecution(String stageName, WebElement stageElement) {
        try {
            // Check if stage status indicates completion
            String status = getStageStatus(stageName);
            boolean isCompleted = status.toLowerCase().contains("completed") || 
                                status.toLowerCase().contains("success") ||
                                status.toLowerCase().contains("done");
            
            if (isCompleted) {
                int recordCount = getStageRecordCount(stageName);
                log.info("Stage '{}' validation: Status={}, Records={}", stageName, status, recordCount);
                return true;
            } else {
                log.warn("Stage '{}' validation failed: Status={}", stageName, status);
                return false;
            }
        } catch (Exception e) {
            log.error("Error validating stage '{}': {}", stageName, e.getMessage());
            return false;
        }
    }
    
    /**
     * Get pipeline execution status
     */
    public String getPipelineExecutionStatus() {
        try {
            if (pipelineStatus.isDisplayed()) {
                return pipelineStatus.getText().trim();
            }
        } catch (Exception e) {
            log.debug("Pipeline status not found");
        }
        return "Unknown";
    }
    
    /**
     * Stop pipeline execution
     */
    public ProphecyPipelinePage stopPipeline() {
        try {
            if (stopPipelineButton.isDisplayed() && stopPipelineButton.isEnabled()) {
                clickElement(stopPipelineButton);
                log.info("Stopped pipeline execution");
            }
        } catch (Exception e) {
            log.error("Failed to stop pipeline: {}", e.getMessage());
        }
        return this;
    }
    
    /**
     * Get execution logs
     */
    public String getExecutionLogs() {
        try {
            if (executionLog.isDisplayed()) {
                return executionLog.getText();
            }
        } catch (Exception e) {
            log.debug("Execution log not found");
        }
        return "";
    }
    
    /**
     * Get data preview from a stage
     */
    public List<Map<String, String>> getStageDataPreview(String stageName) {
        try {
            // Click on stage to view its data
            WebElement stage = findStageByName(stageName);
            if (stage != null) {
                clickElement(stage);
                
                // Wait for data preview to load
                Thread.sleep(1000);
                
                if (dataPreviewTable.isDisplayed()) {
                    return extractTableData(dataPreviewTable);
                }
            }
        } catch (Exception e) {
            log.warn("Could not get data preview for stage: {}", stageName);
        }
        return List.of();
    }
    
    // Helper methods
    private List<WebElement> getStageElements() {
        try {
            return driver.findElements(stageLocator);
        } catch (Exception e) {
            return pipelineStages;
        }
    }
    
    private String getStageNameFromElement(WebElement stageElement) {
        try {
            WebElement nameElement = stageElement.findElement(stageNameLocator);
            return nameElement.getText().trim();
        } catch (Exception e) {
            return stageElement.getAttribute("title");
        }
    }
    
    private WebElement findStageByName(String stageName) {
        return getStageElements().stream()
                .filter(stage -> {
                    try {
                        String name = getStageNameFromElement(stage);
                        return name.contains(stageName);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .findFirst()
                .orElse(null);
    }
    
    private void waitForStageExecution(WebElement stage) {
        try {
            // Wait for stage to complete (status change or visual indicator)
            wait.until(driver -> {
                try {
                    String status = stage.findElement(stageStatusLocator).getText();
                    return status.toLowerCase().contains("completed") || 
                           status.toLowerCase().contains("success") ||
                           status.toLowerCase().contains("failed") ||
                           status.toLowerCase().contains("error");
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (Exception e) {
            // Fallback: wait for a fixed time
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private List<Map<String, String>> extractTableData(WebElement table) {
        try {
            List<Map<String, String>> data = new java.util.ArrayList<>();
            List<WebElement> headers = table.findElements(By.xpath(".//th"));
            List<WebElement> rows = table.findElements(By.xpath(".//tr[position()>1]"));
            
            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.xpath(".//td"));
                Map<String, String> rowData = new HashMap<>();
                
                for (int i = 0; i < Math.min(headers.size(), cells.size()); i++) {
                    String header = headers.get(i).getText();
                    String cellValue = cells.get(i).getText();
                    rowData.put(header, cellValue);
                }
                data.add(rowData);
            }
            return data;
        } catch (Exception e) {
            log.warn("Could not extract table data: {}", e.getMessage());
            return List.of();
        }
    }
    
    private void clickElement(WebElement element) {
        waitForElementToBeClickable(element);
        element.click();
    }
    
    private void waitForElementToBeClickable(WebElement element) {
        wait.until(driver -> element.isDisplayed() && element.isEnabled());
    }
}