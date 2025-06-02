package service;

import model.TestCase;
import org.openqa.selenium.WebDriver;
import java.util.function.Consumer;

public class PerformanceTestExecutor {
    public static void execute(WebDriver driver, TestCase testCase, Consumer<String> logCallback) {
        switch (testCase.getAction().toLowerCase()) {
            case "load test" -> executeLoadTest(driver, testCase, logCallback);
            case "stress test" -> executeStressTest(driver, testCase, logCallback);
            case "spike test" -> executeSpikeTest(driver, testCase, logCallback);
            case "endurance test" -> executeEnduranceTest(driver, testCase, logCallback);
            default -> throw new IllegalArgumentException("Unknown performance test type: " + testCase.getAction());
        }
    }

    private static void executeLoadTest(WebDriver driver, TestCase testCase, Consumer<String> logCallback) {
        long startTime = System.currentTimeMillis();
        driver.get(testCase.getUrl());
        long loadTime = System.currentTimeMillis() - startTime;
        logCallback.accept(String.format("Load test completed. Page load time: %dms", loadTime));
    }

    private static void executeStressTest(WebDriver driver, TestCase testCase, Consumer<String> logCallback) {
        int iterations = 5;
        long totalTime = 0;

        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();
            driver.get(testCase.getUrl());
            totalTime += System.currentTimeMillis() - startTime;
            logCallback.accept(String.format("Stress test iteration %d completed", i + 1));
        }

        double avgTime = totalTime / (double) iterations;
        logCallback.accept(String.format("Stress test completed. Average response time: %.2fms", avgTime));
    }

    private static void executeSpikeTest(WebDriver driver, TestCase testCase, Consumer<String> logCallback) {
        int spikes = 3;
        long[] responseTimes = new long[spikes];

        for (int i = 0; i < spikes; i++) {
            long startTime = System.currentTimeMillis();
            driver.get(testCase.getUrl());
            responseTimes[i] = System.currentTimeMillis() - startTime;
            logCallback.accept(String.format("Spike test iteration %d: %dms", i + 1, responseTimes[i]));
        }

        double avgTime = calculateAverage(responseTimes);
        logCallback.accept(String.format("Spike test completed. Average response time: %.2fms", avgTime));
    }

    private static void executeEnduranceTest(WebDriver driver, TestCase testCase, Consumer<String> logCallback) {
        int duration = 3; // seconds
        long startTime = System.currentTimeMillis();
        int requests = 0;

        while ((System.currentTimeMillis() - startTime) < duration * 1000) {
            driver.get(testCase.getUrl());
            requests++;
            logCallback.accept(String.format("Endurance test request %d completed", requests));
        }

        logCallback.accept(String.format("Endurance test completed. Processed %d requests in %d seconds",
                requests, duration));
    }

    private static double calculateAverage(long[] numbers) {
        long sum = 0;
        for (long number : numbers) {
            sum += number;
        }
        return sum / (double) numbers.length;
    }
}