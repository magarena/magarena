package magic.ui.theme;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class DefaultTheme extends AbstractTheme {

    public DefaultTheme(final String name,final BufferedImage background,final BufferedImage component,final Color textColor) {

        super(name);

        addToTheme(TEXTURE_BACKGROUND,background);
        addToTheme(TEXTURE_COMPONENT,component);
        addToTheme(TEXTURE_BATTLEFIELD,background);
        addToTheme(TEXTURE_PLAYER,background);
        addToTheme(TEXTURE_HAND,background);

        addToTheme(COLOR_TEXT_FOREGROUND,textColor);
        addToTheme(COLOR_NAME_FOREGROUND,textColor);
    }
}
