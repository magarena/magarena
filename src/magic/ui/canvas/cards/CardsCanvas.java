package magic.ui.canvas.cards;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import magic.model.MagicCard;

@SuppressWarnings("serial")
public class CardsCanvas extends JPanel {

    public enum LayoutMode {
        SCALE_TO_FIT
    }

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

    // CTR
    public CardsCanvas(final Dimension preferredCardSize) {

        setOpaque(false);

        this.preferredCardSize = preferredCardSize;
        aspectRatio = (double)this.preferredCardSize.width / this.preferredCardSize.height;

        this.imageHandler = new ImageHandler(null);

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

    public void refresh(final List<MagicCard> newCards, final Dimension preferredCardSize) {
        final List<CardCanvas> canvasCards = getCanvasCards(newCards);
        this.preferredCardSize = preferredCardSize;
        if (useAnimation && newCards != null) {
            executor.execute(getDealCardsRunnable(canvasCards));
        } else {
            createListOfCardCanvasObjects(canvasCards);
            maxCardsVisible = cards.size();
            repaint();
        }
    }

    private List<CardCanvas> getCanvasCards(final List<MagicCard> magicCards) {
        final List <CardCanvas> canvasCards = new ArrayList<>();
        if (magicCards != null) {
            for (MagicCard magicCard : magicCards) {
                canvasCards.add(new CardCanvas(magicCard));
            }
        }
        return canvasCards;
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
        if (this.getWidth() > 0) {
            setScaleToFitLayout();
            for (int i = 0; i < maxCardsVisible; i++) {
                final CardCanvas card = cards.get(i);
                drawCard(g, card);
            }
        }
    }

    public void incrementCardWidth() {
        final int newWidth = preferredCardSize.width + 1;
        final int newHeight = (int)(newWidth / aspectRatio);
        preferredCardSize = new Dimension(newWidth, newHeight);
        repaint();
    }

    private void drawCard(final Graphics g, final CardCanvas canvasCard) {

        final int X = canvasCard.getPosition().x;
        final int Y = canvasCard.getPosition().y;
        final int W = (int)(preferredCardSize.width * cardCanvasScale);
        final int H = (int)(preferredCardSize.height * cardCanvasScale);

        final boolean isScalingRequired =
                !canvasCard.getSize().equals(preferredCardSize) || (cardCanvasScale != 1);
        final BufferedImage unscaledImage = canvasCard.getFrontImage();
        final BufferedImage image =
                isScalingRequired ? imageHandler.getScaledImage(unscaledImage, W) : unscaledImage;

        g.drawImage(image, X, Y, W, H, null);

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
