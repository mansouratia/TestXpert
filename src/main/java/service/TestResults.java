package service;

    import java.io.*;
    import java.nio.charset.StandardCharsets;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.function.Consumer;
    import java.util.logging.Logger;
    import java.util.logging.Level;

    public class TestResults {
        private static final Logger LOGGER = Logger.getLogger(TestResults.class.getName());

        // ANSI color codes for console output
        private static final String RESET = "\u001B[0m";
        private static final String GREEN = "\u001B[32m";
        private static final String RED = "\u001B[31m";
        private static final String YELLOW = "\u001B[33m";
        private static final String BLUE = "\u001B[34m";
        private static final String CYAN = "\u001B[36m";
        private static final String WHITE_BG = "\u001B[47m";
        private static final String BLACK = "\u001B[30m";

        private int executed = 0;
        private int passed = 0;
        private int failed = 0;
        private final List<String> messages = new ArrayList<>();
        private final List<String> actionResults = new ArrayList<>();
        private final LocalDateTime startTime = LocalDateTime.now();
        private String currentReportPath;
        private boolean reportGenerated = false;

        public void addSuccess(String message) {
            messages.add(GREEN + "[PASS] " + message + RESET);
            executed++;
            passed++;
        }

        public void addFailure(String message) {
            messages.add(RED + "[FAIL] " + message + RESET);
            executed++;
            failed++;
        }

        public void addActionResult(String result) {
            actionResults.add(BLUE + result + RESET);
        }

        public boolean generateReport(Consumer<String> logCallback) {
            try {
                if (executed == 0) {
                    String warning = YELLOW + "No tests were executed. Report generation skipped." + RESET;
                    LOGGER.warning(warning);
                    if (logCallback != null) {
                        logCallback.accept(warning);
                    }
                    reportGenerated = false;
                    return false;
                }
                String reportContent = generateReportContent();
                writeReportToFile(reportContent);
                if (verifyReport()) {
                    reportGenerated = true;
                    String success = GREEN + "Test execution completed. Report generated: " + currentReportPath + RESET;
                    if (logCallback != null) {
                        logCallback.accept(success);
                    }
                    LOGGER.info(success);
                    return true;
                } else {
                    String error = RED + "Report verification failed after writing" + RESET;
                    LOGGER.severe(error);
                    if (logCallback != null) {
                        logCallback.accept(error);
                    }
                    reportGenerated = false;
                    return false;
                }
            } catch (Exception e) {
                String error = RED + "Failed to generate report: " + e.getMessage() + RESET;
                LOGGER.log(Level.SEVERE, error);
                if (logCallback != null) {
                    logCallback.accept(error);
                }
                reportGenerated = false;
                return false;
            }
        }

        private String generateReportContent() {
            StringBuilder sb = new StringBuilder();

            // Header
            sb.append("AUTOMATED TEST EXECUTION REPORT\n");
            sb.append("\n");

            // Execution Summary
            sb.append("EXECUTION SUMMARY\n");
            sb.append("-----------------\n");
            sb.append(String.format("Start Time    : %s\n",
                    startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            sb.append(String.format("End Time      : %s\n",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            sb.append(String.format("Duration      : %s\n\n",
                    formatDuration(startTime, LocalDateTime.now())));

            // Test Statistics
            sb.append("TEST STATISTICS\n");
            sb.append("--------------\n");
            sb.append(String.format("Total Tests   : %d\n", executed));
            sb.append(String.format("Passed        : %d\n", passed));
            sb.append(String.format("Failed        : %d\n", failed));
            double successRate = executed > 0 ? (passed * 100.0 / executed) : 0;
            sb.append(String.format("Success Rate  : %.2f%%\n\n", successRate));

            // Test Results
            sb.append("TEST RESULTS\n");
            sb.append("------------\n");
            for (String message : messages) {
                sb.append(stripAnsiCodes(message)).append("\n");
            }
            sb.append("\n");

            // Action Details
            sb.append("ACTION DETAILS\n");
            sb.append("--------------\n");
            for (String result : actionResults) {
                sb.append(stripAnsiCodes(result)).append("\n");
            }
            sb.append("\n");

            return sb.toString();
        }

        private String formatDuration(LocalDateTime start, LocalDateTime end) {
            long seconds = java.time.Duration.between(start, end).getSeconds();
            long minutes = seconds / 60;
            seconds = seconds % 60;
            return String.format("%02d:%02d (mm:ss)", minutes, seconds);
        }

        private String stripAnsiCodes(String input) {
            return input.replaceAll("\u001B\\[[;\\d]*m", "");
        }

        private List<String> splitIntoLines(String text, int maxLength) {
            List<String> lines = new ArrayList<>();
            while (text.length() > maxLength) {
                int splitIndex = text.lastIndexOf(' ', maxLength);
                if (splitIndex == -1) splitIndex = maxLength;
                lines.add(text.substring(0, splitIndex));
                text = text.substring(splitIndex).trim();
            }
            if (!text.isEmpty()) {
                lines.add(text);
            }
            return lines;
        }

        private void writeReportToFile(String reportContent) throws IOException {
            Path reportsDir = Paths.get("test-reports").toAbsolutePath();
            if (!Files.exists(reportsDir)) {
                try {
                    Files.createDirectories(reportsDir);
                    LOGGER.info(CYAN + "Created reports directory: " + reportsDir + RESET);
                } catch (IOException e) {
                    LOGGER.severe(RED + "Failed to create reports directory: " + e.getMessage() + RESET);
                    throw e;
                }
            }
            String fileName = "report_" + startTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
            Path reportPath = reportsDir.resolve(fileName);
            currentReportPath = reportPath.toString();
            try {
                Files.write(reportPath, reportContent.getBytes(StandardCharsets.UTF_8));
                LOGGER.info(GREEN + "Report written successfully to: " + currentReportPath + RESET);
            } catch (IOException e) {
                LOGGER.severe(RED + "Failed to write report to file: " + e.getMessage() + RESET);
                throw e;
            }
        }

        private boolean verifyReport() {
            if (currentReportPath == null || currentReportPath.isEmpty()) {
                LOGGER.severe(RED + "Report path is null or empty" + RESET);
                return false;
            }
            Path reportPath = Paths.get(currentReportPath);
            try {
                if (!Files.exists(reportPath)) {
                    LOGGER.severe(RED + "Report file does not exist: " + reportPath + RESET);
                    return false;
                }
                if (!Files.isReadable(reportPath)) {
                    LOGGER.severe(RED + "Report file is not readable: " + reportPath + RESET);
                    return false;
                }
                long fileSize = Files.size(reportPath);
                if (fileSize == 0) {
                    LOGGER.severe(RED + "Report file is empty: " + reportPath + RESET);
                    return false;
                }
                String content = new String(Files.readAllBytes(reportPath), StandardCharsets.UTF_8);
                if (!content.contains("AUTOMATED TEST EXECUTION REPORT") ||
                    !content.contains("Total Tests   : " + executed)) {
                    LOGGER.severe(RED + "Report content verification failed" + RESET);
                    return false;
                }
                return true;
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, RED + "Error verifying report file: " + e.getMessage() + RESET);
                return false;
            }
        }

        public void openReport() {
            if (!reportGenerated) {
                LOGGER.warning(YELLOW + "Attempting to open report before generation" + RESET);
                System.err.println("No report available - report has not been generated");
                return;
            }
            if (currentReportPath == null || currentReportPath.isEmpty()) {
                LOGGER.warning(YELLOW + "No report available - currentReportPath is null or empty" + RESET);
                System.err.println("No report available");
                return;
            }
            Path reportPath = Paths.get(currentReportPath);
            if (!Files.exists(reportPath)) {
                LOGGER.warning(YELLOW + "Report file not found at: " + reportPath + RESET);
                System.err.println("Report file not found: " + currentReportPath);
                return;
            }
            try {
                String os = System.getProperty("os.name").toLowerCase();
                ProcessBuilder pb;
                if (os.contains("linux")) {
                    pb = new ProcessBuilder("xdg-open", reportPath.toString());
                } else if (os.contains("mac")) {
                    pb = new ProcessBuilder("open", reportPath.toString());
                } else if (os.contains("windows")) {
                    pb = new ProcessBuilder("cmd", "/c", "start", reportPath.toString());
                } else {
                    String message = "Unsupported operating system: " + os;
                    LOGGER.severe(RED + message + RESET);
                    throw new UnsupportedOperationException(message);
                }
                LOGGER.info(CYAN + "Attempting to open report with command: " + String.join(" ", pb.command()) + RESET);
                pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                Process process = pb.start();
                Thread.sleep(1000);
                if (!process.isAlive() && process.exitValue() != 0) {
                    throw new IOException("Failed to open report - process exited with code: " + process.exitValue());
                }
                LOGGER.info(GREEN + "Report opened successfully" + RESET);
            } catch (IOException | InterruptedException e) {
                LOGGER.log(Level.SEVERE, RED + "Error opening report: " + e.getMessage() + RESET);
                System.err.println("Error opening report: " + e.getMessage());
            }
        }

        public boolean isReportGenerated() {
            return reportGenerated;
        }

        public String getCurrentReportPath() {
            return currentReportPath;
        }
    }