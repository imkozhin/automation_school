import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

@AndroidFindBy(id = "com.yandex.browser:id/bro_suggest_search_history")
public class SuggestHistory{
    @AndroidFindBy(id = "com.yandex.browser:id/bro_common_omnibox_text_layout")
    public AndroidElement suggestText;

    @AndroidFindBy(id = "com.yandex.browser:id/bro_common_omnibox_image")
    public AndroidElement clockIcon;
}
