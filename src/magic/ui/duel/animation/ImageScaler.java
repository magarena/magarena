package magic.ui.duel.animation;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import magic.ui.utility.GraphicsUtils;

public class ImageScaler {

    private final BufferedImage origImage;
    private BufferedImage scaledImage;

    public ImageScaler(final BufferedImage aImage) {
        this.origImage = aImage;
        this.scaledImage = aImage;
    }

    public void setSize(final Dimension boundary) {

        if (boundary.height == scaledImage.getHeight() || boundary.height < 20)
            return;

        // retain aspect ratio.
        final double widthRatio = boundary.getWidth() / origImage.getWidth();
        final double heightRatio = boundary.getHeight() / origImage.getHeight();
        final double ratio = Math.min(widthRatio, heightRatio);

        // performance more important than quality during animation.
        scaledImage = GraphicsUtils.scale(
            origImage,
            (int) (origImage.getWidth() * ratio),
            (int) (origImage.getHeight() * ratio),
            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR,
            false
        );

    }

    public Image getImage() {
        return scaledImage;
    }

    void setSize(float f) {
        setSize(new Dimension((int)(origImage.getWidth() * f), (int)(origImage.getHeight() * f)));
    }

    Dimension getScaledSize() {
        return new Dimension(scaledImage.getWidth(), scaledImage.getHeight());
    }

    Image getOrigImage() {
        return origImage;
    }

    Dimension getOrigSize() {
        return new Dimension(origImage.getWidth(), origImage.getHeight());
    }

}
