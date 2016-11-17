import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ScreenshotCollector {
    private AppiumDriver driver;

    public ScreenshotCollector(AppiumDriver driver) {
        this.driver = driver;
    }

    public BufferedImage collect(WebElement element) throws Exception {
        File scFile = driver.getScreenshotAs(OutputType.FILE);
        BufferedImage fullScreenshot = ImageIO.read(scFile);

        Point elementLocation = element.getLocation();
        int elementWidth = element.getSize().getWidth();
        int elementHeight = element.getSize().getHeight();

        return fullScreenshot.getSubimage(elementLocation.getX(), elementLocation.getY(), elementWidth, elementHeight);
    }
}