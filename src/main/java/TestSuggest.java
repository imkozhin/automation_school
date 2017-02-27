import io.appium.java_client.android.AndroidDriver;
import java.awt.*;
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

public class TestSuggest {
    private AndroidDriver driver;
    private StepsForTests step;
    private PageObjects pageObjects;

    @Rule
    public AndroidCustomRule androidCustomRule = new AndroidCustomRule();

    @Before
    public void setUp() throws UnknownHostException {
        driver = androidCustomRule.getDriver();
        step = new StepsForTests(driver);
        pageObjects = new PageObjects(driver);
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

    @Title("Проверка наличия саджеста")
    @Test
    public void checkSuggestNotNull() throws Exception {
        step.closeTutorial();
        step.clickToOmnibox();
        step.sendKeys("cats");
        step.checkSuggestNotNull(1);
    }

    @Title("Проверка цвета в историческом саджесте")
    @Test
    public void checkSuggestTextColor() throws Exception {
        step.closeTutorial();
        step.clickToOmnibox();
        step.sendKeys("cats");
        step.suggestClick(1);
        step.tapOmniboxOnWebPage();
        step.tapOmniboxDeleteButton();
        step.sendKeys("cats");
        Color color1 = new Color(148, 148, 148);
        Color color2 = new Color(86, 28, 140);
        step.checkColorInHistorySuggest(pageObjects.historySuggest, color1, color2);
    }

    @Title("Проверка колдунщика погоды")
    @Test
    public void checkTextSuggestWizard() throws Exception {
        step.closeTutorial();
        step.clickToOmnibox();
        step.sendKeys("pogoda");
        step.checkWeatherWizard();
    }
}