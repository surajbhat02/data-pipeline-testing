# Prophecy Web Testing Framework

A comprehensive web testing framework for Prophecy data pipelines using Selenium PageObject pattern with stage-by-stage execution and custom data validation.

## 🎯 Key Features

### ✅ **What This Framework Does**
1. **🌐 Web-based Prophecy Testing** - Login to Prophecy web interface and interact with pipelines
2. **🔄 Stage-by-Stage Execution** - Execute pipeline stages individually and validate each step
3. **📊 Custom Data Input** - Support for JSON, tabular data, and file uploads as mock data sources
4. **✅ Record Validation** - Validate record counts and data transformations at each stage
5. **🏗️ PageObject Pattern** - Uses Selenium's PageFactory for maintainable page objects
6. **🥒 BDD Testing** - Comprehensive Gherkin scenarios for all testing scenarios

### 🚀 **Core Capabilities**
- **Login & Authentication** - Automated login to Prophecy platform
- **Pipeline Navigation** - Browse and open specific pipelines
- **Custom Data Injection** - Input JSON, CSV, or tabular data into pipelines
- **Stage-by-Stage Execution** - Execute and validate each pipeline stage
- **Data Validation** - Verify record counts, data quality, and transformations
- **Error Handling** - Test error scenarios and recovery mechanisms
- **Performance Testing** - Validate execution times and resource usage

## 🏗️ Framework Architecture

### 📁 Project Structure
```
src/main/java/com/prophecy/testing/
├── config/
│   ├── ConfigManager.java           # Configuration management
│   └── WebDriverManager.java        # WebDriver lifecycle management
├── pages/                           # PageObject classes
│   ├── BasePage.java               # Base page with common functionality
│   ├── ProphecyLoginPage.java      # Login page interactions
│   ├── ProphecyDashboardPage.java  # Dashboard navigation
│   ├── ProphecyPipelinesPage.java  # Pipeline listing and search
│   └── ProphecyPipelinePage.java   # Main pipeline execution page
└── utils/
    ├── ScreenshotUtils.java        # Screenshot utilities
    └── WaitUtils.java              # Wait utilities

src/test/java/com/prophecy/testing/
├── stepdefinitions/
│   └── ProphecyWebTestSteps.java   # Cucumber step definitions
├── runners/
│   ├── PipelineTestRunner.java     # Main test runner
│   ├── StageByStageTestRunner.java # Stage-by-stage focused runner
│   ├── ProphecyAdvancedTestRunner.java # Advanced scenarios
│   └── ProphecyEdgeCasesTestRunner.java # Edge cases
└── hooks/
    └── TestHooks.java              # Test setup and cleanup

src/test/resources/features/
├── pipeline_testing.feature        # Main testing scenarios
├── prophecy_advanced_scenarios.feature # Advanced test cases
└── prophecy_edge_cases.feature     # Edge cases and boundary testing
```

## 🚀 Quick Start

### 1. **Basic Pipeline Test**
```gherkin
@WebTest @StageByStage
Scenario: Execute pipeline stage by stage with custom JSON data
  When I navigate to Pipelines section
  And I open pipeline "Customer Data Pipeline"
  And I have custom JSON data:
    """
    [
      {"id": 1, "name": "John Doe", "age": 30, "city": "New York"},
      {"id": 2, "name": "Jane Smith", "age": 25, "city": "Los Angeles"}
    ]
    """
  When I execute the pipeline stage by stage
  Then the pipeline execution should be successful
  And all stages should complete successfully
  And stage "Data Source" should process 2 records
```

### 2. **Custom Tabular Data Test**
```gherkin
@WebTest @CustomData
Scenario: Execute pipeline with custom tabular data
  When I navigate to Pipelines section
  And I open pipeline "Sales Data Pipeline"
  And I have custom tabular data:
    | product_id | product_name | price | category   |
    | 1          | Laptop       | 999.99| Electronics|
    | 2          | Mouse        | 29.99 | Electronics|
  When I execute the pipeline stage by stage
  Then stage "Product Source" should process 2 records
  And stage "Price Calculation" should complete successfully
```

### 3. **File Upload Test**
```gherkin
@WebTest @FileUpload
Scenario: Execute pipeline with uploaded data file
  When I navigate to Pipelines section
  And I open pipeline "File Processing Pipeline"
  And I upload custom data file "/path/to/test-data.csv"
  When I execute the pipeline stage by stage
  Then the pipeline execution should be successful
```

## 🎯 Test Execution

### **Run All Tests**
```bash
mvn test -Dtest=PipelineTestRunner
```

### **Run Stage-by-Stage Tests Only**
```bash
mvn test -Dtest=StageByStageTestRunner
```

### **Run Advanced Scenarios**
```bash
mvn test -Dtest=ProphecyAdvancedTestRunner
```

### **Run Edge Cases**
```bash
mvn test -Dtest=ProphecyEdgeCasesTestRunner
```

### **Run Specific Tags**
```bash
mvn test -Dcucumber.filter.tags="@WebTest and @StageByStage"
mvn test -Dcucumber.filter.tags="@WebTest and @CustomData"
mvn test -Dcucumber.filter.tags="@WebTest and @Performance"
```

## 📋 Comprehensive Test Scenarios

### 🔐 **Authentication & Navigation**
- ✅ Successful login with valid credentials
- ❌ Failed login with invalid credentials
- 🧭 Navigate to pipelines section
- 🔍 Search for specific pipelines
- 🚪 Logout functionality

### 🔄 **Pipeline Execution**
- 📊 Execute pipeline with custom JSON data
- 📋 Execute pipeline with tabular data
- 📁 Execute pipeline with file upload
- ⚡ Stage-by-stage execution with validation
- 🛑 Stop pipeline execution
- 🔄 Pipeline retry mechanisms

### ✅ **Data Validation**
- 📈 Record count validation at each stage
- 🔍 Data format validation
- 🧪 Data transformation verification
- 📊 Data quality checks
- 🎯 Expected vs actual data comparison

### 🚨 **Error Handling**
- ❌ Invalid data handling
- 🔄 Error recovery mechanisms
- 📝 Error logging and reporting
- 🛡️ Graceful failure handling

### ⚡ **Performance Testing**
- ⏱️ Execution time validation
- 📊 Large dataset processing
- 💾 Memory usage monitoring
- 🔄 Concurrent execution testing

### 🎯 **Advanced Scenarios**
- 🌐 Complex nested JSON data
- 🔢 Various data types and null values
- 📅 Date/time boundary testing
- 🔗 Multi-source data integration
- 🧮 Custom functions and expressions

### 🚧 **Edge Cases**
- 📭 Empty datasets
- 1️⃣ Single record processing
- 🌍 Unicode and special characters
- 🔢 Extreme numerical values
- 💥 Malformed data handling
- 🔄 Circular reference detection

## 🛠️ Configuration

### **Browser Configuration** (`src/test/resources/config.properties`)
```properties
# Browser settings
browser.type=chrome
browser.headless=false
browser.maximize=true

# Timeouts
implicit.timeout=10
explicit.timeout=20
page.load.timeout=30

# Prophecy settings
prophecy.base.url=https://app.prophecy.io
prophecy.username=test@example.com
prophecy.password=password123
```

## 🎨 PageObject Pattern Usage

### **Base Page Class**
```java
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    public BasePage() {
        this.driver = WebDriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }
    
    // Common methods for all pages
    protected void clickElement(WebElement element) { ... }
    protected void sendTextToElement(WebElement element, String text) { ... }
    protected String getTextFromElement(WebElement element) { ... }
}
```

### **Pipeline Page Example**
```java
public class ProphecyPipelinePage extends BasePage {
    @FindBy(xpath = "//button[contains(text(), 'Run')]")
    private WebElement runPipelineButton;
    
    @FindBy(xpath = "//div[contains(@class, 'stage')]")
    private List<WebElement> pipelineStages;
    
    public ProphecyPipelinePage executeStageByStage() {
        // Implementation for stage-by-stage execution
        return this;
    }
}
```

## 📊 Test Reporting

### **Generated Reports**
- **HTML Reports**: `target/cucumber-reports/prophecy-web-test-report/`
- **JSON Reports**: `target/cucumber-reports/prophecy-web-test.json`
- **JUnit XML**: `target/cucumber-reports/prophecy-web-test.xml`

## 🎯 Best Practices

### **Test Design**
- ✅ Use descriptive scenario names
- ✅ Keep scenarios focused and atomic
- ✅ Use appropriate tags for organization
- ✅ Include both positive and negative test cases

### **Page Objects**
- ✅ Use meaningful element locators
- ✅ Implement wait strategies
- ✅ Keep page methods focused
- ✅ Use PageFactory pattern consistently

## 🚀 Getting Started

### **Prerequisites**
- Java 17+
- Maven 3.6+
- Chrome/Firefox browser
- Access to Prophecy platform

### **Setup**
1. Clone the repository
2. Update configuration in `config.properties`
3. Run `mvn clean compile` to build
4. Run `mvn test` to execute tests

### **First Test**
```bash
# Run a simple login test
mvn test -Dcucumber.filter.tags="@WebTest and @Login"

# Run stage-by-stage execution test
mvn test -Dcucumber.filter.tags="@WebTest and @StageByStage"
```

## 🎉 Success Metrics

### ✅ **Framework Capabilities**
1. **🌐 Web Interface Testing** - Complete Prophecy web UI automation
2. **🔄 Stage-by-Stage Execution** - Individual stage testing and validation
3. **📊 Custom Data Support** - JSON, tabular, and file-based data input
4. **✅ Comprehensive Validation** - Record counts, data quality, performance
5. **🏗️ PageObject Pattern** - Maintainable and scalable page objects
6. **🥒 BDD Scenarios** - 50+ comprehensive test scenarios
7. **🎯 Multiple Test Runners** - Specialized runners for different test types
8. **📊 Rich Reporting** - HTML, JSON, and XML test reports

---

**🎯 Ready to test your Prophecy pipelines with confidence!** 🚀

This framework provides everything you need to test Prophecy pipelines through the web interface with custom data sources and comprehensive validation at every stage.