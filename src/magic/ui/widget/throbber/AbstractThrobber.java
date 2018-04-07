package magic.ui.widget.throbber;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.HierarchyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import magic.ui.helpers.ImageHelper;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.RepeatBehavior;

/**
 * TODO
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractThrobber extends JComponent {

    public enum SpinDirection {
        CLOCKWISE,
        ANTICLOCKWISE
    }

    private final Timeline timeline = new Timeline();
    private BufferedImage currentFrameImage = null;
    private final boolean isAntiAliasOn;
    private final SpinDirection spinDirection;
    private final boolean isDebugMode;

    // Indicates whether color was set via builder property.
    protected final boolean isColorSet;

    // each sub-class uses this to draw a single frame.
    abstract protected void drawFrame(final Graphics2D g2, final int angle);

    protected static abstract class Init<T extends Init<T>> {

        protected abstract T self();

        // optional properties with default values.
        // these are common to all AbstractThrobbers sub-classes.
        private boolean antiAlias = true;
        private SpinDirection spinDirection = SpinDirection.CLOCKWISE;
        private int spinPeriod = 2000;
        private Color color = null;
        private boolean isDebugMode = false;

        /**
         * Invariably produces a smoother looking image (default is {@code true}).
         */
        public T antiAlias(boolean val) {
            this.antiAlias = val;
            return self();
        }

        /**
         * Default is {@code SpinDirection.CLOCKWISE}.
         */
        public T spinDirection(SpinDirection val) {
            this.spinDirection = val;
            return self();
        }

        /**
         * The time it should take to perform one revolution. Default is 2000 milliseconds.
         */
        public T spinPeriod(int val) {
            this.spinPeriod = val;
            return self();
        }

        /**
         * Defaults to the container foreground color.
         */
        public T color(Color c) {
            this.color = c;
            return self();
        }

        /**
         * Displays bounds and center point.
         */
        public T debugMode(boolean val) {
            this.isDebugMode = val;
            return self();
        }

    }

    public static class Builder extends Init<Builder> {

        @Override
        protected Builder self() {
            return this;
        }
    }

    protected AbstractThrobber(final Init<?> init) {
        this.isDebugMode = init.isDebugMode;
        this.spinDirection = init.spinDirection;
        this.isAntiAliasOn = init.antiAlias;
        this.setForeground(init.color);
        isColorSet = (init.color != null);
        addHierarchyListener((HierarchyEvent e) -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) == HierarchyEvent.SHOWING_CHANGED) {
                doOnShowingChanged(init.spinPeriod);
            }
        });
    }

    private void doOnShowingChanged(int spinPeriod) {
        if (isShowing()) {
            if (timeline.getState() == Timeline.TimelineState.IDLE) {
                startTimeline(spinPeriod);
            } else {
                timeline.resume();
            }
        } else {
            timeline.suspend();
        }
    }

    /**
     * Must be public to work with Trident time-line.
     */
    public void setAngle(final int angle) {
        paintNextFrame(angle);
    }

    private void paintNextFrame(final int angle) {

//        System.out.println("paintNextFrame : " + angle);

        // draw the next frame to an off-screen image buffer on timeline
        // thread to minimize work required on EDT paintComponent().
        assert !SwingUtilities.isEventDispatchThread();
        BufferedImage image = getNextFrameImage(angle);

        if (image != null) {
            // All the EDT needs to do is draw the image.
            SwingUtilities.invokeLater(() -> {
                currentFrameImage = image;
                repaint();
            });
        }

    }

    private BufferedImage getNextFrameImage(final int angle) {

        final int W = getWidth();
        final int H = getHeight();
        if (W <= 0 || H <= 0) {
            return null;
        }

        final BufferedImage image = ImageHelper.getCompatibleBufferedImage(W, H, Transparency.TRANSLUCENT);
        final Graphics2D g2d = image.createGraphics();

        if (isAntiAliasOn) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // These make a visible difference producing smoother looking images...
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }

        drawFrame(g2d, angle);

        return image;

    }

    protected double getRadians(final double degrees) {
        return Math.PI * degrees / 180;
    }

    private void startTimeline(final int period) {
        timeline.abort();
        timeline.addPropertyToInterpolate(
                Timeline.property("angle")
                .on(this)
                .from(0)
                .to(356));
        timeline.setDuration(period);
        timeline.playLoop(RepeatBehavior.LOOP);
    }

    /**
     * keep the amount of work this has to do to the absolute minimum for smoother animation.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isDebugMode) {
            drawCrosshair(g);
        }
        if (currentFrameImage != null) {
            g.drawImage(currentFrameImage, 0, 0, null);
        }
    }

    protected SpinDirection getSpinDirection() {
        return spinDirection;
    }

    private void drawCrosshair(final Graphics g) {
        final int W = getWidth();
        final int H = getHeight();
        final int cX = W / 2;
        final int cY = H / 2;
        g.setColor(Color.red);
        g.drawLine(0, cY, W, cY);
        g.drawLine(cX, 0, cX, H);
        g.drawRect(0, 0, W - 1, H - 1);
    }
}
