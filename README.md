# Prophecy Data Pipeline Testing Framework

A comprehensive Selenium Cucumber testing framework for automated testing of Prophecy data pipelines with BDD approach, Page Object Model, and extensive test data management capabilities.

## 🚀 Features

- **BDD Framework**: Cucumber with Gherkin syntax for readable test scenarios
- **Page Object Model**: Maintainable and reusable page objects for Prophecy UI
- **Test Data Management**: Mock data generation and test data management utilities
- **Pipeline Validation**: Comprehensive pipeline testing and validation capabilities
- **Reporting**: ExtentReports integration with screenshots and detailed logs
- **Cross-browser Support**: Chrome, Firefox, and Edge browser support
- **Parallel Execution**: Support for parallel test execution
- **CI/CD Ready**: Maven-based project structure for easy integration

## 📁 Project Structure

```
data-pipeline-testing/
├── src/
│   ├── main/java/com/prophecy/testing/
│   │   ├── config/           # Configuration management
│   │   ├── data/             # Test data management
│   │   ├── models/           # Data models and enums
│   │   ├── pages/            # Page Object Model classes
│   │   └── utils/            # Utility classes
│   └── test/
│       ├── java/com/prophecy/testing/
│       │   ├── hooks/        # Cucumber hooks
│       │   ├── runners/      # Test runners
│       │   └── stepdefinitions/ # Step definitions
│       └── resources/
│           ├── config/       # Configuration files
│           ├── features/     # Cucumber feature files
│           └── testdata/     # Test data files
├── target/                   # Build outputs and reports
├── pom.xml                   # Maven configuration
└── README.md                 # This file
```

## 🛠️ Technology Stack

- **Java 11+**: Programming language
- **Maven**: Build and dependency management
- **Selenium WebDriver 4.15.0**: Web automation
- **Cucumber 7.14.0**: BDD framework
- **TestNG 7.8.0**: Test execution framework
- **ExtentReports**: Test reporting
- **Jackson**: JSON/YAML processing
- **Apache POI**: Excel file handling
- **JavaFaker**: Test data generation
- **Log4j2**: Logging framework

## 🚦 Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Chrome/Firefox/Edge browser
- Git

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd data-pipeline-testing
```

2. Install dependencies:
```bash
mvn clean install
```

3. Configure the application:
   - Update `src/test/resources/config/application.properties` with your Prophecy instance details
   - Set up test user credentials (use environment variables for security)

### Configuration

Edit `src/test/resources/config/application.properties`:

```properties
# Prophecy Application Configuration
prophecy.base.url=https://your-prophecy-instance.com
prophecy.login.url=https://your-prophecy-instance.com/login

# Browser Configuration
browser.name=chrome
browser.headless=false
browser.maximize=true

# Test Data Configuration
test.data.path=src/test/resources/testdata
mock.data.path=src/test/resources/testdata/mock
pipeline.config.path=src/test/resources/testdata/pipelines
```

## 🧪 Running Tests

### Run All Tests
```bash
mvn test
```

### Run Smoke Tests Only
```bash
mvn test -Dtest=SmokeTestRunner
```

### Run Regression Tests
```bash
mvn test -Dtest=RegressionTestRunner
```

### Run Tests with Specific Tags
```bash
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@pipeline and @positive"
mvn test -Dcucumber.filter.tags="not @ignore"
```

### Run Tests in Headless Mode
```bash
mvn test -Dbrowser.headless=true
```

### Run Tests with Different Browser
```bash
mvn test -Dbrowser.name=firefox
mvn test -Dbrowser.name=edge
```

## 📊 Test Scenarios

### Pipeline Creation Tests
- Create simple pipelines with source and target stages
- Create complex pipelines with transformations
- Create pipelines with multiple data sources
- Validate pipeline configurations
- Handle error scenarios

### Pipeline Execution Tests
- Execute pipelines and monitor progress
- Validate execution results
- Handle large datasets
- Performance testing
- Error handling and recovery

### Data Validation Tests
- Schema validation
- Data quality checks
- Format validation
- Business rule validation
- Data consistency checks

## 📈 Test Data Management

### Mock Data Generation

The framework includes a powerful mock data generator:

```java
MockDataGenerator generator = new MockDataGenerator();
DataSchema schema = generator.generateSampleSchema("customer_schema");
List<Map<String, Object>> data = generator.generateMockData(schema, 1000);
```

### Test Data Files

- **Schemas**: Define data structures in JSON format
- **Mock Data**: Generated CSV/JSON files for testing
- **Scenarios**: Test scenario configurations
- **Pipelines**: Pipeline configuration files

## 📋 Page Object Model

The framework uses Page Object Model for maintainable test automation:

- **BasePage**: Common functionality for all pages
- **LoginPage**: Prophecy login page interactions
- **DashboardPage**: Main dashboard operations
- **PipelinesPage**: Pipeline management operations
- **PipelineEditorPage**: Pipeline creation and editing
- **ProjectsPage**: Project management operations

## 🔧 Utilities

### Screenshot Management
- Automatic screenshots on test failures
- Manual screenshot capture
- Screenshot cleanup utilities

### Wait Utilities
- Smart waits for elements
- Custom wait conditions
- Pipeline-specific waits

### Configuration Management
- Centralized configuration handling
- Environment-specific configurations
- Property file management

## 📊 Reporting

### ExtentReports Integration
- Detailed HTML reports with screenshots
- Test execution statistics
- Step-by-step execution details
- Failed test analysis

### Report Locations
- HTML Reports: `target/cucumber-reports/`
- Screenshots: `target/reports/screenshots/`
- Logs: `target/logs/`

## 🔄 CI/CD Integration

### Maven Commands for CI/CD

```bash
# Clean and compile
mvn clean compile

# Run tests with reporting
mvn clean test -Dcucumber.publish.enabled=true

# Generate reports
mvn surefire-report:report

# Package the project
mvn clean package
```

### Jenkins Integration

```groovy
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'mvn clean test'
            }
            post {
                always {
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/cucumber-reports',
                        reportFiles: 'cucumber-html-report.html',
                        reportName: 'Cucumber Report'
                    ])
                }
            }
        }
    }
}
```

## 🐛 Troubleshooting

### Common Issues

1. **WebDriver Issues**
   - Ensure browser drivers are compatible with browser versions
   - Use WebDriverManager for automatic driver management

2. **Element Not Found**
   - Check if page is fully loaded
   - Verify element locators
   - Increase wait timeouts if needed

3. **Test Data Issues**
   - Verify test data file paths
   - Check data schema definitions
   - Ensure mock data generation is working

### Debug Mode

Run tests with debug logging:
```bash
mvn test -Dlog.level=DEBUG
```

## 📊 Project Status

✅ **Complete** - This framework is ready for production use with comprehensive testing capabilities.

### Completed Components
- ✅ Maven project structure with comprehensive dependencies
- ✅ Configuration management system (ConfigManager, WebDriverManager)
- ✅ Complete data model classes for pipeline testing
- ✅ Mock data generation utilities with JavaFaker integration
- ✅ Enhanced TestDataManager with validation and comparison
- ✅ Page Object Model for Prophecy UI (Login, Dashboard, Pipelines, Editor)
- ✅ Cucumber feature files with comprehensive scenarios
- ✅ Step definitions for all major operations
- ✅ Test runners for different test suites (Smoke, Regression, Example)
- ✅ Test hooks with conditional execution
- ✅ ExtentReports integration and configuration
- ✅ API testing utilities (REST client, JSON parsing)
- ✅ Performance testing utilities (load testing, metrics)
- ✅ CI/CD pipeline configuration with security scans
- ✅ Comprehensive documentation and usage guide

### Framework Features
- 🎯 **BDD Testing**: Cucumber with Gherkin syntax for readable test scenarios
- 🖥️ **UI Testing**: Selenium WebDriver with Page Object Model
- 🔌 **API Testing**: HTTP client with JSON validation and response parsing
- ⚡ **Performance Testing**: Load testing and execution metrics collection
- 📊 **Data Testing**: Mock data generation, validation, and comparison
- 📈 **Reporting**: ExtentReports with detailed test results and screenshots
- 🔧 **Configuration**: Flexible property-based configuration management
- 🚀 **CI/CD Ready**: GitHub Actions with automated testing and security scans

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📝 Best Practices

### Test Design
- Write clear, descriptive test scenarios
- Use meaningful test data
- Implement proper error handling
- Follow Page Object Model principles

### Code Quality
- Follow Java coding standards
- Write clean, maintainable code
- Add appropriate comments
- Use meaningful variable names

### Test Maintenance
- Regular test review and updates
- Keep test data current
- Update page objects when UI changes
- Monitor test execution times

## 📚 Documentation

- [Cucumber Documentation](https://cucumber.io/docs)
- [Selenium WebDriver Documentation](https://selenium.dev/documentation/)
- [TestNG Documentation](https://testng.org/doc/)
- [ExtentReports Documentation](https://extentreports.com/)

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For questions and support:
- Create an issue in the repository
- Contact the development team
- Check the troubleshooting section

---

**Happy Testing! 🚀**