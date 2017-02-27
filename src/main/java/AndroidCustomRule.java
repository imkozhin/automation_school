import io.appium.java_client.android.AndroidDriver;
import net.lightbody.bmp.BrowserMobProxyServer;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.net.MalformedURLException;
import java.net.URL;

public class AndroidCustomRule extends TestWatcher {
    private AndroidDriver driver;
    BrowserMobProxyServer server;

    public AndroidCustomRule() {
        createDriver();
    }


    public void createDriver(){
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "Emulator");
        capabilities.setCapability("appPackage",  "com.yandex.browser");
        capabilities.setCapability("appActivity", ".YandexBrowserActivity");
//        capabilities.setCapability("appWaitActivity", ".firstscreen.FirstScreenActivity");

        try {
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
    }

    public AndroidDriver getDriver(){
        return driver;
    }

    @Override
    protected void finished(Description description) {
        if(server != null) {server.stop();}

        if(driver != null) {
            driver.quit();
        }
    }

    @Override
    protected void failed(Throwable e, Description description) {
        makeScreenshot();
        if(server != null) {
            server.stop();
        }

        if(driver != null) {
            driver.quit();
        }
    }

    @Attachment(type = "image/png")
    public byte[] makeScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}