import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.google.common.collect.Lists;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static junit.framework.TestCase.assertTrue;


public class StartBrowser {
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
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void test() throws Exception {

        //создаю объект WebDriverWait с его помощью сделаю ожидание событий.
        WebDriverWait wait = new WebDriverWait(driver, 10);

        try {
            //Если не нахожу омнибокс значит появилось окно приветствия.
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bro_sentry_bar_fake")));
        }
        catch (Exception s){
            //закрываю экран приветствия если появился
            wait.until(ExpectedConditions.elementToBeClickable(By.id("activity_tutorial_close_button"))).click();
        }
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bro_sentry_bar_fake")));

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
            logEntryList = (List<LogEntry>) driver.manage().logs().get("logcat").filter(Level.ALL);

            //Создаю обьект LogParser передаю в него массив логов и время тапа
            LogsParser lp = new LogsParser(logEntryList, starttime);
            //Запускаю поиск. если находим "url opened" то выходим
            if(lp.FindStringInLog("url opened")){
                i=3;
                pageload =true;
            }
            else
                i++;
        }
        //Проверка
        assertTrue(pageload);
    }

}
