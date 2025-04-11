import java.util.Map;

public class TestCaseGenerator {
    private Map<String, String> translationMap;

    public TestCaseGenerator(Map<String, String> translationMap) {
        this.translationMap = translationMap;
    }

    public String generateCodeFromStep(String step) {
        for (String key : translationMap.keySet()) {
            if (step.startsWith(key.split(" ")[0])) {
                String[] parts = step.split(" ");
                String[] keyParts = key.split(" ");
                if (parts.length >= keyParts.length) {
                    String template = translationMap.get(key);
                    for (int i = 0; i < parts.length - 1; i++) {
                        template = template.replace("{" + i + "}", parts[i + 1]);
                    }
                    return template;
                }
            }
        }
        return "// Unrecognized step: " + step;
    }
}