package magic.ui.utility;


import javax.swing.BorderFactory;
import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;

/**
 * Utility class for implementing consistent UI style/effects.
 *
 */
public final class MagicStyle {

    private MagicStyle() {}

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
            component.setBorder(BorderFactory.createLineBorder(getRolloverColor(), 2));
            component.setBackground(BG1);
        } else {
            component.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            component.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
            component.setBackground(BG2);
        }
    }

    /**
     * Convenience method to ensure we always get the latest theme.
     * <p>
     * It is not a good idea to store a reference to a theme since
     * it will not be updated if the theme is changed.
     */
    public static Theme getTheme() {
        return ThemeFactory.getInstance().getCurrentTheme();
    }

    public static Color getTranslucentColor(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static void refreshComponentStyle(final JComponent container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JComponent) {
                final JComponent widget = (JComponent)component;
                if (widget.getComponentCount() > 0) {
                    refreshComponentStyle(widget);
                }
                if (widget instanceof IThemeStyle) {
                    ((IThemeStyle)widget).refreshStyle();
                }
            }
        }
    }

    public static Color getRarityColor(final MagicCardDefinition cardDef) {
        final Theme theme = getTheme();
        switch (cardDef.getRarity()) {
            case 2: return theme.getColor(Theme.COLOR_UNCOMMON_FOREGROUND);
            case 3: return theme.getColor(Theme.COLOR_RARE_FOREGROUND);
            case 4: return theme.getColor(Theme.COLOR_RARE_FOREGROUND);
            default: return theme.getColor(Theme.COLOR_COMMON_FOREGROUND);
        }
    }

    public static Color getRolloverColor(Theme aTheme) {
        return aTheme.hasValue(Theme.COLOR_MOUSEOVER)
            ? aTheme.getColor(Theme.COLOR_MOUSEOVER)
            : GeneralConfig.getInstance().getRolloverColor();
    }

    public static Color getRolloverColor() {
        return getRolloverColor(getTheme());
    }

    public static Color getPressedColor() {
        return getRolloverColor().darker();
    }

  /**
   * Lightens a color by a given amount
   *
   * @param color The color to lighten
   * @param amount The amount to lighten the color. 0 will leave the color unchanged; 1 will make
   *        the color completely white
   * @return The bleached color
   */
    public static Color bleach1(Color color, float amount) {
        int red = (int) Math.min(255, color.getRed() + 255 * amount);
        int green = (int) Math.min(255, color.getGreen() + 255 * amount);
        int blue = (int) Math.min(255, color.getBlue() + 255 * amount);
        return new Color(red, green, blue, color.getAlpha());
    }

  /**
   * Lightens a color by a given amount
   *
   * @param color The color to lighten
   * @param amount The amount to lighten the color. 0 will leave the color unchanged; 1 will make
   *        the color completely white
   * @return The bleached color
   */
    public static Color bleach2(Color color, float amount) {
        int red = (int) ((color.getRed() * (1 - amount) / 255 + amount) * 255);
        int green = (int) ((color.getGreen() * (1 - amount) / 255 + amount) * 255);
        int blue = (int) ((color.getBlue() * (1 - amount) / 255 + amount) * 255);
        return new Color(red, green, blue, color.getAlpha());
    }

}
