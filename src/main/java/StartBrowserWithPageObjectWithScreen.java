import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import io.appium.java_client.AppiumDriver;
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

public class StartBrowserWithPageObjectWithScreen {

    private AppiumDriver driver;
    PageObject PageObject;
    private static final String screenshotPath = "C:\\AppiumProject\\ErrorScreenshot";

    @Before
    public void setup() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName","Emulator");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appPackage", "com.yandex.browser");
        capabilities.setCapability("appActivity", "YandexBrowserActivity");
        driver = new AppiumDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        PageObject = new PageObject(driver);
    }

    @After
    public void tearDown() throws Exception{
    }

    @Rule
    public TestWatcher testWatcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            String screenshotName = description.getMethodName() + ".png";
            try {
                File scFile = driver.getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scFile, new File("C:\\ErrorsScreenshots/" + screenshotName));
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
    public void test() throws Exception {

        //создаю объект WebDriverWait с его помощью сделаю ожидание событий.
        WebDriverWait wait = new WebDriverWait(driver, 10);

        try {
            //Если не нахожу омнибокс значит появилось окно приветствия.
            wait.until(ExpectedConditions.elementToBeClickable(PageObject.omnibox));
        }
        catch (Exception s){
            //закрываю экран приветствия если появился
            wait.until(ExpectedConditions.elementToBeClickable(PageObject.closeTutorialButton)).click();
        }

        //тап в омнибокс
        PageObject.omnibox.click();

        //ввожу cats в строку поиска
        PageObject.omniboxEditText.sendKeys("cats");

        //нахожу 3 строку в саджесте и тапаю
        PageObject.suggestThirdElement.get(2).click();

        //Определение загрузки страницы по логам
        Date starttime = new Date();    //фиксирую время тапа
        List<LogEntry> logEntryList;    //массив для хранения логов
        boolean pageload = false;       //индикатор загрузки страницы

        //проверяю логи на наличие события "url opened"
        //Проверять буду 3 раза с интервалом 5 секунд
        for(int i = 0; i < 3; i++) {

            //Жду 5 секунд
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
            LogsParser lp = new LogsParser(logEntryList, starttime);
            //Запускаю поиск. если находим "url opened" то выходим
            if(lp.FindStringInLog("url opened")){
                i=3;
                pageload = true;
            }
            else
                i++;
        }
        //Проверка
        assertTrue(pageload);
    }

}