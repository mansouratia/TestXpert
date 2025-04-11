
# ✅ Testing & Quality Assurance

## 🧪 Test Plan

The goal of testing in TestXpert is to ensure test generation and execution logic behaves as expected across different scenarios.

### Test Environment

- **OS**: Windows / Linux / macOS
- **Browser**: Google Chrome
- **Tools**: Java 11+, Maven, ChromeDriver

## 📋 Test Scenarios

| Test Case ID | Description                               | Expected Result                      | Status |
|--------------|-------------------------------------------|--------------------------------------|--------|
| TC001        | Load commands.xlsx                        | Mapping loaded successfully          | ✅     |
| TC002        | Generate test code from valid input       | Code is written to file              | ✅     |
| TC003        | Execute test with valid steps             | Browser opens, steps are performed   | ✅     |
| TC004        | Execute with invalid command              | Error message shown, log updated     | ✅     |
| TC005        | Capture screenshot on failure             | Image saved in output directory      | ✅     |
| TC006        | GUI responds correctly to user input      | GUI updates and displays results     | ✅     |

## 🖼️ Sample Test Evidence

- **`test_report.txt`**: Logs test execution steps and errors.
- **`screenshot_fail_*.png`**: Screenshots of the browser on test failure.

## 🔁 Testing Types

- **Unit Testing**: Core functions tested (e.g., Excel reading, code generation)
- **Integration Testing**: Full flow from input to execution tested together
- **Manual Testing**: GUI and overall behavior tested manually
- **User Acceptance Testing (UAT)**: Performed by peers and instructor

## 🐞 Bug Reporting

All detected issues were logged during development and handled as follows:

| Bug ID | Description                        | Fix Summary                            | Status |
|--------|------------------------------------|-----------------------------------------|--------|
| BUG001 | Excel file path not found error    | Added file existence check              | Fixed  |
| BUG002 | Failure screenshot overwrites file | Timestamped filename added              | Fixed  |
| BUG003 | Output area not updating in GUI    | Linked console output to text area      | Fixed  |

## 🧰 Tools Used

- JUnit (optional integration possible)
- Maven for build and test flow
- Manual test logs in `.txt` format

