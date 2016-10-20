package magic.ui.helpers;

import java.awt.Color;

public final class ColorHelper {

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


    private ColorHelper() {}
}
