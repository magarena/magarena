package magic.ui;

import magic.ui.helpers.ImageHelper;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Supplier;

public final class ImageFileIO {
    private ImageFileIO() { }

    private static BufferedImage loadImage(final File input) {
        try {
            return javax.imageio.ImageIO.read(input);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to read from " + input);
            return null;
        } catch (final IllegalArgumentException ex) {
            System.err.println("ERROR! Unable to read from null");
            return null;
        }
    }

    public static BufferedImage toImg(final File input, final BufferedImage def) {
        return toImg(input, () -> def);
    }

    public static BufferedImage toImg(final File input, final Supplier<BufferedImage> def) {
        final BufferedImage img = loadImage(input);
        if (img == null) {
            // no registered ImageReader able to read the file, likely file is corrupted
            input.delete();
            return def.get();
        } else {
            final BufferedImage optimizedImage =
                    ImageHelper.getCompatibleBufferedImage(img.getWidth(), img.getHeight(), img.getTransparency());
            optimizedImage.getGraphics().drawImage(img, 0, 0 , null);
            return optimizedImage;
        }
    }

    public static BufferedImage getOptimizedImage(final File imageFile) {
        BufferedImage sourceImage = loadImage(imageFile);
        if (sourceImage == null) {
            // no registered ImageReader able to read the file, likely file is corrupted
            imageFile.delete();
            return null;
        }
        BufferedImage optimizedImage = ImageHelper.getCompatibleBufferedImage(
            sourceImage.getWidth(),
            sourceImage.getHeight(),
            sourceImage.getTransparency()
        );
        optimizedImage.getGraphics().drawImage(sourceImage, 0, 0, null);
        return optimizedImage;
    }

    public static BufferedImage toImg(final URL input, final BufferedImage def) {
        BufferedImage img = def;
        try {
            img = javax.imageio.ImageIO.read(input);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to read from " + input);
        } catch (final IllegalArgumentException ex) {
            System.err.println("ERROR! Unable to read from null");
        }
        return img;
    }

    public static BufferedImage toImg(final InputStream input, final BufferedImage def) {
        BufferedImage img = def;
        try (final InputStream is = input) {
            img = javax.imageio.ImageIO.read(is);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to read from input stream");
        } catch (final IllegalArgumentException ex) {
            System.err.println("ERROR! Unable to read from null");
        }
        return img;
    }
}
