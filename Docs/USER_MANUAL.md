
# 👩‍🏫 User Manual — TestXpert

Welcome to **TestXpert** – a simple test automation tool that helps you generate and run Selenium test cases with ease.

---

## 🖥️ System Requirements

- Java JDK 11 or higher
- Maven 3.6+
- Google Chrome browser
- ChromeDriver (added to system path)

---

## 📦 Installation Guide

1. **Download or Clone the Repository**

```bash
git clone https://github.com/yourusername/TestXpert.git
cd TestXpert
```

2. **Build the Project**

```bash
mvn clean install
```

---

## ▶️ How to Run the Application

```bash
mvn exec:java -Dexec.mainClass="MainGUI"
```

---

## ✏️ Using the Application

### Step 1: Load Excel File

- Click the **"Load Commands"** button to load the `commands.xlsx` file containing command-to-Selenium translations.

### Step 2: Write Test Steps

- Use the input area to enter each step (e.g., `Click login`, `Type username`).
- Press **Enter** after each step.

### Step 3: Generate Code

- Click **"Generate Code"** to create Selenium instructions based on your inputs.
- View the generated steps in `generated_test_steps.txt`.

### Step 4: Execute Test

- Click **"Execute Test"** to run the test in Chrome.
- Execution log will appear in the console area.
- If any test fails, a screenshot will be saved in the project directory.

---

## 📄 Output Files

- `generated_test_steps.txt`: Stores auto-generated test steps.
- `test_report.txt`: Logs the success or failure of executed steps.
- `screenshot_fail_*.png`: Captured on test failure.

---

## 📌 Tips

- Do not close the Excel file while loading it.
- Ensure ChromeDriver matches your Chrome version.
- Use clear and consistent test step names as defined in `commands.xlsx`.

---

## ❓ Troubleshooting

| Problem                            | Solution                                              |
|-----------------------------------|--------------------------------------------------------|
| App doesn't start                 | Check Java and Maven installation                     |
| Excel not loading                 | Make sure `commands.xlsx` is in the project directory |
| Chrome not launching              | Ensure ChromeDriver path is correctly set             |

---

## 🙋 Support

For any issues, raise an issue on the GitHub repository or contact the project maintainer.

