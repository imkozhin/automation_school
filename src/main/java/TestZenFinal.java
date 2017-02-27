import io.appium.java_client.android.AndroidDriver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import ru.yandex.qatools.allure.annotations.Title;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ArrayList;

public class TestZenFinal {
    private AndroidDriver driver;
    private StepsForTests step;
    private AndroidProxy server;
    private PageObjects pageObjects;
    private List<Response> responseList;

    public final static String ZEN_EXPORT = "api/v2/android/export_ob";
    public final static String ZEN_MORE = "api/v2/android/more";
    public final static String ZEN_SIMILAR = "api/v2/android/similar";

    @Rule
    public AndroidCustomRule androidCustomRule = new AndroidCustomRule();

    @Before
    public void setUp() throws UnknownHostException{
        driver = androidCustomRule.getDriver();
        step = new StepsForTests(driver);
        pageObjects = new PageObjects(driver);
        server = new AndroidProxy();
        server.startProxy();
        step.copyProxyFile(server.getProxyServer().getPort());
        step.coldStartBrowser();
    }

    public static String fileToString(String path){
        StringBuffer stringBuffer = new StringBuffer();
        try{
            FileInputStream fileStream = new FileInputStream(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileStream, "UTF-8"));
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuffer.append(line);
                line = bufferedReader.readLine();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    @Title("Карточки похожих статей, по которым был осуществлен переход, не отмечаются прочтенным")
    @Test
    public void similarityCardNotChangeColorWhenRead() throws UnknownHostException {
        ArrayList<String> handleList = new ArrayList();
        handleList.add(ZEN_EXPORT);
        handleList.add(ZEN_MORE);
        handleList.add(ZEN_SIMILAR);

        ArrayList<String>jsonList = new ArrayList();
        jsonList.add(fileToString("resources\\zenCards.json"));
        jsonList.add(fileToString("resources\\zenSingleCard.json"));
        jsonList.add(fileToString("resources\\similar.json"));
        server.replaceJsonInResponse(handleList, jsonList);
        responseList = server.getResponseList();

        step.closeTutorial();
        step.checkZenTitle();
        step.swipeFromBottomToTop();
        step.clickToZenCard();
        step.waitSimilarResponce(responseList, ZEN_SIMILAR);
        step.checkOmniboxOnWebPage();
        step.tapBack();
        step.waitSimilarVisible();
        BufferedImage similarImageBeforeTap = step.getElementScreenshot(pageObjects.zenSimilarityCard.image);
        step.tapFirstSimilarityCard();
        step.checkOmniboxOnWebPage();
        step.tapBack();
        step.waitSimilarVisible();
        BufferedImage similarImageAfterTap = step.getElementScreenshot(pageObjects.zenSimilarityCard.image);
        Boolean expectedResult = true;
        step.compareBufferedImage(expectedResult, similarImageBeforeTap, similarImageAfterTap);
    }
}