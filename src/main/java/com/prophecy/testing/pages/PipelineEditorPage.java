package com.prophecy.testing.pages;

import com.prophecy.testing.models.PipelineStage;
import com.prophecy.testing.models.StageType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object Model for Prophecy Pipeline Editor Page
 */
public class PipelineEditorPage extends BasePage {
    
    // Locators
    private static final By PIPELINE_CANVAS = By.xpath("//div[contains(@class, 'pipeline-canvas') or contains(@class, 'canvas')]");
    private static final By STAGE_PALETTE = By.xpath("//div[contains(@class, 'stage-palette') or contains(@class, 'palette')]");
    private static final By SAVE_BUTTON = By.xpath("//button[contains(text(), 'Save') or contains(@title, 'Save')]");
    private static final By RUN_BUTTON = By.xpath("//button[contains(text(), 'Run') or contains(@title, 'Run')]");
    private static final By VALIDATE_BUTTON = By.xpath("//button[contains(text(), 'Validate') or contains(@title, 'Validate')]");
    private static final By PIPELINE_NAME_INPUT = By.xpath("//input[contains(@placeholder, 'Pipeline Name') or contains(@name, 'name')]");
    
    // Stage elements
    private static final By STAGE_ELEMENTS = By.xpath("//div[contains(@class, 'stage') or contains(@class, 'node')]");
    private static final By SOURCE_STAGES = By.xpath("//div[contains(@class, 'source-stage')]");
    private static final By TRANSFORMATION_STAGES = By.xpath("//div[contains(@class, 'transform-stage')]");
    private static final By TARGET_STAGES = By.xpath("//div[contains(@class, 'target-stage')]");
    
    // Palette items
    private static final By SOURCE_PALETTE_ITEM = By.xpath("//div[contains(@class, 'palette-item') and contains(text(), 'Source')]");
    private static final By TRANSFORM_PALETTE_ITEM = By.xpath("//div[contains(@class, 'palette-item') and contains(text(), 'Transform')]");
    private static final By TARGET_PALETTE_ITEM = By.xpath("//div[contains(@class, 'palette-item') and contains(text(), 'Target')]");
    
    // Properties panel
    private static final By PROPERTIES_PANEL = By.xpath("//div[contains(@class, 'properties-panel') or contains(@class, 'properties')]");
    private static final By STAGE_NAME_INPUT = By.xpath("//input[contains(@placeholder, 'Stage Name') or contains(@name, 'stageName')]");
    private static final By STAGE_DESCRIPTION_INPUT = By.xpath("//textarea[contains(@placeholder, 'Description') or contains(@name, 'description')]");
    
    // Execution panel
    private static final By EXECUTION_PANEL = By.xpath("//div[contains(@class, 'execution-panel') or contains(@class, 'execution')]");
    private static final By EXECUTION_STATUS = By.xpath("//div[contains(@class, 'execution-status') or contains(@class, 'status')]");
    private static final By EXECUTION_LOGS = By.xpath("//div[contains(@class, 'execution-logs') or contains(@class, 'logs')]");
    
    // Data preview
    private static final By DATA_PREVIEW_PANEL = By.xpath("//div[contains(@class, 'data-preview') or contains(@class, 'preview')]");
    private static final By PREVIEW_DATA_BUTTON = By.xpath("//button[contains(text(), 'Preview') or contains(@title, 'Preview')]");
    
    /**
     * Check if pipeline editor is loaded
     */
    public boolean isPipelineEditorLoaded() {
        try {
            waitForPageToLoad();
            return isElementDisplayed(PIPELINE_CANVAS) && isElementDisplayed(STAGE_PALETTE);
        } catch (Exception e) {
            logger.error("Error checking if pipeline editor is loaded: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Set pipeline name
     */
    public void setPipelineName(String pipelineName) {
        if (isElementDisplayed(PIPELINE_NAME_INPUT)) {
            enterText(PIPELINE_NAME_INPUT, pipelineName);
            logger.info("Set pipeline name: {}", pipelineName);
        } else {
            logger.warn("Pipeline name input not found");
        }
    }
    
    /**
     * Add source stage to pipeline
     */
    public void addSourceStage(String stageType) {
        dragStageFromPalette(SOURCE_PALETTE_ITEM, stageType);
        logger.info("Added source stage: {}", stageType);
    }
    
    /**
     * Add transformation stage to pipeline
     */
    public void addTransformationStage(String stageType) {
        dragStageFromPalette(TRANSFORM_PALETTE_ITEM, stageType);
        logger.info("Added transformation stage: {}", stageType);
    }
    
    /**
     * Add target stage to pipeline
     */
    public void addTargetStage(String stageType) {
        dragStageFromPalette(TARGET_PALETTE_ITEM, stageType);
        logger.info("Added target stage: {}", stageType);
    }
    
    /**
     * Connect two stages
     */
    public void connectStages(String sourceStage, String targetStage) {
        WebElement sourceElement = findStageByName(sourceStage);
        WebElement targetElement = findStageByName(targetStage);
        
        // Perform drag and drop to connect stages
        dragAndDropStages(sourceElement, targetElement);
        logger.info("Connected stages: {} -> {}", sourceStage, targetStage);
    }
    
    /**
     * Configure stage properties
     */
    public void configureStage(String stageName, String propertyName, String propertyValue) {
        selectStage(stageName);
        
        if (isElementDisplayed(PROPERTIES_PANEL)) {
            By propertyInput = By.xpath("//input[@name='" + propertyName + "' or @placeholder*='" + propertyName + "']");
            if (isElementDisplayed(propertyInput)) {
                enterText(propertyInput, propertyValue);
                logger.info("Configured stage {} property {}: {}", stageName, propertyName, propertyValue);
            } else {
                logger.warn("Property input not found: {}", propertyName);
            }
        } else {
            logger.warn("Properties panel not visible");
        }
    }
    
    /**
     * Set stage name
     */
    public void setStageNameAndDescription(String currentName, String newName, String description) {
        selectStage(currentName);
        
        if (isElementDisplayed(STAGE_NAME_INPUT)) {
            enterText(STAGE_NAME_INPUT, newName);
        }
        
        if (isElementDisplayed(STAGE_DESCRIPTION_INPUT)) {
            enterText(STAGE_DESCRIPTION_INPUT, description);
        }
        
        logger.info("Set stage name: {} and description: {}", newName, description);
    }
    
    /**
     * Save pipeline
     */
    public void savePipeline() {
        clickElement(SAVE_BUTTON);
        waitFor(2); // Wait for save to complete
        logger.info("Pipeline saved");
    }
    
    /**
     * Run pipeline
     */
    public void runPipeline() {
        clickElement(RUN_BUTTON);
        logger.info("Pipeline execution started");
    }
    
    /**
     * Validate pipeline
     */
    public void validatePipeline() {
        clickElement(VALIDATE_BUTTON);
        waitFor(2); // Wait for validation to complete
        logger.info("Pipeline validation completed");
    }
    
    /**
     * Get pipeline execution status
     */
    public String getExecutionStatus() {
        if (isElementDisplayed(EXECUTION_STATUS)) {
            return getText(EXECUTION_STATUS);
        }
        return "Unknown";
    }
    
    /**
     * Get execution logs
     */
    public String getExecutionLogs() {
        if (isElementDisplayed(EXECUTION_LOGS)) {
            return getText(EXECUTION_LOGS);
        }
        return "";
    }
    
    /**
     * Preview data for a stage
     */
    public void previewStageData(String stageName) {
        selectStage(stageName);
        
        if (isElementDisplayed(PREVIEW_DATA_BUTTON)) {
            clickElement(PREVIEW_DATA_BUTTON);
            logger.info("Previewing data for stage: {}", stageName);
        } else {
            logger.warn("Preview data button not found");
        }
    }
    
    /**
     * Get data preview content
     */
    public String getDataPreview() {
        if (isElementDisplayed(DATA_PREVIEW_PANEL)) {
            return getText(DATA_PREVIEW_PANEL);
        }
        return "";
    }
    
    /**
     * Get list of stages in pipeline
     */
    public List<String> getStageNames() {
        List<WebElement> stageElements = waitForElementsToBePresent(STAGE_ELEMENTS);
        return stageElements.stream()
                .map(element -> {
                    try {
                        return element.getAttribute("data-stage-name");
                    } catch (Exception e) {
                        return element.getText();
                    }
                })
                .toList();
    }
    
    /**
     * Check if stage exists in pipeline
     */
    public boolean isStageExists(String stageName) {
        try {
            findStageByName(stageName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Delete stage from pipeline
     */
    public void deleteStage(String stageName) {
        WebElement stageElement = findStageByName(stageName);
        stageElement.click(); // Select stage
        
        // Right-click to open context menu
        clickElementWithJS(By.xpath("//div[contains(@class, 'context-menu')]//button[contains(text(), 'Delete')]"));
        
        logger.info("Deleted stage: {}", stageName);
    }
    
    /**
     * Wait for pipeline execution to complete
     */
    public void waitForExecutionToComplete() {
        int maxWaitTime = 300; // 5 minutes
        int waitTime = 0;
        
        while (waitTime < maxWaitTime) {
            String status = getExecutionStatus();
            if (status.toLowerCase().contains("completed") || 
                status.toLowerCase().contains("failed") || 
                status.toLowerCase().contains("success")) {
                break;
            }
            
            waitFor(5);
            waitTime += 5;
        }
        
        logger.info("Pipeline execution completed with status: {}", getExecutionStatus());
    }
    
    /**
     * Helper method to drag stage from palette
     */
    private void dragStageFromPalette(By paletteItem, String stageType) {
        WebElement paletteElement = waitForElementToBeVisible(paletteItem);
        WebElement canvas = waitForElementToBeVisible(PIPELINE_CANVAS);
        
        // Expand palette if needed
        paletteElement.click();
        
        // Find specific stage type
        By specificStage = By.xpath("//div[contains(@class, 'stage-type') and contains(text(), '" + stageType + "')]");
        if (isElementDisplayed(specificStage)) {
            WebElement stageTypeElement = driver.findElement(specificStage);
            dragAndDropToCanvas(stageTypeElement, canvas);
        } else {
            // Generic drag to canvas
            dragAndDropToCanvas(paletteElement, canvas);
        }
    }
    
    /**
     * Helper method to find stage by name
     */
    private WebElement findStageByName(String stageName) {
        List<WebElement> stageElements = waitForElementsToBePresent(STAGE_ELEMENTS);
        
        for (WebElement stage : stageElements) {
            try {
                String stageNameAttr = stage.getAttribute("data-stage-name");
                if (stageName.equals(stageNameAttr)) {
                    return stage;
                }
            } catch (Exception e) {
                if (stage.getText().contains(stageName)) {
                    return stage;
                }
            }
        }
        
        throw new RuntimeException("Stage not found: " + stageName);
    }
    
    /**
     * Helper method to select a stage
     */
    private void selectStage(String stageName) {
        WebElement stageElement = findStageByName(stageName);
        stageElement.click();
        logger.debug("Selected stage: {}", stageName);
    }
    
    /**
     * Helper method to drag and drop stages
     */
    private void dragAndDropStages(WebElement source, WebElement target) {
        // Implementation depends on the specific UI framework used by Prophecy
        // This is a placeholder for the actual drag and drop implementation
        executeJavaScript("arguments[0].dispatchEvent(new Event('dragstart'));", source);
        executeJavaScript("arguments[0].dispatchEvent(new Event('drop'));", target);
    }
    
    /**
     * Helper method to drag and drop to canvas
     */
    private void dragAndDropToCanvas(WebElement source, WebElement canvas) {
        // Implementation depends on the specific UI framework used by Prophecy
        // This is a placeholder for the actual drag and drop implementation
        executeJavaScript("arguments[0].dispatchEvent(new Event('dragstart'));", source);
        executeJavaScript("arguments[0].dispatchEvent(new Event('drop'));", canvas);
    }
}