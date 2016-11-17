import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

@AndroidFindBy(id = "com.yandex.browser:id/bro_suggest_wizard")
public class SuggestWizard{
    @AndroidFindBy(id = "com.yandex.browser:id/bro_common_omnibox_wizard_text")
    public AndroidElement wizardText;
}