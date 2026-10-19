package core;

public record TestConfig(String browser, boolean headless, String baseUrl, int timeoutSeconds) {
    public static TestConfig from(String xmlBrowser, String xmlHeadless, String xmlBaseUrl, String xmlTimeout) {
        String browser = resolve("browser", xmlBrowser, "chrome");
        boolean headless = Boolean.parseBoolean(resolve("headless", xmlHeadless, "true"));
        String baseUrl = resolve("baseUrl", xmlBaseUrl,
                "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        int timeout = parsePositiveInt(resolve("timeoutSeconds", xmlTimeout, "15"));
        return new TestConfig(browser, headless, baseUrl, timeout);
    }

    private static String resolve(String systemProperty, String xmlValue, String defaultValue) {
        String override = System.getProperty(systemProperty);
        if (override != null && !override.isBlank()) {
            return override.trim();
        }
        if (xmlValue != null && !xmlValue.isBlank()) {
            return xmlValue.trim();
        }
        return defaultValue;
    }

    private static int parsePositiveInt(String rawValue) {
        try {
            int value = Integer.parseInt(rawValue);
            if (value <= 0) {
                throw new IllegalArgumentException("timeoutSeconds must be greater than zero");
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("timeoutSeconds must be an integer: " + rawValue, exception);
        }
    }
}
