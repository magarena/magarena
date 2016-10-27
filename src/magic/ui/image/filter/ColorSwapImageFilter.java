package magic.ui.image.filter;

import java.awt.Color;

/**
 * Can be used to swap {@code oldColor} for a {@code newColor}.
 * Most effective with the monochrome icons used in Magarena's UI.
  */
public class ColorSwapImageFilter extends MagicRGBImageFilter {

    private final float[] filter;
    private final int[] rgbAdj;

    public ColorSwapImageFilter(Color oldColor, Color newColor) {
        this.rgbAdj = new int[]{
            255 - oldColor.getRed(), 
            255 - oldColor.getGreen(),
            255 - oldColor.getBlue()
        };
        filter = getImageFilterValues(newColor);
        canFilterIndexColorModel = true;
    }

    @Override
    public int filterRGB(int x, int y, int argb) {
        int r = (int)(Math.abs(rgbAdj[0] - getRed(argb)) * filter[0]);
        int g = (int)(Math.abs(rgbAdj[1] - getGreen(argb)) * filter[1]);
        int b = (int)(Math.abs(rgbAdj[2] - getBlue(argb)) * filter[2]);
        return (argb & ALPHA_MASK) | (r << 16) | (g << 8) | b;
    }

    private float[] getImageFilterValues(final Color color) {
        return new float[]{
            color.getRed() / 255f,
            color.getGreen() / 255f,
            color.getBlue() / 255f};
    }

}
