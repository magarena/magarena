package magic.ui.widget.duel.viewer;

import magic.ui.utility.ImageDrawingUtils;
import magic.ui.IChoiceViewer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.model.MagicType;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.ui.MagicImages;
import magic.ui.helpers.ImageHelper;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.duel.viewerinfo.CardViewerInfo;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.FontsAndBorders;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
public class ImageCardListViewer extends JPanel implements IChoiceViewer {

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();
    private static final MagicCardList EMPTY_CARD_LIST=new MagicCardList();
    private static final int CARD_WIDTH=100;
    private static final int CARD_HEIGHT=140;
    private static final int SPACING=10;
    private static final BasicStroke MOUSE_OVER_STROKE = new BasicStroke(2);
    private static final Color MOUSE_OVER_COLOR = MagicStyle.getRolloverColor();
    private static final Color MOUSE_OVER_TCOLOR = MagicStyle.getTranslucentColor(MOUSE_OVER_COLOR, 20);

    private final SwingGameController controller;
    private MagicCardList cardList;
    private List<Point> cardPoints;
    private Set<?> validChoices;
    private boolean showInfo;
    private int currentCardIndex = 0;
    private int cardStep = 0;

    public ImageCardListViewer(final SwingGameController controller) {

        setOpaque(false);

        this.controller=controller;
        cardList=EMPTY_CARD_LIST;
        cardPoints = new ArrayList<>();
        validChoices=Collections.emptySet();

        setMouseListener();
        setMouseMotionListener();
        setMouseWheelListener();

        controller.registerChoiceViewer(this);
    }

    private void setMouseWheelListener() {
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                final int cardIndex = getCardIndexAt(event.getX(), event.getY());
                if (event.getWheelRotation() < 0) { // rotate mousewheel forward
                    if (cardIndex >= 0) {
                        showCardPopup(cardIndex);
                    }
                } else if (event.getWheelRotation() > 0) { // rotate mousewheel back
                    if (cardIndex >= 0) {
                        controller.hideInfo();
                    }
                }
            }
        });
    }

    private void setMouseMotionListener() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(final MouseEvent event) {
                final int cardIndex = getCardIndexAt(event.getX(), event.getY());
                final boolean isCardChanged = (currentCardIndex != cardIndex);
                if (cardIndex >= 0) {
                    if (isCardChanged) {
                        if (!CONFIG.isMouseWheelPopup() || controller.isPopupVisible()) {
                            showCardPopup(cardIndex);
                        } else {
                            // handles case where mousewheel popup is enabled and the mouseExited
                            // event does not fire because cards overlap.
                            controller.hideInfo();
                        }
                    }
                } else if (isCardChanged) {
                    controller.hideInfo();
                }
                if (isCardChanged) {
                    // highlight new card mouse cursor is over.
                    currentCardIndex = cardIndex;
                    repaint();
                }
            }
        });
    }

    private void setMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent event) {
                if (CONFIG.isTouchscreen()) {
                    final int cardIndex = getCardIndexAt(event.getX(), event.getY());
                    final boolean isDoubleClick = event.getClickCount() == 2;
                    if (cardIndex >= 0 && isDoubleClick) {
                        controller.processClick(cardList.get(cardIndex));
                        controller.hideInfo();
                    }
                }
            }
            @Override
            public void mouseReleased(MouseEvent event) {
                if (!CONFIG.isTouchscreen()) {
                    if (ImageCardListViewer.this.contains(event.getPoint())) {
                        if (SwingUtilities.isRightMouseButton(event)) {
                            controller.actionKeyPressed();
                        } else if (SwingUtilities.isLeftMouseButton(event)) {
                            final int cardIndex = getCardIndexAt(event.getX(), event.getY());
                            if (cardIndex >= 0) {
                                controller.processClick(cardList.get(cardIndex));
                            }
                        }
                    }
                }
            }
            @Override
            public void mouseExited(final MouseEvent event) {
                controller.hideInfo();
                // unselect card and remove highlight.
                currentCardIndex = -1;
                repaint();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                // highlight card mouse cursor is over.
                repaint();
            }
        });
    }

    private void showCardPopup(int index) {
        final MagicCard card=cardList.get(index);
        final Point pointOnScreen=getLocationOnScreen();
        final Point point=cardPoints.get(index);
        final Rectangle rect=
                new Rectangle(pointOnScreen.x+point.x,pointOnScreen.y+point.y,CARD_WIDTH,CARD_HEIGHT);
        controller.viewCardPopup(card, rect, true);
    }

    private int getCardIndexAt(final int x,final int y) {
        for (int index=cardPoints.size()-1;index>=0;index--) {
            final Point point=cardPoints.get(index);
            if (x>=point.x&&y>=point.y&&x<point.x+CARD_WIDTH&&y<point.y+CARD_HEIGHT) {
                return index;
            }
        }
        return -1;
    }

    public void setCardList(final MagicCardList aCardList,final boolean aShowInfo) {
        final List<Point> tCardPoints=new ArrayList<Point>();
        final int cardCount = aCardList.size();
        final int preferredWidth = CARD_WIDTH * cardCount + (cardCount - 1) * SPACING;
        int availableWidth = getWidth();
        int step;
        if (preferredWidth <availableWidth ||cardCount ==1) {
            int x=0;
            step = CARD_WIDTH + SPACING;
            for (int index=0;index<cardCount ;index++) {

                tCardPoints.add(new Point(x,1));
                x+=step;
            }
        } else {
            step = (availableWidth - CARD_WIDTH) / (cardCount - 1);
            int x = 0;
            for (int index=0;index<cardCount ;index++) {

                tCardPoints.add(new Point(x, 1));
                x += step;
            }
        }
        this.cardStep = step > CARD_WIDTH ? CARD_WIDTH : step;
        this.cardList=aCardList;
        this.cardPoints=tCardPoints;
        this.showInfo=aShowInfo;
    }

    @Override
    public void paintComponent(final Graphics g) {
        if (cardList.isEmpty()) {
            return;
        }

        g.setFont(FontsAndBorders.FONT1);
        final FontMetrics metrics=g.getFontMetrics();
        final Graphics2D g2d=(Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        final Stroke defaultStroke = g2d.getStroke();

        final Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mousePoint, this);
        Rectangle mouseOverRect = new Rectangle();

        for (int index=0; index < cardList.size(); index++) {
            final MagicCard card=cardList.get(index);
            final MagicCardDefinition cardDefinition=card.getCardDefinition();
            final Point point=cardPoints.get(index);
            final int x1=point.x;
            final int y1=point.y;
            final int x2=point.x+CARD_WIDTH;
            final int y2=point.y+CARD_HEIGHT;

            final BufferedImage image = ImageHelper.scale(
                MagicImages.getCardImage(cardDefinition),
                CARD_WIDTH,
                CARD_HEIGHT
            );
            g2d.drawImage(image, x1, y1, this);

            ImageDrawingUtils.drawCardId(g, card.getId(), x1, 0);

            //draw the overlay icons
            if (showInfo) {
                if (cardDefinition.isLand()) {
                    ImageDrawingUtils.drawManaInfo(
                        g,
                        this,
                        cardDefinition.getManaActivations(),
                        card.hasType(MagicType.Snow),
                        x1+1,
                        y2-17
                    );
                } else {
                    ImageDrawingUtils.drawCostInfo(g,this,card.getGameCost(),x1,x2-1,y1+2);
                }
                if (cardDefinition.isCreature()) {
                    ImageDrawingUtils.drawAbilityInfo(g,this,cardDefinition.genAbilityFlags(),x1+2,y2-18);
                    final String pt = card.genPowerToughness().toString();
                    final int ptWidth=metrics.stringWidth(pt);
                    ImageDrawingUtils.drawCreatureInfo(g,metrics,pt,ptWidth,"","",x2-ptWidth-4,y2-18,false);
                }
            }

            //show that card is a valid choice
            if (validChoices.contains(card)) {
                if (CONFIG.isHighlightOverlay() ||
                    (CONFIG.isHighlightTheme() &&
                    ThemeFactory.getInstance().getCurrentTheme().getOptionUseOverlay())) {
                        final Color choiceColor = ThemeFactory.getInstance().getCurrentTheme().getChoiceColor();
                        //draw a transparent overlay of choiceColor
                        g2d.setPaint(choiceColor);
                        g2d.fillRect(x1-1,y1-1,CARD_WIDTH+2,CARD_HEIGHT+2);
                }
                else if (!CONFIG.isHighlightNone()){
                    final Color choiceColor = ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_CHOICE_BORDER);
                    //draw a one pixel border of choiceColor
                    g2d.setPaint(new Color(choiceColor.getRGB()));
                    final Stroke stroke = g2d.getStroke();
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRect(x1,y1,CARD_WIDTH-1,CARD_HEIGHT);
                    g2d.setStroke(stroke);
                }
            }
            final int highlightWidth = (index == cardList.size()-1) ? CARD_WIDTH : cardStep;
            final Rectangle rect = new Rectangle(x1, y1, highlightWidth, y2 - y1);
            if (rect.contains(mousePoint)) {
                mouseOverRect = rect;
            }
        }
        paintMouseOverHighlight2(g2d, mouseOverRect, defaultStroke);
    }

    /**
     * draw filled rectangle using translucent color over visible portion of card.
     */
    private void paintMouseOverHighlight2(final Graphics2D g2d, final Rectangle rect, final Stroke defaultStroke) {
        g2d.setPaint(MOUSE_OVER_TCOLOR);
        g2d.fillRect(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
    }

    @Override
    public void showValidChoices(final Set<?> aValidChoices) {
        this.validChoices=aValidChoices;
        repaint();
    }

    public Point getCardPosition(final CardViewerInfo cardInfo) {
        Point cardPosition = null;
        for (int index=0; index < cardList.size(); index++) {
            final MagicCard card = cardList.get(index);
            if (card.getId() == cardInfo.getId()) {
                cardPosition = cardPoints.get(index);
                break;
            }
        }
        if (cardPosition != null) {
            cardPosition = new Point(
                    cardPosition.x + getParent().getParent().getLocation().x + 45,
                    cardPosition.y + getParent().getLocation().y);
        }
        return cardPosition;
    }

    public Dimension getCardSize() {
        return new Dimension(CARD_WIDTH, CARD_HEIGHT);
    }
}
