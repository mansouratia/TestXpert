
# 📚 Literature Review

## 🔍 Overview

The purpose of this literature review is to explore existing solutions and tools in the domain of automated test case generation and execution. This serves as a foundation for understanding how **TestXpert** contributes to or improves upon the current landscape.

## 💡 Related Tools and Systems

### 1. Selenium WebDriver
- **Description:** A popular open-source tool for automating web browsers.
- **Limitation:** Requires manual coding in Java/Python/C#, which is not user-friendly for non-programmers.
- **Relevance:** TestXpert uses Selenium under the hood but provides a GUI for ease of use.

### 2. Katalon Studio
- **Description:** A codeless automation platform built on top of Selenium and Appium.
- **Limitation:** Heavy-weight and more suitable for enterprise solutions.
- **Relevance:** TestXpert offers a lightweight alternative focused purely on web automation via command translation.

### 3. TestProject
- **Description:** A collaborative automation platform with record-and-playback features.
- **Limitation:** Cloud-based dependency and limited offline features.
- **Relevance:** TestXpert operates offline, with no dependency on cloud services.

### 4. Robot Framework
- **Description:** Keyword-driven test automation framework.
- **Limitation:** Requires scripting and setup overhead.
- **Relevance:** TestXpert simplifies the keyword-to-action process using Excel mappings and instant code generation.

## 🆕 Improvements Introduced by TestXpert

| Feature                    | Existing Tools     | TestXpert Advantage                     |
|---------------------------|--------------------|------------------------------------------|
| GUI for Test Creation     | ✅ (Katalon)        | ✅ Lightweight, built with Java Swing     |
| Excel-based Mapping       | ❌                 | ✅ Easy customization by editing Excel     |
| Offline Execution         | ❌ (TestProject)    | ✅ Fully offline                          |
| Direct Selenium Execution | ✅                 | ✅ Plus error screenshots & logs          |
| Lightweight Architecture  | ❌                 | ✅ No installation needed beyond Java     |

## 📝 Summary

TestXpert fills the gap between complex test automation tools and beginner-friendly interfaces by providing:
- A no-code test authoring experience
- Easy integration with Selenium
- Local execution with full transparency and control

This positions it as a suitable academic and educational tool for demonstrating test automation principles.

