package support;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    private ScreenshotUtils() {
    }

    public static Screenshot capture(WebDriver driver, String testName) throws IOException {
        byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Path directory = Path.of("artifacts", "screenshots");
        Files.createDirectories(directory);
        String safeName = testName.replaceAll("[^a-zA-Z0-9._-]", "_");
        Path path = directory.resolve(safeName + "-" + LocalDateTime.now().format(TIMESTAMP) + ".png");
        Files.write(path, bytes);
        return new Screenshot(path.toAbsolutePath(), bytes);
    }

    public record Screenshot(Path path, byte[] bytes) {
    }
}
