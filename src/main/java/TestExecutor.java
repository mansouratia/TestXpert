import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Arrays;
import java.util.Date;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import javax.swing.*;

public class TestExecutor {
    private JTextArea outputArea;  // Reference to outputArea to show live execution

    public TestExecutor(JTextArea outputArea) {
        this.outputArea = outputArea;  // Store reference to update outputArea
    }

    public void executeTestSteps(List<String> steps) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\mans\\IdeaProjects\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        int totalSteps = steps.size();
        int passedSteps = 0;
        int failedSteps = 0;
        long startTime = System.currentTimeMillis();

        try (FileWriter writer = new FileWriter("test_report.txt")) {
            writer.write("=== Test Execution Started ===\n");
            writer.write("Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n\n");

            for (String step : steps) {
                if (step.startsWith("//")) continue;

                long stepStartTime = System.currentTimeMillis();
                try {
                    executeStep(driver, step);
                    long stepDuration = (System.currentTimeMillis() - stepStartTime) / 1000;

                    // Update live execution in the outputArea
                    outputArea.append("PASS [✓] " + step + " (Time: " + stepDuration + "s)\n");
                    passedSteps++;
                } catch (Exception e) {
                    long stepDuration = (System.currentTimeMillis() - stepStartTime) / 1000;
                    outputArea.append("FAIL [✗] " + step + " (Error: " + e.getMessage().split("\n")[0] + ", Time: " + stepDuration + "s)\n");
                    failedSteps++;

                    // Capture screenshot for failed steps
                    takeScreenshot(driver, "screenshot_fail_" + System.currentTimeMillis() + ".png");
                }
            }

            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime) / 1000;

            writer.write("\n=== Test Summary ===\n");
            writer.write("Total Steps: " + totalSteps + "\n");
            writer.write("Passed Steps: " + passedSteps + "\n");
            writer.write("Failed Steps: " + failedSteps + "\n");
            writer.write("Success Rate: " + (passedSteps * 100 / totalSteps) + "%\n");
            writer.write("Total Execution Time: " + duration + " seconds\n");
            writer.write("\n=== Test Completed ===");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private void executeStep(WebDriver driver, String step) throws Exception {
        String[] parts = step.split(" ");
        switch (parts[0]) {
            case "Navigate":
                driver.get(parts[1]);
                break;
            case "Click":
                if (parts[1].equals("id")) driver.findElement(By.id(parts[2])).click();
                else if (parts[1].equals("name")) driver.findElement(By.name(parts[2])).click();
                else if (parts[1].equals("class")) driver.findElement(By.className(parts[2])).click();
                else if (parts[1].equals("xpath")) driver.findElement(By.xpath(parts[2])).click();
                else if (parts[1].equals("css")) driver.findElement(By.cssSelector(parts[2])).click();
                else if (parts[1].equals("linkText")) driver.findElement(By.linkText(parts[2])).click();
                break;
            case "Type":
                String text = String.join(" ", Arrays.copyOfRange(parts, 3, parts.length));
                if (parts[1].equals("id")) driver.findElement(By.id(parts[2])).sendKeys(text);
                else if (parts[1].equals("name")) driver.findElement(By.name(parts[2])).sendKeys(text);
                break;
            case "Wait":
                Thread.sleep(Integer.parseInt(parts[1]));
                break;
            case "Maximize":
                driver.manage().window().maximize();
                break;
            case "Go":
                if (parts[1].equals("Back")) driver.navigate().back();
                else if (parts[1].equals("Forward")) driver.navigate().forward();
                break;
            case "Refresh":
                driver.navigate().refresh();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported step: " + step);
        }
    }

    private void takeScreenshot(WebDriver driver, String fileName) {
        try {
            BufferedImage image = new Robot().createScreenCapture(
                    new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(image, "png", new File(fileName));
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
}
