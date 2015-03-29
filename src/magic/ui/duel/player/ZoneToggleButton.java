package magic.ui.duel.player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import javax.swing.JToggleButton;
import magic.data.MagicIcon;
import magic.model.MagicPlayerZone;
import magic.ui.GraphicsUtilities;
import magic.ui.IconImages;
import magic.ui.MagicStyle;
import org.pushingpixels.trident.Timeline;


@SuppressWarnings("serial")
public class ZoneToggleButton extends JToggleButton {

    enum ValueStyle {
        NORMAL,
        OUTLINE
    }

    private static final Font ZONE_FONT = new Font("Dialog", Font.BOLD, 14);

    private BufferedImage zoneIconImage = null;
    private final MagicIcon magicIcon;
    private String cardCountString = "0";
    private final ValueStyle valueStyle;
    private final MagicPlayerZone playerZone;
    private Timeline timeline1;
    private int imageOffset = 0;
    private boolean animateOnChange = false;
    private boolean isActive = false;

    public int getImageOffset() {
        return imageOffset;
    }

    public void setImageOffset(int value) {
        this.imageOffset = value * 2;
        repaint();
    }
    
    // CTR
    private ZoneToggleButton(
            final MagicPlayerZone zone,
            final int cardCount,
            final ValueStyle valueStyle,
            final boolean isActive) {

        this.playerZone = zone;
        this.magicIcon = zone.getIcon();
        this.valueStyle = valueStyle;
        this.animateOnChange = true;
        this.isActive = isActive;
        setToolTipText(zone.getName());
        setEnabled(false);
        setFocusable(false);
        setRolloverEnabled(false);
        setContentAreaFilled(false);
        setNumberOfCardsInZone(cardCount);
        setMinimumSize(new Dimension(40, 60));
    }
    
    // CTR
    ZoneToggleButton(
            final MagicPlayerZone playerZone,
            final int cardCount,
            final boolean isActive) {
        
        this(playerZone, cardCount, ValueStyle.NORMAL, isActive);
    }

    public MagicPlayerZone getPlayerZone() {
        return playerZone;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final Image image = getZoneIconAsImage();
        final int x = getWidth() / 2 - image.getWidth(null) / 2;

        if (animateOnChange) {
            g.drawImage(image, x, 4, x+32, 4+32, 0+imageOffset, 0+imageOffset, 32-imageOffset, 32-imageOffset, null);
        } else {
            g.drawImage(image, x, 4, null);
        }

        drawZoneValueOverlay((Graphics2D)g, cardCountString, x, getHeight(), image);

        if (isSelected()) {
            drawSelectedRoundBorder(g);
        }
    }

    private void drawSelectedFill(Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(MagicStyle.getTranslucentColor(MagicStyle.HIGHLIGHT_COLOR, 110));
        g2d.fillRoundRect(0, 0, getWidth(), getHeight()-1, 18, 18);
    }

    private void drawSelectedBorder(Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(4.0f));
        g2d.setColor(MagicStyle.HIGHLIGHT_COLOR);
        g2d.drawRect(0, 0, getWidth(), getHeight());
    }

    private void drawSelectedRoundBorder(Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(3.0f));
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 16, 16);
    }

    private BufferedImage getZoneIconAsImage() {
        if (zoneIconImage == null) {
            zoneIconImage = GraphicsUtilities.getCompatibleBufferedImage(32, 32, Transparency.TRANSLUCENT);
            Graphics2D g2d = (Graphics2D) zoneIconImage.getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            final Image iconImage = GraphicsUtilities.getConvertedIcon(IconImages.getIcon(magicIcon));
            g2d.drawImage(iconImage, 0, 0, this);
            g2d.dispose();
        }
        return zoneIconImage;
    }

    private void drawZoneValueOverlay(Graphics2D g2d, String text, int x, int y, final Image iconImage) {
        g2d.setFont(ZONE_FONT);
        g2d.setColor(Color.BLACK);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontRenderContext frc = g2d.getFontRenderContext();
        final int textHeight = Math.round(ZONE_FONT.getLineMetrics(text, frc).getHeight());
        final FontMetrics metrics = g2d.getFontMetrics(ZONE_FONT);
        int textWidth = metrics.stringWidth(text);
        int textX = x + ((iconImage.getWidth(null) / 2) - (textWidth / 2));
        int textY = y - 6;
        if (valueStyle == ValueStyle.OUTLINE) {
            GraphicsUtilities.drawStringWithOutline(g2d, text, textX, textY);
        } else {
            g2d.drawString(text, textX, textY);
        }
    }

    final void setNumberOfCardsInZone(final int cardCount) {
        final boolean isModified = !Integer.toString(cardCount).equals(cardCountString);
        cardCountString = Integer.toString(cardCount);
        if (isModified) {
            repaint();
        }
    }

    public void doAlertAnimation() {
        timeline1 = new Timeline();
        timeline1.setDuration(200);
        timeline1.addPropertyToInterpolate(
                Timeline.property("imageOffset").on(this).from(0).to(4));
        timeline1.playLoop(4, Timeline.RepeatBehavior.REVERSE);
    }

    public boolean isActive() {
        return isActive;
    }

}
