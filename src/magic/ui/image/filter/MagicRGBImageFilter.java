package magic.ui.image.filter;

import java.awt.image.RGBImageFilter;

abstract class MagicRGBImageFilter extends RGBImageFilter {

    protected static final int ALPHA_MASK   = 0xff000000;
    private static final int RED_MASK       = 0x00ff0000;
    private static final int GREEN_MASK     = 0x0000ff00;
    private static final int BLUE_MASK      = 0x000000ff;

    @Override
    public abstract int filterRGB(int x, int y, int rgb);

    final protected int getRed(int argb) {
        return (argb & RED_MASK) >> 16;
    }

    final protected int getGreen(int argb) {
        return (argb & GREEN_MASK) >> 8;
    }

    final protected int getBlue(int argb) {
        return argb & BLUE_MASK;
    }

}
