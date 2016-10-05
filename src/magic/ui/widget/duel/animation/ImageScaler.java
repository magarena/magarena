package magic.ui.widget.duel.animation;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import magic.ui.utility.GraphicsUtils;

class ImageScaler {

    private static final int MIN_SCALE_HEIGHT = 20;

    private final BufferedImage origImage;
    private BufferedImage scaledImage;
    private boolean isHighQuality = false;

    ImageScaler(final BufferedImage aImage) {
        this.origImage = aImage;
        this.scaledImage = aImage;
    }

    void setScaledImage(final Dimension rect) {

        // fit to rect whilst retaining aspect ratio.
        final double widthRatio = rect.getWidth() / origImage.getWidth();
        final double heightRatio = rect.getHeight() / origImage.getHeight();
        final double aspectRatio = Math.min(widthRatio, heightRatio);

        scaledImage = GraphicsUtils.scale(
            origImage,
            (int) (origImage.getWidth() * aspectRatio),
            (int) (origImage.getHeight() * aspectRatio),
            isHighQuality
                ? RenderingHints.VALUE_INTERPOLATION_BILINEAR
                : RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR,
            isHighQuality
        );
    }

    void setLQSize(Dimension rect) {
        if (rect.height > MIN_SCALE_HEIGHT && rect.height != scaledImage.getHeight()) {
            isHighQuality = false;
            setScaledImage(rect);
        }
    }

    private void setHQSize(Dimension rect) {
        if (rect.height > MIN_SCALE_HEIGHT && (rect.height != scaledImage.getHeight() || !isHighQuality)) {
            isHighQuality = true;
            setScaledImage(rect);
        }
    }

    void setSize(Dimension boundary, boolean highQualityScale) {
        if (highQualityScale) {
            setHQSize(boundary);
        } else {
            setLQSize(boundary);
        }
    }

    Image getImage() {
        return scaledImage;
    }

}
