/*
 * $Id$
 *
 * Dual-licensed under LGPL (Sun and Romain Guy) and BSD (Romain Guy).
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * Copyright (c) 2006 Romain Guy <romain.guy@mac.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package magic.utility;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import magic.ui.theme.Theme;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

/**
 * <p><code>GraphicsUtilities</code> contains a set of tools to perform
 * common graphics operations easily.
 *
 * @author Romain Guy <romain.guy@mac.com>
 * @author rbair
 * @author Karl Schaefer
 */
final public class GraphicsUtilities {

    private final static GraphicsConfiguration GC = (java.awt.GraphicsEnvironment.isHeadless() == false) ?
        GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration() :
        null;

    private GraphicsUtilities() {}

    public static BufferedImage scale(
            final BufferedImage img,
            final int targetWidth,
            final int targetHeight) {
        if (img.getWidth() == targetWidth && img.getHeight() == targetHeight) {
            return img;
        } else {
            return scale(
                    img,
                    targetWidth,
                    targetHeight,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR,
                    true);
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
    public static BufferedImage scale(final BufferedImage img,
                               final int targetWidth,
                               final int targetHeight,
                               final Object hint,
                               final boolean higherQuality) {
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

    /**
     * Creates an image of the contents of container and saves to file.
     */
    public static File doScreenshotToFile(final Component container, final Path filePath) throws IOException {
        final File imageFile = new File(filePath.toString());
        ImageIO.write(getScreenshotImage(container), "png", imageFile);
        return imageFile;
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

    public static boolean isValidImageFile(final Path imageFilePath) {
        try {
            final BufferedImage image = ImageIO.read(imageFilePath.toFile());
            return (image != null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * The images returned by ImageIO are often in custom formats which can draw really,
     * really slowly. For best performance, it's often best to draw any image returned by
     * ImageIO into a new image of the appropriate pixel format for your system.
     * (http://www.jhlabs.com/ip/managed_images.html)
     */
    public static BufferedImage getOptimizedImage(final BufferedImage source) {
        final BufferedImage buffImage = getCompatibleBufferedImage(source.getWidth(), source.getHeight());
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
            GraphicsUtilities.getOptimizedImage(image) :
            MagicStyle.getTheme().getTexture(Theme.TEXTURE_BACKGROUND);
    }
}
