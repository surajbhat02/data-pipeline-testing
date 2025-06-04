package com.prophecy.testing.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object Model for Prophecy Projects Page
 */
public class ProjectsPage extends BasePage {
    
    // Locators
    private static final By CREATE_PROJECT_BUTTON = By.xpath("//button[contains(text(), 'Create Project') or contains(text(), 'New Project')]");
    private static final By PROJECT_LIST = By.xpath("//div[contains(@class, 'project-list') or contains(@class, 'projects')]");
    private static final By PROJECT_ITEMS = By.xpath("//div[contains(@class, 'project-item') or contains(@class, 'project-card')]");
    private static final By SEARCH_PROJECT = By.xpath("//input[@placeholder*='Search' or @placeholder*='project']");
    private static final By LOADING_INDICATOR = By.xpath("//div[contains(@class, 'loading') or contains(@class, 'spinner')]");
    
    // Project actions
    private static final By PROJECT_NAME_LINK = By.xpath(".//a[contains(@class, 'project-name') or contains(@class, 'title')]");
    private static final By OPEN_PROJECT_BUTTON = By.xpath(".//button[contains(text(), 'Open') or contains(@title, 'Open')]");
    private static final By EDIT_PROJECT_BUTTON = By.xpath(".//button[contains(text(), 'Edit') or contains(@title, 'Edit')]");
    private static final By DELETE_PROJECT_BUTTON = By.xpath(".//button[contains(text(), 'Delete') or contains(@title, 'Delete')]");
    
    /**
     * Check if projects page is loaded
     */
    public boolean isProjectsPageLoaded() {
        try {
            waitForPageToLoad();
            return isElementDisplayed(CREATE_PROJECT_BUTTON) || isElementDisplayed(PROJECT_LIST);
        } catch (Exception e) {
            logger.error("Error checking if projects page is loaded: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Create new project
     */
    public void createNewProject() {
        clickElement(CREATE_PROJECT_BUTTON);
        logger.info("Clicked create new project button");
    }
    
    /**
     * Search for project
     */
    public void searchProject(String projectName) {
        if (isElementDisplayed(SEARCH_PROJECT)) {
            enterText(SEARCH_PROJECT, projectName);
            logger.info("Searched for project: {}", projectName);
            waitFor(2); // Wait for search results
        } else {
            logger.warn("Search box not found on projects page");
        }
    }
    
    /**
     * Get list of project names
     */
    public List<String> getProjectNames() {
        List<WebElement> projectElements = waitForElementsToBePresent(PROJECT_ITEMS);
        return projectElements.stream()
                .map(element -> {
                    try {
                        WebElement nameElement = element.findElement(PROJECT_NAME_LINK);
                        return nameElement.getText();
                    } catch (Exception e) {
                        return element.getText().split("\n")[0]; // Get first line as name
                    }
                })
                .toList();
    }
    
    /**
     * Open project by name
     */
    public void openProject(String projectName) {
        WebElement projectElement = findProjectByName(projectName);
        try {
            WebElement openButton = projectElement.findElement(OPEN_PROJECT_BUTTON);
            openButton.click();
        } catch (Exception e) {
            // Try clicking on project name link
            WebElement nameElement = projectElement.findElement(PROJECT_NAME_LINK);
            nameElement.click();
        }
        logger.info("Opened project: {}", projectName);
    }
    
    /**
     * Edit project by name
     */
    public void editProject(String projectName) {
        WebElement projectElement = findProjectByName(projectName);
        WebElement editButton = projectElement.findElement(EDIT_PROJECT_BUTTON);
        editButton.click();
        logger.info("Clicked edit button for project: {}", projectName);
    }
    
    /**
     * Delete project by name
     */
    public void deleteProject(String projectName) {
        WebElement projectElement = findProjectByName(projectName);
        WebElement deleteButton = projectElement.findElement(DELETE_PROJECT_BUTTON);
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
        
        logger.info("Deleted project: {}", projectName);
    }
    
    /**
     * Check if project exists
     */
    public boolean isProjectExists(String projectName) {
        try {
            findProjectByName(projectName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Wait for projects to load
     */
    public void waitForProjectsToLoad() {
        // Wait for loading indicator to disappear
        if (isElementDisplayed(LOADING_INDICATOR)) {
            waitForElementToDisappear(LOADING_INDICATOR);
        }
        
        // Wait for project list to be visible
        waitForElementToBeVisible(PROJECT_LIST);
        
        logger.info("Projects loaded successfully");
    }
    
    /**
     * Get total number of projects
     */
    public int getProjectCount() {
        try {
            List<WebElement> projectElements = driver.findElements(PROJECT_ITEMS);
            return projectElements.size();
        } catch (Exception e) {
            logger.warn("Could not count projects: {}", e.getMessage());
            return 0;
        }
    }
    
    /**
     * Helper method to find project by name
     */
    private WebElement findProjectByName(String projectName) {
        List<WebElement> projectElements = waitForElementsToBePresent(PROJECT_ITEMS);
        
        for (WebElement project : projectElements) {
            try {
                WebElement nameElement = project.findElement(PROJECT_NAME_LINK);
                if (nameElement.getText().equals(projectName)) {
                    return project;
                }
            } catch (Exception e) {
                // Try alternative approach - check if project text contains the name
                if (project.getText().contains(projectName)) {
                    return project;
                }
            }
        }
        
        throw new RuntimeException("Project not found: " + projectName);
    }
    
    /**
     * Refresh projects list
     */
    public void refreshProjectsList() {
        refreshPage();
        waitForProjectsToLoad();
        logger.info("Refreshed projects list");
    }
}