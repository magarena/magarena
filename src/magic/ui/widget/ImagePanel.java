package magic.ui.widget;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {

    public enum ScaleMode {AUTOMATIC, PERFORMANCE, QUALITY}

    private BufferedImage sourceImage = null;

    private Dimension sourceSizeWhenVertical;
    private Dimension sourceSizeWhenHorizontal;

    private double imageScale = 1.0;
    private int degreesOfRotation = 0;

    private boolean sizeToFit = false;
    private boolean restrictToImageSize = false;


    public ImagePanel() {
        setOpaque(false);
    }

    public ImagePanel(BufferedImage image) {
        this(image, 0);
    }

    public ImagePanel(BufferedImage image, int initialRotation) {
        setImage(image);
        setRotation(initialRotation);
        setOpaque(false);
    }

    public void setImage(BufferedImage image) {
        this.sourceImage = image;
        this.sourceSizeWhenVertical = new Dimension(image.getWidth(), image.getHeight());
        this.sourceSizeWhenHorizontal = new Dimension(image.getHeight(), image.getWidth());
    }

    public void repack() {
        this.setSize(this.getDisplayedImageSize());
    }

    public double getDisplayedImageDiagonalLength() {
        return Math.sqrt(Math.pow(this.getDisplayedImageDimensions().height, 2) + Math.pow(this.getDisplayedImageDimensions().width,  2));
    }

    /**
     * The width and height of the image depend on its orientation.
     */
    private Dimension getSourceImageDimensions() {
        boolean isVerticalOrientation = (degreesOfRotation % 180 == 0);
        return (isVerticalOrientation ? sourceSizeWhenVertical : sourceSizeWhenHorizontal);
    }

    /**
     * Convenience function to show dimensions of panel using a dashed border.
     */
    public void showBounds(boolean showBounds) {
        setBorder(showBounds ? BorderFactory.createDashedBorder(null) : null);
    }

    /**
     * Currently, will round to the nearest 90 degrees. This means the image
     * can ONLY have either a vertical or horizontal orientation.
     */
    public void setRotation(int degrees) {
        this.degreesOfRotation = degrees;
        repaint();
    }

    public int getRotation() {
        return this.degreesOfRotation;
    }

    public Dimension getSourceImageSize(boolean asDisplayed) {
        if (!asDisplayed) {
            return this.sourceSizeWhenVertical;
        } else {
            return getSourceImageDimensions();
        }
    }

    private Dimension getDisplayedImageDimensions() {
        int w =(int) (getSourceImageDimensions().width * this.imageScale);
        int h =(int) (getSourceImageDimensions().height * this.imageScale);
        return new Dimension(w, h);
    }

    public Dimension getDisplayedImageSize() {
        return getDisplayedImageDimensions();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (sourceImage != null) {
            paintWithoutResampling((Graphics2D) g);
        }
    }

    /**
     * Renders image without using any additional re-sampling.
     * <p>
     * For scales of approximately 0.6 or above this should produce more than acceptable results.
     * For thumbnail size images there is a distinct degradation in quality and a resampling
     * algorithm should be used if image quality is more important than performance.
     * </p>
     */
    private void paintWithoutResampling(Graphics2D g2d) {
        // These make a visible difference...
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        // ...not so sure about...
        g2d.setComposite(AlphaComposite.Src);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(sourceImage, getAffineTransform(sourceImage, true), null);
    }

    /**
     * Scale and rotate the image using an Affine transformation.
     *
     * @see
     *      <a href="http://stackoverflow.com/questions/4918482/rotating-bufferedimage-instances">rotating BufferedImage instances</a>
     *
     */
    private AffineTransform getAffineTransform(BufferedImage sourceImage, boolean createScaleTransform) {

        // Note that transformations happen in reverse order.
        AffineTransform at = new AffineTransform();

        // 4. move image to the center of the component. Remember, image center point is (0,0).
        at.translate(getWidth() / 2, getHeight() / 2);

        // 3. rotate around (0,0) if applicable.
        applyRotateTransform(at);

        // 2. scale image.
        if (createScaleTransform) {
            if (sizeToFit) { setScaleToFit(); }
            at.scale(this.imageScale, this.imageScale);
        }

        // 1. move the image so that its center is at (0,0).
        at.translate(-sourceImage.getWidth() / 2, -sourceImage.getHeight() / 2);

       return at;

    }

    private void applyRotateTransform(AffineTransform at) {
        at.rotate(Math.toRadians((double) degreesOfRotation));
    }

    public double getScale() {
        return this.imageScale;
    }

    /**
     * Display image at its original size regardless of the panel size.
     */
    public void sizeImageToOriginal() {
        this.sizeToFit = false;
        this.imageScale = 1.0;
        repaint();
    }

    /**
     * Display image at its original size unless the panel is too small
     * in which case reduce size to fit (whilst retaining aspect ratio).
     * <p>
     * Image will not expand to fit a larger panel beyond its original size.
     * </p>
     */
    public void sizeImageToFitMaxOriginal() {
        this.sizeToFit = true;
        this.restrictToImageSize = true;
        repaint();
    }

    /**
     * Size image to a specified scale whilst retaining its aspect ratio.
     * <p>
     * Ignores panel dimensions and does not resize with panel.
     * <br><br>
     * Examples:<br>
     * - a scale of 0.5 will display the image at half its size.<br>
     * - a scale of 2.0 will display the image at twice its size.<br>
     * - a scale of 1.0 will display the image at its original size.
     * </p>
     */
    public void sizeImageToScale(double newScale) {
        this.sizeToFit = false;
        this.restrictToImageSize = false;
        this.imageScale = newScale;
        repaint();
    }

    /**
     * Image will resize to fit the panel whilst retaining its aspect ratio.
     */
    public void sizeImageToFitPanel() {
        this.sizeToFit = true;
        this.restrictToImageSize = false;
        repaint();
    }

    /**
     * Maintains aspect ratio.
     */
    private void setScaleToFit() {

        int thisWidth = this.getWidth();
        int thisHeight = this.getHeight();

        int imageWidth = getSourceImageDimensions().width;
        int imageHeight = getSourceImageDimensions().height;

        int newWidth = thisWidth;
        if (this.restrictToImageSize) {
            if (newWidth > imageWidth) {
                newWidth = imageWidth;
            }
        }
        this.imageScale = (double)newWidth / imageWidth;

        int newHeight = (int) (this.imageScale * imageHeight);
        if (newHeight > thisHeight) {
            newHeight = thisHeight;
            this.imageScale = (double)newHeight / imageHeight;
        }

    }

    public void clearImage() {
        this.sourceImage = null;
        repaint();
    }

}
