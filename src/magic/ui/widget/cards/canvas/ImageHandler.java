package magic.ui.widget.cards.canvas;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import magic.ui.helpers.ImageHelper;

public class ImageHandler {

    private final ImageObserver observer;
    private final ImageCache imageCache;

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
        final BufferedImage pic
                = ImageHelper.getCompatibleBufferedImage(
                        sourceImage.getWidth(observer),
                        sourceImage.getHeight(observer));
        Graphics2D g2d = (Graphics2D) pic.getGraphics();
        g2d.drawImage(sourceImage, 0, 0, null);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("dialog", Font.BOLD, 32));
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
                final double aspectRatio = (double) sourceImage.getWidth() / sourceImage.getHeight();
                final int newHeight = (int) (newWidth / aspectRatio);
                final boolean useQualityScale = newWidth < sourceImage.getWidth();
                scaledImage = ImageHelper.scale(
                        sourceImage,
                        newWidth, newHeight,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR,
                        useQualityScale);
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
     * The images returned by ImageIO are often in custom formats which can draw really, really slowly. For best performance, it's often best to draw any image returned by ImageIO into a new image of the appropriate pixel format for your system. (http://www.jhlabs.com/ip/managed_images.html)
     */
    private BufferedImage loadImageResourceFromFile(final String filename) {
        final BufferedImage source = loadImage(filename);
        final BufferedImage buffImage = ImageHelper.getCompatibleBufferedImage(source.getWidth(), source.getHeight());
        buffImage.getGraphics().drawImage(source, 0, 0, observer);
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

}
