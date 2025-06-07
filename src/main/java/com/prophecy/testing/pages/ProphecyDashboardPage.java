package com.prophecy.testing.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object for Prophecy Dashboard Page
 */
@Slf4j
public class ProphecyDashboardPage extends BasePage {
    
    // Dashboard elements
    @FindBy(xpath = "//h1[contains(text(), 'Dashboard') or contains(text(), 'Welcome')]")
    private WebElement dashboardTitle;
    
    @FindBy(xpath = "//button[contains(text(), 'Create') or contains(text(), 'New')]")
    private WebElement createButton;
    
    @FindBy(xpath = "//a[contains(text(), 'Pipelines') or contains(@href, 'pipeline')]")
    private WebElement pipelinesLink;
    
    @FindBy(xpath = "//a[contains(text(), 'Projects') or contains(@href, 'project')]")
    private WebElement projectsLink;
    
    @FindBy(xpath = "//div[contains(@class, 'user-menu') or contains(@class, 'profile')]")
    private WebElement userMenu;
    
    @FindBy(xpath = "//button[contains(text(), 'Logout') or contains(text(), 'Sign Out')]")
    private WebElement logoutButton;
    
    // Pipeline-related elements
    @FindBy(xpath = "//div[contains(@class, 'pipeline-card') or contains(@class, 'pipeline-item')]")
    private List<WebElement> pipelineCards;
    
    @FindBy(xpath = "//input[@placeholder='Search' or @type='search']")
    private WebElement searchBox;
    
    // Alternative locators
    private final By pipelinesMenuLocator = By.xpath("//nav//a[contains(text(), 'Pipelines')]");
    private final By createPipelineButtonLocator = By.xpath("//button[contains(text(), 'Create Pipeline')]");
    
    /**
     * Verify dashboard is loaded
     */
    public boolean isDashboardLoaded() {
        try {
            waitForPageToLoad();
            return dashboardTitle.isDisplayed() || 
                   getCurrentUrl().contains("dashboard") || 
                   getCurrentUrl().contains("home") ||
                   isElementDisplayed(pipelinesMenuLocator);
        } catch (Exception e) {
            log.warn("Dashboard not loaded: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Navigate to Pipelines section
     */
    public ProphecyPipelinesPage navigateToPipelines() {
        try {
            if (pipelinesLink.isDisplayed()) {
                clickElement(pipelinesLink);
            } else {
                clickElement(pipelinesMenuLocator);
            }
        } catch (Exception e) {
            clickElement(pipelinesMenuLocator);
        }
        log.info("Navigated to Pipelines section");
        waitForPageToLoad();
        return new ProphecyPipelinesPage();
    }
    
    /**
     * Click create button
     */
    public ProphecyDashboardPage clickCreateButton() {
        try {
            if (createButton.isDisplayed()) {
                clickElement(createButton);
            } else {
                clickElement(createPipelineButtonLocator);
            }
        } catch (Exception e) {
            clickElement(createPipelineButtonLocator);
        }
        log.info("Clicked create button");
        return this;
    }
    
    /**
     * Search for pipelines
     */
    public ProphecyDashboardPage searchPipelines(String searchTerm) {
        try {
            if (searchBox.isDisplayed()) {
                searchBox.clear();
                searchBox.sendKeys(searchTerm);
                log.info("Searched for: {}", searchTerm);
            }
        } catch (Exception e) {
            log.warn("Search box not found");
        }
        return this;
    }
    
    /**
     * Get list of pipeline names
     */
    public List<String> getPipelineNames() {
        try {
            return pipelineCards.stream()
                    .map(card -> {
                        try {
                            return card.findElement(By.xpath(".//h3 | .//h4 | .//span[contains(@class, 'name')]")).getText();
                        } catch (Exception e) {
                            return card.getText();
                        }
                    })
                    .toList();
        } catch (Exception e) {
            log.warn("Could not get pipeline names: {}", e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Click on a specific pipeline by name
     */
    public ProphecyPipelinePage openPipeline(String pipelineName) {
        try {
            WebElement pipelineCard = pipelineCards.stream()
                    .filter(card -> {
                        try {
                            String cardText = card.getText();
                            return cardText.contains(pipelineName);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Pipeline not found: " + pipelineName));
            
            clickElement(pipelineCard);
            log.info("Opened pipeline: {}", pipelineName);
            waitForPageToLoad();
            return new ProphecyPipelinePage();
            
        } catch (Exception e) {
            log.error("Failed to open pipeline: {}", pipelineName, e);
            throw new RuntimeException("Failed to open pipeline: " + pipelineName, e);
        }
    }
    
    /**
     * Check if specific pipeline exists
     */
    public boolean isPipelinePresent(String pipelineName) {
        try {
            return pipelineCards.stream()
                    .anyMatch(card -> {
                        try {
                            return card.getText().contains(pipelineName);
                        } catch (Exception e) {
                            return false;
                        }
                    });
        } catch (Exception e) {
            log.warn("Error checking pipeline presence: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Get number of pipelines displayed
     */
    public int getPipelineCount() {
        try {
            return pipelineCards.size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Navigate to Projects section
     */
    public ProphecyDashboardPage navigateToProjects() {
        try {
            if (projectsLink.isDisplayed()) {
                clickElement(projectsLink);
                log.info("Navigated to Projects section");
            }
        } catch (Exception e) {
            log.warn("Projects link not found");
        }
        return this;
    }
    
    /**
     * Open user menu
     */
    public ProphecyDashboardPage openUserMenu() {
        try {
            if (userMenu.isDisplayed()) {
                clickElement(userMenu);
                log.info("Opened user menu");
            }
        } catch (Exception e) {
            log.warn("User menu not found");
        }
        return this;
    }
    
    /**
     * Logout from application
     */
    public ProphecyLoginPage logout() {
        try {
            openUserMenu();
            if (logoutButton.isDisplayed()) {
                clickElement(logoutButton);
                log.info("Logged out successfully");
                waitForPageToLoad();
                return new ProphecyLoginPage();
            }
        } catch (Exception e) {
            log.error("Failed to logout: {}", e.getMessage());
        }
        return new ProphecyLoginPage();
    }
    
    /**
     * Get dashboard title
     */
    public String getDashboardTitle() {
        try {
            if (dashboardTitle.isDisplayed()) {
                return dashboardTitle.getText();
            }
        } catch (Exception e) {
            log.debug("Dashboard title not found");
        }
        return getPageTitle();
    }
    
    // Helper methods
    private void clickElement(WebElement element) {
        waitForElementToBeClickable(element);
        element.click();
    }
    
    private void waitForElementToBeClickable(WebElement element) {
        wait.until(driver -> element.isDisplayed() && element.isEnabled());
    }
}