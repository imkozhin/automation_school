import com.google.common.collect.Lists;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static junit.framework.TestCase.assertTrue;

public class TestStartBrowser {
    WebDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName","Emulator");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appPackage", "com.yandex.browser");
        capabilities.setCapability("appActivity", "YandexBrowserActivity");
        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void startBrowserAndTapThirdSuggestResult() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            //Если не нахожу омнибокс, значит появился туториал
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bro_sentry_bar_fake")));
        }
        catch (Exception s){
            //закрываю туториал
            wait.until(ExpectedConditions.elementToBeClickable(By.id("activity_tutorial_close_button"))).click();
        }

        //тап в омнибокс
        WebElement arrow = driver.findElement(By.id("bro_sentry_bar_fake_text"));
        arrow.click();

        //ввожу cats в строку поиска
        WebElement arrowEdit = driver.findElement(By.id("bro_sentry_bar_input_edittext"));
        arrowEdit.sendKeys("cats");

        //нахожу 3 строку в саджесте
        List<WebElement> suggestList = driver.findElements(By.id("bro_common_omnibox_text_layout"));
        Lists.reverse(suggestList).get(2).click();

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