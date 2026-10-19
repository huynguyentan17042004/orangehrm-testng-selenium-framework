package support;

import core.DriverManager;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class TestListener implements ITestListener, ISuiteListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onStart(ISuite suite) {
        LOGGER.info("========== SUITE START: {} ==========", suite.getName());
        writeAllureEnvironment(suite);
    }

    @Override
    public void onFinish(ISuite suite) {
        LOGGER.info("========== SUITE END: {} ==========", suite.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        LOGGER.info("TEST START: {}", displayName(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOGGER.info("TEST PASSED: {}", displayName(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOGGER.error("TEST FAILED: {}", displayName(result), result.getThrowable());
        attachFailureScreenshot(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOGGER.warn("TEST SKIPPED: {}", displayName(result));
    }

    private void attachFailureScreenshot(ITestResult result) {
        WebDriver driver = DriverManager.getDriverOrNull();
        if (driver == null) {
            LOGGER.warn("No active WebDriver; failure screenshot was not captured");
            return;
        }
        try {
            ScreenshotUtils.Screenshot screenshot = ScreenshotUtils.capture(driver, result.getMethod().getMethodName());
            Allure.addAttachment("Failure screenshot", "image/png",
                    new ByteArrayInputStream(screenshot.bytes()), ".png");
            LOGGER.info("Failure screenshot saved at {}", screenshot.path());
        } catch (Exception exception) {
            LOGGER.error("Could not capture failure screenshot", exception);
        }
    }

    private String displayName(ITestResult result) {
        Object[] parameters = result.getParameters();
        if (parameters.length == 0) {
            return result.getMethod().getQualifiedName();
        }
        return result.getMethod().getQualifiedName() + " " + java.util.Arrays.toString(parameters);
    }

    private void writeAllureEnvironment(ISuite suite) {
        Path resultsDirectory = Path.of(System.getProperty("allure.results.directory", "target/allure-results"));
        Properties properties = new Properties();
        properties.setProperty("Java", System.getProperty("java.version"));
        properties.setProperty("OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));
        properties.setProperty("Architecture", System.getProperty("os.arch"));
        properties.setProperty("Browser", resolvedSuiteValue(suite, "browser", "chrome"));
        properties.setProperty("Headless", resolvedSuiteValue(suite, "headless", "true"));
        properties.setProperty("Base URL", resolvedSuiteValue(suite, "baseUrl", "not configured"));

        try {
            Files.createDirectories(resultsDirectory);
            try (OutputStream stream = Files.newOutputStream(resultsDirectory.resolve("environment.properties"))) {
                properties.store(stream, "OrangeHRM test environment");
            }
        } catch (IOException exception) {
            LOGGER.warn("Could not write Allure environment.properties", exception);
        }
    }

    private String resolvedSuiteValue(ISuite suite, String name, String fallback) {
        String systemValue = System.getProperty(name);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }
        String suiteValue = suite.getParameter(name);
        return suiteValue == null || suiteValue.isBlank() ? fallback : suiteValue;
    }
}
