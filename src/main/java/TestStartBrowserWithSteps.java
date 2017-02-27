import io.appium.java_client.android.AndroidDriver;
import java.io.File;
import java.net.UnknownHostException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import ru.yandex.qatools.allure.annotations.Title;

public class TestStartBrowserWithSteps {
    private AndroidDriver driver;
    private StepsForTests step;

    @Rule
    public AndroidCustomRule androidCustomRule = new AndroidCustomRule();

    @Before
    public void setUp() throws UnknownHostException {
        driver = androidCustomRule.getDriver();
        step = new StepsForTests(driver);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Rule
    public TestWatcher testWatcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            String screenshotName = description.getMethodName() + ".png";
            String screenshotPath = "C:\\ErrorScreenshot/";

            try {
                File scFile = driver.getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scFile, new File(screenshotPath + screenshotName));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        @Override
        protected void finished(Description description) {
            driver.quit();
        }
    };

    @Title("Успешный тест")
    @Test
    public void testSuggest1() throws Exception {
        step.closeTutorial();
        step.clickToOmnibox();
        step.sendKeys("cats");
        step.suggestClick(3);
        step.waitLoadPage(30);
    }

    @Title("Неуспешный тест")
    @Test
    public void testSuggest2() throws Exception {
        step.closeTutorial();
        step.clickToOmnibox();
        step.sendKeys("cats");
        step.suggestClick(3);
        step.waitLoadPage(30);
        step.failedDebugStep();
    }
}