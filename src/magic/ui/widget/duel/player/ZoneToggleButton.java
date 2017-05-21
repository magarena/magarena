package magic.ui.widget.duel.player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.Set;
import javax.swing.JToggleButton;
import magic.data.MagicIcon;
import magic.model.MagicCardList;
import magic.model.MagicPlayerZone;
import magic.ui.MagicImages;
import magic.ui.helpers.ImageHelper;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.duel.animation.AnimationFx;
import magic.ui.widget.duel.animation.MagicAnimations;
import org.pushingpixels.trident.Timeline;


@SuppressWarnings("serial")
public class ZoneToggleButton extends JToggleButton {

    enum ValueStyle {
        NORMAL,
        OUTLINE
    }

    private static final Font ZONE_FONT = new Font("Dialog", Font.BOLD, 14);
    private static final Color DEFAULT_BORDER_COLOR = new Color(0, 0, 0, 100);
    private static final Color HIGHLIGHT_COLOR = MagicStyle.getTranslucentColor(MagicStyle.getRolloverColor(), 110);
    private static final Stroke DEFAULT_BORDER_STROKE = new BasicStroke(3.0f);

    private BufferedImage zoneIconImage = null;
    private final MagicIcon magicIcon;
    private String cardCountString = "0";
    private final ValueStyle valueStyle;
    private final MagicPlayerZone playerZone;
    private Timeline timeline1;
    private int imageOffset = 0;
    private boolean animateOnChange = false;
    private boolean isActive = false;
    private boolean isHighlighted = false;
    private boolean hasChoices = false;
    private MagicCardList cards;

    public int getImageOffset() {
        return imageOffset;
    }

    public void setImageOffset(int value) {
        this.imageOffset = value * 2;
        repaint();
    }

    // CTR
    private ZoneToggleButton(final MagicPlayerZone zone, final MagicCardList cards, final ValueStyle valueStyle, final boolean isActive) {
        this.playerZone = zone;
        this.magicIcon = zone.getIcon();
        this.valueStyle = valueStyle;
        this.animateOnChange = true;
        this.isActive = isActive;
        setCards(cards);
        setToolTipText(zone.getName());
        setEnabled(false);
        setFocusable(false);
        setRolloverEnabled(false);
        setContentAreaFilled(false);
        setMinimumSize(new Dimension(40, 60));
    }

    // CTR
    ZoneToggleButton(final MagicPlayerZone playerZone, final MagicCardList cards, final boolean isActive) {
        this(playerZone, cards, ValueStyle.NORMAL, isActive);
    }

    public MagicPlayerZone getPlayerZone() {
        return playerZone;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final Image image = getZoneIconAsImage();
        final int x = getWidth() / 2 - image.getWidth(null) / 2;

        if (isHighlighted) {
            drawSelectedFill(g);
        }

        if (animateOnChange) {
            g.drawImage(image, x, 4, x+32, 4+32, 0+imageOffset, 0+imageOffset, 32-imageOffset, 32-imageOffset, null);
        } else {
            g.drawImage(image, x, 4, null);
        }

        drawZoneValueOverlay((Graphics2D)g, cardCountString, x, getHeight(), image);

        if (hasChoices) {
            drawSelectedRoundBorder(g, MagicStyle.getTheme().getColor(Theme.COLOR_CHOICE_BORDER));
        }

        if (isSelected()) {
            drawSelectedRoundBorder(g);
        }

    }

    private void drawSelectedFill(Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(HIGHLIGHT_COLOR);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight()-1, 16, 16);
        drawSelectedRoundBorder(g, MagicStyle.getRolloverColor());
    }

    private void drawSelectedRoundBorder(Graphics g, Color aColor) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(DEFAULT_BORDER_STROKE);
        g2d.setColor(aColor);
        g2d.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 16, 16);
    }

    private void drawSelectedRoundBorder(Graphics g) {
        drawSelectedRoundBorder(g, DEFAULT_BORDER_COLOR);
    }

    private BufferedImage getZoneIconAsImage() {
        if (zoneIconImage == null) {
            zoneIconImage = ImageHelper.getCompatibleBufferedImage(32, 32, Transparency.TRANSLUCENT);
            Graphics2D g2d = (Graphics2D) zoneIconImage.getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            final Image iconImage = ImageHelper.getConvertedIcon(MagicImages.getIcon(magicIcon));
            g2d.drawImage(iconImage, 0, 0, this);
            g2d.dispose();
        }
        return zoneIconImage;
    }

    private void drawZoneValueOverlay(Graphics2D g2d, String text, int x, int y, final Image iconImage) {
        g2d.setFont(ZONE_FONT);
        g2d.setColor(Color.BLACK);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final FontMetrics metrics = g2d.getFontMetrics(ZONE_FONT);
        int textWidth = metrics.stringWidth(text);
        int textX = x + ((iconImage.getWidth(null) / 2) - (textWidth / 2));
        int textY = y - 6;
        if (valueStyle == ValueStyle.OUTLINE) {
            ImageHelper.drawStringWithOutline(g2d, text, textX, textY);
        } else {
            g2d.drawString(text, textX, textY);
        }
    }

    final void setCards(final MagicCardList cards) {
        this.cards = cards;
        setNumberOfCardsInZone(cards.size());
    }

    private void setNumberOfCardsInZone(final int cardCount) {
        final boolean isModified = !Integer.toString(cardCount).equals(cardCountString);
        cardCountString = Integer.toString(cardCount);
        if (isModified) {
            repaint();
        }
    }

    private void doAlertAnimation(int loopCount) {
        if (MagicAnimations.isOn(AnimationFx.ZBUTTON_PULSE)) {
            timeline1 = new Timeline();
            timeline1.setDuration(200);
            timeline1.addPropertyToInterpolate(
                    Timeline.property("imageOffset").on(this).from(0).to(4));
            timeline1.playLoop(loopCount, Timeline.RepeatBehavior.REVERSE);
        }
    }

    public void doAlertAnimation() {
        doAlertAnimation(6);
    }

    public boolean isActive() {
        return isActive;
    }

    void doHighlight(boolean b) {
        if (b != isHighlighted) {
            isHighlighted = b;
            if (isHighlighted) {
                doAlertAnimation(2);
            } else {
                if (timeline1 != null) {
                    timeline1.abort();
                    setImageOffset(0);
                }
            }
            repaint();
        }
    }

    void setValidChoices(Set<?> validChoices) {
        hasChoices = cards.stream().anyMatch(card -> validChoices.contains(card));
        repaint();
    }

    boolean isNot(MagicPlayerZone aZone) {
        return playerZone != aZone;
    }

}
