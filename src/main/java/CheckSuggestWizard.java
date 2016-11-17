import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.yandex.qatools.allure.annotations.Title;

import java.awt.*;
import java.net.URL;

@Title("Урок 6 Проверка Колдунщика погоды")
public class CheckSuggestWizard {

    private static final String TESTOBJECT = "http://127.0.0.1:4723/wd/hub";
    private AppiumDriver driver;
    private Steps steps;

    @Before
    public void setUp() throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName","Emulator");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appPackage", "com.yandex.browser");
        capabilities.setCapability("appActivity", "YandexBrowserActivity");

        driver = new AndroidDriver(new URL(TESTOBJECT), capabilities);
        steps = new Steps(driver);
    }

    @Rule
    public TestRule watchman = new TestWatcher() {

        @Override
        protected void finished(Description description) {
            driver.quit();
        }

        @Override
        protected void failed(Throwable e, Description description) {
            steps.makeScreenshot();
            driver.quit();
        }
    };

    @Title("Проверка колдунщика погоды")
    @Test
    public void checkTextSuggestWizard() throws Exception {
        steps.closeTutorial();
        steps.clickToOmnibox();
        steps.sendKeys("pogoda v moskve");
        steps.shouldNotBeDisplayed(PageObject.historySuggest.clockIcon);
        steps.shouldContainText(PageObject.wizardSuggest.wizardText, "°C");

    }


}