package magic.ui.screen.duel.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import magic.ui.utility.GraphicsUtils;


public class BattlefieldTextOverlay {

    private static final Font ZONE_FONT = new Font("Dialog", Font.BOLD, 12);

    private BufferedImage textOverlayImage = null;
    private String zoneName = "";
    private boolean refreshOverlay = true;

    public BufferedImage getOverlayImage(final int battlefieldWidth, final int batttlefieldHeight) {

        final boolean createNewImage =
                textOverlayImage == null ||
                textOverlayImage.getHeight() != batttlefieldHeight ||
                textOverlayImage.getWidth() != battlefieldWidth;

        if (createNewImage) {
            textOverlayImage = GraphicsUtils.getCompatibleBufferedImage(
                    battlefieldWidth, batttlefieldHeight, Transparency.BITMASK
            );
        }

        if (refreshOverlay) {
            refreshOverlay = false;
            GraphicsUtils.clearImage(textOverlayImage);
            final Graphics2D g2d = textOverlayImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            drawZoneName(g2d);
            g2d.dispose();
        }

        return textOverlayImage;
    }

    private void drawZoneName(final Graphics2D g2d) {
        g2d.setFont(ZONE_FONT);
        final int textWidth = g2d.getFontMetrics(ZONE_FONT).stringWidth(zoneName);
        final int textX = (textOverlayImage.getWidth() / 2) - (textWidth / 2);
        final int textY = textOverlayImage.getHeight() - 4;
        GraphicsUtils.drawStringWithOutline(g2d, zoneName, textX, textY, Color.YELLOW, Color.DARK_GRAY);
    }

    public void setPlayerZoneName(final String text) {
        this.zoneName = text;
        refreshOverlay = true;
    }

}
