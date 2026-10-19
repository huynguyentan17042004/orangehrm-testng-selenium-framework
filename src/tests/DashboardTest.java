package tests;

import pages.DashboardPage;
import pages.LoginPage;
import core.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("OrangeHRM Web UI")
@Feature("Dashboard")
public final class DashboardTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Verify Dashboard after a successful login")
    @Story("Dashboard landing page")
    public void dashboardShouldDisplayAfterLogin() {
        String username = System.getenv().getOrDefault("ORANGEHRM_USERNAME", "Admin");
        String password = System.getenv().getOrDefault("ORANGEHRM_PASSWORD", "admin123");

        LoginPage loginPage = new LoginPage(driver(), config.timeoutSeconds(), config.baseUrl()).open();
        loginPage.login(username, password);

        DashboardPage dashboard = new DashboardPage(driver(), config.timeoutSeconds());
        SoftAssert softly = new SoftAssert();
        softly.assertTrue(dashboard.isLoaded(), "Dashboard should be loaded after valid login");
        softly.assertFalse(dashboard.headingText().isBlank(), "Dashboard heading should not be blank");
        softly.assertTrue(dashboard.isUserMenuVisible(), "User menu should be visible");
        softly.assertFalse(dashboard.userMenuText().isBlank(), "Logged-in user name should not be blank");
        softly.assertAll();
    }
}
