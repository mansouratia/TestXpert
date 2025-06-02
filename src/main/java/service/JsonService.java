package service;

    import com.google.gson.Gson;
    import com.google.gson.GsonBuilder;
    import model.TestSuite;
    import java.io.*;
    import java.nio.file.Files;
    import java.nio.file.Paths;

    public class JsonService {
        private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        public static void saveTestSuite(TestSuite suite, String filePath) throws IOException {
            // Ensure parent directories exist
            Files.createDirectories(Paths.get(filePath).getParent());

            // Write the file with UTF-8 encoding
            try (Writer writer = new OutputStreamWriter(
                    new FileOutputStream(filePath), "UTF-8")) {
                gson.toJson(suite, writer);
            }
        }

        public static TestSuite loadTestSuite(String filePath) throws IOException {
            // Read the file with UTF-8 encoding
            try (Reader reader = new InputStreamReader(
                    new FileInputStream(filePath), "UTF-8")) {
                return gson.fromJson(reader, TestSuite.class);
            }
        }
    }