package magic.ui.image.filter;

public class GrayScaleImageFilter extends MagicRGBImageFilter {

    public GrayScaleImageFilter() {
        canFilterIndexColorModel = true;
    }

    @Override
    public int filterRGB(int x, int y, int argb) {
        final int gray = (int)(
                (getRed(argb) * 0.21f) +
                (getGreen(argb) * 0.72f) +
                (getBlue(argb) * 0.07f)
        );
        return (argb & ALPHA_MASK) | (gray << 16) | (gray << 8) | gray;
    }
    
}
