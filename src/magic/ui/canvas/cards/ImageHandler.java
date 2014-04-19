package magic.ui.canvas.cards;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;

public class ImageHandler {

    private final ImageObserver observer;
    private final ImageCache imageCache;
    private boolean useScaledInstanceMethod = false;

    public ImageHandler(final ImageObserver observer0) {
        this.observer = observer0;
        this.imageCache = new ImageCache(80);
    }

    public BufferedImage getImageResource(final String filename, final boolean useCache) {
        BufferedImage image = null;
        if (useCache) {
            image = imageCache.get(filename);
            if (image == null) {
                image = loadImageResourceFromFile(filename);
                imageCache.put(filename, image);
            }
        } else {
            image = loadImageResourceFromFile(filename);
        }
        return image;
    }

    public BufferedImage getAnnotatedImage(final String id, final Image sourceImage) {
        final BufferedImage pic =
                getCompatibleBufferedImage(
                        sourceImage.getWidth(observer),
                        sourceImage.getHeight(observer));
        Graphics2D g2d = (Graphics2D)pic.getGraphics();
        g2d.drawImage(sourceImage, 0, 0, null);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("dialog",Font.BOLD,32));
        g2d.drawString(id, 40, 110);
        g2d.dispose();
        return pic;
    }

    public BufferedImage getScaledImage(final String filename, final int newWidth) {
        return getScaledImage(
                getImageResource(filename, true),
                newWidth);
    }

    public BufferedImage getScaledImage(final BufferedImage sourceImage, final int newWidth) {
        // Only need to scale if required size is different to source.
        if (newWidth != sourceImage.getWidth()) {
            // Check whether scaled image is already in cache.
            final String cacheKey = Integer.toHexString(sourceImage.hashCode()).toUpperCase() + newWidth;
            BufferedImage scaledImage = imageCache.get(cacheKey);
            if (scaledImage == null) {
                // not in cache so will need to scale.
                if (useScaledInstanceMethod) {
                    final Image image = ((Image)sourceImage).getScaledInstance(newWidth, -1, Image.SCALE_SMOOTH);
                    scaledImage = getCompatibleBufferedImage(image.getWidth(null), image.getHeight(null));
                    scaledImage.getGraphics().drawImage(image, 0, 0 , null);
                } else {
                    final double aspectRatio = (double)sourceImage.getWidth() / sourceImage.getHeight();
                    final int newHeight = (int)(newWidth / aspectRatio);
                    final boolean useQualityScale = newWidth < sourceImage.getWidth();
                    scaledImage = getScaledInstance(
                            sourceImage,
                            newWidth, newHeight,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR,
                            useQualityScale);
                }
                // add scaled image to cache for faster access next time.
                imageCache.put(cacheKey, scaledImage);
            }
            return scaledImage;
        } else {
            return sourceImage;
        }
    }

    /**
     * Loads an image from file and ensures it is in optimum format.
     * <p>
     * The images returned by ImageIO are often in custom formats which can draw really,
     * really slowly. For best performance, it's often best to draw any image returned by
     * ImageIO into a new image of the appropriate pixel format for your system.
     * (http://www.jhlabs.com/ip/managed_images.html)
     */
    private BufferedImage loadImageResourceFromFile(final String filename) {
        final BufferedImage source = loadImage(filename);
        final BufferedImage buffImage = getCompatibleBufferedImage(source.getWidth(), source.getHeight());
        buffImage.getGraphics().drawImage(source, 0, 0 , observer);
        return buffImage;
    }

    private BufferedImage loadImage(final String filename) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(getClass().getClassLoader().getResource(filename));
        } catch (IOException e) {
            System.err.println("ImageHandler.loadImage() : " + e);
        }
        if (img == null) {
            throw new NullPointerException("Failed to load image file: " + filename);
        }
        return img;
    }

    private BufferedImage getCompatibleBufferedImage(final int width, final int height) {
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice gs = ge.getDefaultScreenDevice();
        final GraphicsConfiguration gc = gs.getDefaultConfiguration();
        return gc.createCompatibleImage(width, height, Transparency.OPAQUE);
    }

    /**
     * Convenience method that returns a scaled instance of the
     * provided {@code BufferedImage}.
     *
     * @see https://today.java.net/pub/a/today/2007/04/03/perils-of-image-getscaledinstance.html
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
    public BufferedImage getScaledInstance(BufferedImage img,
                                           int targetWidth,
                                           int targetHeight,
                                           Object hint,
                                           boolean higherQuality)
    {
        BufferedImage ret = img;
        int w, h;
        if (higherQuality) {
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

            BufferedImage tmp = getCompatibleBufferedImage(w, h);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }

}
