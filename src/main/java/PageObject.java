import com.google.common.collect.Lists;
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

}
