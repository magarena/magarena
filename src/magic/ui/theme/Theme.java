package magic.ui.theme;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public interface Theme {

	public static final String TEXTURE_BACKGROUND="texture_background";
	public static final String TEXTURE_COMPONENT="texture_component";
	public static final String TEXTURE_BATTLEFIELD="texture_battlefield";
	public static final String TEXTURE_PLAYER="texture_player";
	public static final String TEXTURE_HAND="texture_hand";

	public static final String ICON_LIFE="icon_life";
	public static final String ICON_PREVENT="icon_prevent";
	public static final String ICON_LAND="icon_land";
	public static final String ICON_HAND="icon_hand";
	public static final String ICON_LIBRARY="icon_library";
	public static final String ICON_GRAVEYARD="icon_graveyard";
	public static final String ICON_MESSAGE="icon_message";
	public static final String ICON_SMALL_BATTLEFIELD="icon_small_battlefield";
	public static final String ICON_SMALL_COMBAT="icon_small_combat";
	public static final String ICON_SMALL_STACK="icon_small_stack";
	public static final String ICON_SMALL_HAND="icon_small_hand";
	public static final String ICON_SMALL_GRAVEYARD="icon_small_graveyard";
	public static final String ICON_SMALL_EXILE="icon_small_exile";

	public static final String COLOR_TITLE_FOREGROUND="color_title_foreground";
	public static final String COLOR_TITLE_BACKGROUND="color_title_background";
	public static final String COLOR_ICON_BACKGROUND="color_icon_background";
	public static final String COLOR_TEXT_FOREGROUND="color_text_foreground";
	public static final String COLOR_NAME_FOREGROUND="color_name_foreground";
	public static final String COLOR_CHOICE_FOREGROUND="color_choice_foreground";
	public static final String COLOR_COMMON_FOREGROUND="color_common_foreground";
	public static final String COLOR_UNCOMMON_FOREGROUND="color_uncommon_foreground";
	public static final String COLOR_RARE_FOREGROUND="color_rare_foreground";
	public static final String COLOR_CHOICE="color_choice";
	public static final String COLOR_COMBAT_CHOICE="color_combat_choice";
	public static final String COLOR_SELECTED_PLAYER="color_selected_player";

	public static final String VALUE_SPACING="value_spacing";
	public static final String VALUE_GAME_LAYOUT="value_game_layout";
	public static final String VALUE_GAME_STRETCH="value_game_stretch";
	public static final String VALUE_GAME_OFFSET="value_game_offset";
	
	public void load();
	
	public String getName();
	
	public BufferedImage getTexture(final String name);
	
	public ImageIcon getIcon(final String name);
	
	public Color getColor(final String name);
	
	public Color getTextColor();
	
	public Color getNameColor();
	
	public Color getChoiceColor();
	
	public int getValue(final String name);
}