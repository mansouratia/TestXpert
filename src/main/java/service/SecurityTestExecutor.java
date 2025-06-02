package service;

            import model.TestCase;
            import org.openqa.selenium.By;
            import org.openqa.selenium.WebDriver;
            import org.openqa.selenium.WebElement;
            import java.util.function.Consumer;

            public class SecurityTestExecutor {
                private static final String[] XSS_PAYLOADS = {
                        "<script>alert('xss')</script>",
                        "<img src='x' onerror='alert(1)'>",
                        "javascript:alert('xss')"
                };

                private static final String[] SQL_PAYLOADS = {
                        "' OR '1'='1",
                        "'; DROP TABLE users--",
                        "' UNION SELECT * FROM users--"
                };

                public static void execute(WebDriver driver, TestCase testCase, Consumer<String> logCallback) {
                    switch (testCase.getAction().toLowerCase()) {
                        case "xss test" -> performXSSTest(driver, testCase, logCallback);
                        case "sql injection test" -> performSQLInjectionTest(driver, testCase, logCallback);
                        case "csrf test" -> performCSRFTest(driver, testCase, logCallback);
                        case "authentication bypass test" -> performAuthBypassTest(driver, testCase, logCallback);
                        default -> throw new IllegalArgumentException("Unknown security test type: " + testCase.getAction());
                    }
                }

                private static void performXSSTest(WebDriver driver, TestCase testCase, Consumer<String> logCallback) {
                    WebElement element = driver.findElement(getBy(testCase.getLocatorType(), testCase.getLocatorValue()));
                    boolean vulnerable = false;

                    for (String payload : XSS_PAYLOADS) {
                        element.clear();
                        element.sendKeys(payload);
                        if (driver.getPageSource().contains(payload)) {
                            vulnerable = true;
                            logCallback.accept("XSS vulnerability detected with payload: " + payload);
                            break;
                        }
                    }

                    if (!vulnerable) {
                        logCallback.accept("No XSS vulnerabilities detected");
                    }
                }

                private static void performSQLInjectionTest(WebDriver driver, TestCase testCase, Consumer<String> logCallback) {
                    WebElement element = driver.findElement(getBy(testCase.getLocatorType(), testCase.getLocatorValue()));
                    boolean vulnerable = false;

                    for (String payload : SQL_PAYLOADS) {
                        element.clear();
                        element.sendKeys(payload);
                        if (driver.getPageSource().contains("error in your SQL syntax")) {
                            vulnerable = true;
                            logCallback.accept("SQL Injection vulnerability detected with payload: " + payload);
                            break;
                        }
                    }

                    if (!vulnerable) {
                        logCallback.accept("No SQL Injection vulnerabilities detected");
                    }
                }

                private static void performCSRFTest(WebDriver driver, TestCase testCase, Consumer<String> logCallback) {
                    // Basic CSRF test implementation
                    String token = driver.findElement(By.name("csrf_token")).getAttribute("value");
                    logCallback.accept("CSRF token found: " + (token != null && !token.isEmpty()));
                }

                private static void performAuthBypassTest(WebDriver driver, TestCase testCase, Consumer<String> logCallback) {
                    // Basic auth bypass test
                    String originalUrl = driver.getCurrentUrl();
                    driver.get(originalUrl + "/admin");
                    boolean vulnerable = !driver.getCurrentUrl().contains("login");
                    logCallback.accept("Authentication bypass vulnerability: " + vulnerable);
                }

                private static By getBy(String locatorType, String locatorValue) {
                    return switch (locatorType.toLowerCase()) {
                        case "id" -> By.id(locatorValue);
                        case "name" -> By.name(locatorValue);
                        case "class name" -> By.className(locatorValue);
                        case "css selector" -> By.cssSelector(locatorValue);
                        case "xpath" -> By.xpath(locatorValue);
                        default -> throw new IllegalArgumentException("Invalid locator type: " + locatorType);
                    };
                }
            }