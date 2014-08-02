package magic.utility;


import javax.swing.BorderFactory;
import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Cursor;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;

/**
 * Utility class for implementing consistent UI style/effects.
 *
 */
public final class MagicStyle {
    private MagicStyle() {}

    public static Color HIGHLIGHT_COLOR = Color.YELLOW;
    private static final Theme THEME = ThemeFactory.getInstance().getCurrentTheme();
    private static final Color refBG = THEME.getColor(Theme.COLOR_TITLE_BACKGROUND);
    private static final Color BG1 = new Color(refBG.getRed(), refBG.getGreen(), refBG.getBlue(), 200);
    private static final Color BG2 = new Color(refBG.getRed(), refBG.getGreen(), refBG.getBlue(), 220);

    /**
     * Changes border color, background and mouse cursor for the specified component
     * to indicate that a mouse click will initiate some kind of action.
     */
    public static void setHightlight(final JComponent component, final boolean value) {
        if (value == true) {
            component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            component.setBorder(BorderFactory.createLineBorder(HIGHLIGHT_COLOR, 2));
            component.setBackground(BG1);
        } else {
            component.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            component.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
            component.setBackground(BG2);
        }
    }

}
