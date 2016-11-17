import io.appium.java_client.android.AndroidDriver;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.proxy.CaptureType;
import org.junit.rules.TestWatcher;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.InetAddress;
import java.net.URL;

public class AndroidDriverRule extends TestWatcher{
    private AndroidDriver driver;

    public AndroidDriverRule() throws Exception{
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName","aphone");
        capabilities.setCapability("appPackage", "com.yandex.browser");
        capabilities.setCapability("appActivity", ".YandexBrowserMainActivity");
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    public BrowserMobProxy getProxy() throws Exception{
        BrowserMobProxyServer proxy = new BrowserMobProxyServer();
        proxy.start(8888, InetAddress.getLocalHost());
        proxy.setHarCaptureTypes(CaptureType.REQUEST_HEADERS);
        proxy.newHar("HAR");
        return proxy;
    }

    public AndroidDriver getDriver() { return driver; }
}