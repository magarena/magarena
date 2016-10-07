package magic.ui.widget.throbber;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.awt.image.BufferedImage;
import magic.ui.helpers.ImageHelper;

@SuppressWarnings("serial")
public class ImageThrobber extends AbstractThrobber {

    // The original reference image.
    private final BufferedImage sourceImage;

    // The actual image displayed. Can be resized, recolored or exactly the same as source.
    private Image displayImage;

    // Optional builder property will scale image to fit container. Default is false.
    private final boolean sizeToFit;

    // If image is exactly the same size as this component no scaling required.
    private boolean isExactFit = false;

    // The center point of this component. Updated on resize.
    private Point centerPoint;

    public static abstract class Init<T extends Init<T>> extends AbstractThrobber.Init<Init<T>> {

        // mandatory, must be set via Builder constructor.
        protected BufferedImage sourceImage;

        // optional properties specific to this class with default values
        private boolean sizeToFit = false;

        public Init<T> sizeToFit(boolean b) {
            this.sizeToFit = b;
            return self();
        }

        public ImageThrobber build() {
            return new ImageThrobber(this);
        }
    }

    public static class Builder extends Init<Builder> {

        // assume image has already been optimized.
        public Builder(final BufferedImage image) {
            this.sourceImage = image;
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    // CTR
    protected ImageThrobber(final Init<?> init) {

        super(init);
        this.sourceImage = init.sourceImage;
        this.sizeToFit = init.sizeToFit;

        setPreferredSize(new Dimension(sourceImage.getWidth(), sourceImage.getHeight()));

        addHierarchyListener((e) -> {
            final boolean isShowingChanged = (e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) == HierarchyEvent.SHOWING_CHANGED;
            if (isShowingChanged) {
                displayImage = isColorSet
                    ? ImageHelper.getColoredImage(sourceImage, getForeground())
                    : sourceImage;
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                isExactFit = (sourceImage.getWidth() == getWidth() && sourceImage.getHeight() == getHeight());
                centerPoint = new Point(getWidth() / 2, getHeight() / 2);
            }

        });

        setBounds(0, 0, sourceImage.getWidth(), sourceImage.getHeight());

    }

    @Override
    protected void drawFrame(Graphics2D g2, int angle) {
        if (displayImage == null)
            return;
        if (centerPoint != null) {
            if (sizeToFit && isExactFit == false) {
                drawSizeToFit(g2, angle);
            } else {
                drawOriginalSize(g2, angle);
            }
        }
    }

    private void drawSizeToFit(Graphics2D g2, int angle) {

        final double radiansToRotate
                = getSpinDirection() == SpinDirection.CLOCKWISE
                ? getRadians(angle) : -getRadians(angle);

        // translate(cx, cy) -> rotate(theta) -> translate(-cx, -cy);
        g2.rotate(radiansToRotate, centerPoint.x, centerPoint.y);
        // draw and scale to container dimensions.
        g2.drawImage(displayImage, 0, 0, getWidth(), getHeight(), null);

        g2.dispose();

    }

    private void drawOriginalSize(Graphics2D g2, int angle) {

        final double radiansToRotate
                = getSpinDirection() == SpinDirection.CLOCKWISE
                ? getRadians(angle) : -getRadians(angle);

        // move origin to center of container before rotating.
        g2.translate(centerPoint.x, centerPoint.y);
        g2.rotate(radiansToRotate);

        // draw image so that its center point = container center point.
        final int cx = displayImage.getWidth(null) / 2;
        final int cy = displayImage.getHeight(null) / 2;
        g2.translate(-cx, -cy);
        g2.drawImage(displayImage, 0, 0, null);

        g2.dispose();

    }

}
