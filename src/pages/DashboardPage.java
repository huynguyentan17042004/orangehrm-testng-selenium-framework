package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public final class DashboardPage extends BasePage {
    private static final By PAGE_HEADING = By.cssSelector("h6.oxd-topbar-header-breadcrumb-module");
    private static final By USER_DROPDOWN = By.cssSelector(".oxd-userdropdown-name");

    public DashboardPage(WebDriver driver, int timeoutSeconds) {
        super(driver, timeoutSeconds);
    }

    public boolean isLoaded() {
        return isVisible(PAGE_HEADING) && currentUrl().contains("/dashboard/index");
    }

    public String headingText() {
        return textOf(PAGE_HEADING);
    }

    public boolean isUserMenuVisible() {
        return isVisible(USER_DROPDOWN);
    }

    public String userMenuText() {
        return textOf(USER_DROPDOWN);
    }
}
