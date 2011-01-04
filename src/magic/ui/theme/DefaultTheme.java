package magic.ui.theme;

import java.awt.Color;
import java.awt.image.BufferedImage;

import magic.data.IconImages;

public class DefaultTheme extends AbstractTheme {

	public DefaultTheme(final String name,final BufferedImage background,final BufferedImage component) {

		super(name);
		
		addToTheme(TEXTURE_BACKGROUND,background);
		addToTheme(TEXTURE_COMPONENT,component);
		
		addToTheme(ICON_LIFE,IconImages.LIFE);
		addToTheme(ICON_PREVENT,IconImages.PREVENT2);
		addToTheme(ICON_LAND,IconImages.LAND2);
		addToTheme(ICON_HAND,IconImages.HAND2);
		addToTheme(ICON_LIBRARY,IconImages.LIBRARY2);
		addToTheme(ICON_GRAVEYARD,IconImages.GRAVEYARD2);
		
		addToTheme(COLOR_TITLE_FOREGROUND,Color.WHITE);
		addToTheme(COLOR_TITLE_BACKGROUND,new Color(0x23,0x6B,0x8E));
	}
}