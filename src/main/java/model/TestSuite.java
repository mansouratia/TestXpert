package model;

import java.util.ArrayList;
import java.util.List;

public class TestSuite {
    private String name;
    private List<TestCase> testCases;

    public TestSuite() {
        this.testCases = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Test suite name cannot be null or empty.");
        }
        this.name = name;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }
}