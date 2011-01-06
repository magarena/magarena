package magic.ui.theme;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public interface Theme {

	public static final String TEXTURE_BACKGROUND="texture_background";
	public static final String TEXTURE_GAME="texture_game";
	public static final String TEXTURE_COMPONENT="texture_component";
	
	public static final String ICON_LIFE="icon_life";
	public static final String ICON_PREVENT="icon_prevent";
	public static final String ICON_LAND="icon_land";
	public static final String ICON_HAND="icon_hand";
	public static final String ICON_LIBRARY="icon_library";
	public static final String ICON_GRAVEYARD="icon_graveyard";

	public static final String COLOR_TITLE_FOREGROUND="color_title_foreground";
	public static final String COLOR_TITLE_BACKGROUND="color_title_background";
	public static final String COLOR_TEXT_FOREGROUND="color_text_foreground";
	public static final String COLOR_NAME_FOREGROUND="color_name_foreground";
	public static final String COLOR_CHOICE_FOREGROUND="color_choice_foreground";
	public static final String COLOR_COMMON_FOREGROUND="color_common_foreground";
	public static final String COLOR_UNCOMMON_FOREGROUND="color_uncommon_foreground";
	public static final String COLOR_RARE_FOREGROUND="color_rare_foreground";
	public static final String COLOR_CHOICE="color_choice";
	public static final String COLOR_COMBAT_CHOICE="color_combat_choice";
	
	public String getName();
	
	public BufferedImage getTexture(final String name);
	
	public ImageIcon getIcon(final String name);
	
	public Color getColor(final String name);
	
	public Color getTextColor();
	
	public Color getNameColor();
	
	public Color getChoiceColor();
}