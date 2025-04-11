# TestXpert

**TestXpert** is a Java-based desktop application for generating and executing Selenium test cases through an intuitive graphical user interface (GUI). It streamlines test automation by allowing users to define test steps in plain language, generate executable Selenium code, and view live test execution with error handling and reporting.

## 🚀 Features

- GUI for writing test steps
- Excel-driven keyword-to-Selenium translation
- Test case code generation (Java + Selenium)
- Automated test execution using ChromeDriver
- Screenshot capture on failure
- Test report generation

## 📂 Project Structure

```
TestXpert/
├── src/main/java/         # Source code
├── resources/             # Excel translation map
├── generated/             # Test output and screenshots
├── docs/                  # Project documentation
├── pom.xml                # Maven build file
└── README.md
```

## 🧱 Technologies Used

- Java (Swing for GUI)
- Selenium WebDriver
- Apache POI (Excel integration)
- Maven (build automation)

## 💻 Installation

### Prerequisites

- Java JDK 11+
- Maven 3+
- Chrome Browser
- ChromeDriver (path should be configured in system)

### Steps

```bash
git clone https://github.com/yourusername/TestXpert.git
cd TestXpert
mvn clean install
```

### To Run:

You can run the program from your IDE or by compiling:

```bash
mvn compile
mvn exec:java -Dexec.mainClass="MainGUI"
```

> Make sure `commands.xlsx` is in the correct path for translation.

## 📊 Sample Output

- `generated_test_steps.txt`: Selenium code generated from input
- `test_report.txt`: Summary of test execution
- `screenshot_fail_*.png`: Screenshots of failures

## 🧪 Testing

- Manual and automated testing included
- Screenshots and logs are captured for verification

## 📁 Documentation

See detailed project documentation in the [`docs/`](Docs) folder:
- [Project Plan](docs/PROJECT_PLAN.md)
- [Requirements](Docs/REQUIREMENTS.md)
- [System Design](docs/SYSTEM_DESIGN.md)
- [Testing & QA](docs/TESTING.md)
- [User Manual](docs/USER_MANUAL.md)

## 👤 Author

- Mnasour
-Youssef
-Mahmoud
-Ahmed
- DEBI
