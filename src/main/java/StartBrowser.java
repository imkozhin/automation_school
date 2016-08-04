import java.net.MalformedURLException;
import java.net.URL;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class StartBrowser {
    WebDriver driver;

    @Before
    public void setUp() throws MalformedURLException{
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName","Emulator");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appPackage", "com.yandex.browser");
        capabilities.setCapability("appActivity", "YandexBrowserActivity");
        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @Test
    public void test() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bro_sentry_bar_fake")));
        }
        catch (Exception s){
            wait.until(ExpectedConditions.elementToBeClickable(By.id("activity_tutorial_close_button"))).click();
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bro_sentry_bar_fake")));
    }

    @After
    public void tearDown(){
        driver.quit();
    }
}
