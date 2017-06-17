package magic.ui.screen.menu;

import java.awt.Font;
import javax.swing.AbstractAction;
import magic.awt.MagicFont;
import magic.data.GeneralConfig;
import magic.translate.MText;
import magic.ui.FontsAndBorders;

@SuppressWarnings("serial")
class CustomMenuButton extends MenuButton {

    private static final Font CUSTOM_FONT = MagicFont.JaceBelerenBold.get().deriveFont(32f);
    private static final Font DEFAULT_FONT = FontsAndBorders.FONT_MENU_BUTTON.deriveFont(30.0f);

    CustomMenuButton(String caption, AbstractAction action, String tooltip, boolean showSeparator) {
        super(caption, action, tooltip);
        setFont(getDisplayFont());
    }

    CustomMenuButton(String caption, AbstractAction action, String tooltip) {
        this(caption, action, tooltip, true);
    }

    CustomMenuButton(String caption, AbstractAction action) {
        this(caption, action, null);
    }

    public static Font getDisplayFont() {
        return MText.canUseCustomFonts() && GeneralConfig.getInstance().useCustomFonts()
            ? CUSTOM_FONT
            : DEFAULT_FONT;
    }

}
