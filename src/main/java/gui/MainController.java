package gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.TestCase;
import model.TestSuite;
import service.JsonService;
import service.TestExecutor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class MainController {
    private static final String DEFAULT_BROWSER = "chrome";
    private static final String SUITES_DIR = "suites";

    @FXML private ComboBox<String> locatorTypeBox;
    @FXML private ComboBox<String> actionBox;
    @FXML private ComboBox<String> performanceTestBox;
    @FXML private ComboBox<String> securityTestBox;
    @FXML private ComboBox<String> browserBox;
    @FXML private TextField locatorValueField;
    @FXML private TextField inputDataField;
    @FXML private TextField urlField;
    @FXML private Button openReportButton;
    @FXML private Button deleteSuiteButton;
    @FXML private Button editTestCaseButton;
    @FXML private ListView<TestCase> testCaseList;
    @FXML private ProgressBar progressBar;
    @FXML private TextArea logArea;
    @FXML private ComboBox<String> suiteComboBox;

    private final ObservableList<TestCase> testCases = FXCollections.observableArrayList();
    private final ObservableList<String> suiteNames = FXCollections.observableArrayList();
    private int editingIndex = -1;

    private void appendLog(String message) {
        if (logArea != null) {
            Platform.runLater(() -> logArea.appendText(message + "\n"));
        }
    }

    public String promptForSuiteName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save Suite");
        dialog.setHeaderText("Please enter a suite name.");
        dialog.setContentText("Suite name:");

        Optional<String> result = dialog.showAndWait();
        return result.filter(name -> !name.trim().isEmpty()).orElse(null);
    }

    @FXML
    public void initialize() {
        setupBrowserBox();
        setupActionBoxes();
        setupTestCaseList();
        setupOtherControls();
        loadSuites();
        setupListeners();
    }

    private void setupListeners() {
        if (actionBox != null) {
            actionBox.valueProperty().addListener((obs, oldVal, newVal) -> {
                updateFieldVisibility(newVal);
                if (newVal != null) {
                    performanceTestBox.setValue(null);
                    securityTestBox.setValue(null);
                }
            });
        }

        if (performanceTestBox != null) {
            performanceTestBox.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    actionBox.setValue(null);
                    securityTestBox.setValue(null);
                    updateFieldVisibility("Performance:" + newVal);
                }
            });
        }

        if (securityTestBox != null) {
            securityTestBox.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    actionBox.setValue(null);
                    performanceTestBox.setValue(null);
                    updateFieldVisibility("Security:" + newVal);
                }
            });
        }
    }

    private void setupBrowserBox() {
        if (browserBox != null) {
            browserBox.getItems().addAll("chrome", "firefox", "edge");
            browserBox.setValue(DEFAULT_BROWSER);
            browserBox.setTooltip(new Tooltip("Select browser for test execution"));
        }
    }

    private void setupActionBoxes() {
        if (locatorTypeBox != null) {
            locatorTypeBox.getItems().addAll(
                    "ID", "Name", "Class Name", "Tag Name", "Link Text",
                    "Partial Link Text", "CSS Selector", "XPath"
            );
        }

        if (actionBox != null) {
            actionBox.getItems().addAll(
                    "Click", "SendKeys (Type Text)", "Clear", "Submit",
                    "Select (Dropdowns)", "Hover (Mouse Over)", "Double Click",
                    "Right Click (Context Click)", "Drag and Drop", "Scroll",
                    "Get Text", "Assert", "Sleep", "Navigate"
            );
        }

        if (performanceTestBox != null) {
            performanceTestBox.getItems().addAll(
                    "Load Test", "Stress Test", "Spike Test", "Endurance Test"
            );
        }

        if (securityTestBox != null) {
            securityTestBox.getItems().addAll(
                    "XSS Test", "SQL Injection Test", "CSRF Test",
                    "Authentication Bypass Test"
            );
        }
    }

    private void setupTestCaseList() {
        if (testCaseList != null) {
            testCaseList.setItems(testCases);
            testCaseList.getSelectionModel().selectedItemProperty()
                    .addListener((obs, oldVal, newVal) -> onTestCaseSelected());
        }
    }

    private void setupOtherControls() {
        if (suiteComboBox != null) {
            suiteComboBox.setItems(suiteNames);
        }
        if (progressBar != null) {
            progressBar.setVisible(false);
        }
        if (openReportButton != null) {
            openReportButton.setDisable(true);
        }
    }

    private void loadSuites() {
        File directory = new File(SUITES_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
            return;
        }
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));
        suiteNames.clear();
        if (files != null) {
            for (File file : files) {
                String name = file.getName().replace(".json", "");
                suiteNames.add(name);
            }
        }
    }

    private void onTestCaseSelected() {
        TestCase selectedCase = testCaseList.getSelectionModel().getSelectedItem();
        if (selectedCase == null) {
            if (editTestCaseButton != null) editTestCaseButton.setDisable(true);
            return;
        }
        if (editTestCaseButton != null) editTestCaseButton.setDisable(false);
    }

    private void clearInputFields() {
        if (urlField != null) urlField.clear();
        if (locatorTypeBox != null) locatorTypeBox.setValue(null);
        if (locatorValueField != null) locatorValueField.clear();
        if (inputDataField != null) inputDataField.clear();
        if (actionBox != null) actionBox.setValue(null);
        if (performanceTestBox != null) performanceTestBox.setValue(null);
        if (securityTestBox != null) securityTestBox.setValue(null);
    }

    private void updateFieldVisibility(String action) {
        if (urlField == null || locatorTypeBox == null ||
                locatorValueField == null || inputDataField == null) return;

        urlField.setVisible(false);
        locatorTypeBox.setVisible(false);
        locatorValueField.setVisible(false);
        inputDataField.setVisible(false);

        if (action == null) return;

        if (action.startsWith("Performance:")) {
            urlField.setVisible(true);
            urlField.setPromptText("Target URL for performance test");
            return;
        }

        if (action.startsWith("Security:")) {
            locatorTypeBox.setVisible(true);
            locatorValueField.setVisible(true);
            locatorValueField.setPromptText("Target input field locator");
            return;
        }

        boolean isNavigateOrSleep = "Navigate".equalsIgnoreCase(action) ||
                "Sleep".equalsIgnoreCase(action);
        boolean requiresInput = Arrays.asList(
                "SendKeys (Type Text)", "Select (Dropdowns)", "Assert", "Sleep"
        ).contains(action);

        urlField.setVisible("Navigate".equalsIgnoreCase(action));
        locatorTypeBox.setVisible(!isNavigateOrSleep);
        locatorValueField.setVisible(!isNavigateOrSleep);
        inputDataField.setVisible(requiresInput);

        if ("Navigate".equalsIgnoreCase(action)) {
            urlField.setPromptText("Enter URL");
        } else if ("Assert".equalsIgnoreCase(action)) {
            inputDataField.setPromptText("Expected Text");
            locatorValueField.setPromptText("Element to verify");
        } else if ("Sleep".equalsIgnoreCase(action)) {
            inputDataField.setPromptText("Sleep duration (ms)");
        } else if ("SendKeys (Type Text)".equalsIgnoreCase(action)) {
            inputDataField.setPromptText("Text to type");
        } else if ("Select (Dropdowns)".equalsIgnoreCase(action)) {
            inputDataField.setPromptText("Option to select");
        } else if ("Drag and Drop".equalsIgnoreCase(action)) {
            locatorValueField.setPromptText("Source;Target locators");
        }

        if (!urlField.isVisible()) urlField.clear();
        if (!locatorTypeBox.isVisible()) locatorTypeBox.setValue(null);
        if (!locatorValueField.isVisible()) locatorValueField.clear();
        if (!inputDataField.isVisible()) inputDataField.clear();
    }

    @FXML
    private void onAddTestCase() {
        String action = actionBox != null ? actionBox.getValue() : null;
        String performanceTest = performanceTestBox != null ? performanceTestBox.getValue() : null;
        String securityTest = securityTestBox != null ? securityTestBox.getValue() : null;

        if ((action != null && (performanceTest != null || securityTest != null)) ||
                (performanceTest != null && securityTest != null)) {
            appendLog("Please select only one type of test.");
            return;
        }

        if (!validateFields(action != null ? action :
                performanceTest != null ? "Performance:" + performanceTest :
                        securityTest != null ? "Security:" + securityTest : null)) {
            return;
        }

        TestCase testCase = createTestCase(action, performanceTest, securityTest);
        if (editingIndex >= 0) {
            testCases.set(editingIndex, testCase);
            editingIndex = -1;
        } else {
            testCases.add(testCase);
        }
        appendLog("Test case " + (editingIndex >= 0 ? "updated: " : "added: ") + testCase);
        clearInputFields();
    }

    @FXML
    private void onEditTestCase() {
        int selectedIndex = testCaseList.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) return;

        TestCase selectedCase = testCases.get(selectedIndex);
        editingIndex = selectedIndex;

        switch (selectedCase.getTestType().toUpperCase()) {
            case "FUNCTIONAL":
                actionBox.setValue(selectedCase.getAction());
                if ("Navigate".equalsIgnoreCase(selectedCase.getAction())) {
                    urlField.setText(selectedCase.getUrl());
                } else {
                    locatorTypeBox.setValue(selectedCase.getLocatorType());
                    locatorValueField.setText(selectedCase.getLocatorValue());
                    inputDataField.setText(selectedCase.getInputData());
                }
                break;
            case "PERFORMANCE":
                performanceTestBox.setValue(selectedCase.getAction());
                urlField.setText(selectedCase.getUrl());
                break;
            case "SECURITY":
                securityTestBox.setValue(selectedCase.getAction());
                locatorTypeBox.setValue(selectedCase.getLocatorType());
                locatorValueField.setText(selectedCase.getLocatorValue());
                break;
        }
    }

    @FXML
    private void onRemoveTestCase() {
        int selectedIndex = testCaseList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            testCases.remove(selectedIndex);
            appendLog("Test case removed.");
        }
    }

    @FXML
    private void onMoveUp() {
        int selectedIndex = testCaseList.getSelectionModel().getSelectedIndex();
        if (selectedIndex > 0) {
            TestCase testCase = testCases.remove(selectedIndex);
            testCases.add(selectedIndex - 1, testCase);
            testCaseList.getSelectionModel().select(selectedIndex - 1);
        }
    }

    @FXML
    private void onMoveDown() {
        int selectedIndex = testCaseList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < testCases.size() - 1) {
            TestCase testCase = testCases.remove(selectedIndex);
            testCases.add(selectedIndex + 1, testCase);
            testCaseList.getSelectionModel().select(selectedIndex + 1);
        }
    }

    @FXML
    private void onRunTestCase() {
        String selectedBrowser = browserBox.getValue();
        if (selectedBrowser == null || selectedBrowser.isEmpty()) {
            appendLog("Please select a browser.");
            return;
        }

        if (testCases.isEmpty()) {
            appendLog("No test cases to run.");
            return;
        }

        progressBar.setVisible(true);
        openReportButton.setDisable(true);

        new Thread(() -> {
            try {
                TestExecutor.executeAll(
                        new ArrayList<>(testCases),
                        (current, total) -> Platform.runLater(() ->
                                progressBar.setProgress((double) current / total)
                        ),
                        this::appendLog,
                        selectedBrowser
                );
                Platform.runLater(() -> openReportButton.setDisable(false));
            } catch (Exception e) {
                appendLog("Error: " + e.getMessage());
            } finally {
                Platform.runLater(() -> progressBar.setVisible(false));
            }
        }).start();
    }

    @FXML
    private void onOpenReport() {
        TestExecutor.openLatestReport();
    }

    @FXML
    private void onSaveSuite() {
        String suiteName = promptForSuiteName();
        if (suiteName == null) {
            appendLog("Please enter a suite name.");
            return;
        }

        File dir = new File(SUITES_DIR);
        if (!dir.exists()) dir.mkdirs();

        TestSuite suite = new TestSuite();
        suite.setTestCases(new ArrayList<>(testCases));

        try {
            JsonService.saveTestSuite(suite, SUITES_DIR + "/" + suiteName + ".json");
            if (!suiteNames.contains(suiteName)) {
                suiteNames.add(suiteName);
            }
            appendLog("Test suite saved successfully.");
        } catch (IOException e) {
            appendLog("Error saving test suite: " + e.getMessage());
        }
    }

    @FXML
    private void onLoadSuite() {
        if (suiteComboBox.getValue() == null) {
            appendLog("Please select a suite to load.");
            return;
        }

        try {
            TestSuite suite = JsonService.loadTestSuite(SUITES_DIR + "/" + suiteComboBox.getValue() + ".json");
            testCases.clear();
            testCases.addAll(suite.getTestCases());
            appendLog("Test suite loaded successfully.");
        } catch (IOException e) {
            appendLog("Error loading test suite: " + e.getMessage());
        }
    }

    @FXML
    private void onDeleteSuite() {
        if (suiteComboBox.getValue() == null) {
            appendLog("Please select a suite to delete.");
            return;
        }

        File file = new File(SUITES_DIR + "/" + suiteComboBox.getValue() + ".json");
        if (file.delete()) {
            suiteNames.remove(suiteComboBox.getValue());
            suiteComboBox.setValue(null);
            appendLog("Test suite deleted successfully.");
        } else {
            appendLog("Error deleting test suite.");
        }
    }

    private boolean validateFields(String action) {
        if (action == null) {
            appendLog("Please select a test type.");
            return false;
        }

        if (action.startsWith("Performance:")) {
            if (urlField == null || urlField.getText().trim().isEmpty()) {
                appendLog("URL is required for performance tests.");
                return false;
            }
            return true;
        }

        if (action.startsWith("Security:")) {
            if (locatorTypeBox == null || locatorTypeBox.getValue() == null ||
                    locatorValueField == null || locatorValueField.getText().trim().isEmpty()) {
                appendLog("Locator type and value are required for security tests.");
                return false;
            }
            return true;
        }

        if ("Sleep".equalsIgnoreCase(action)) {
            if (inputDataField == null || inputDataField.getText().trim().isEmpty()) {
                appendLog("Sleep duration is required.");
                return false;
            }
            try {
                Long.parseLong(inputDataField.getText().trim());
                return true;
            } catch (NumberFormatException e) {
                appendLog("Sleep duration must be a valid number.");
                return false;
            }
        }

        if ("Navigate".equalsIgnoreCase(action)) {
            if (urlField == null || urlField.getText().trim().isEmpty()) {
                appendLog("URL is required for Navigate action.");
                return false;
            }
            return true;
        }

        if (locatorTypeBox == null || locatorTypeBox.getValue() == null) {
            appendLog("Locator type is required.");
            return false;
        }
        if (locatorValueField == null || locatorValueField.getText().trim().isEmpty()) {
            appendLog("Locator value is required.");
            return false;
        }

        boolean requiresInput = Arrays.asList(
                "SendKeys (Type Text)", "Select (Dropdowns)", "Assert"
        ).contains(action);

        if (requiresInput && (inputDataField == null || inputDataField.getText().trim().isEmpty())) {
            appendLog("Input data is required for this action.");
            return false;
        }

        return true;
    }

    private TestCase createTestCase(String action, String performanceTest, String securityTest) {
        TestCase testCase = new TestCase();

        if (action != null) {
            testCase.setTestType("FUNCTIONAL");
            testCase.setAction(action);
            if ("Navigate".equalsIgnoreCase(action)) {
                testCase.setUrl(urlField != null ? urlField.getText() : null);
            } else if ("Sleep".equalsIgnoreCase(action)) {
                testCase.setInputData(inputDataField != null ? inputDataField.getText() : null);
            } else {
                testCase.setLocatorType(locatorTypeBox != null ? locatorTypeBox.getValue() : null);
                testCase.setLocatorValue(locatorValueField != null ? locatorValueField.getText() : null);
                testCase.setInputData(inputDataField != null ? inputDataField.getText() : null);
            }
        } else if (performanceTest != null) {
            testCase.setTestType("PERFORMANCE");
            testCase.setAction(performanceTest);
            testCase.setUrl(urlField != null ? urlField.getText() : null);
        } else if (securityTest != null) {
            testCase.setTestType("SECURITY");
            testCase.setAction(securityTest);
            testCase.setLocatorType(locatorTypeBox != null ? locatorTypeBox.getValue() : null);
            testCase.setLocatorValue(locatorValueField != null ? locatorValueField.getText() : null);
        }

        return testCase;
    }
}