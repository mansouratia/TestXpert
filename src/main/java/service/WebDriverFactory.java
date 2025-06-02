package service;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class WebDriverFactory {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final int PAGE_LOAD_TIMEOUT = 30;
    private static final int IMPLICIT_WAIT = 10;

    public static WebDriver createDriver(String browser) {
        WebDriver driver;
        try {
            switch (browser.toLowerCase()) {
                case "chrome":
                    driver = createChromeDriver();
                    break;
                case "firefox":
                    driver = createFirefoxDriver();
                    break;
                case "edge":
                    driver = createEdgeDriver();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
            configureDriver(driver);
            driverThreadLocal.set(driver);
            return driver;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create WebDriver: " + e.getMessage(), e);
        }
    }

    private static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.default_content_settings.popups", 0);
        // Enhanced security and password settings
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_account_storage_enabled", false);
        prefs.put("profile.block_third_party_cookies", true);
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("profile.password_manager_enable", false);
        prefs.put("autofill.profile_enabled", false);
        prefs.put("privacy.password_leak_detection.enabled", false);
        prefs.put("profile.content_settings.exceptions.password_manager", new HashMap<>());
        prefs.put("credentials_enable_autosignin", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-blink-features=PasswordManager");
        options.addArguments("--disable-features=PasswordLeakDetection");
        options.addArguments("--disable-web-security");
        options.addArguments("--dns-prefetch-disable");
        options.addArguments("--disable-background-networking");
        options.addArguments("--incognito");
        options.setExperimentalOption("excludeSwitches",
                java.util.Arrays.asList("enable-automation", "enable-logging"));
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver() {
        FirefoxProfile profile = new FirefoxProfile();
        // Enhanced security and password settings
        profile.setPreference("signon.rememberSignons", false);
        profile.setPreference("signon.autofillForms", false);
        profile.setPreference("signon.storeWhenAutocompleteOff", false);
        profile.setPreference("signon.management.page.breach-alerts.enabled", false);
        profile.setPreference("browser.privatebrowsing.autostart", true);
        profile.setPreference("privacy.password_leak_detection", false);
        profile.setPreference("security.password_lifetime", 0);
        profile.setPreference("security.ask_for_password", 0);
        profile.setPreference("dom.disable_beforeunload", true);
        profile.setPreference("security.insecure_field_warning.contextual.enabled", false);
        profile.setPreference("security.insecure_password.ui.enabled", false);
        profile.setPreference("signon.generation.enabled", false);
        profile.setPreference("signon.management.page.fileImport.enabled", false);
        profile.setPreference("signon.passwordEditCapture.enabled", false);
        profile.setPreference("signon.showAutoCompleteFooter", false);
        profile.setPreference("signon.autofillForms.http", false);
        profile.setPreference("signon.debug", false);

        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(profile);
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("-private");
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver() {
        EdgeOptions options = new EdgeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("autofill.profile_enabled", false);
        prefs.put("privacy.password_leak_detection.enabled", false);
        prefs.put("password_manager_enabled", false);
        prefs.put("password_leak_detection", false);
        prefs.put("profile.password_account_storage_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=PasswordManager");
        options.addArguments("--disable-features=PasswordLeakDetection");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--inprivate");
        return new EdgeDriver(options);
    }

    private static void configureDriver(WebDriver driver) {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
        //driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
    }

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Error quitting WebDriver: " + e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    public static void closeCurrentWindow() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.close();
            } catch (Exception e) {
                System.err.println("Error closing current window: " + e.getMessage());
            }
        }
    }
}