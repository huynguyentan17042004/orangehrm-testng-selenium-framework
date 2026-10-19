package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public final class LifecycleAnnotationsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LifecycleAnnotationsTest.class);

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        LOGGER.info("LIFECYCLE -> @BeforeSuite");
    }

    @BeforeTest(alwaysRun = true)
    public void beforeTest() {
        LOGGER.info("LIFECYCLE -> @BeforeTest");
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        LOGGER.info("LIFECYCLE -> @BeforeClass");
    }

    @BeforeGroups(groups = "lifecycle", alwaysRun = true)
    public void beforeGroups() {
        LOGGER.info("LIFECYCLE -> @BeforeGroups");
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        LOGGER.info("LIFECYCLE -> @BeforeMethod");
    }

    @Test(groups = "lifecycle", description = "Print the TestNG annotation execution order")
    public void lifecycleTest() {
        LOGGER.info("LIFECYCLE -> @Test");
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        LOGGER.info("LIFECYCLE -> @AfterMethod");
    }

    @AfterGroups(groups = "lifecycle", alwaysRun = true)
    public void afterGroups() {
        LOGGER.info("LIFECYCLE -> @AfterGroups");
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        LOGGER.info("LIFECYCLE -> @AfterClass");
    }

    @AfterTest(alwaysRun = true)
    public void afterTest() {
        LOGGER.info("LIFECYCLE -> @AfterTest");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        LOGGER.info("LIFECYCLE -> @AfterSuite");
    }
}
