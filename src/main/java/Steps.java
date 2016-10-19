import io.appium.java_client.AppiumDriver;
import junit.framework.TestCase;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import static org.junit.Assert.assertTrue;

class Steps {

    private PageObject PageObject;

    public AppiumDriver driver;

    public Steps (AppiumDriver driver) {

        this.driver = driver;
        PageObject = new PageObject(driver);
    }

    @Step
    public void closeTutorial() {

        WebDriverWait waitDriver = new WebDriverWait(driver, 10);

        try {
            waitDriver.until(ExpectedConditions.elementToBeClickable(PageObject.omnibox));
        }
        catch (Exception s){
            waitDriver.until(ExpectedConditions.elementToBeClickable(PageObject.closeTutorialButton)).click();
        }
    }

    @Step
    public void clickToOmnibox() {
        PageObject.omnibox.click();
    }

    @Step
    public void sendKeys(String string) {
        PageObject.omniboxEditText.sendKeys(string);
    }

    @Step
    public void suggestClick(int suggestNumber){

        PageObject.suggestThirdElement.get(2).click();
    }

    @Step
    public void waitLoadPage(int waitTime){

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
        TestCase.assertTrue(pageload);
    }

    //Степ для отладки
    @Step
    public void failedDebugStep() {
        assertTrue(false);
    }

    @Attachment(type = "image/png")
    public byte[] makeScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}