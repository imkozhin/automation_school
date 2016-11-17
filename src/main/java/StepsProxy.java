import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarNameValuePair;
import ru.yandex.qatools.allure.annotations.Step;

import static org.hamcrest.MatcherAssert.assertThat;

public class StepsProxy {
    private BrowserMobProxy proxy;

    public StepsProxy(BrowserMobProxy proxy) {
        this.proxy = proxy;
    }

    @Step("Request to: {0} should contain text: {2} in Header: {1}")
    public void shouldContainTextInHeader(String url, String header, String text) throws Exception {
        Har har = proxy.getHar();
        for (HarEntry entry : har.getLog().getEntries()) {
            if (entry.getRequest().getUrl().contains(url)) {
                for (HarNameValuePair headerPair : entry.getRequest().getHeaders()) {
                    if (headerPair.getName().equals(header)) {
                        assertThat(text + " not found in Header " + header, headerPair.getValue().contains(text));
                        return;
                    }
                }
            }
        }
    }
}