import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainGUI {

    private static List<String> steps = new ArrayList<>();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Selenium Test Generator");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Panel for Step Entry and Add Step Button
        JPanel stepPanel = new JPanel();
        stepPanel.setLayout(new BoxLayout(stepPanel, BoxLayout.Y_AXIS));

        JTextField inputField = new JTextField(50);
        JTextArea outputArea = new JTextArea(15, 60);
        outputArea.setEditable(false);

        DefaultListModel<String> stepListModel = new DefaultListModel<>();
        JList<String> stepList = new JList<>(stepListModel);

        // Panel for buttons (Run Test, Save, Load, Clear, View Report)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton addButton = new JButton("Add Step");
        JButton runButton = new JButton("Run Test");
        JButton saveButton = new JButton("Save Test Steps");
        JButton loadButton = new JButton("Load Test Steps");
        JButton clearButton = new JButton("Clear Selected Step");
        JButton reportButton = new JButton("View Report");

        // Add load button to load steps from a text file
        loadButton.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Test Steps File");
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                loadTestStepsFromFile(file, stepListModel);
            }
        });

        // Save button to save steps to a new file
        saveButton.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Test Steps");
            int result = fileChooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                saveTestStepsToNewFile(file);
            }
        });

        // Clear specific step button to delete selected step
        clearButton.addActionListener((ActionEvent e) -> {
            int selectedIndex = stepList.getSelectedIndex(); // Get the selected index

            // If a step is selected
            if (selectedIndex != -1) {
                String selectedStep = stepListModel.getElementAt(selectedIndex); // Get selected step
                steps.remove(selectedStep); // Remove from the list of steps
                stepListModel.remove(selectedIndex); // Remove from the list model (GUI)
                outputArea.append("Step \"" + selectedStep + "\" deleted.\n");
            } else {
                outputArea.append("No step selected to delete.\n");
            }
        });

        // Run button to run the test
        runButton.addActionListener((ActionEvent e) -> {
            // Ensure that steps are not empty
            if (steps.isEmpty()) {
                outputArea.setText("No test steps to run!");
                return;
            }

            // Instantiate TestExecutor and execute the test steps
            TestExecutor testExecutor = new TestExecutor(outputArea);
            testExecutor.executeTestSteps(steps);  // Pass the steps to TestExecutor

            outputArea.append("\nTest steps are running... Check the console for the test results.");
        });

        // Report button to show the test report (read from the file)
        reportButton.addActionListener((ActionEvent e) -> {
            try {
                // Read the test report file
                File reportFile = new File("test_report.txt");

                // If the file exists, read it
                if (reportFile.exists()) {
                    // Read all lines from the report
                    String reportContent = new String(Files.readAllBytes(reportFile.toPath()));

                    // Show the report content in a dialog box
                    JOptionPane.showMessageDialog(frame, reportContent, "Test Report", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // If the file does not exist, inform the user
                    outputArea.setText("No test report found! Please run a test first.");
                    JOptionPane.showMessageDialog(frame, "No test report found! Please run a test first.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                // Handle any errors reading the file
                JOptionPane.showMessageDialog(frame, "Error reading the test report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addButton.addActionListener((ActionEvent e) -> {
            String stepText = inputField.getText();
            steps.add(stepText);
            stepListModel.addElement(stepText);
            inputField.setText(""); // Clear input
        });

        // Organize the panels
        stepPanel.add(new JLabel("Enter Step:"));
        stepPanel.add(inputField);
        stepPanel.add(addButton);

        buttonPanel.add(runButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(clearButton);  // Updated clear button
        buttonPanel.add(reportButton);

        mainPanel.add(stepPanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(new JScrollPane(stepList));
        mainPanel.add(new JScrollPane(outputArea));

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // Load test steps from a text file
    private static void loadTestStepsFromFile(File file, DefaultListModel<String> stepListModel) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            steps.clear();  // Clear any existing steps before loading new ones
            stepListModel.clear();  // Clear the list model to reset the displayed steps

            while ((line = reader.readLine()) != null) {
                steps.add(line);  // Add the loaded step to the steps list
                stepListModel.addElement(line);  // Add the loaded step to the JList model
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Save test steps to a new file
    private static void saveTestStepsToNewFile(File file) {
        try {
            // Add timestamp to create a unique file every time
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String newFileName = file.getAbsolutePath().replaceAll("\\.txt$", "") + "_" + timestamp + ".txt";
            File newFile = new File(newFileName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
                for (String step : steps) {
                    writer.write(step);
                    writer.newLine();
                }
            }

            JOptionPane.showMessageDialog(null, "Test steps saved as: " + newFile.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
