package magic.ui.duel.viewer;

import magic.ui.duel.PermanentViewerInfo;
import magic.ui.utility.ImageDrawingUtils;
import magic.data.GeneralConfig;
import magic.ui.CachedImagesProvider;
import magic.ui.MagicImages;
import magic.model.MagicType;
import magic.model.MagicAbility;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;

import javax.swing.JPanel;

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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.SwingUtilities;
import magic.ui.CardImagesProvider;
import magic.data.MagicIcon;
import magic.ui.utility.GraphicsUtils;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
public class ImagePermanentViewer extends JPanel {

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();
    private static final int LOGICAL_X_MARGIN=50;
    private static final int LOGICAL_Y_MARGIN=70;
    private static final Color MOUSE_OVER_COLOR = MagicStyle.getRolloverColor();
    private static final Color MOUSE_OVER_TCOLOR = MagicStyle.getTranslucentColor(MOUSE_OVER_COLOR, 30);

    private final ImagePermanentsViewer viewer;
    public final PermanentViewerInfo permanentInfo;
    public final List<PermanentViewerInfo> linkedInfos;
    private final Dimension logicalSize;
    private final List<Rectangle> linkedLogicalRectangles;
    private List<Rectangle> linkedScreenRectangles;
    private Point logicalPosition;
    private int logicalRow=1;
    private boolean isMouseOver = false;
    private static int currentCardIndex = -1;
    private long highlightedId = 0;

    public ImagePermanentViewer(final ImagePermanentsViewer viewer,final PermanentViewerInfo permanentInfo) {
        this.viewer=viewer;
        this.permanentInfo=permanentInfo;
        linkedInfos=new ArrayList<PermanentViewerInfo>();
        buildLinkedPermanents(linkedInfos,permanentInfo);
        linkedLogicalRectangles=new ArrayList<Rectangle>();
        logicalSize=calculateLogicalSize(linkedLogicalRectangles);
        linkedScreenRectangles=Collections.emptyList();

        setOpaque(false);

        setMouseListener();
        setMouseMotionListener();
        setMouseWheelListener();

    }

    private void setMouseWheelListener() {
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                final int cardIndex = getPermanentInfoIndexAt(event.getX(), event.getY());
                if (cardIndex >= 0) {
                    if (event.getWheelRotation() < 0) { // rotate mousewheel forward
                        showCardPopup(cardIndex);
                    } else if (event.getWheelRotation() > 0) { // rotate mousewheel back
                        viewer.getController().hideInfo();
                    }
                }
            }
        });
    }

    private void setMouseMotionListener() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(final MouseEvent event) {
                final int cardIndex = getPermanentInfoIndexAt(event.getX(), event.getY());
                final boolean isCardChanged = (currentCardIndex != cardIndex);
                if (cardIndex >= 0) {
                    if (isCardChanged) {
                        if (!CONFIG.isMouseWheelPopup() || viewer.getController().isPopupVisible()) {
                            showCardPopup(cardIndex);
                        }
                    }
                } else {
                    viewer.getController().hideInfo();
                }
                currentCardIndex = cardIndex;
                if (linkedScreenRectangles.size() > 1) {
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
                    final int cardIndex = getPermanentInfoIndexAt(event.getX(), event.getY());
                    final boolean isDoubleClick = event.getClickCount() == 2;
                    if (cardIndex >= 0 && isDoubleClick) {
                        viewer.getController().processClick(linkedInfos.get(cardIndex).permanent);
                        viewer.getController().hideInfo();
                    }
                }
            }
            @Override
            public void mouseReleased(MouseEvent event) {
                if (!CONFIG.isTouchscreen()) {
                    final int cardIndex = getPermanentInfoIndexAt(event.getX(), event.getY());
                    if (cardIndex >= 0 && SwingUtilities.isLeftMouseButton(event)) {
                        viewer.getController().processClick(linkedInfos.get(cardIndex).permanent);
                    }
                }
            }
            @Override
            public void mouseExited(final MouseEvent event) {
                viewer.getController().hideInfo();
                currentCardIndex = -1;
                isMouseOver = false;
                repaint();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                isMouseOver = true;
                repaint();
            }
        });
    }

    private void showCardPopup(int index) {
        final PermanentViewerInfo info=linkedInfos.get(index);
        final Point pointOnScreen=getLocationOnScreen();
        final Rectangle rect=new Rectangle(linkedScreenRectangles.get(index));
        rect.x+=pointOnScreen.x;
        rect.y+=pointOnScreen.y;
        viewer.getController().viewCardPopup(info.permanent, info.index, rect, true);
    }

    private int getPermanentInfoIndexAt(final int x,final int y) {
        for (int index=linkedScreenRectangles.size()-1;index>=0;index--) {
            final Rectangle rect=linkedScreenRectangles.get(index);
            if (x>=rect.x&&y>=rect.y&&x<rect.x+rect.width&&y<rect.y+rect.height) {
                return index;
            }
        }
        return -1;
    }

    private void buildLinkedPermanents(final List<PermanentViewerInfo> aLinkedInfos,final PermanentViewerInfo info) {
        for (final PermanentViewerInfo blocker : info.blockers) {
            buildLinkedPermanents(aLinkedInfos,blocker);
        }
        aLinkedInfos.addAll(info.linked);
        aLinkedInfos.add(info);
    }

    private Dimension calculateLogicalSize(final List<Rectangle> aLinkedLogicalRectangles) {
        int width=0;
        int height=0;
        int x=-LOGICAL_X_MARGIN;
        final Dimension imageSize = GraphicsUtils.getMaxCardImageSize();
        for (final PermanentViewerInfo linkedInfo : linkedInfos) {
            x+=LOGICAL_X_MARGIN;
            final int y=linkedInfo.lowered?LOGICAL_Y_MARGIN:0;
            final Rectangle rect;
            if (linkedInfo.tapped) {
                width = Math.max(width, x + imageSize.height);
                height = Math.max(height, y + imageSize.width);
                rect = new Rectangle(x, y, imageSize.height, imageSize.width);
            } else {
                width = Math.max(width, x + imageSize.width);
                height = Math.max(height, y + imageSize.height);
                rect = new Rectangle(x, y, imageSize.width, imageSize.height);
            }
            aLinkedLogicalRectangles.add(rect);
        }
        return new Dimension(width,height);
    }

    @Override
    public void setSize(final int width,final int height) {
        super.setSize(width,height);

        linkedScreenRectangles=new ArrayList<Rectangle>();
        for (final Rectangle logicalRect : linkedLogicalRectangles) {
            final Rectangle screenRect=new Rectangle();
            screenRect.x=(logicalRect.x*width)/logicalSize.width;
            screenRect.y=(logicalRect.y*height)/logicalSize.height;
            screenRect.width=(logicalRect.width*width)/logicalSize.width;
            screenRect.height=(logicalRect.height*height)/logicalSize.height;
            linkedScreenRectangles.add(screenRect);
        }
    }

    public int getPosition() {
        return permanentInfo.position;
    }

    public Dimension getLogicalSize() {
        return logicalSize;
    }

    public void setLogicalPosition(final Point logicalPosition) {
        this.logicalPosition=logicalPosition;
    }

    public Point getLogicalPosition() {
        return logicalPosition;
    }

    public void setLogicalRow(final int logicalRow) {
        this.logicalRow=logicalRow;
    }

    public int getLogicalRow() {
        return logicalRow;
    }

    @Override
    public void paintComponent(final Graphics g) {

        final Dimension imageSize = GraphicsUtils.getMaxCardImageSize();

        g.setFont(FontsAndBorders.FONT1);
        final FontMetrics metrics = g.getFontMetrics();
        
        final Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        final Stroke defaultStroke = g2d.getStroke();

        final CardImagesProvider imageCache = CachedImagesProvider.getInstance();
        
        for (int index = 0; index < linkedScreenRectangles.size(); index++) {
            
            final PermanentViewerInfo linkedInfo = linkedInfos.get(index);
            final BufferedImage image = imageCache.getImage(linkedInfo.cardDefinition, linkedInfo.index, false);
            
            final Rectangle linkedRect = linkedScreenRectangles.get(index);
            final int x1 = linkedRect.x;
            final int y1 = linkedRect.y;
            final int x2 = x1 + linkedRect.width - 1;
            final int y2 = y1 + linkedRect.height - 1;

            //
            // Draw card image.
            if (linkedInfo.tapped) {
                final AffineTransform transform = getTappedTransform(linkedRect, imageSize, new Point(x1, y1));
                g2d.drawImage(image, transform, this);
            } else {
                g.drawImage(image, x1, y1, x2, y2, 0, 0, imageSize.width, imageSize.height, this);
            }

            ImageDrawingUtils.drawCardId(g, linkedInfo.permanent.getCard().getId(), 0, 0);

            // Add overlays, unless card image size is so small the overlays would be unreadable.
            if (linkedRect.height > CONFIG.getOverlayMinimumHeight()) {

                int ax = x1 + 1;
                final int ay = y2 - 17;
                // Counters
                if (linkedInfo.permanent.hasCounters()) {
                    ax = ImageDrawingUtils.drawCountersInfo(g, this, linkedInfo.permanent, ax, ay);
                }

                // Common combat ability icons.
                if (linkedInfo.creature) {
                    if (linkedInfo.canNotTap) {
                        g.drawImage(MagicImages.getIcon(MagicIcon.CANNOTTAP).getImage(), ax, ay, this);
                        ax += 16;
                    }
                    final Set<MagicAbility> abilityFlags = linkedInfo.abilityFlags;
                    ax = ImageDrawingUtils.drawAbilityInfo(g, this, abilityFlags, ax, ay);
                }

                // Mana symbols
                if (linkedInfo.permanent.getManaActivations().size() > 0) {
                    ax = ImageDrawingUtils.drawManaInfo(
                        g, 
                        this, 
                        linkedInfo.permanent.getManaActivations(), 
                        linkedInfo.permanent.hasType(MagicType.Snow),
                        ax, 
                        ay
                    );
                }

                // Power, toughness, damage
                final String pt = linkedInfo.powerToughness;
                if (!pt.isEmpty()) {
                    final String damage = linkedInfo.damage > 0 ? String.valueOf(linkedInfo.damage) : "";
                    final boolean isDamage = damage.length() > 0;
                    final int ptWidth = metrics.stringWidth(pt);
                    if (linkedInfo.blocking) {
                        ImageDrawingUtils.drawCreatureInfo(g, metrics, pt, ptWidth, damage, x1, y1, false);
                    } else {
                        ImageDrawingUtils.drawCreatureInfo(g, metrics, pt, ptWidth, damage, x2 - ptWidth - 4, y2 - (isDamage ? 32 : 18), true);
                    }
                }
            }

            // Valid choice selection highlight
            if (viewer.isValidChoice(linkedInfo)) {
                if (CONFIG.isHighlightOverlay() ||
                    (CONFIG.isHighlightTheme() &&
                    ThemeFactory.getInstance().getCurrentTheme().getOptionUseOverlay())) {
                    final Color choiceColor = viewer.getController().isCombatChoice() ?
                        ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_COMBAT_CHOICE) :
                        ThemeFactory.getInstance().getCurrentTheme().getChoiceColor();

                    //draw a transparent overlay of choiceColor
                    g2d.setPaint(choiceColor);
                    g2d.fillRect(x1-1,y1-1,x2-x1+2,y2-y1+2);
                } else if (!CONFIG.isHighlightNone()) {
                    final Color choiceColor = viewer.getController().isCombatChoice() ?
                        ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_COMBAT_CHOICE_BORDER) :
                        ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_CHOICE_BORDER);

                    //draw a one pixel border of choiceColor
                    g2d.setPaint(new Color(choiceColor.getRGB()));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRect(x1+1,y1+1,x2-x1-1,y2-y1-1);
                    g2d.setStroke(defaultStroke);
                }
            }

            if (isMouseOver) {
                paintMouseOverHighlight(g2d, getMouseOverRectangle());
            }

            if (highlightedId == linkedInfo.permanent.getCard().getId()) {
                g2d.setPaint(MagicStyle.getRolloverColor());
                g2d.setStroke(new BasicStroke(4));
                g2d.drawRect(x1 + 2, y1 + 2, x2 - x1 - 2, y2 - y1 - 2);
                g2d.setStroke(defaultStroke);
            }

        }
    }

    /**
     * draw filled rectangle using translucent color over visible portion of card.
     */
    private void paintMouseOverHighlight(final Graphics2D g2d, final Rectangle rect) {
        g2d.setPaint(MOUSE_OVER_TCOLOR);
        g2d.fillRect(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
        if (linkedScreenRectangles.size() > 1) {
            g2d.setPaint(MOUSE_OVER_COLOR);
            g2d.drawRect(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
        }
    }

    private Rectangle getMouseOverRectangle() {
        final Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mousePoint, this);
        for (int i = linkedScreenRectangles.size()-1; i >= 0; i--) {
            final Rectangle rect = linkedScreenRectangles.get(i);
            if (rect.contains(mousePoint)) {
                return rect;
            }
        }
        return linkedScreenRectangles.get(0);
    }

    private AffineTransform getTappedTransform(final Rectangle linkedRect, final Dimension imageSize, final Point translation) {
        final AffineTransform transform = new AffineTransform();
        final double scale = linkedRect.width * 1.0 / imageSize.height;
        transform.translate(translation.x, translation.y);
        transform.scale(scale, scale);
        transform.translate(imageSize.height/2, imageSize.width/2);
        transform.rotate(Math.PI/2);
        transform.translate(-imageSize.width/2, -imageSize.height/2);
        return transform;
    }

    void doShowHighlight(long id) {
        highlightedId = id;
        repaint();
    }

}
