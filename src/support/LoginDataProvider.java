package support;

import org.testng.annotations.DataProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class LoginDataProvider {
    private static final String DATA_FILE = "testdata/login-data.csv";

    private LoginDataProvider() {
    }

    @DataProvider(name = "validLoginData")
    public static Object[][] validLoginData() {
        return filterBy(ExpectedResult.DASHBOARD);
    }

    @DataProvider(name = "invalidLoginData")
    public static Object[][] invalidLoginData() {
        return filterBy(ExpectedResult.INVALID_CREDENTIALS);
    }

    @DataProvider(name = "usernameRequiredData")
    public static Object[][] usernameRequiredData() {
        return filterBy(ExpectedResult.USERNAME_REQUIRED);
    }

    @DataProvider(name = "passwordRequiredData")
    public static Object[][] passwordRequiredData() {
        return filterBy(ExpectedResult.PASSWORD_REQUIRED);
    }

    @DataProvider(name = "bothRequiredData")
    public static Object[][] bothRequiredData() {
        return filterBy(ExpectedResult.BOTH_REQUIRED);
    }

    private static Object[][] filterBy(ExpectedResult expectedResult) {
        Object[][] matchingCases = readCases().stream()
                .filter(testCase -> testCase.expectedResult() == expectedResult)
                .map(testCase -> new Object[]{testCase})
                .toArray(Object[][]::new);

        if (matchingCases.length == 0) {
            throw new IllegalStateException("No login test data found for expected result: " + expectedResult);
        }
        return matchingCases;
    }

    private static List<LoginTestData> readCases() {
        InputStream stream = LoginDataProvider.class.getClassLoader().getResourceAsStream(DATA_FILE);
        if (stream == null) {
            throw new IllegalStateException("Test data file not found: " + DATA_FILE);
        }

        List<LoginTestData> cases = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            boolean headerSkipped = false;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || line.stripLeading().startsWith("#")) {
                    continue;
                }
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                String[] columns = line.split(",", -1);
                if (columns.length != 4) {
                    throw new IllegalArgumentException("Expected 4 CSV columns but got " + columns.length + ": " + line);
                }
                cases.add(new LoginTestData(
                        columns[0].trim(),
                        resolveValue(columns[1].trim()),
                        resolveValue(columns[2].trim()),
                        ExpectedResult.valueOf(columns[3].trim())));
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read test data: " + DATA_FILE, exception);
        }

        if (cases.isEmpty()) {
            throw new IllegalStateException("No test cases found in: " + DATA_FILE);
        }
        return cases;
    }

    private static String resolveValue(String value) {
        return switch (value) {
            case "${ORANGEHRM_USERNAME}" -> System.getenv().getOrDefault("ORANGEHRM_USERNAME", "Admin");
            case "${ORANGEHRM_PASSWORD}" -> System.getenv().getOrDefault("ORANGEHRM_PASSWORD", "admin123");
            default -> value;
        };
    }
}
