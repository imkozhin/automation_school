import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.hamcrest.CoreMatchers.is;

public class CustomMatcher{
    public static Matcher<BufferedImage> hasColor(final Color expectedColor) {
        return new FeatureMatcher<BufferedImage, Boolean>(is(true), "Color " + expectedColor, "Color " + expectedColor){
            @Override
            protected Boolean featureValueOf(BufferedImage elementScreenshot) {
                return findColor(elementScreenshot, expectedColor);
            }
        };
    }

    static boolean findColor(BufferedImage elementScreenshot, Color expectedColor) {
        for (int x = 0; x < elementScreenshot.getWidth(); x++) {
            for (int y = 0; y < elementScreenshot.getHeight(); y++) {
                int rgb = elementScreenshot.getRGB(x, y);
                if (rgb == expectedColor.getRGB()) {
                    return true;
                }
            }
        }
        return false;
    }
}