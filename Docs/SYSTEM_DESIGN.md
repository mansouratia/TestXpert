
# 🧩 System Analysis & Design

## 🔧 Problem Statement

Manual test case creation is time-consuming, especially for non-technical users. Existing tools are either too complex or too simplistic, lacking flexibility. **TestXpert** aims to provide a middle ground — a user-friendly test generation and execution tool with powerful backend support via Selenium.

## 🎯 Objectives

- Design a desktop-based tool for test generation and execution.
- Allow users to define test steps in plain language.
- Convert steps to executable Selenium code using a mapping table.
- Provide live test results and visual failure feedback.

## 👤 Use Case Description

**Primary Actor**: QA Engineer / Student Tester  
**Goal**: Generate and execute test steps on a web application

### Use Case Example:
- **Title**: Execute Login Test
- **Steps**:
  1. User opens TestXpert GUI.
  2. Inputs test steps like: `Click login`, `Enter username`, etc.
  3. Tool generates and executes test using Selenium.
  4. Result shown in log, screenshot if failed.

## 🏛️ Software Architecture

**Architecture Style**: Modular MVC-like pattern  
- **View**: Java Swing-based GUI (`MainGUI.java`)
- **Model**: Data handling through `ExcelReader.java`
- **Controller**: Code generation and test execution logic (`TestCaseGenerator.java`, `TestExecutor.java`)

## 🗃️ Data Design

- **Mapping Data**: Stored in `commands.xlsx`, loaded into a key-value map
- **Generated Code**: Temporarily stored in `generated_test_steps.txt`
- **Test Output**: Execution logs and screenshots saved locally

## 🧩 Class Overview

| Class Name          | Responsibility                                    |
|---------------------|---------------------------------------------------|
| `MainGUI`           | Swing GUI for interacting with the tool           |
| `ExcelReader`       | Loads command mapping from Excel file             |
| `TestCaseGenerator` | Translates user test steps into Selenium commands |
| `TestExecutor`      | Runs tests, logs results, captures screenshots    |

## 📈 UI/UX Mock (Textual Description)

- **Main Window**:
  - Input area for test steps
  - Buttons for loading Excel, generating code, and executing tests
  - Console/output pane for real-time feedback

## 🔧 Technology Stack

| Component  | Technology     |
|------------|----------------|
| GUI        | Java Swing     |
| Test Engine| Selenium WebDriver |
| Data Input | Apache POI for Excel |
| Build Tool | Maven          |

