package magic.utility;

import magic.ui.widget.FontsAndBorders;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Cursor;

/**
 * Utility class for implementing consistent UI style/effects.
 *
 */
public final class MagicStyle {
    private MagicStyle() {}

    public static Color HIGHLIGHT_COLOR = Color.YELLOW;

    /**
     * Changes border color, background and mouse cursor for the specified component
     * to indicate that a mouse click will initiate some kind of action.
     */
    public static void setHightlight(final JComponent component, final boolean value) {
        if (value == true) {
            component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            component.setBorder(BorderFactory.createLineBorder(HIGHLIGHT_COLOR, 2));
            component.setBackground(FontsAndBorders.MENUPANEL_COLOR);
        } else {
            component.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            component.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
            component.setBackground(FontsAndBorders.MAGSCREEN_BAR_COLOR);
        }
    }

}
