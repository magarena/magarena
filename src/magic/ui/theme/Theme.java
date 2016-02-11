package magic.ui.theme;


import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public interface Theme {

    String TEXTURE_BACKGROUND="texture_background";
    String TEXTURE_COMPONENT="texture_component";
    String TEXTURE_BATTLEFIELD="texture_battlefield";
    String TEXTURE_PLAYER="texture_player";
    String TEXTURE_HAND="texture_hand";

    String ICON_SMALL_BATTLEFIELD="icon_small_battlefield";
    String ICON_SMALL_COMBAT="icon_small_combat";
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
    String COLOR_GAME_BORDER="color_game_border";

    String VALUE_SPACING="value_spacing";
    String VALUE_BACKGROUND_STRETCH="value_background_stretch";
    String VALUE_GAME_LAYOUT="value_game_layout";
    String VALUE_GAME_STRETCH="value_game_stretch";
    String VALUE_GAME_OFFSET="value_game_offset";
    String VALUE_GAME_BORDER="value_game_border";

    void load();

    String getName();

    BufferedImage getTexture(final String name);

    ImageIcon getIcon(final String name);

    Color getColor(final String name);

    Color getTextColor();

    Boolean getOptionUseOverlay();

    Color getChoiceColor();

    int getValue(final String name);

    ImageIcon getAvatarIcon(final int index,final int size);

    ImageIcon getAbilityIcon(final AbilityIcon ability);

    BufferedImage getBackgroundImage();
}
