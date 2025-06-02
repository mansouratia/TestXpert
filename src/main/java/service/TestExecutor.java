package service;

        import model.TestCase;
        import org.openqa.selenium.*;
        import org.openqa.selenium.interactions.Actions;
        import org.openqa.selenium.support.ui.WebDriverWait;
        import org.openqa.selenium.support.ui.ExpectedConditions;

        import java.time.Duration;
        import java.util.List;
        import java.util.function.BiConsumer;
        import java.util.function.Consumer;

        public class TestExecutor {
            private static TestResults testResults = new TestResults();

            public static void executeElementAction(WebDriver driver, WebElement element, String action, TestCase testCase) {
                String actionLog = String.format("[%s] %s: %s", action, testCase.getLocatorType(), testCase.getLocatorValue());

                try {
                    switch (action.toLowerCase()) {
                        case "click":
                            element.click();
                            testResults.addSuccess("Click action on: " + testCase.getLocatorValue());
                            break;
                        case "sendkeys (type text)":
                            element.clear();
                            element.sendKeys(testCase.getInputData());
                            actionLog += String.format(" | Input: %s", testCase.getInputData());
                            testResults.addSuccess("SendKeys action on: " + testCase.getLocatorValue());
                            break;
                        case "clear":
                            element.clear();
                            testResults.addSuccess("Clear action on: " + testCase.getLocatorValue());
                            break;
                        case "submit":
                            element.submit();
                            testResults.addSuccess("Submit action on: " + testCase.getLocatorValue());
                            break;
                        case "get text":
                            String text = element.getText();
                            if (text.isEmpty()) {
                                text = element.getAttribute("value");
                            }
                            actionLog += String.format(" | Text: %s", text);
                            testResults.addSuccess("GetText action on: " + testCase.getLocatorValue());
                            break;
                        case "assert":
                            String actualText = element.getText();
                            if (!actualText.contains(testCase.getInputData())) {
                                String message = String.format("Expected text not found. Expected: %s, Actual: %s",
                                        testCase.getInputData(), actualText);
                                testResults.addFailure(message);
                                throw new AssertionError(message);
                            }
                            actionLog += String.format(" | Verified text: %s", testCase.getInputData());
                            testResults.addSuccess("Assert action passed on: " + testCase.getLocatorValue());
                            break;
                        case "hover (mouse over)":
                            new Actions(driver).moveToElement(element).perform();
                            testResults.addSuccess("Hover action on: " + testCase.getLocatorValue());
                            break;
                        case "double click":
                            new Actions(driver).doubleClick(element).perform();
                            testResults.addSuccess("Double click action on: " + testCase.getLocatorValue());
                            break;
                        case "right click (context click)":
                            new Actions(driver).contextClick(element).perform();
                            testResults.addSuccess("Right click action on: " + testCase.getLocatorValue());
                            break;
                        case "scroll":
                            // Scroll to element (already handled here)
                            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                            testResults.addSuccess("Scroll action on: " + testCase.getLocatorValue());
                            break;
                        default:
                            String message = "Unsupported action: " + action;
                            testResults.addFailure(message);
                            throw new IllegalArgumentException(message);
                    }
                } catch (Exception e) {
                    testResults.addFailure("Action failed: " + e.getMessage());
                    throw e;
                }

                testResults.addActionResult(actionLog);
            }

            public static void executePerformanceTest(WebDriver driver, TestCase testCase) throws Exception {
                try {
                    long startTime = System.currentTimeMillis();
                    driver.get(testCase.getUrl());
                    long loadTime = System.currentTimeMillis() - startTime;

                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    Long navigationStart = (Long) js.executeScript("return window.performance.timing.navigationStart");
                    Long responseStart = (Long) js.executeScript("return window.performance.timing.responseStart");
                    Long domComplete = (Long) js.executeScript("return window.performance.timing.domComplete");

                    String performanceLog = String.format(
                            "Performance Test Results:\nURL: %s\nTotal Load Time: %dms\nTime to First Byte: %dms\nDOM Complete Time: %dms",
                            testCase.getUrl(),
                            loadTime,
                            responseStart - navigationStart,
                            domComplete - navigationStart
                    );

                    testResults.addActionResult(performanceLog);
                    testResults.addSuccess("Performance test completed for: " + testCase.getUrl());
                } catch (Exception e) {
                    testResults.addFailure("Performance test failed: " + e.getMessage());
                    throw e;
                }
            }

            public static void executeSecurityTest(WebDriver driver, TestCase testCase) {
                try {
                    String securityLog = String.format("Security Test Executed for URL: %s", testCase.getUrl());
                    testResults.addActionResult(securityLog);
                    testResults.addSuccess("Security test completed for: " + testCase.getUrl());
                } catch (Exception e) {
                    testResults.addFailure("Security test failed: " + e.getMessage());
                    throw e;
                }
            }

            public static By getLocator(String type, String value) {
                if (type == null || value == null) {
                    String message = "Locator type and value cannot be null";
                    testResults.addFailure(message);
                    throw new IllegalArgumentException(message);
                }

                try {
                    switch (type.toLowerCase()) {
                        case "id": return By.id(value);
                        case "name": return By.name(value);
                        case "class name": return By.className(value);
                        case "tag name": return By.tagName(value);
                        case "link text": return By.linkText(value);
                        case "partial link text": return By.partialLinkText(value);
                        case "css selector": return By.cssSelector(value);
                        case "xpath": return By.xpath(value);
                        default:
                            String message = "Unsupported locator type: " + type;
                            testResults.addFailure(message);
                            throw new IllegalArgumentException(message);
                    }
                } catch (Exception e) {
                    testResults.addFailure("Invalid locator: " + e.getMessage());
                    throw e;
                }
            }

            public static String getLatestReportPath() {
                return testResults.getCurrentReportPath();
            }

            public static void openLatestReport() {
                if (testResults.generateReport(System.out::println)) {
                    testResults.openReport();
                } else {
                    System.err.println("Failed to generate report");
                }
            }

            public static void executeAll(
                    List<TestCase> testCases,
                    BiConsumer<Integer, Integer> progressCallback,
                    Consumer<String> logCallback,
                    String browser
            ) {
                int total = testCases.size();
                WebDriver driver = null;
                try {
                    driver = WebDriverFactory.createDriver(browser);
                    testResults.addSuccess("Browser initialized: " + browser);

                    for (int i = 0; i < total; i++) {
                        TestCase testCase = testCases.get(i);
                        try {
                            if ("PERFORMANCE".equalsIgnoreCase(testCase.getTestType())) {
                                executePerformanceTest(driver, testCase);
                                logCallback.accept("Performance test executed for: " + testCase.getUrl() + " [PASS]");
                            } else if ("SECURITY".equalsIgnoreCase(testCase.getTestType())) {
                                executeSecurityTest(driver, testCase);
                                logCallback.accept("Security test executed for: " + testCase.getLocatorValue() + " [PASS]");
                            } else if ("navigate".equalsIgnoreCase(testCase.getAction())) {
                                driver.get(testCase.getUrl());
                                logCallback.accept("Navigated to: " + testCase.getUrl() + " [PASS]");
                                testResults.addSuccess("Navigation: " + testCase.getUrl());
                            } else if ("sleep".equalsIgnoreCase(testCase.getAction())) {
                                try {
                                    long ms = Long.parseLong(testCase.getInputData());
                                    Thread.sleep(ms);
                                    logCallback.accept("Slept for: " + ms + " ms [PASS]");
                                    testResults.addSuccess("Sleep: " + ms + "ms");
                                } catch (InterruptedException | NumberFormatException e) {
                                    String message = "Error during sleep: " + e.getMessage();
                                    logCallback.accept(message + " [FAIL]");
                                    testResults.addFailure(message);
                                }
                            } else if ("scroll".equalsIgnoreCase(testCase.getAction())
                                    && (testCase.getLocatorType() == null || testCase.getLocatorType().isEmpty())
                                    && (testCase.getLocatorValue() == null || testCase.getLocatorValue().isEmpty())) {
                                // Page scroll by amount/direction
                                // Expecting inputData as "direction:amount", e.g., "down:500" or "up:300"
                                String input = testCase.getInputData();
                                String[] parts = input != null ? input.split(":") : new String[0];
                                String direction = (parts.length > 0) ? parts[0].trim() : "down";
                                int amount = (parts.length > 1) ? Integer.parseInt(parts[1].trim()) : 500;
                                int scrollValue = direction.equalsIgnoreCase("down") ? amount : -amount;
                                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, arguments[0]);", scrollValue);
                                logCallback.accept("Page scrolled " + direction + " by " + amount + "px [PASS]");
                                testResults.addSuccess("Page scrolled " + direction + " by " + amount + "px");
                            } else {
                                By locator = getLocator(testCase.getLocatorType(), testCase.getLocatorValue());
                                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                                WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                                executeElementAction(driver, element, testCase.getAction(), testCase);
                                logCallback.accept("Executed test case: " + testCase.getAction() + " on " + testCase.getLocatorValue() + " [PASS]");
                            }
                        } catch (Exception e) {
                            String message = "Error executing test case: " + e.getMessage();
                            logCallback.accept(message + " [FAIL]");
                            testResults.addFailure(message);
                        }

                        int current = i + 1;
                        int percent = (int) ((current * 100.0f) / total);
                        progressCallback.accept(current, total);
                    }
                } catch (Exception e) {
                    String message = "Test execution failed: " + e.getMessage();
                    logCallback.accept(message + " [FAIL]");
                    testResults.addFailure(message);
                } finally {
                    if (driver != null) {
                        WebDriverFactory.quitDriver();
                        testResults.addActionResult("WebDriver closed");
                    }
                }
            }
        }