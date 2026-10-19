package core;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public abstract class BaseTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected TestConfig config;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "headless", "baseUrl", "timeoutSeconds"})
    public void setUp(
            @Optional("chrome") String browser,
            @Optional("true") String headless,
            @Optional("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login") String baseUrl,
            @Optional("15") String timeoutSeconds) {
        config = TestConfig.from(browser, headless, baseUrl, timeoutSeconds);
        logger.info("Starting browser={}, headless={}, timeout={}s", config.browser(), config.headless(),
                config.timeoutSeconds());
        DriverManager.setDriver(BrowserFactory.create(config.browser(), config.headless()));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        logger.info("Finished test '{}' with status={}", result.getMethod().getMethodName(), statusName(result));
        DriverManager.quitDriver();
    }

    protected WebDriver driver() {
        return DriverManager.getDriver();
    }

    private String statusName(ITestResult result) {
        return switch (result.getStatus()) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            case ITestResult.SKIP -> "SKIPPED";
            default -> "UNKNOWN";
        };
    }
}
