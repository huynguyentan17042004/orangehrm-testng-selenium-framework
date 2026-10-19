package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public final class LoginPage extends BasePage {
    private static final By USERNAME_INPUT = By.name("username");
    private static final By PASSWORD_INPUT = By.name("password");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    private static final By ERROR_ALERT = By.cssSelector(".oxd-alert-content-text");
    private static final By USERNAME_REQUIRED = By.xpath(
            "//input[@name='username']/ancestor::div[contains(@class,'oxd-input-group')]//span[contains(@class,'oxd-input-field-error-message')]");
    private static final By PASSWORD_REQUIRED = By.xpath(
            "//input[@name='password']/ancestor::div[contains(@class,'oxd-input-group')]//span[contains(@class,'oxd-input-field-error-message')]");

    private final String baseUrl;

    public LoginPage(WebDriver driver, int timeoutSeconds, String baseUrl) {
        super(driver, timeoutSeconds);
        this.baseUrl = baseUrl;
    }

    public LoginPage open() {
        logger.info("Opening OrangeHRM login page: {}", baseUrl);
        driver.get(baseUrl);
        waitUntilVisible(USERNAME_INPUT);
        return this;
    }

    public void login(String username, String password) {
        logger.info("Submitting login form for username='{}'", username == null ? "" : username);
        type(USERNAME_INPUT, username);
        type(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
    }

    public boolean isUsernameRequiredVisible() {
        return isVisible(USERNAME_REQUIRED);
    }

    public boolean isPasswordRequiredVisible() {
        return isVisible(PASSWORD_REQUIRED);
    }

    public boolean isInvalidCredentialsVisible() {
        return isVisible(ERROR_ALERT);
    }

    public String usernameRequiredMessage() {
        return textOf(USERNAME_REQUIRED);
    }

    public String passwordRequiredMessage() {
        return textOf(PASSWORD_REQUIRED);
    }

    public String invalidCredentialsMessage() {
        return textOf(ERROR_ALERT);
    }
}
