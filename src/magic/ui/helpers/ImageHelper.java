package magic.ui.helpers;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice.WindowTranslucency;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.ui.image.filter.ColorSwapImageFilter;
import magic.ui.image.filter.GrayScaleImageFilter;
import magic.ui.image.filter.WhiteColorSwapImageFilter;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.utility.MagicFileSystem.DataPath;
import magic.utility.MagicFileSystem;

final public class ImageHelper {

    private static final GrayScaleImageFilter GRAYSCALE_FILTER = new GrayScaleImageFilter();

    private final static GraphicsConfiguration GC = (java.awt.GraphicsEnvironment.isHeadless() == false) ?
        GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration() :
        null;

    private ImageHelper() {}

    public static BufferedImage scale(final BufferedImage img, final int targetWidth, final int targetHeight) {
        if (img.getWidth() == targetWidth && img.getHeight() == targetHeight) {
            return img;
        } else {
            return scale(
                img,
                targetWidth,
                targetHeight,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR,
                targetWidth < img.getWidth()
            );
        }
    }

    /**
     * Convenience method that returns a scaled instance of the
     * provided {@code BufferedImage}.
     *
     * @param img the original image to be scaled
     * @param targetWidth the desired width of the scaled instance,
     *    in pixels
     * @param targetHeight the desired height of the scaled instance,
     *    in pixels
     * @param hint one of the rendering hints that corresponds to
     *    {@code RenderingHints.KEY_INTERPOLATION} (e.g.
     *    {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality if true, this method will use a multi-step
     *    scaling technique that provides higher quality than the usual
     *    one-step technique (only useful in downscaling cases, where
     *    {@code targetWidth} or {@code targetHeight} is
     *    smaller than the original dimensions, and generally only when
     *    the {@code BILINEAR} hint is specified)
     * @return a scaled version of the original {@code BufferedImage}
     */
    public static BufferedImage scale(
        final BufferedImage img,
        final int targetWidth,
        final int targetHeight,
        final Object hint,
        final boolean higherQuality
    ) {
        BufferedImage ret = img;
        int w;
        int h;
        if (higherQuality && (img.getWidth() > targetWidth && img.getHeight() > targetHeight)) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            final BufferedImage tmp = getCompatibleBufferedImage(w, h, img.getTransparency());
            final Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }

    public static File doScreenshotToFile(final Component container, final Path filePath, final String imageType) throws IOException {
        final File imageFile = new File(filePath.toString());
        ImageIO.write(getScreenshotImage(container), imageType, imageFile);
        return imageFile;
    }

    /**
     * Creates an image of the contents of container and saves to file.
     */
    public static File doScreenshotToFile(final Component container, final Path filePath) throws IOException {
        return doScreenshotToFile(container, filePath, "png");
    }

    private static BufferedImage getScreenshotImage(final Component container) {
        final Rectangle rec = container.getBounds();
        final BufferedImage capture = getCompatibleBufferedImage(rec.width, rec.height);
        container.paint(capture.getGraphics());
        return capture;
    }

    public static BufferedImage getCompatibleBufferedImage(final int width, final int height, final int transparency) {
        if (GC != null) {
            return GC.createCompatibleImage(width, height, transparency);
        } else {
            final int type = (transparency == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
            return new BufferedImage(width, height, type);
        }
    }

    public static BufferedImage getCompatibleBufferedImage(final int width, final int height) {
        return getCompatibleBufferedImage(width, height, Transparency.OPAQUE);
    }

    public static BufferedImage getCompatibleBufferedImage(BufferedImage image) {
        return getCompatibleBufferedImage(image.getWidth(), image.getHeight(), image.getTransparency());
    }

    public static BufferedImage getBufferedImage(ImageIcon icon) {
        BufferedImage bi = getCompatibleBufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TRANSLUCENT);
        Graphics g = bi.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        return bi;
    }

    public static boolean isValidImageFile(final Path imageFilePath) {
        try {
            final BufferedImage image = ImageIO.read(imageFilePath.toFile());
            return (image != null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isValidImageFile(File aFile) {
        return isValidImageFile(aFile.toPath());
    }

    /**
     * The images returned by ImageIO are often in custom formats which can draw really,
     * really slowly. For best performance, it's often best to draw any image returned by
     * ImageIO into a new image of the appropriate pixel format for your system.
     * (http://www.jhlabs.com/ip/managed_images.html)
     */
    public static BufferedImage getOptimizedImage(final BufferedImage source) {
        final BufferedImage buffImage = getCompatibleBufferedImage(
                source.getWidth(),
                source.getHeight(),
                source.getTransparency()
        );
        buffImage.getGraphics().drawImage(source, 0, 0, null);
        return buffImage;
    }

    public static BufferedImage getCustomBackgroundImage() {
        final File file = MagicFileSystem.getDataPath(DataPath.MODS).resolve("background.image").toFile();

        BufferedImage image = null;

        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (image != null) ?
            ImageHelper.getOptimizedImage(image) :
            MagicStyle.getTheme().getTexture(Theme.TEXTURE_BACKGROUND);
    }

    public static boolean isWindowTranslucencySupported() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.isWindowTranslucencySupported(WindowTranslucency.TRANSLUCENT);
    }

    private static final Paint debugBorderPaint = new GradientPaint(0, 0, Color.red, 100, 100, Color.white, true);

    public static void setDebugBorder(final JComponent component) {
        component.setBorder(BorderFactory.createDashedBorder(debugBorderPaint));
    }

    public static BufferedImage getConvertedIcon(final ImageIcon icon) {
        final BufferedImage bi =
                ImageHelper.getCompatibleBufferedImage(
                        icon.getIconWidth(), icon.getIconHeight(), Transparency.TRANSLUCENT);
        final Graphics g = bi.createGraphics();
        // paint the Icon to the BufferedImage.
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        return bi;
    }

    public static void drawStringWithOutline(final Graphics g, final String str, int x, int y, Color fillColor, Color outlineColor) {
        g.setColor(outlineColor);
        for (int i = 1; i <= 1; i++) {
            g.drawString(str, x+i, y);
            g.drawString(str, x-i, y);
            g.drawString(str, x, y+i);
            g.drawString(str, x, y-i);
        }
        g.setColor(fillColor);
        g.drawString(str,x,y);
    }

    public static void drawStringWithOutline(final Graphics g, final String str, int x, int y) {
        drawStringWithOutline(g, str, x, y, Color.WHITE, Color.BLACK);
    }

    public static void clearImage(final BufferedImage image) {
        final Graphics2D g2d = image.createGraphics();
        final Composite composite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.setComposite(composite);
        g2d.dispose();
    }

    public static Icon getRecoloredIcon(ImageIcon icon, Color oldColor, Color newColor) {
        final FilteredImageSource fis = new FilteredImageSource(
                icon.getImage().getSource(),
                new ColorSwapImageFilter(oldColor, newColor)
        );
        return new ImageIcon(Toolkit.getDefaultToolkit().createImage(fis));
    }

    public static Icon getRecoloredIcon(MagicIcon icon, Color oldColor, Color newColor) {
        return getRecoloredIcon(MagicImages.getIcon(icon), oldColor, newColor);
    }

    /**
     * Converts a white icon to the specified color.
     *
     * Recommend use {@link #getRecoloredIcon(ImageIcon, Color, Color) } instead.
     */
    public static Icon getRecoloredIcon(ImageIcon icon, Color newColor) {
        return getRecoloredIcon(icon, Color.WHITE, newColor);
    }

    /**
     * Given a WHITE image, converts to given color.
     */
    public static Image getColoredImage(final Image aImage, final Color newColor) {
        final FilteredImageSource fis = new FilteredImageSource(
                aImage.getSource(),
                new WhiteColorSwapImageFilter(newColor)
        );
        return Toolkit.getDefaultToolkit().createImage(fis);
    }

    public static Image getGreyScaleImage(final Image colorImage) {
        final FilteredImageSource fis = new FilteredImageSource(
                colorImage.getSource(),
                GRAYSCALE_FILTER
        );
        return Toolkit.getDefaultToolkit().createImage(fis);
    }

    public static BufferedImage getTranslucentImage(BufferedImage image, float opacity) {
        final BufferedImage newImage = ImageHelper.getCompatibleBufferedImage(image);
        final Graphics2D g2d = newImage.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return newImage;
    }

    public static Image getTranslucentImage(Image image, float opacity) {
        final BufferedImage newImage = getCompatibleBufferedImage(
            image.getWidth(null), image.getHeight(null), Transparency.TRANSLUCENT
        );
        final Graphics2D g2d = newImage.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return newImage;
    }


    /**
     *  Returns an optimized subimage defined by a specified rectangular region.
     * <p>
     *  getSubImage() on its own causes image to become unaccelerated.
     *  (see <a href="http://www.jhlabs.com/ip/managed_images.html">external link</a>)
     */
    public static BufferedImage getOptimizedSubimage(BufferedImage image, Rectangle rect) {
        return ImageHelper.getOptimizedImage(image.getSubimage(rect.x, rect.y, rect.width, rect.height));
    }

}
