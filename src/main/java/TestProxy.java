import io.appium.java_client.android.AndroidDriver;
import net.lightbody.bmp.BrowserMobProxy;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import ru.yandex.qatools.allure.annotations.Title;

public class TestProxy {
    private AndroidDriver driver;
    private BrowserMobProxy proxy;
    private PageObjects pageObjects;
    private StepsForTests step;
    private StepsProxy stepProxy;

    @Before
    public void setUp() throws Exception {
        AndroidDriverRule androidDriverRules = new AndroidDriverRule();
        proxy = androidDriverRules.getProxy();
        driver = androidDriverRules.getDriver();
        pageObjects = new PageObjects(driver);
        step = new StepsForTests(driver);
        stepProxy = new StepsProxy(proxy);
    }

    @Rule
    public TestWatcher testWatcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            step.saveScreenshot();
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
        step.closeTutorial();
        step.clickToOmnibox();
        step.sendKeys("wikip");
        step.click(pageObjects.omniNavigationLink);
        step.waitLoadPage(30);
        stepProxy.shouldContainTextInHeader("wikipedia", "Referer", "android");
    }
}