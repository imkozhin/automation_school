import com.google.common.collect.Lists;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

class PageObjects {
    public PageObjects(WebDriver driver){
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    //омнибокс на ПЦ
    @AndroidFindBy(id = "bro_sentry_bar_fake")
    public WebElement omnibox;

    //кнопка закрытия туториала
    @AndroidFindBy(id = "activity_tutorial_close_button")
    public WebElement closeTutorialButton;

    //строка ввода в омнибоксе
    @AndroidFindBy(id = "bro_sentry_bar_input_edittext")
    public WebElement omniboxEditText;

    //саджест (3й элемент)
    @AndroidFindBy(id = "bro_common_omnibox_text_layout")
    public List<WebElement> suggestThirdElement;
    public List<WebElement> suggestList() {
        List<WebElement> suggestList = Lists.reverse(suggestThirdElement);
        return suggestList;
    }

    //саджест
    @AndroidFindBy(id = "bro_common_omnibox_text_layout")
    public List<WebElement> suggestList;

    //омнибокс на вкладке
    @AndroidFindBy(id = "bro_omnibar_address_title_text")
    public WebElement omniboxWebPage;

    //кнопка удалить в омнибоксе
    @AndroidFindBy(id = "bro_sentry_bar_input_button")
    public WebElement omniboxDeleteButton;

    //исторический саджест
    @AndroidFindBy(id = "bro_suggest_history")
    public WebElement historySuggest;

    //иконка и текст погода
    @AndroidFindBy(id = "bro_common_omnibox_wizard_text")
    public AndroidElement wizardText;

    //навигационник в омнибоксе
    @AndroidFindBy(id = "bro_sentry_bar_input_blue_link")
    public AndroidElement omniNavigationLink;

    //заголовок группы карточек похожих Дзена
    @AndroidFindBy(id = "zen_ribbon_text_similarity_card")
    public AndroidElement similarityText;

    //карточка похожих Дзена
    @AndroidFindBy(id = "zen_ribbon_image_similarity_card")
    public ZenSimilarityCard zenSimilarityCard;

    //заголовок карточки Дзена
    @AndroidFindBy(id = "zen_title_and_body")
    public WebElement zen_title_and_body;

    //карточка Дзена
    @AndroidFindBy(id = "zen_ribbon_image_card")
    public WebElement zen_card;

    //омнибокс на веб-вкладке
    @AndroidFindBy(id = "bro_omnibox")
    public WebElement omnibox_on_web_page;
}