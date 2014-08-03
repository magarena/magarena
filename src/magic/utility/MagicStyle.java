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
    private static Color BG1;
    private static Color BG2;
    private static Theme currentTheme = null;

    /**
     * Changes border color, background and mouse cursor for the specified component
     * to indicate that a mouse click will initiate some kind of action.
     */
    public static void setHightlight(final JComponent component, final boolean value) {
        if (currentTheme != ThemeFactory.getInstance().getCurrentTheme()) {
            currentTheme = ThemeFactory.getInstance().getCurrentTheme();
            final Color refBG = currentTheme.getColor(Theme.COLOR_TITLE_BACKGROUND);
            BG1 = new Color(refBG.getRed(), refBG.getGreen(), refBG.getBlue(), 200);
            BG2 = new Color(refBG.getRed(), refBG.getGreen(), refBG.getBlue(), 220);
        }
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
