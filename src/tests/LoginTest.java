package tests;

import pages.DashboardPage;
import pages.LoginPage;
import core.BaseTest;
import support.LoginDataProvider;
import support.LoginTestData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("OrangeHRM Web UI")
@Feature("Authentication")
public final class LoginTest extends BaseTest {

    @Test(dataProvider = "loginData", dataProviderClass = LoginDataProvider.class,
            groups = {"regression"}, description = "Validate login decision-table scenarios")
    @Story("Login validation")
    public void loginScenarios(LoginTestData testData) {
        logger.info("Executing login case: {}", testData.id());
        LoginPage loginPage = new LoginPage(driver(), config.timeoutSeconds(), config.baseUrl()).open();
        loginPage.login(testData.username(), testData.password());

        SoftAssert softly = new SoftAssert();
        switch (testData.expectedResult()) {
            case DASHBOARD -> assertDashboard(softly);
            case USERNAME_REQUIRED -> {
                softly.assertTrue(loginPage.isUsernameRequiredVisible(), "Username required message should be visible");
                softly.assertFalse(loginPage.usernameRequiredMessage().isBlank(),
                        "Username required message should not be blank");
                softly.assertTrue(loginPage.currentUrl().contains("/auth/login"), "Browser should remain on login page");
            }
            case PASSWORD_REQUIRED -> {
                softly.assertTrue(loginPage.isPasswordRequiredVisible(), "Password required message should be visible");
                softly.assertFalse(loginPage.passwordRequiredMessage().isBlank(),
                        "Password required message should not be blank");
                softly.assertTrue(loginPage.currentUrl().contains("/auth/login"), "Browser should remain on login page");
            }
            case BOTH_REQUIRED -> {
                softly.assertTrue(loginPage.isUsernameRequiredVisible(), "Username required message should be visible");
                softly.assertTrue(loginPage.isPasswordRequiredVisible(), "Password required message should be visible");
                softly.assertFalse(loginPage.usernameRequiredMessage().isBlank(),
                        "Username required message should not be blank");
                softly.assertFalse(loginPage.passwordRequiredMessage().isBlank(),
                        "Password required message should not be blank");
            }
            case INVALID_CREDENTIALS -> {
                softly.assertTrue(loginPage.isInvalidCredentialsVisible(), "Invalid credentials alert should be visible");
                softly.assertFalse(loginPage.invalidCredentialsMessage().isBlank(),
                        "Invalid credentials alert should not be blank");
                softly.assertTrue(loginPage.currentUrl().contains("/auth/login"), "Browser should remain on login page");
            }
        }
        softly.assertAll();
    }

    private void assertDashboard(SoftAssert softly) {
        DashboardPage dashboard = new DashboardPage(driver(), config.timeoutSeconds());
        softly.assertTrue(dashboard.isLoaded(), "Dashboard should be loaded");
        softly.assertFalse(dashboard.headingText().isBlank(), "Dashboard heading should not be blank");
        softly.assertTrue(dashboard.isUserMenuVisible(), "User menu should be visible");
        softly.assertFalse(dashboard.userMenuText().isBlank(), "User menu text should not be blank");
    }
}
