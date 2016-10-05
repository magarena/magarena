package magic.ui.canvas.cards;

import java.awt.BasicStroke;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import magic.model.MagicCard;
import magic.ui.dialog.prefs.ImageSizePresets;
import magic.ui.utility.GraphicsUtils;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
public class CardsCanvas extends JPanel {

    public enum LayoutMode {
        SCALE_TO_FIT
    }

    private static final Color MOUSE_OVER_COLOR = MagicStyle.getRolloverColor();
    private static final Color MOUSE_OVER_TCOLOR = MagicStyle.getTranslucentColor(MOUSE_OVER_COLOR, 20);
    private static final Color MOUSE_OVER_BORDER_COLOR = MagicStyle.getTranslucentColor(MOUSE_OVER_COLOR, 160);

    private int dealCardDelay = 80; // milliseconds
    private int removeCardDelay = 50; // millseconds

    private final List<CardCanvas> cards = new CopyOnWriteArrayList<>();
    private final HashMap<Integer, Integer> cardTypeCount = new HashMap<>();
    public boolean showIndex = true;
    private volatile boolean useAnimation = true;
    private volatile int maxCardsVisible = 0;
    private volatile boolean isAnimateThreadRunning = false;
    public Dimension preferredCardSize;
    private final ImageHandler imageHandler;
    private double cardCanvasScale = 1.0;
    private LayoutMode layoutMode = LayoutMode.SCALE_TO_FIT;
    private final double aspectRatio;
    private boolean stackDuplicateCards = true;
    private final ExecutorService executor = Executors.newFixedThreadPool(1);
    private Dimension canvasSize;
    private int currentCardIndex = -1;
    private boolean refreshLayout = false;
    private ICardsCanvasListener listener = new NullCardsCanvasListener();

    public CardsCanvas() {

        setOpaque(false);

        this.preferredCardSize = ImageSizePresets.getDefaultSize();
        aspectRatio = (double)this.preferredCardSize.width / this.preferredCardSize.height;

        this.imageHandler = new ImageHandler(null);

        setMouseListener();
        setMouseMotionListener();

    }

    public void setListener(ICardsCanvasListener aListener) {
        this.listener = aListener;
    }

    private void setMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                clearCardHighlight();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isAnimateThreadRunning) {
                    GraphicsUtils.setBusyMouseCursor(true);
                    final int cardIndex = getCardIndexAt(e.getPoint());
                    if (cardIndex >= 0) {
                        new CardImageOverlay(cards.get(cardIndex).getMagicCard());
                    }
                    GraphicsUtils.setBusyMouseCursor(false);
                }
            }
        });
    }

    private void clearCardHighlight() {
        currentCardIndex = -1;
        repaint();
    }

    private void setMouseMotionListener() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(final MouseEvent event) {
                final int cardIndex = getCardIndexAt(event.getX(), event.getY());
                if (currentCardIndex != cardIndex) {
                    if (cardIndex >= 0) {
                        listener.cardSelected(cards.get(cardIndex).getMagicCard());
                    }
                    currentCardIndex = cardIndex;
                    repaint();
                }
            }
        });
    }

    private int getCardIndexAt(final Point aPoint) {
        return getCardIndexAt(aPoint.x, aPoint.y);
    }

    private int getCardIndexAt(final int x, final int y) {
        for (int i = 0; i < cards.size(); i++) {
            final Rectangle rect = cards.get(i).getBounds();
            if (x >= rect.x && y >= rect.y && x < rect.x + rect.width && y < rect.y + rect.height) {
                return i;
            }
        }
        return -1;
    }

    private Runnable getDealCardsRunnable(final List<CardCanvas> newCards) {
        return new Runnable() {
            @Override
            public void run() {
                isAnimateThreadRunning = true;
                clearCards();
                if (isAnimationEnabled()) {
                    preCacheImages();
                }
                dealCards();
                isAnimateThreadRunning = false;
            }
            private void dealCards() {
                if (useAnimation) {
                    dealCardsAnimation();
                } else {
                    maxCardsVisible = cards.size();
                    repaint();
                }
            }
            private void dealCardsAnimation() {
                while (maxCardsVisible++ < cards.size()) {
                    repaint();
                    pause(dealCardDelay);
                }
                maxCardsVisible--;
            }
            private void clearCards() {
                if (cards != null && useAnimation) {
                    clearCardsAnimation();
                }
                createListOfCardCanvasObjects(newCards);
            }
            private void clearCardsAnimation() {
                if (maxCardsVisible > 0) {
                    while (maxCardsVisible-- >= 0) {
                        repaint();
                        pause(removeCardDelay);
                    }
                }
            }
            private void pause(final long milliseconds) {
                try {
                    Thread.sleep(milliseconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void preCacheImages() {
        for (CardCanvas card : cards) {
            final boolean isScalingRequired =
                    !card.getSize().equals(preferredCardSize) || (cardCanvasScale != 1);
            if (isScalingRequired) {
                final BufferedImage unscaledImage = card.getFrontImage();
                final int W = (int)(preferredCardSize.width * cardCanvasScale);
                imageHandler.getScaledImage(unscaledImage, W);
            }
        }
    }

    private void createListOfCardCanvasObjects(final List<CardCanvas> newCards) {
        cards.clear();
        cardTypeCount.clear();
        if (newCards != null) {
            CardCanvas lastCard = null;
            for (final CardCanvas cardCanvas : newCards) {
                if (stackDuplicateCards) {
                    final int cardHashCode = cardCanvas.hashCode();
                    if (lastCard == null || cardHashCode != lastCard.hashCode()) {
                        cards.add(cardCanvas);
                        cardTypeCount.put(cardHashCode, 1);
                    } else {
                        final int cardCount = cardTypeCount.get(cardHashCode);
                        cardTypeCount.put(cardHashCode, cardCount + 1);
                    }
                    lastCard = cardCanvas;
                } else {
                    cards.add(cardCanvas);
                }
            }
        }
    }

    public void refresh(final List<MagicCard> newCards, final Dimension aSize) {
        final List<CardCanvas> canvasCards = getCanvasCards(newCards);
        this.preferredCardSize = aSize;
        refreshLayout = true;
        currentCardIndex = -1;
        if (useAnimation && newCards != null) {
            executor.execute(getDealCardsRunnable(canvasCards));
        } else {
            createListOfCardCanvasObjects(canvasCards);
            maxCardsVisible = cards.size();
            repaint();
        }
    }

    public void refresh(final List<MagicCard> newCards) {
        refresh(newCards, preferredCardSize);
    }

    private List<CardCanvas> getCanvasCards(List<MagicCard> cards) {
        return cards.stream()
            .map(card -> new CardCanvas(card))
            .collect(Collectors.toList());
    }

    public void setScale(final double newScale) {
        this.cardCanvasScale = newScale;
        repaint();
    }
    public double getScale() {
        return this.cardCanvasScale;
    }

    public void setAnimationEnabled(final boolean b) {
        this.useAnimation = b;
    }
    public boolean isAnimationEnabled() {
        return useAnimation;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCards(g);
    }

    private void drawCards(final Graphics g) {
        if (this.getWidth() > 0) {
            if (!getSize().equals(canvasSize) || refreshLayout || isAnimateThreadRunning) {
                refreshLayout = false;
                canvasSize = new Dimension(getSize());
                setScaleToFitLayout();
            }
            for (int i = 0; i < maxCardsVisible; i++) {
                final CardCanvas card = cards.get(i);
                drawCard(g, card);
            }
            highlightCardUnderMousePointer(g);
        }
    }

    private void highlightCardUnderMousePointer(final Graphics g) {
        if (currentCardIndex >= 0 && !isAnimateThreadRunning) {
            final Rectangle rect = cards.get(currentCardIndex).getBounds();
            final Graphics2D g2d = (Graphics2D) g;
            drawHighlightOverlay(g2d, rect);
//            drawHighlightBorder(g2d, rect);
        }
    }

    private void drawHighlightBorder(Graphics2D g2d, Rectangle rect) {
        final int w = 4;
        g2d.setStroke(new BasicStroke(w));
        g2d.setPaint(MOUSE_OVER_BORDER_COLOR);
        g2d.drawRect(rect.x + (w / 2), rect.y + (w / 2), rect.width - w, rect.height - w);
    }

    private void drawHighlightOverlay(Graphics2D g2d, Rectangle rect) {
        g2d.setPaint(MOUSE_OVER_TCOLOR);
        g2d.fillRect(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
    }

    public void incrementCardWidth() {
        final int newWidth = preferredCardSize.width + 1;
        final int newHeight = (int)(newWidth / aspectRatio);
        preferredCardSize = new Dimension(newWidth, newHeight);
        repaint();
    }

    private void drawCard(final Graphics g, final CardCanvas canvasCard) {

        final int X = canvasCard.getBounds().x;
        final int Y = canvasCard.getBounds().y;
        final int W = canvasCard.getBounds().width;
        final int H = canvasCard.getBounds().height;

        g.drawImage(GraphicsUtils.scale(canvasCard.getFrontImage(), W, H), X, Y, null);

        if (stackDuplicateCards) {
            drawCardCount(g, X, Y, W, H, canvasCard);
        }
    }

    private void drawCardCount(Graphics g, int X, int Y, int W, int H, final CardCanvas card) {
        final int cardCount = cardTypeCount.get(card.hashCode());
        if (cardCount > 1) {
            g.setColor(Color.WHITE);
            final String text = Integer.toString(cardCount);
            int h = (int)(H * 0.15);
            h = h > 8 ? (int)(H * 0.15) : 9;
            final Font f = new Font("Dialog", Font.BOLD, h);
            final int w = g.getFontMetrics(f).stringWidth(text);
            g.setFont(f);
            drawStringWithOutline(g, text, X + ((W - w) / 2), Y + ((H - h) / 3));
        }
    }

    private void drawStringWithOutline(final Graphics g, final String str, int x, int y) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.DARK_GRAY);
        for (int i = 1; i <= 2; i++) {
            g.drawString(str,x+i,y);
            g.drawString(str,x-i,y);
            g.drawString(str,x,y+i);
            g.drawString(str,x,y-i);
        }
        g.setColor(Color.WHITE);
        g.drawString(str,x,y);
    }

    public void setLayoutMode(final LayoutMode layout) {
        this.layoutMode = layout;
        if (this.getWidth() > 0) {
            repaint();
        }
    }
    public LayoutMode getLayoutMode() {
        return this.layoutMode;
    }

    public boolean isBusy() {
        return isAnimateThreadRunning;
    }

    private Dimension getLayoutGridDimensions() {
        final int cardCount = cards.size();
        final int containerWidth = this.getWidth();
        final int containerHeight = this.getHeight();
        final double containerAspectRatio = (double)containerWidth / containerHeight;
        final double normalizedAspectRatio = containerAspectRatio / aspectRatio;
        final double cols = Math.sqrt(cardCount * normalizedAspectRatio);
        final double rows = Math.sqrt(cardCount / normalizedAspectRatio);
        final int floorR = (int)Math.floor(rows);
        final int ceilR = (int)Math.ceil(rows);
        final int floorC = (int)Math.floor(cols);
        final int ceilC = (int)Math.ceil(cols);
        final int[] cells = new int[3];
        cells[0] = floorR * ceilC;
        cells[1] = ceilR * floorC;
        cells[2] = ceilR * ceilC;
        Arrays.sort(cells);
        if (cells[0] >= cardCount) {
            return new Dimension(ceilC, floorR);
        } else if (cells[1] >= cardCount) {
            return new Dimension(floorC, ceilR);
        } else {
            return new Dimension(ceilC, ceilR);
        }
    }

    private void setScaleToFitLayout() {

        final int containerHeight = this.getHeight();
        final int containerWidth = this.getWidth();
        final int totalCards = cards.size();
        if (containerWidth == 0 || totalCards == 0) { return; }

        final Dimension grid = getLayoutGridDimensions();
        int cardWidth = Math.min(containerWidth/grid.width, preferredCardSize.width);
        int cardHeight = (int)(cardWidth / aspectRatio);
        if ((cardHeight * grid.height) > containerHeight) {
            cardHeight = Math.min(containerHeight/grid.height, preferredCardSize.height);
            cardWidth = (int)(cardHeight * aspectRatio);
        }

        final Rectangle rect = new Rectangle(grid.width * cardWidth, grid.height * cardHeight);
        final int xStart = (containerWidth - rect.width) / 2;
        final int yStart = (containerHeight - rect.height) / 2;
        int row = 0;
        int col = 0;
        for (int cardIndex = 0; cardIndex < totalCards; cardIndex++) {
            final CardCanvas card = cards.get(cardIndex);
            int xPoint = xStart + (col * cardWidth);
            int yPoint = yStart + (row * (cardHeight-1));
            card.setPosition(new Point(xPoint, yPoint));
            card.setSize(cardWidth, cardHeight);
            col++;
            if (col >= grid.width) {
                col = 0;
                row++;
            }
        }

        cardCanvasScale = (double)cardWidth / preferredCardSize.width;
    }

    public void setAnimationDelay(final int dealCardDelay, final int removeCardDelay) {
        this.dealCardDelay = dealCardDelay;
        this.removeCardDelay = removeCardDelay;
    }

    public void setStackDuplicateCards(boolean stackDuplicateCards) {
        this.stackDuplicateCards = stackDuplicateCards;
    }

}
