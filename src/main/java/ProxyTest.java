import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import net.lightbody.bmp.BrowserMobProxy;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.yandex.qatools.allure.annotations.Title;

import java.net.URL;

public class ProxyTest {
    private static final String TESTOBJECT = "http://127.0.0.1:4723/wd/hub";
    private AndroidDriver driver;
    private BrowserMobProxy proxy;
    private PageObject PageObject;
    private Steps steps;
    private StepsProxy StepsProxy;


    @Before
    public void setUp() throws Exception {
        AndroidDriverRule androidDriverRules = new AndroidDriverRule();
        proxy = androidDriverRules.getProxy();
        driver = androidDriverRules.getDriver();
        PageObject = new PageObject(driver);
        steps = new Steps(driver);
        StepsProxy = new StepsProxy(proxy);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName","Emulator");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appPackage", "com.yandex.browser");
        capabilities.setCapability("appActivity", "YandexBrowserActivity");

        driver = new AndroidDriver(new URL(TESTOBJECT), capabilities);
    }

    @Rule
    public TestWatcher testWatcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            steps.saveScreenshot();
        }

        @Override
        protected void finished(Description description) {
            driver.quit();
            proxy.stop();
        }
    };


    @Title("Проверка текста в хедере")
    @Test
    public void checkReferrerHeader() throws Exception {
        steps.closeTutorial();
        steps.clickToOmnibox();
        steps.sendKeys("wikip");
        steps.click(PageObject.omniNavigationLink);
        steps.waitLoadPage(30);
        StepsProxy.shouldContainTextInHeader("wikipedia", "Referer", "android");
    }
}