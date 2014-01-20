package magic.ui.canvas.cards;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CardsCanvas extends JPanel {

	public enum LayoutMode {
		SCALE_TO_FIT
	}

	private List<CardCanvas> cards = new CopyOnWriteArrayList<CardCanvas>();
	public boolean showIndex = true;
	private volatile boolean useAnimation = true;
	private volatile int maxCardsVisible = 0;
	private volatile boolean isAnimateThreadRunning = false;
	public Dimension preferredCardSize;
	private final ImageHandler imageHandler;
	private double cardCanvasScale = 1.0;
	private LayoutMode layoutMode = LayoutMode.SCALE_TO_FIT;
	private final double aspectRatio;
	private ExecutorService executor = Executors.newFixedThreadPool(1);

	// CTR
	public CardsCanvas(final Dimension preferredCardSize) {

		setOpaque(false);

		this.preferredCardSize = preferredCardSize;
		aspectRatio = (double)this.preferredCardSize.width / this.preferredCardSize.height;

		this.imageHandler = new ImageHandler(null);

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				refreshCardsLayout();
			}
		});

	}

	private Runnable getDealCardsRunnable(final List<? extends ICardCanvas> newCards) {
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
					pause(80);
				}
				maxCardsVisible--;
			}
			private void clearCards() {
				if (cards != null && useAnimation) {
					clearCardsAnimation();
				}
				createListOfCardCanvasObjects(newCards);
				refreshCardsLayout();
			}
			private void clearCardsAnimation() {
				if (maxCardsVisible > 0) {
					while (maxCardsVisible-- >= 0) {
						repaint();
						pause(50);
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
				final BufferedImage unscaledImage = card.getCard().getFrontImage();
				final int W = (int)(preferredCardSize.width * cardCanvasScale);
				imageHandler.getScaledImage(unscaledImage, W);
			}
		}
	}

	private void createListOfCardCanvasObjects(final List<? extends ICardCanvas> newCards) {
		cards.clear();
		if (newCards != null) {
			for (ICardCanvas cardObject : newCards) {
				cards.add(new CardCanvas(cardObject));
			}
		}
	}

	public void refresh(
			final List<? extends ICardCanvas> newCards,
			final Dimension preferredCardSize) {

		if (cards != null) {
			this.preferredCardSize = preferredCardSize;
			if (useAnimation) {
				executor.execute(getDealCardsRunnable(newCards));
			} else {
				createListOfCardCanvasObjects(newCards);
				refreshCardsLayout();
				maxCardsVisible = cards.size();
				repaint();
			}
		}
	}

	public void setScale(final double newScale) {
		this.cardCanvasScale = newScale;
		refreshCardsLayout();
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
		for (int i = 0; i < maxCardsVisible; i++) {
			final CardCanvas card = cards.get(i);
			drawCard(g, card);
		}
	}

	public void incrementCardWidth() {
		final int newWidth = preferredCardSize.width + 1;
		final int newHeight = (int)(newWidth / aspectRatio);
		preferredCardSize = new Dimension(newWidth, newHeight);
		refreshCardsLayout();
		repaint();
	}

	private void drawCard(final Graphics g, final CardCanvas card) {

		final int X = card.getPosition().x;
		final int Y = card.getPosition().y;
		final int W = (int)(preferredCardSize.width * cardCanvasScale);
		final int H = (int)(preferredCardSize.height * cardCanvasScale);

		final boolean isScalingRequired =
				!card.getSize().equals(preferredCardSize) || (cardCanvasScale != 1);
		final BufferedImage unscaledImage = card.getCard().getFrontImage();
		final BufferedImage image =
				isScalingRequired ?	imageHandler.getScaledImage(unscaledImage, W) : unscaledImage;

		g.drawImage(image, X, Y, W, H, null);

	}

	private void refreshCardsLayout() {
        setScaleToFitLayout();
	}

	public void setLayoutMode(final LayoutMode layout) {
		this.layoutMode = layout;
		refreshCardsLayout();
		repaint();
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

}
