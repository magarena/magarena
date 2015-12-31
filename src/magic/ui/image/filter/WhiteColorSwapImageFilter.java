package magic.ui.image.filter;

import java.awt.Color;

public class WhiteColorSwapImageFilter extends MagicRGBImageFilter {

    private final float[] filter;

    public WhiteColorSwapImageFilter(Color newColor) {
        filter = getImageFilterValues(newColor);
        canFilterIndexColorModel = true;
    }

    @Override
    public int filterRGB(int x, int y, int argb) {
        int r = (int)(getRed(argb) * filter[0]);
        int g = (int)(getGreen(argb) * filter[1]);
        int b = (int)(getBlue(argb) * filter[2]);
        return (argb & ALPHA_MASK) | (r << 16) | (g << 8) | b;
    }

    private float[] getImageFilterValues(final Color color) {
        return new float[]{
            color.getRed() / 255f,
            color.getGreen() / 255f,
            color.getBlue() / 255f};
    }

}
