# TestXpert project

# 🧪 Bedaya Automation Framework

**Bedaya** is a professional, modular, and user-friendly automation testing framework that enables testers to create, edit, and execute automated test cases through a rich graphical interface — all without writing code. Built using **Java, TestNG, Selenium**, and **JavaFX**, it supports **cross-browser execution**, dynamic UI, and customizable test flows.

---

## 🚀 Key Features

- 🖥 **Graphical Test Builder**: Intuitive JavaFX-based GUI lets users create and manage test steps easily.
- 🌐 **Multi-Browser Support**: Choose from Chrome, Firefox, or Edge at runtime.
- 🧩 **Dynamic Field Hints**: GUI shows only relevant input fields depending on selected action (e.g., URL for Navigate).
- 🧠 **Smart Test Logic**: Built-in input validation and guidance for better usability.
- 📂 **Modular Suite Management**:
  - Save test cases in JSON format
  - Load, edit, delete, and reorder them visually
- 📑 **TestNG Integration**: Run functional test suites with TestNG and WebDriver.
- 🧪 **Test Modes**: Support for Functional, Performance, and placeholder Security tests.

---

## 🧰 Tech Stack

- Java 8+
- JavaFX (GUI)
- Selenium WebDriver
- TestNG
- Maven
- JSON (for test suite storage)

---

## 📁 Project Structure

```text
Bedaya/
├── src/
│   ├── application/         # JavaFX app setup
│   ├── controllers/         # GUI control logic
│   ├── models/              # Test step data structure
│   ├── runners/             # TestNG & WebDriver runners
│   └── utils/               # Browser drivers, helpers, and JSON handlers
├── resources/               # FXML UI layout and icons
├── suits/                   # Saved JSON test case files
├── pom.xml                  # Maven build file
└── README.md
```

---

## ⚙️ Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/mansouratia/Bedaya-framework.git
cd bedaya-automation-framework
```

### 2. Build the Project
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn javafx:run
```

> 💡 **Requirements**: JDK 8 or above, Maven installed, and a compatible WebDriver (e.g., `chromedriver.exe`) in your system PATH.

---

## 🧪 How to Use

1. **Launch the GUI**
2. **Enter the Base URL**
3. **Add Test Steps**
   - Choose an action (e.g., Navigate, Click, InputText)
   - Fields dynamically change based on action selected
4. **Select Browser (Chrome, Firefox, Edge)**
5. **Choose Test Mode (Functional / Performance / Security)**
6. **Save, Load, Edit, Delete or Reorder Test Steps**
7. **Click Execute** to run the test with live logging

---

## 📸 Screenshots
Project Structure
![Screenshot from 2025-05-24 15-14-54](https://github.com/user-attachments/assets/60ee6cfa-94f3-4f05-b225-4de8822df91d)
GUI
![Screenshot from 2025-05-24 15-16-46](https://github.com/user-attachments/assets/2f79066c-e13f-471c-8158-449743575cb0)
Report
![Screenshot from 2025-05-24 15-17-55](https://github.com/user-attachments/assets/5c82125d-53a1-464a-93d3-0d0c4b2159f4)

---

## 🗺 future Roadmap

- [ ] Add Drag-and-Drop support for reordering steps
- [ ] Allure based report generation
- [ ] Expand Security Test Mode (SQL Injection simulation)
- [ ] Mobile Web Testing support (Appium integration)
- [ ] CI/CD integration support

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!  
Feel free to fork the repo and open a pull request.

---

## 👨‍💻 Author

- Manasour Atia
-Youssef Nasser
-Mahmoud Galal
-Ahmed Shawqy
- DEBI (Digital Egypt Builders Initiative)

-## 📁 Drive link
(all files uploaded to this drive link)
https://drive.google.com/drive/folders/1Q06cVeiZAfvcEcMom9--fJL-i14xjIhr?usp=sharing

