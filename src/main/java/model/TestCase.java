package model;

                        public class TestCase {
                            private String url;
                            private String testType;
                            private String action;
                            private String locatorType;
                            private String locatorValue;
                            private String inputData;

                            // Getters and Setters
                            public String getUrl() { return url; }
                            public void setUrl(String url) { this.url = url; }

                            public String getTestType() { return testType; }
                            public void setTestType(String testType) { this.testType = testType; }

                            public String getAction() { return action; }
                            public void setAction(String action) { this.action = action; }

                            public String getLocatorType() { return locatorType; }
                            public void setLocatorType(String locatorType) { this.locatorType = locatorType; }

                            public String getLocatorValue() { return locatorValue; }
                            public void setLocatorValue(String locatorValue) { this.locatorValue = locatorValue; }

                            public String getInputData() { return inputData; }
                            public void setInputData(String inputData) { this.inputData = inputData; }

                            @Override
                            public String toString() {
                                return String.format("TestCase{url='%s', type='%s', action='%s', locator='%s %s'}",
                                        url, testType, action, locatorType, locatorValue);
                            }
                        }