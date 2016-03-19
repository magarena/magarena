package magic.ui.card;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicObject;
import magic.model.MagicPermanent;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.duel.viewer.info.CardViewerInfo;
import magic.ui.duel.SwingGameController;
import magic.ui.duel.animation.AnimationFx;
import magic.ui.duel.animation.MagicAnimations;
import magic.ui.theme.AbilityIcon;
import magic.ui.utility.GraphicsUtils;
import magic.ui.widget.FontsAndBorders;
import magic.utility.MagicSystem;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.ease.Spline;

@SuppressWarnings("serial")
public class AnnotatedCardPanel extends JPanel {

    private static final Color BCOLOR = new Color(0, 0, 0, 0);
    private static final Font PT_FONT = new Font("Serif", Font.BOLD, 16);
    private static final Color GRADIENT_FROM_COLOR = Color.WHITE;
    private static final Color GRADIENT_TO_COLOR = Color.WHITE.darker();

    private final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private MagicObject magicObject = null;
    private Timeline fadeInTimeline;
    private float opacity = 1.0f;
    private SwingGameController controller;
    private BufferedImage cardImage;
    private String modifiedPT;
    private Dimension imageOnlyPopupSize;
    private Dimension popupSize;
    private List<CardIcon> cardIcons = new ArrayList<>();
    private final List<Shape> iconShapes = new ArrayList<>();
    private Timer visibilityTimer;
    private BufferedImage popupImage;
    private final MagicInfoWindow infoWindow = new MagicInfoWindow();
    private final Rectangle containerRect;
    private boolean preferredVisibility = false;

    public AnnotatedCardPanel() {

        this.containerRect = getWindowRect();

        setOpaque(false);

        setDelayedVisibilityTimer();

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                if (event.getWheelRotation() > 0) { // rotate mousewheel back or towards you.
                    setVisible(false);
                }
            }
        });

        setMouseMovedListener();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                infoWindow.setVisible(false);
            }
        });

        setVisible(false);

    }

    private static Rectangle getWindowRect() {
        return new Rectangle(
                    ScreenController.getMainFrame().getLocationOnScreen(),
                    ScreenController.getMainFrame().getSize());
    }

    private void setDelayedVisibilityTimer() {
        visibilityTimer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (!AnnotatedCardPanel.this.isVisible() && preferredVisibility == true) {
                    showPopup();
                } else if (preferredVisibility == false) {
                    setVisible(false);
                    magicObject = null;
                    opacity = 0f;
                }
            }
        });
        visibilityTimer.setRepeats(false);
    }

    public void showDelayed(final int delay) {
        assert SwingUtilities.isEventDispatchThread();
        preferredVisibility = true;
        visibilityTimer.setInitialDelay(delay);
        visibilityTimer.restart();
    }

    /**
     * Hides the card image panel after {@code delay} milliseconds.
     * <p>
     * The hide request is cancelled if a request to show a card image is received
     * before the delay expires (see {@link hideDelayed()}).
     *
     * @param delay the time in milliseconds to wait before hiding the card panel.
     */
    private void hideCardPanel(int delay) {
        assert SwingUtilities.isEventDispatchThread();
        preferredVisibility = false;
        visibilityTimer.setInitialDelay(delay);
        visibilityTimer.restart();
    }

    /**
     * Requests that the card image panel is hidden after 100 milliseconds.
     * <p>
     * This method is primarily used to prevent the popup flickering on and off
     * as the mouse is moved over cards on the battlefield and in the player zones.
     */
    public void hideDelayed() {
        hideCardPanel(100);
    }

    public void hideNoDelay() {
        hideCardPanel(0);
    }

    private void showPopup() {
        if (MagicAnimations.isOn(AnimationFx.CARD_FADEIN)) {
            if (opacity == 0f) {
                fadeInTimeline = new Timeline();
                fadeInTimeline.setDuration(200);
                fadeInTimeline.setEase(new Spline(0.8f));
                fadeInTimeline.addPropertyToInterpolate(
                    Timeline.property("opacity")
                    .on(this)
                    .from(0.0f)
                    .to(1.0f));
                fadeInTimeline.play();
            } else {
                opacity = 1.0f;
            }
        } else {
            opacity = 1.0f;
        }
        setVisible(true);
    }

    public void setCard(final MagicCardDefinition cardDef, final Dimension containerSize) {
        this.cardImage = getCardImage(cardDef);
        // <--- order important
        cardIcons = AbilityIcon.getIcons(cardDef);
        setPanelSize(containerSize);
        // --->
        this.magicObject = null;
        this.modifiedPT = "";
        setPopupImage();
    }

    public void setCard(CardViewerInfo cardInfo, final Dimension containerSize) {
        this.cardImage = cardInfo.getImage();
        // <--- order important
        cardIcons = AbilityIcon.getIcons(cardInfo.getMagicObject());
        setPanelSize(containerSize);
        // --->
        this.modifiedPT = getModifiedPT(cardInfo.getMagicObject());
        this.magicObject = cardInfo.getMagicObject();
        setPopupImage();
    }

    /**
     * Both MagicPermanent and MagicCard are instances of MagicObject.
     * MagicObject returns the correct abilities of a card including any
     * additional abilities added during game-play (via enchantments, etc).
     */
    public void setCard(final MagicObject magicObject, final Dimension containerSize) {
        this.cardImage = getCardImage(magicObject);
        // <--- order important
        cardIcons = AbilityIcon.getIcons(magicObject);
        setPanelSize(containerSize);
        // --->
        this.magicObject = magicObject;
        this.modifiedPT = getModifiedPT(magicObject);
        setPopupImage();
    }

    private void setPopupImage() {
        // create a blank canvas of the appropriate size.
        popupImage = GraphicsUtils.getCompatibleBufferedImage(getWidth(), getHeight(), Transparency.TRANSLUCENT);
        final Graphics g = popupImage.getGraphics();
        final Graphics2D g2d = (Graphics2D)g;
        // don't overwrite original image with modified PT overlay, use a copy.
        final BufferedImage cardCanvas = !modifiedPT.isEmpty() ? getImageCopy(cardImage) : cardImage;
        // draw modified PT on original image so it is scaled properly.
        drawPowerToughnessOverlay(cardCanvas);
        // scale card image if required.
        final BufferedImage scaledImage = GraphicsUtils.scale(cardCanvas, imageOnlyPopupSize.width, imageOnlyPopupSize.height);
        //
        // draw card image onto popup canvas, right-aligned.
        g.drawImage(scaledImage, popupSize.width - imageOnlyPopupSize.width, 0, this);
        //
        drawIcons(g2d);

        if (MagicSystem.isDevMode() && magicObject != null && magicObject instanceof MagicPermanent) {
            final MagicPermanent card = (MagicPermanent) magicObject;
            g.setFont(FontsAndBorders.FONT1);
            GraphicsUtils.drawStringWithOutline(g, Long.toString(card.getCard().getId()), 2, 14);
        }

    }

    private BufferedImage getImageCopy(final BufferedImage image) {
        final BufferedImage imageCopy = new BufferedImage(image.getWidth(), image.getHeight(), image.getTransparency());
        final Graphics g = imageCopy.createGraphics();
        g.drawImage(image, 0, 0, null);
        return imageCopy;
    }

    private BufferedImage getPTOverlay(Color maskColor) {

        // create fixed size empty transparent image.
        final BufferedImage overlay = GraphicsUtils.getCompatibleBufferedImage(312, 445, Transparency.TRANSLUCENT);
        final Graphics2D g2d = overlay.createGraphics();

        // use a rectangular opaque mask to hide original P/T.
        final Rectangle mask = new Rectangle(254, 408, 38, 18);
        g2d.setColor(maskColor);
        g2d.fillRect(mask.x, mask.y, mask.width, mask.height);

        // draw the modified P/T on top of the mask.
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.setFont(PT_FONT);
        final FontMetrics metrics = g2d.getFontMetrics();
        final int ptWidth = metrics.stringWidth(modifiedPT);
        g2d.drawString(modifiedPT, mask.x + ((mask.width - ptWidth) / 2), mask.y + 14);
        g2d.dispose();

        return overlay;
    }

    /**
     * Draws modified P/T onto a 312 x 445 overlay which is then scaled to-
     * and drawn on top of the original card image.
     */
    private void drawPowerToughnessOverlay(final BufferedImage cardImage) {

        if (modifiedPT.isEmpty())
            return;

        // get approximate background color of P/T box on card.
        final Color maskColor = getPTOverlayBackgroundColor(
            cardImage,
            (int)(cardImage.getWidth() * 0.929d),
            (int)(cardImage.getHeight() * 0.919d)
        );

        // get transparent P/T overlay and size to card image.
        final BufferedImage overlay = GraphicsUtils.scale(
            getPTOverlay(maskColor),
            cardImage.getWidth(),
            cardImage.getHeight()
        );

        // draw tranparent P/T overlay on top of original card.
        final Graphics2D g2d = cardImage.createGraphics();
        g2d.drawImage(overlay, 0, 0, null);
        g2d.dispose();

    }

    private Color getPTOverlayBackgroundColor(BufferedImage image, final int x, final int y) {
        final int rgb = image.getRGB(x, y);
        final int r = (rgb >> 16) & 0xFF;
        final int g = (rgb >> 8) & 0xFF;
        final int b = (rgb & 0xFF);
        return new Color(r, g, b);
    }

    private BufferedImage getCardImage(final MagicCardDefinition cardDef) {
        return MagicImages.getCardImageUseCache(cardDef);
    }

    private BufferedImage getCardImage(final MagicObject magicObject) {
        if (magicObject instanceof MagicPermanent) {
            final MagicPermanent perm = (MagicPermanent)magicObject;
            return canRevealTrueFace(perm) ?
                getCardImage(perm.getRealCardDefinition()) :
                getCardImage(perm.getCardDefinition());
        } else {
            return getCardImage(magicObject.getCardDefinition());
        }
    }

    /**
     * primarily used to determine whether a face-down card will
     * show its hidden face when displaying mouse-over popup.
     */
    private boolean canRevealTrueFace(final MagicPermanent perm) {
        return perm.getController().isHuman() || MagicSystem.isAiVersusAi();
    }

    private String getModifiedPT(final MagicObject magicObject) {
        if (magicObject instanceof MagicPermanent) {
            final MagicPermanent permanent = (MagicPermanent)magicObject;
            final String permanentPT = permanent.getPower() + "/" + permanent.getToughness();
            final String cardDefPT = permanent.getCardDefinition().getCardPower() + "/" + permanent.getCardDefinition().getCardToughness();
            if (!permanentPT.equals(cardDefPT)) {
                return permanent.getPower() + "/" + permanent.getToughness();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public MagicObject getMagicObject() {
        return magicObject;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (opacity < 1.0f) {
            final Graphics2D g2d = (Graphics2D)g;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2d.setColor(BCOLOR);
            Rectangle r = g2d.getClipBounds();
            g2d.fillRect(r.x, r.y, r.width, r.height);
        }
        if (popupImage != null) {
            g.drawImage(popupImage, 0, 0, this);
        }
        super.paintComponent(g);
    }

    public void setOpacity(final float opacity) {
        this.opacity = opacity;
        repaint();
    }
    public float getOpacity() {
        return opacity;
    }

    @Override
    public void setVisible(final boolean isVisible) {
        super.setVisible(isVisible);
        if (controller != null && CONFIG.isGamePausedOnPopup()) {
            final boolean aiHasPriority = controller.getViewerInfo().getPriorityPlayer().isAi();
            controller.setGamePaused(isVisible && aiHasPriority);
        }
    }

    private void setPanelSize(final Dimension containerSize) {
        final Dimension preferredSize = MagicImages.getPreferredImageSize(cardImage);
        // keep scaled card in correct proportion.
        final double cardAspectRatio = (double)preferredSize.height / preferredSize.width;
        final int actualHeight = Math.min(containerSize.height, preferredSize.height);
        final int actualWidth = (int)(actualHeight / cardAspectRatio);
        final Dimension actualCardImageSize = new Dimension(actualWidth, actualHeight);
        imageOnlyPopupSize = actualCardImageSize;
        final Dimension annotatedPopupSize = new Dimension(actualCardImageSize.width + 26, actualCardImageSize.height);
        popupSize = cardIcons.isEmpty() ? imageOnlyPopupSize : annotatedPopupSize;
        //
        setPreferredSize(popupSize);
        setMaximumSize(popupSize);
        setMinimumSize(popupSize);
        setSize(popupSize);
    }

    private void drawIcons(final Graphics2D g2d) {
        if (!cardIcons.isEmpty()) {
            final int BORDER_WIDTH = 2;
            final BasicStroke BORDER_STROKE = new BasicStroke(BORDER_WIDTH);
            final Stroke defaultStroke = g2d.getStroke();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // draw icons
            int y = 10;
            int x = 0;
            final int ICON_WIDTH = 36;
            final int ICON_HEIGHT = 32;
            final int CORNER_ARC = 16;
            final GradientPaint PAINT_COLOR = new GradientPaint(0, 0, GRADIENT_FROM_COLOR, ICON_WIDTH, 0, GRADIENT_TO_COLOR);
            iconShapes.clear();
            for (CardIcon cardIcon : cardIcons) {
                // icon bounds should be relative to CardPopupPanel.
                final Rectangle2D iconShapeRect = new Rectangle2D.Double((double)x, (double)y, (double)ICON_WIDTH, 32d);
                iconShapes.add(iconShapeRect);
                //
                final Rectangle rect = new Rectangle(x, y, ICON_WIDTH, ICON_HEIGHT);
                g2d.setPaint(PAINT_COLOR);
                g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, CORNER_ARC, CORNER_ARC);
                g2d.setPaint(Color.BLACK);
                g2d.setStroke(BORDER_STROKE);
                g2d.drawRoundRect(rect.x, rect.y, rect.width, rect.height, CORNER_ARC, CORNER_ARC);
                g2d.setStroke(defaultStroke);
                //
                final ImageIcon icon = cardIcon.getIcon();
                final int iconOffsetX = (ICON_WIDTH / 2) - (icon.getIconWidth() / 2);
                final int iconOffsetY = 16 - (icon.getIconHeight() / 2);
                icon.paintIcon(this, g2d, x + iconOffsetX, y + iconOffsetY);
                y += ICON_HEIGHT + 1;
            }
        }
    }

    private void setMouseMovedListener() {
        addMouseMotionListener(new MouseMotionAdapter() {
            private CardIcon currentIcon;
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!cardIcons.isEmpty() && !iconShapes.isEmpty()) {
                    final Shape lastShape = iconShapes.get(iconShapes.size() - 1);
                    final Dimension lastShapeSize = new Dimension(lastShape.getBounds().width, lastShape.getBounds().y + lastShape.getBounds().height);
                    final Rectangle rect = new Rectangle(0, 0, lastShapeSize.width, lastShapeSize.height);
                    if (rect.contains(e.getPoint())) {
                        for (Shape iconShape : iconShapes) {
                            if (iconShape.contains(e.getPoint())) {
                                final CardIcon cardIcon = cardIcons.get(iconShapes.indexOf(iconShape));
                                if (currentIcon != cardIcon) {
                                    currentIcon = cardIcon;
                                    showInfoTip(cardIcon, new Point(e.getXOnScreen(), e.getYOnScreen()));
                                }
                                break;
                            }
                        }
                    } else if (infoWindow.isVisible()) {
                        boolean hideInfo = true;
                        currentIcon = null;
                        if (hideInfo) {
                            infoWindow.setVisible(false);
                        }
                    }
                }
            }
        });
    }

    private void showInfoTip(final CardIcon cardIcon, final Point position) {
        //
        infoWindow.setTitle(cardIcon.getName());
        infoWindow.setDescription("<html>" + cardIcon.getDescription().replaceAll("\r\n|\r|\n", "<br>") + "</html>");
        infoWindow.pack();
        //
        final int infoWidth = infoWindow.getWidth();
        int mx = position.x + 10;
        final int sx = containerRect.x;
        final int sw = containerRect.width - 10;
        if (mx + infoWidth > sx + sw) {
            mx = mx - ((mx + infoWidth) - (sx + sw));
        }
        final Point p = new Point(mx, position.y + 10);
        infoWindow.setLocation(p);
        infoWindow.setAlwaysOnTop(true);
        infoWindow.setVisible(true);
    }

    public void setController(SwingGameController aController) {
        this.controller = aController;
        this.controller.setImageCardViewer(this);
    }

}
