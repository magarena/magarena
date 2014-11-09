package magic.ui.theme;

import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.image.BufferedImage;

public interface Theme {

    String TEXTURE_LOGO="texture_logo";
    String TEXTURE_BACKGROUND="texture_background";
    String TEXTURE_COMPONENT="texture_component";
    String TEXTURE_BATTLEFIELD="texture_battlefield";
    String TEXTURE_PLAYER="texture_player";
    String TEXTURE_HAND="texture_hand";

    String ICON_LIFE="icon_life";
    String ICON_PREVENT="icon_prevent";
    String ICON_POISON="icon_poison";
    String ICON_LAND="icon_land";
    String ICON_HAND="icon_hand";
    String ICON_LIBRARY="icon_library";
    String ICON_GRAVEYARD="icon_graveyard";
    String ICON_MESSAGE="icon_message";
    String ICON_SMALL_BATTLEFIELD="icon_small_battlefield";
    String ICON_SMALL_COMBAT="icon_small_combat";
    String ICON_SMALL_STACK="icon_small_stack";
    String ICON_SMALL_HAND="icon_small_hand";
    String ICON_SMALL_GRAVEYARD="icon_small_graveyard";
    String ICON_SMALL_EXILE="icon_small_exile";

    String COLOR_TITLE_FOREGROUND="color_title_foreground";
    String COLOR_TITLE_BACKGROUND="color_title_background";
    String COLOR_ICON_BACKGROUND="color_icon_background";
    String COLOR_TEXT_FOREGROUND="color_text_foreground";
    String COLOR_NAME_FOREGROUND="color_name_foreground";
    String COLOR_CHOICE_FOREGROUND="color_choice_foreground";
    String COLOR_COMMON_FOREGROUND="color_common_foreground";
    String COLOR_UNCOMMON_FOREGROUND="color_uncommon_foreground";
    String COLOR_RARE_FOREGROUND="color_rare_foreground";
    String OPTION_USE_OVERLAY="option_use_overlay";
    String COLOR_CHOICE="color_choice";
    String COLOR_COMBAT_CHOICE="color_combat_choice";
    String COLOR_CHOICE_BORDER="color_choice_border";
    String COLOR_COMBAT_CHOICE_BORDER="color_combat_choice_border";
    String COLOR_SELECTED_PLAYER="color_selected_player";
    String COLOR_GAME_BORDER="color_game_border";
    String COLOR_VIEWER_BACKGROUND="color_viewer_background";
    String COLOR_SEPARATOR_BACKGROUND="color_separator_background";

    String VALUE_SPACING="value_spacing";
    String VALUE_BACKGROUND_STRETCH="value_background_stretch";
    String VALUE_GAME_LAYOUT="value_game_layout";
    String VALUE_GAME_STRETCH="value_game_stretch";
    String VALUE_GAME_OFFSET="value_game_offset";
    String VALUE_GAME_BORDER="value_game_border";

    void load();

    String getName();

    BufferedImage getTexture(final String name);

    BufferedImage getLogoTexture();

    ImageIcon getIcon(final String name);

    Color getColor(final String name);

    Color getTextColor();

    Color getNameColor();

    Boolean getOptionUseOverlay();

    Color getChoiceColor();

    int getValue(final String name);

    int getNumberOfAvatars();

    ImageIcon getAvatarIcon(final int index,final int size);

    ImageIcon getAbilityIcon(final AbilityIcon ability);
}
