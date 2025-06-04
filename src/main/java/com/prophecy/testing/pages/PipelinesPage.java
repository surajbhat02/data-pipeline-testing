package com.prophecy.testing.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object Model for Prophecy Pipelines Page
 */
public class PipelinesPage extends BasePage {
    
    // Locators
    private static final By CREATE_PIPELINE_BUTTON = By.xpath("//button[contains(text(), 'Create Pipeline') or contains(text(), 'New Pipeline')]");
    private static final By PIPELINE_LIST = By.xpath("//div[contains(@class, 'pipeline-list') or contains(@class, 'pipelines')]");
    private static final By PIPELINE_ITEMS = By.xpath("//div[contains(@class, 'pipeline-item') or contains(@class, 'pipeline-card')]");
    private static final By SEARCH_PIPELINE = By.xpath("//input[@placeholder*='Search' or @placeholder*='pipeline']");
    private static final By FILTER_DROPDOWN = By.xpath("//select[contains(@class, 'filter') or contains(@name, 'filter')]");
    private static final By SORT_DROPDOWN = By.xpath("//select[contains(@class, 'sort') or contains(@name, 'sort')]");
    private static final By LOADING_INDICATOR = By.xpath("//div[contains(@class, 'loading') or contains(@class, 'spinner')]");
    
    // Pipeline actions
    private static final By PIPELINE_NAME_LINK = By.xpath(".//a[contains(@class, 'pipeline-name') or contains(@class, 'title')]");
    private static final By EDIT_PIPELINE_BUTTON = By.xpath(".//button[contains(text(), 'Edit') or contains(@title, 'Edit')]");
    private static final By DELETE_PIPELINE_BUTTON = By.xpath(".//button[contains(text(), 'Delete') or contains(@title, 'Delete')]");
    private static final By RUN_PIPELINE_BUTTON = By.xpath(".//button[contains(text(), 'Run') or contains(@title, 'Run')]");
    private static final By PIPELINE_STATUS = By.xpath(".//span[contains(@class, 'status') or contains(@class, 'state')]");
    
    /**
     * Check if pipelines page is loaded
     */
    public boolean isPipelinesPageLoaded() {
        try {
            waitForPageToLoad();
            return isElementDisplayed(CREATE_PIPELINE_BUTTON) || isElementDisplayed(PIPELINE_LIST);
        } catch (Exception e) {
            logger.error("Error checking if pipelines page is loaded: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Create new pipeline
     */
    public PipelineEditorPage createNewPipeline() {
        clickElement(CREATE_PIPELINE_BUTTON);
        logger.info("Clicked create new pipeline button");
        return new PipelineEditorPage();
    }
    
    /**
     * Search for pipeline
     */
    public void searchPipeline(String pipelineName) {
        if (isElementDisplayed(SEARCH_PIPELINE)) {
            enterText(SEARCH_PIPELINE, pipelineName);
            logger.info("Searched for pipeline: {}", pipelineName);
            waitFor(2); // Wait for search results
        } else {
            logger.warn("Search box not found on pipelines page");
        }
    }
    
    /**
     * Get list of pipeline names
     */
    public List<String> getPipelineNames() {
        List<WebElement> pipelineElements = waitForElementsToBePresent(PIPELINE_ITEMS);
        return pipelineElements.stream()
                .map(element -> {
                    try {
                        WebElement nameElement = element.findElement(PIPELINE_NAME_LINK);
                        return nameElement.getText();
                    } catch (Exception e) {
                        return element.getText().split("\n")[0]; // Get first line as name
                    }
                })
                .toList();
    }
    
    /**
     * Open pipeline by name
     */
    public PipelineEditorPage openPipeline(String pipelineName) {
        List<WebElement> pipelineElements = waitForElementsToBePresent(PIPELINE_ITEMS);
        
        for (WebElement pipeline : pipelineElements) {
            try {
                WebElement nameElement = pipeline.findElement(PIPELINE_NAME_LINK);
                if (nameElement.getText().equals(pipelineName)) {
                    nameElement.click();
                    logger.info("Opened pipeline: {}", pipelineName);
                    return new PipelineEditorPage();
                }
            } catch (Exception e) {
                logger.debug("Could not find name element in pipeline item");
            }
        }
        
        throw new RuntimeException("Pipeline not found: " + pipelineName);
    }
    
    /**
     * Edit pipeline by name
     */
    public PipelineEditorPage editPipeline(String pipelineName) {
        WebElement pipelineElement = findPipelineByName(pipelineName);
        WebElement editButton = pipelineElement.findElement(EDIT_PIPELINE_BUTTON);
        editButton.click();
        logger.info("Clicked edit button for pipeline: {}", pipelineName);
        return new PipelineEditorPage();
    }
    
    /**
     * Run pipeline by name
     */
    public void runPipeline(String pipelineName) {
        WebElement pipelineElement = findPipelineByName(pipelineName);
        WebElement runButton = pipelineElement.findElement(RUN_PIPELINE_BUTTON);
        runButton.click();
        logger.info("Started pipeline run: {}", pipelineName);
    }
    
    /**
     * Delete pipeline by name
     */
    public void deletePipeline(String pipelineName) {
        WebElement pipelineElement = findPipelineByName(pipelineName);
        WebElement deleteButton = pipelineElement.findElement(DELETE_PIPELINE_BUTTON);
        deleteButton.click();
        
        // Handle confirmation dialog if present
        try {
            waitFor(1);
            if (isElementDisplayed(By.xpath("//button[contains(text(), 'Confirm') or contains(text(), 'Yes')]"))) {
                clickElement(By.xpath("//button[contains(text(), 'Confirm') or contains(text(), 'Yes')]"));
            }
        } catch (Exception e) {
            logger.debug("No confirmation dialog found");
        }
        
        logger.info("Deleted pipeline: {}", pipelineName);
    }
    
    /**
     * Get pipeline status by name
     */
    public String getPipelineStatus(String pipelineName) {
        WebElement pipelineElement = findPipelineByName(pipelineName);
        try {
            WebElement statusElement = pipelineElement.findElement(PIPELINE_STATUS);
            return statusElement.getText();
        } catch (Exception e) {
            logger.warn("Could not find status for pipeline: {}", pipelineName);
            return "Unknown";
        }
    }
    
    /**
     * Check if pipeline exists
     */
    public boolean isPipelineExists(String pipelineName) {
        try {
            findPipelineByName(pipelineName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Wait for pipelines to load
     */
    public void waitForPipelinesToLoad() {
        // Wait for loading indicator to disappear
        if (isElementDisplayed(LOADING_INDICATOR)) {
            waitForElementToDisappear(LOADING_INDICATOR);
        }
        
        // Wait for pipeline list to be visible
        waitForElementToBeVisible(PIPELINE_LIST);
        
        logger.info("Pipelines loaded successfully");
    }
    
    /**
     * Get total number of pipelines
     */
    public int getPipelineCount() {
        try {
            List<WebElement> pipelineElements = driver.findElements(PIPELINE_ITEMS);
            return pipelineElements.size();
        } catch (Exception e) {
            logger.warn("Could not count pipelines: {}", e.getMessage());
            return 0;
        }
    }
    
    /**
     * Apply filter
     */
    public void applyFilter(String filterValue) {
        if (isElementDisplayed(FILTER_DROPDOWN)) {
            WebElement filterDropdown = driver.findElement(FILTER_DROPDOWN);
            filterDropdown.click();
            
            By filterOption = By.xpath("//option[contains(text(), '" + filterValue + "')]");
            clickElement(filterOption);
            
            logger.info("Applied filter: {}", filterValue);
            waitFor(2); // Wait for filter to apply
        } else {
            logger.warn("Filter dropdown not found");
        }
    }
    
    /**
     * Sort pipelines
     */
    public void sortPipelines(String sortOption) {
        if (isElementDisplayed(SORT_DROPDOWN)) {
            WebElement sortDropdown = driver.findElement(SORT_DROPDOWN);
            sortDropdown.click();
            
            By sortOptionLocator = By.xpath("//option[contains(text(), '" + sortOption + "')]");
            clickElement(sortOptionLocator);
            
            logger.info("Applied sort: {}", sortOption);
            waitFor(2); // Wait for sort to apply
        } else {
            logger.warn("Sort dropdown not found");
        }
    }
    
    /**
     * Helper method to find pipeline by name
     */
    private WebElement findPipelineByName(String pipelineName) {
        List<WebElement> pipelineElements = waitForElementsToBePresent(PIPELINE_ITEMS);
        
        for (WebElement pipeline : pipelineElements) {
            try {
                WebElement nameElement = pipeline.findElement(PIPELINE_NAME_LINK);
                if (nameElement.getText().equals(pipelineName)) {
                    return pipeline;
                }
            } catch (Exception e) {
                // Try alternative approach - check if pipeline text contains the name
                if (pipeline.getText().contains(pipelineName)) {
                    return pipeline;
                }
            }
        }
        
        throw new RuntimeException("Pipeline not found: " + pipelineName);
    }
    
    /**
     * Refresh pipelines list
     */
    public void refreshPipelinesList() {
        refreshPage();
        waitForPipelinesToLoad();
        logger.info("Refreshed pipelines list");
    }
}