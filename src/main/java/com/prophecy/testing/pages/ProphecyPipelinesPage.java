package com.prophecy.testing.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for Prophecy Pipelines Page
 */
@Slf4j
public class ProphecyPipelinesPage extends BasePage {
    
    // Page elements
    @FindBy(xpath = "//h1[contains(text(), 'Pipelines') or contains(text(), 'Pipeline')]")
    private WebElement pipelinesTitle;
    
    @FindBy(xpath = "//button[contains(text(), 'Create Pipeline') or contains(text(), 'New Pipeline')]")
    private WebElement createPipelineButton;
    
    @FindBy(xpath = "//input[@placeholder='Search pipelines' or @placeholder='Search' or @type='search']")
    private WebElement searchBox;
    
    @FindBy(xpath = "//div[contains(@class, 'pipeline-list') or contains(@class, 'pipeline-grid')]//div[contains(@class, 'pipeline-item') or contains(@class, 'pipeline-card')]")
    private List<WebElement> pipelineItems;
    
    @FindBy(xpath = "//button[contains(text(), 'Filter') or contains(@class, 'filter')]")
    private WebElement filterButton;
    
    @FindBy(xpath = "//select[contains(@name, 'sort') or contains(@class, 'sort')]")
    private WebElement sortDropdown;
    
    // Alternative locators
    private final By pipelineListLocator = By.xpath("//div[contains(@class, 'pipeline') and contains(@class, 'list')]");
    private final By pipelineNameLocator = By.xpath(".//h3 | .//h4 | .//span[contains(@class, 'name')] | .//a[contains(@class, 'title')]");
    private final By pipelineStatusLocator = By.xpath(".//span[contains(@class, 'status')] | .//div[contains(@class, 'status')]");
    
    /**
     * Verify pipelines page is loaded
     */
    public boolean isPipelinesPageLoaded() {
        try {
            waitForPageToLoad();
            return pipelinesTitle.isDisplayed() || 
                   getCurrentUrl().contains("pipeline") ||
                   isElementDisplayed(pipelineListLocator);
        } catch (Exception e) {
            log.warn("Pipelines page not loaded: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Search for pipelines
     */
    public ProphecyPipelinesPage searchPipelines(String searchTerm) {
        try {
            if (searchBox.isDisplayed()) {
                searchBox.clear();
                searchBox.sendKeys(searchTerm);
                // Wait for search results
                Thread.sleep(1000);
                log.info("Searched for pipelines: {}", searchTerm);
            }
        } catch (Exception e) {
            log.warn("Search functionality not available: {}", e.getMessage());
        }
        return this;
    }
    
    /**
     * Get list of all pipeline names
     */
    public List<String> getAllPipelineNames() {
        try {
            return pipelineItems.stream()
                    .map(item -> {
                        try {
                            WebElement nameElement = item.findElement(pipelineNameLocator);
                            return nameElement.getText().trim();
                        } catch (Exception e) {
                            return item.getText().split("\n")[0].trim();
                        }
                    })
                    .filter(name -> !name.isEmpty())
                    .toList();
        } catch (Exception e) {
            log.warn("Could not get pipeline names: {}", e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Open a specific pipeline by name
     */
    public ProphecyPipelinePage openPipeline(String pipelineName) {
        try {
            WebElement pipelineItem = findPipelineByName(pipelineName);
            if (pipelineItem != null) {
                clickElement(pipelineItem);
                log.info("Opened pipeline: {}", pipelineName);
                waitForPageToLoad();
                return new ProphecyPipelinePage();
            } else {
                throw new RuntimeException("Pipeline not found: " + pipelineName);
            }
        } catch (Exception e) {
            log.error("Failed to open pipeline: {}", pipelineName, e);
            throw new RuntimeException("Failed to open pipeline: " + pipelineName, e);
        }
    }
    
    /**
     * Check if a pipeline exists
     */
    public boolean isPipelinePresent(String pipelineName) {
        return findPipelineByName(pipelineName) != null;
    }
    
    /**
     * Get pipeline status
     */
    public String getPipelineStatus(String pipelineName) {
        try {
            WebElement pipelineItem = findPipelineByName(pipelineName);
            if (pipelineItem != null) {
                WebElement statusElement = pipelineItem.findElement(pipelineStatusLocator);
                String status = statusElement.getText().trim();
                log.info("Pipeline '{}' status: {}", pipelineName, status);
                return status;
            }
        } catch (Exception e) {
            log.warn("Could not get status for pipeline: {}", pipelineName);
        }
        return "Unknown";
    }
    
    /**
     * Get total number of pipelines
     */
    public int getPipelineCount() {
        try {
            return pipelineItems.size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Click create pipeline button
     */
    public ProphecyPipelinePage createNewPipeline() {
        try {
            if (createPipelineButton.isDisplayed()) {
                clickElement(createPipelineButton);
                log.info("Clicked create pipeline button");
                waitForPageToLoad();
                return new ProphecyPipelinePage();
            }
        } catch (Exception e) {
            log.error("Failed to create new pipeline: {}", e.getMessage());
        }
        return new ProphecyPipelinePage();
    }
    
    /**
     * Apply filter
     */
    public ProphecyPipelinesPage applyFilter() {
        try {
            if (filterButton.isDisplayed()) {
                clickElement(filterButton);
                log.info("Applied filter");
            }
        } catch (Exception e) {
            log.warn("Filter not available: {}", e.getMessage());
        }
        return this;
    }
    
    /**
     * Sort pipelines
     */
    public ProphecyPipelinesPage sortPipelines(String sortOption) {
        try {
            if (sortDropdown.isDisplayed()) {
                sortDropdown.click();
                WebElement option = driver.findElement(By.xpath("//option[contains(text(), '" + sortOption + "')]"));
                option.click();
                log.info("Sorted pipelines by: {}", sortOption);
            }
        } catch (Exception e) {
            log.warn("Sort functionality not available: {}", e.getMessage());
        }
        return this;
    }
    
    /**
     * Get pipelines page title
     */
    public String getPipelinesPageTitle() {
        try {
            if (pipelinesTitle.isDisplayed()) {
                return pipelinesTitle.getText();
            }
        } catch (Exception e) {
            log.debug("Pipelines title not found");
        }
        return getPageTitle();
    }
    
    /**
     * Refresh pipelines list
     */
    public ProphecyPipelinesPage refreshPipelinesList() {
        refreshPage();
        log.info("Refreshed pipelines list");
        return this;
    }
    
    // Helper methods
    private WebElement findPipelineByName(String pipelineName) {
        try {
            return pipelineItems.stream()
                    .filter(item -> {
                        try {
                            String itemText = item.getText();
                            WebElement nameElement = item.findElement(pipelineNameLocator);
                            String nameText = nameElement.getText();
                            return itemText.contains(pipelineName) || nameText.contains(pipelineName);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            log.warn("Error finding pipeline: {}", pipelineName, e);
            return null;
        }
    }
    
    
    private void waitForElementToBeClickable(WebElement element) {
        wait.until(driver -> element.isDisplayed() && element.isEnabled());
    }
}