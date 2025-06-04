package com.prophecy.testing.pages;

import org.openqa.selenium.By;

/**
 * Page Object Model for Prophecy Dashboard Page
 */
public class DashboardPage extends BasePage {
    
    // Locators
    private static final By USER_MENU = By.xpath("//div[contains(@class, 'user-menu') or contains(@class, 'profile')]");
    private static final By LOGOUT_BUTTON = By.xpath("//button[contains(text(), 'Logout') or contains(text(), 'Sign Out')]");
    private static final By CREATE_PROJECT_BUTTON = By.xpath("//button[contains(text(), 'Create Project') or contains(text(), 'New Project')]");
    private static final By PROJECTS_SECTION = By.xpath("//div[contains(@class, 'projects') or contains(text(), 'Projects')]");
    private static final By PIPELINES_SECTION = By.xpath("//div[contains(@class, 'pipelines') or contains(text(), 'Pipelines')]");
    private static final By SEARCH_BOX = By.xpath("//input[@placeholder*='Search' or @type='search']");
    private static final By NAVIGATION_MENU = By.xpath("//nav | //div[contains(@class, 'navigation')]");
    
    // Navigation links
    private static final By PIPELINES_LINK = By.xpath("//a[contains(text(), 'Pipelines') or contains(@href, 'pipeline')]");
    private static final By PROJECTS_LINK = By.xpath("//a[contains(text(), 'Projects') or contains(@href, 'project')]");
    private static final By DATASETS_LINK = By.xpath("//a[contains(text(), 'Datasets') or contains(@href, 'dataset')]");
    private static final By JOBS_LINK = By.xpath("//a[contains(text(), 'Jobs') or contains(@href, 'job')]");
    
    /**
     * Check if dashboard is loaded
     */
    public boolean isDashboardLoaded() {
        try {
            waitForPageToLoad();
            return isElementDisplayed(NAVIGATION_MENU) || 
                   isElementDisplayed(PROJECTS_SECTION) || 
                   isElementDisplayed(PIPELINES_SECTION);
        } catch (Exception e) {
            logger.error("Error checking if dashboard is loaded: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Navigate to pipelines section
     */
    public PipelinesPage navigateToPipelines() {
        clickElement(PIPELINES_LINK);
        logger.info("Navigated to pipelines section");
        return new PipelinesPage();
    }
    
    /**
     * Navigate to projects section
     */
    public ProjectsPage navigateToProjects() {
        clickElement(PROJECTS_LINK);
        logger.info("Navigated to projects section");
        return new ProjectsPage();
    }
    
    /**
     * Create new project
     */
    public void createNewProject() {
        clickElement(CREATE_PROJECT_BUTTON);
        logger.info("Clicked create new project button");
    }
    
    /**
     * Search for items
     */
    public void searchFor(String searchTerm) {
        if (isElementDisplayed(SEARCH_BOX)) {
            enterText(SEARCH_BOX, searchTerm);
            logger.info("Searched for: {}", searchTerm);
        } else {
            logger.warn("Search box not found on dashboard");
        }
    }
    
    /**
     * Logout from application
     */
    public LoginPage logout() {
        try {
            // Click user menu first if it exists
            if (isElementDisplayed(USER_MENU)) {
                clickElement(USER_MENU);
                waitFor(1); // Wait for menu to expand
            }
            
            clickElement(LOGOUT_BUTTON);
            logger.info("Logged out successfully");
            return new LoginPage();
        } catch (Exception e) {
            logger.error("Error during logout: {}", e.getMessage());
            throw new RuntimeException("Failed to logout", e);
        }
    }
    
    /**
     * Get dashboard title
     */
    public String getDashboardTitle() {
        return getPageTitle();
    }
    
    /**
     * Check if user is logged in
     */
    public boolean isUserLoggedIn() {
        return isDashboardLoaded() && !getCurrentUrl().contains("login");
    }
    
    /**
     * Verify dashboard elements
     */
    public boolean verifyDashboardElements() {
        boolean navigationPresent = isElementDisplayed(NAVIGATION_MENU);
        boolean pipelinesLinkPresent = isElementDisplayed(PIPELINES_LINK);
        boolean projectsLinkPresent = isElementDisplayed(PROJECTS_LINK);
        
        logger.info("Dashboard elements verification - Navigation: {}, Pipelines: {}, Projects: {}", 
                   navigationPresent, pipelinesLinkPresent, projectsLinkPresent);
        
        return navigationPresent && (pipelinesLinkPresent || projectsLinkPresent);
    }
    
    /**
     * Wait for dashboard to fully load
     */
    public void waitForDashboardToLoad() {
        waitForPageToLoad();
        
        // Wait for main content to be visible
        try {
            waitForElementToBeVisible(NAVIGATION_MENU);
        } catch (Exception e) {
            // Try alternative approach
            waitFor(3);
        }
        
        logger.info("Dashboard fully loaded");
    }
    
    /**
     * Get current user info (if displayed)
     */
    public String getCurrentUserInfo() {
        try {
            if (isElementDisplayed(USER_MENU)) {
                return getText(USER_MENU);
            }
        } catch (Exception e) {
            logger.debug("Could not retrieve user info: {}", e.getMessage());
        }
        return "";
    }
    
    /**
     * Check if create project button is available
     */
    public boolean isCreateProjectButtonAvailable() {
        return isElementDisplayed(CREATE_PROJECT_BUTTON);
    }
    
    /**
     * Navigate to datasets section
     */
    public void navigateToDatasets() {
        if (isElementDisplayed(DATASETS_LINK)) {
            clickElement(DATASETS_LINK);
            logger.info("Navigated to datasets section");
        } else {
            logger.warn("Datasets link not found");
        }
    }
    
    /**
     * Navigate to jobs section
     */
    public void navigateToJobs() {
        if (isElementDisplayed(JOBS_LINK)) {
            clickElement(JOBS_LINK);
            logger.info("Navigated to jobs section");
        } else {
            logger.warn("Jobs link not found");
        }
    }
}