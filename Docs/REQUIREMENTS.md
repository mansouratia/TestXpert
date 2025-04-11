
# 📋 Requirements Specification


## ✅ Functional Requirements

1. GUI to input and manage test steps.
2. Load command-to-Selenium mapping from an Excel file.
3. Generate Selenium Java code from user input.
4. Execute generated test cases in Chrome browser.
5. Capture screenshots on test failure.
6. Generate and display execution reports.

## ⚙️ Non-Functional Requirements

| Category       | Requirement                                                                 |
|----------------|-------------------------------------------------------------------------------|
| Performance    | Should execute small to medium test suites within seconds                    |
| Usability      | Intuitive GUI with minimal training required                                 |
| Portability    | Must run on any system with Java installed                                   |
| Reliability    | Execution should handle unexpected errors without crashing                   |
| Security       | Must handle input safely (e.g., sanitize user entries to avoid injections)   |

## 📄 Assumptions

- Chrome browser and compatible driver are installed.
- Java and Maven are properly configured on the system.
- Excel files follow a predefined format for mapping commands.

