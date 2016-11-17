import com.google.common.collect.Lists;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

class PageObject {

    public PageObject(WebDriver driver){
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public static SuggestHistory historySuggest;
    public static SuggestWizard wizardSuggest;

    //Омнибокс
    @AndroidFindBy(id = "bro_sentry_bar_fake")
    public WebElement omnibox;

    //Кнопка закрытия туториала
    @AndroidFindBy(id = "activity_tutorial_close_button")
    public WebElement closeTutorialButton;

    //Строка ввода в омнибоксе
    @AndroidFindBy(id = "bro_sentry_bar_input_edittext")
    public WebElement omniboxEditText;

    //3 элемент саджеста
    @AndroidFindBy(id = "bro_common_omnibox_text_layout")
    public List<WebElement> suggestThirdElement;
    public List<WebElement> suggestList() {
        List<WebElement> suggestList = Lists.reverse(suggestThirdElement);
        return suggestList;
    }

    //Cаджест
    @AndroidFindBy(id = "bro_common_omnibox_text_layout")
    public List<WebElement> suggestList;

    //Омнибокс на вкладке
    @AndroidFindBy(id = "bro_omnibar_address_title_text")
    public WebElement omniboxWebPage;

    //Кнопка удалить в омнибоксе
    @AndroidFindBy(id = "bro_sentry_bar_input_button")
    public WebElement omniboxDeleteButton;

    @AndroidFindBy(id = "bro_sentry_bar_input_blue_link")
    public AndroidElement omniNavigationLink;

}
