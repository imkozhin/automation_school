import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import junit.framework.TestCase;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.matchers.decorators.MatcherDecorators;
import ru.yandex.qatools.matchers.decorators.MatcherDecoratorsBuilder;

import static java.net.InetAddress.getLocalHost;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertTrue;
import static ru.yandex.qatools.matchers.decorators.MatcherDecorators.timeoutHasExpired;

class StepsForTests {
    private PageObjects pageObjects;
    private AndroidDriver driver;
    Dimension size;

    public StepsForTests(AndroidDriver driver) {
        this.driver = driver;
        pageObjects = new PageObjects(driver);
    }

    @Attachment
    public byte[] saveScreenshot() {
        return driver.getScreenshotAs(OutputType.BYTES);
    }

    @Step("Взять скриншот элемента \"{0}\"")
    public BufferedImage getElementScreenshot(AndroidElement androidElement){
        BufferedImage bufferedImage = makeScreenshotAll();
        Point location = androidElement.getLocation();
        int width = androidElement.getSize().getWidth();
        int height = androidElement.getSize().getHeight();
        return bufferedImage.getSubimage(location.getX(), location.getY(), width, height);
    }

    @Step("Проверка цвета в историческом саджесте")
    public void checkColorInHistorySuggest(WebElement webElement, Color findColor1, Color findColor2 ){
        BufferedImage bufferedImage = makeScreenshotAll();
        Point location = webElement.getLocation();
        int width = webElement.getSize().getWidth();
        int height = webElement.getSize().getHeight();
        BufferedImage elementImage = bufferedImage.getSubimage(location.getX(), location.getY(), width, height);
        assertThat(elementImage, both(hasColor(findColor1)).and(hasColor(findColor2)));
    }

    @Step
    public void checkOmniboxOnWebPage() {
        WebDriverWait waitDriver = new WebDriverWait(driver, 10);
        try {
            waitDriver.until(ExpectedConditions.elementToBeClickable(pageObjects.omnibox_on_web_page));
        }
        catch (Exception s){
            failedDebugStep();
        }
    }

    @Step
    public void checkSuggestNotNull(int i) {
        assertThat(pageObjects.suggestList, hasSize(greaterThan(i)));
    }

    @Step
    public void checkWeatherWizard() {
        WebDriverWait waitDriver = new WebDriverWait(driver, 10);
        try {
            waitDriver.until(ExpectedConditions.elementToBeClickable(pageObjects.wizardText));
        }
        catch (Exception s){
            failedDebugStep();
        }
    }

    @Step
    public void checkZenTitle() {
        WebDriverWait waitDriver = new WebDriverWait(driver, 10);
        try {
            waitDriver.until(ExpectedConditions.elementToBeClickable(pageObjects.zen_title_and_body));
        }
        catch (Exception s){
            failedDebugStep();
        }
    }

    @Step("Tap on {0}")
    public void click(WebElement element){
        element.click();
    }

    @Step
    public void clickToOmnibox() {
        pageObjects.omnibox.click();
    }

    @Step
    public void clickToZenCard() {
        pageObjects.zen_card.click();
    }

    @Step
    public void closeTutorial() {
        WebDriverWait waitDriver = new WebDriverWait(driver, 10);
        try {
            waitDriver.until(ExpectedConditions.elementToBeClickable(pageObjects.omnibox));
        }
        catch (Exception s){
            waitDriver.until(ExpectedConditions.elementToBeClickable(pageObjects.closeTutorialButton)).click();
        }
    }

    @Step("Рестарт браузера")
    public void coldStartBrowser(){
        driver.resetApp();
    }

    @Step("Сравнение двух изображения ожидается: {0}")
    public void compareBufferedImage(Boolean expectedResult, BufferedImage image1, BufferedImage image2){
        assertThat(image1, compareImage(image2, expectedResult));
    }

    @Step("Копирую на устройство файл с настройками прокси. Прокси порт:{0}")
    public void copyProxyFile(int port) throws UnknownHostException {
        String fileString = "yandex --proxy-server=" + getLocalHost().getHostAddress() + ":" + port;
        driver.pushFile("/data/local/tmp/yandex-browser-command-line", fileString.getBytes());
    }

    @Step
    public void failedDebugStep() {
        assertTrue(false);
    }

    @Step("Беру у элемента \"{0}\" центральную точку по Х")
    public int getCenterX(AndroidElement element) {
        int leftX = element.getLocation().getX();
        int width = element.getSize().getWidth();
        return leftX + width / 2;
    }

    @Step("Беру у элемента \"{0}\" центральную точку по У")
    public int getCenterY(AndroidElement element) {
        int topY = element.getLocation().getY();
        int height = element.getSize().getHeight();
        return topY + height / 2;
    }

    @Step
    public void sendKeys(String string) {
        pageObjects.omniboxEditText.sendKeys(string);
    }

    @Step
    public void suggestClick(int suggestNumber){
        pageObjects.suggestThirdElement.get(2).click();
    }

    @Step
    public void swipeFromBottomToTop() {
        size = driver.manage().window().getSize();
        int starty = (int) (size.height * 0.80);
        int endy = (int) (size.height * 0.20);
        int startx = size.width / 2;
        driver.swipe(startx, starty, startx, endy, 1000);
    }

    @Step("Тап BACK")
    public void tapBack(){
        driver.pressKeyCode(AndroidKeyCode.BACK);
    }

    @Step("Тап по превой карточке похожих")
    public void tapFirstSimilarityCard(){
        int CenterY = getCenterY(pageObjects.zenSimilarityCard);
        int CenterX = getCenterX(pageObjects.zenSimilarityCard);
        driver.tap(1, CenterX, CenterY, 100);
    }

    @Step
    public void tapOmniboxDeleteButton() {
        pageObjects.omniboxDeleteButton.click();
    }

    @Step
    public void tapOmniboxOnWebPage() {
        pageObjects.omniboxWebPage.click();
    }

    @Step
    public void waitLoadPage(int waitTime){
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
            //Запускаю поиск. если находим "url opened" то выходим
            if(lp.FindStringInLog("url opened")){
                i=3;
                pageLoad = true;
            }
            else
                i++;
        }
        TestCase.assertTrue(pageLoad);
    }

    @Step("Ждём ответа ручки Similar")
    public void waitSimilarResponce(List<Response> responseList, String similarUrl){
        assertThat(responseList, withWaitFor(hasItem(hasResponceURL(similarUrl)), 10000));
    }

    @Step("Ждём появления похожих")
    public void waitSimilarVisible(){
        assertThat("Нет похожих", exists(pageObjects.similarityText));
    }

    //ниже идут внутренние методы

    private BufferedImage makeScreenshotAll() {
        File screenshot = driver.getScreenshotAs(OutputType.FILE);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(screenshot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return bufferedImage;
        }
    }

    public static Color findWebElementColor(BufferedImage bufferedImage, Color color){
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        for(int y= 0; y<height; y++ ){
            for(int x= 0; x<width; x++){
                Color takenColor = new Color(bufferedImage.getRGB(x, y));
                if(takenColor.getRGB() == color.getRGB()) {
                    return takenColor;
                }
            }
        }
        return null;
    }

    static Matcher<BufferedImage> hasColor(final Color color) {
        return new FeatureMatcher<BufferedImage, Color>(equalTo(color), "Элемент имеет цвет - ", "цвет -") {
            @Override
            protected Color featureValueOf(BufferedImage bufferedImage) {
                return findWebElementColor(bufferedImage, color);
            }
        };
    }

    public static Matcher<BufferedImage> compareImage(final BufferedImage image, Boolean expectedResult) {
        return new FeatureMatcher<BufferedImage, Boolean>(equalTo(expectedResult), "expected - ", "actual - ") {
            @Override
            protected Boolean featureValueOf(BufferedImage actualImage) {
                if (actualImage.getWidth() == image.getWidth() && actualImage.getHeight() == image.getHeight()) {
                    for (int x = 0; x < actualImage.getWidth(); x++) {
                        for (int y = 0; y < image.getHeight(); y++) {
                            if (actualImage.getRGB(x, y) != image.getRGB(x, y))
                                return false;
                        }
                    }
                } else {
                    return false;
                }
                return true;
            }
        };
    }

    public static boolean exists(AndroidElement mainElement){
        try {
            mainElement.isDisplayed();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    //Стырил у автоматизаторов
    public static <T> MatcherDecoratorsBuilder withWaitFor(Matcher<? super T> matcher, long ms) {
        return MatcherDecorators.should(matcher).whileWaitingUntil(timeoutHasExpired(ms));
    }

    static Matcher<Response> hasResponceURL(final String item) {
        return new FeatureMatcher<Response, Boolean>(equalTo(true), "expected - ", "actual - ") {
            @Override
            protected Boolean featureValueOf(Response actual) {
                return actual.getResponseURL().toString().contains(item);
            }
        };
    }
}