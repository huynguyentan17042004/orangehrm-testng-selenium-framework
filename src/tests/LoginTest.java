package tests;

import core.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.DashboardPage;
import pages.LoginPage;
import support.LoginDataProvider;
import support.LoginTestData;

@Epic("OrangeHRM Web UI")
@Feature("Authentication")
public final class LoginTest extends BaseTest {

    private LoginPage login(LoginTestData testData) {
        logger.info("Executing login case: {}", testData.id());
        LoginPage loginPage = new LoginPage(driver(), config.timeoutSeconds(), config.baseUrl()).open();
        loginPage.login(testData.username(), testData.password());
        return loginPage;
    }

    @Test(dataProvider = "validLoginData", dataProviderClass = LoginDataProvider.class,
            groups = {"smoke", "regression"}, description = "Valid login opens the Dashboard")
    @Story("Valid login")
    public void validLoginShouldOpenDashboard(LoginTestData testData) {
        login(testData);

        DashboardPage dashboard = new DashboardPage(driver(), config.timeoutSeconds());
        Assert.assertTrue(dashboard.isLoaded(), "Dashboard should load after valid login");

        SoftAssert softly = new SoftAssert();
        softly.assertFalse(dashboard.headingText().isBlank(), "Dashboard heading should not be blank");
        softly.assertTrue(dashboard.isUserMenuVisible(), "User menu should be visible");
        softly.assertFalse(dashboard.userMenuText().isBlank(), "User menu text should not be blank");
        softly.assertAll();
    }

    @Test(dataProvider = "invalidLoginData", dataProviderClass = LoginDataProvider.class,
            groups = {"regression"}, description = "Invalid credentials show an error alert")
    @Story("Invalid login")
    public void invalidLoginShouldShowError(LoginTestData testData) {
        LoginPage loginPage = login(testData);

        Assert.assertTrue(loginPage.isInvalidCredentialsVisible(),
                "Invalid credentials error should be visible");
        Assert.assertFalse(loginPage.invalidCredentialsMessage().isBlank(),
                "Invalid credentials error should not be blank");
        Assert.assertTrue(loginPage.currentUrl().contains("/auth/login"),
                "Browser should remain on the login page");
    }

    @Test(dataProvider = "usernameRequiredData", dataProviderClass = LoginDataProvider.class,
            groups = {"regression"}, description = "Empty username shows required validation")
    @Story("Required username")
    public void emptyUsernameShouldShowRequired(LoginTestData testData) {
        LoginPage loginPage = login(testData);

        Assert.assertTrue(loginPage.isUsernameRequiredVisible(),
                "Username required message should be visible");
        Assert.assertFalse(loginPage.usernameRequiredMessage().isBlank(),
                "Username required message should not be blank");
    }

    @Test(dataProvider = "passwordRequiredData", dataProviderClass = LoginDataProvider.class,
            groups = {"regression"}, description = "Empty password shows required validation")
    @Story("Required password")
    public void emptyPasswordShouldShowRequired(LoginTestData testData) {
        LoginPage loginPage = login(testData);

        Assert.assertTrue(loginPage.isPasswordRequiredVisible(),
                "Password required message should be visible");
        Assert.assertFalse(loginPage.passwordRequiredMessage().isBlank(),
                "Password required message should not be blank");
    }

    @Test(dataProvider = "bothRequiredData", dataProviderClass = LoginDataProvider.class,
            groups = {"regression"}, description = "Empty credentials show both required validations")
    @Story("Required credentials")
    public void emptyCredentialsShouldShowBothRequired(LoginTestData testData) {
        LoginPage loginPage = login(testData);

        SoftAssert softly = new SoftAssert();
        softly.assertTrue(loginPage.isUsernameRequiredVisible(),
                "Username required message should be visible");
        softly.assertTrue(loginPage.isPasswordRequiredVisible(),
                "Password required message should be visible");
        softly.assertFalse(loginPage.usernameRequiredMessage().isBlank(),
                "Username required message should not be blank");
        softly.assertFalse(loginPage.passwordRequiredMessage().isBlank(),
                "Password required message should not be blank");
        softly.assertAll();
    }
}
