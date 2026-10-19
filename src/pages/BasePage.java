package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected BasePage(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    protected WebElement waitUntilVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitUntilClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void type(By locator, String value) {
        WebElement element = waitUntilVisible(locator);
        element.clear();
        if (value != null && !value.isEmpty()) {
            element.sendKeys(value);
        }
    }

    protected void click(By locator) {
        waitUntilClickable(locator).click();
    }

    protected String textOf(By locator) {
        return waitUntilVisible(locator).getText().trim();
    }

    protected boolean isVisible(By locator) {
        try {
            return waitUntilVisible(locator).isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}
