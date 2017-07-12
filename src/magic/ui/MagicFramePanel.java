package magic.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.data.settings.BooleanSetting;
import magic.ui.helpers.ImageHelper;
import magic.ui.screen.MScreen;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class MagicFramePanel extends JPanel {

    private static final Color BACKCOLOR = new Color(8, 8, 8);

    private Theme activeTheme;
    private BufferedImage sourceImage;
    private BufferedImage cachedImage;
    private boolean stretchTexture;
    private Dimension oldSize = new Dimension();

    MagicFramePanel() {
        setBackground(BACKCOLOR);
        setLayout(new MigLayout("insets 0, gap 0, nogrid, novisualpadding"));
    }

    private void drawMLogo(final Graphics g) {
        g.drawImage(
            MagicImages.LOGO,
            (getWidth() - MagicImages.LOGO.getWidth()) / 2,
            (getHeight() - MagicImages.LOGO.getHeight()) / 2,
            null
        );
    }

    private void drawCachedImage(final Graphics g) {
        final Rectangle r = g.getClipBounds();
        if (r == null) {
            g.drawImage(cachedImage, 0, 0, null);
        } else {
            g.drawImage(cachedImage,
                r.x, r.y, r.x + r.width, r.y + r.height,
                r.x, r.y, r.x + r.width, r.y + r.height,
                null
            );
        }
    }

    private void drawNewImageAndCache(final Graphics g, Dimension newSize) {
        if (stretchTexture) {
            drawStretchedImage(g, sourceImage, newSize);
        } else {
            drawTiledImage(g, sourceImage, newSize);
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {

        super.paintComponent(g);

        SwingUtilities.invokeLater(() -> {
            setBackgroundImage();
        });

        if (sourceImage != null) {
            final Dimension newSize = getSize();
            if (oldSize.equals(newSize) && cachedImage != null) {
                drawCachedImage(g);
            } else {
                oldSize = new Dimension(newSize);
                drawNewImageAndCache(g, newSize);
            }
        } else {
            drawMLogo(g);
        }
    }

    void refreshBackground() {
        activeTheme = null;
        repaint();
    }

    private void setBackgroundImage() {
        if (activeTheme != MagicStyle.getTheme()) {
            activeTheme = MagicStyle.getTheme();
            sourceImage = getBackgroundImage();
            cachedImage = null;
            stretchTexture = activeTheme.getValue(Theme.VALUE_BACKGROUND_STRETCH) == 1
                || GeneralConfig.get(BooleanSetting.CUSTOM_BACKGROUND);
            repaint();
        }
    }

    private BufferedImage getBackgroundImage() {
        return GeneralConfig.get(BooleanSetting.CUSTOM_BACKGROUND)
            ? ImageHelper.getCustomBackgroundImage()
            : activeTheme.getBackgroundImage();
    }

    private void drawStretchedImage(final Graphics g, final BufferedImage aImage, Dimension container) {

        final int iw = aImage.getWidth();
        final int ih = aImage.getHeight();
        final int iw2 = ih * container.width / container.height;

        final Rectangle imageRect;
        if (iw2 <= iw) {
            imageRect = new Rectangle((iw - iw2) / 2, 0, iw2, ih);
        } else {
            final int ih2 = iw * container.height / container.width;
            imageRect = new Rectangle(0, (ih - ih2) / 2, iw, ih2);
        }

        final BufferedImage subImage = ImageHelper.getOptimizedSubimage(aImage, imageRect);
        cachedImage = ImageHelper.scale(subImage, container.width, container.height);
        g.drawImage(cachedImage, 0, 0, this);
    }

    private void drawTiledImage(final Graphics g, final BufferedImage aImage, Dimension container) {
        final int imageWidth = aImage.getWidth();
        final int imageHeight = aImage.getHeight();
        final int x2 = container.width;
        final int y2 = container.height;
        for (int y = 0; y < y2; y += imageHeight) {
            for (int x = 0; x < x2; x += imageWidth) {
                g.drawImage(aImage, x, y, this);
            }
        }
    }

    void setScreen(MScreen s) {
        removeAll();
        s.addToLayout(this, "dock center");
        revalidate();
        repaint();
    }

}
