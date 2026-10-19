package core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.Locale;

public final class BrowserFactory {
    private BrowserFactory() {
    }

    public static WebDriver create(String browserName, boolean headless) {
        WebDriver driver = switch (browserName.toLowerCase(Locale.ROOT)) {
            case "chrome" -> createChrome(headless);
            case "firefox" -> createFirefox(headless);
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        };

        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        if (!headless) {
            driver.manage().window().maximize();
        }
        return driver;
    }

    private static WebDriver createChrome(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=en-US", "--window-size=1440,1000");
        if (headless) {
            options.addArguments("--headless=new");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefox(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("intl.accept_languages", "en-US");
        if (headless) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }
}
