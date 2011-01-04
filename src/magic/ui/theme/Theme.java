package magic.ui.theme;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public interface Theme {

	public static final String TEXTURE_BACKGROUND="texture_background";
	public static final String TEXTURE_COMPONENT="texture_component";
	
	public static final String ICON_LIFE="icon_life";
	public static final String ICON_PREVENT="icon_prevent";
	public static final String ICON_LAND="icon_land";
	public static final String ICON_HAND="icon_hand";
	public static final String ICON_LIBRARY="icon_library";
	public static final String ICON_GRAVEYARD="icon_graveyard";

	public static final String COLOR_TITLE_FOREGROUND="color_title_foreground";
	public static final String COLOR_TITLE_BACKGROUND="color_title_background";
	
	public String getName();
	
	public BufferedImage getTexture(final String name);
	
	public ImageIcon getIcon(final String name);
	
	public Color getColor(final String name);
}