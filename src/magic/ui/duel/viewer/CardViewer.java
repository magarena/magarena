package magic.ui.duel.viewer;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.ui.MagicImages;
import magic.ui.cardtable.ICardSelectionListener;
import magic.ui.prefs.ImageSizePresets;
import magic.ui.utility.GraphicsUtils;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.throbber.AbstractThrobber;
import magic.ui.widget.throbber.ImageThrobber;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CardViewer extends JPanel implements ICardSelectionListener {

	private final Dimension IMAGE_SIZE = getImageSize();

	private Image thisImage;
	private MagicCardDefinition thisCard = MagicCardDefinition.UNKNOWN;
	private boolean isSwitchedAspect = false;
	private MagicCardDefinition cardPending = MagicCardDefinition.UNKNOWN;
	private final Timer aTimer = getCooldownTimer();
	private long lastTime = System.currentTimeMillis();
	private boolean isImagePending;
	private final AbstractThrobber throbber;
	private final JLabel cardLabel;

	public CardViewer() {

		setPreferredSize(IMAGE_SIZE);
		setMinimumSize(IMAGE_SIZE);
		setMaximumSize(IMAGE_SIZE);

		setBackground(MagicStyle.getTranslucentColor(Color.DARK_GRAY, 140));

		throbber = new ImageThrobber.Builder(MagicImages.loadImage("round-shield.png")).build();
		throbber.setVisible(false);

		cardLabel = getLabel("");

		setLayout(new MigLayout("flowy, aligny center"));
		add(new JLabel());
		add(throbber, "alignx center");
		add(cardLabel, "w 100%");

		setDefaultImage();
		setTransformCardListener();
	}

	private void setDefaultImage() {
		setImage(getCardImage(MagicCardDefinition.UNKNOWN, getImageSize(), this));
	}

	private JLabel getLabel(String text) {
		JLabel lbl = new JLabel(text);
		lbl.setForeground(Color.WHITE);
		lbl.setFont(FontsAndBorders.FONT2);
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		return lbl;
	}

	private void setTransformCardListener() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (thisCard != null) {
					SwingUtilities.invokeLater(() -> {
						switchCardAspect();
					});
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (thisCard.hasMultipleAspects() && thisCard.isValid()) {
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (isSwitchedAspect) {
					switchCardAspect();
				}
			}
		});
	}

	private void switchCardAspect() {
		if (thisCard.hasMultipleAspects()) {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			if (thisCard.isDoubleFaced()) {
				setCard(thisCard.getTransformedDefinition());
			} else if (thisCard.isFlipCard()) {
				setCard(thisCard.getFlippedDefinition());
			}
			isSwitchedAspect = !isSwitchedAspect;
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	}

	public final void setCard(final MagicCardDefinition aCard) {

		if (aCard == thisCard) {
			return;
		}

		cardLabel.setText("..." + aCard.getName() + "...");

//		boolean isCooldownRequired = System.currentTimeMillis() - lastTime < 120;
//		lastTime = System.currentTimeMillis();
//		if (isCooldownRequired) {
//			cardPending = aCard;
//			aTimer.restart();
//			setImage(null);
//			return;
//		}

		thisCard = aCard;
		
		repaint();

	}

	@Override
	public void newCardSelected(final MagicCardDefinition card) {
		SwingUtilities.invokeLater(() -> {
			setCard(card);
		});
	}

	void setImage(final Image aImage) {
		if (aImage == null) {
			// isImagePending = true;
			throbber.setVisible(true);
			cardLabel.setVisible(true);
		} else {
			this.thisImage = aImage;
			// isImagePending = true;
			throbber.setVisible(false);
			cardLabel.setVisible(false);
		}
		repaint();
	}

	@Override
	public void paintComponent(final Graphics g) {
		System.out.println("CardViewer:"+thisCard.getName());
		
		Image image = CardViewer.getCardImage(thisCard, getImageSize(), this);
		if (image != null) {
			g.drawImage(image, 0, 0, null);
		}
	}

	static Dimension getImageSize() {
		ImageSizePresets sizePreset = GeneralConfig.getInstance().getPreferredImageSize();
		if (sizePreset == ImageSizePresets.SIZE_ORIGINAL) {
			return ImageSizePresets.SIZE_312x445.getSize();
		} else if (sizePreset.getSize().width < ImageSizePresets.SIZE_312x445.getSize().width) {
			return ImageSizePresets.SIZE_312x445.getSize();
		} else {
			return sizePreset.getSize();
		}
	}

	static Image getCardImage(MagicCardDefinition aCard, Dimension prefSize, JComponent jcomp) {

		if (aCard == null) {
			aCard = MagicCardDefinition.UNKNOWN;
		}

		BufferedImage image = MagicImages.getOrigSizeCardImage(aCard, jcomp);

		if (image.getWidth() != prefSize.width || image.getHeight() != prefSize.height) {
			image = GraphicsUtils.scale(image, prefSize.width, prefSize.height);
		}

		if (aCard.isInvalid() && image != MagicImages.getMissingCardImage()) {
			return GraphicsUtils.getGreyScaleImage(image);
		} else {
			return image;
		}
	}

	private Timer getCooldownTimer() {
		Timer t = new Timer(150, (e) -> {
			setCard(cardPending);
		});
		t.setRepeats(false);
		return t;
	}

	public static Dimension getSidebarImageSize() {
		Dimension size = ImageSizePresets.getDefaultSize();
		return size.width < ImageSizePresets.SIZE_312x445.getSize().width ? ImageSizePresets.SIZE_312x445.getSize()
				: size;
	}
}
