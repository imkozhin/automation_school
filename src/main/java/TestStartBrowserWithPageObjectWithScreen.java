import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static junit.framework.TestCase.assertTrue;

public class TestStartBrowserWithPageObjectWithScreen {

    private AppiumDriver driver;
    PageObjects pageObjects;

    @Before
    public void setup() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName","Emulator");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appPackage", "com.yandex.browser");
        capabilities.setCapability("appActivity", "YandexBrowserActivity");
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
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

    @Test
    public void startBrowserWithPageObjectWithScreenAndTapThirdSuggestResult() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(pageObjects.omnibox));
        }
        catch (Exception s){
            wait.until(ExpectedConditions.elementToBeClickable(pageObjects.closeTutorialButton)).click();
        }

        pageObjects.omnibox.click();
        pageObjects.omniboxEditText.sendKeys("cats");
        pageObjects.suggestThirdElement.get(2).click();

        //Определение загрузки страницы по логам
        Date startTime = new Date();    //фиксирую время тапа
        List<LogEntry> logEntryList;    //массив для хранения логов
        boolean pageLoad = false;       //индикатор загрузки страницы

        //проверяю логи на наличие события "url opened"
        //Проверять буду 3 раза с интервалом 5 секунд
        for(int i = 0; i < 3; i++) {

            WebDriverWait wd = new WebDriverWait(driver, 5);
            try {
                //Пытаемся найти несуществующий элимент i секунд
                wd.until(ExpectedConditions.elementToBeClickable(By.id("NONEXISTENT ELEMENT")));
            }
            catch (Exception s){
                //Ничего не делаем
            }

            //беру у драйвера логи
            logEntryList = driver.manage().logs().get("logcat").filter(Level.ALL);

            //Создаю обьект LogParser передаю в него массив логов и время тапа
            LogsParser lp = new LogsParser(logEntryList, startTime);
            //Запускаю поиск: если находим "url opened", то выходим
            if(lp.FindStringInLog("url opened")){
                i=3;
                pageLoad = true;
            }
            else
                i++;
        }
        assertTrue(pageLoad);
    }

}