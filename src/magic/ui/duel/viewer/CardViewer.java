package magic.ui.duel.viewer;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import magic.ui.CachedImagesProvider;
import magic.ui.CardImagesProvider;
import magic.data.GeneralConfig;
import magic.ui.IconImages;
import magic.model.MagicCardDefinition;
import magic.ui.utility.GraphicsUtils;
import magic.ui.cardtable.ICardSelectionListener;
import magic.ui.widget.TransparentImagePanel;

/**
 * Class responsible for showing the card pic popup
 */
@SuppressWarnings("serial")
public class CardViewer extends JPanel implements ICardSelectionListener {

    private static CardImagesProvider IMAGE_HELPER = CachedImagesProvider.getInstance();
    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final TransparentImagePanel cardPanel = new TransparentImagePanel();
    private MagicCardDefinition currentCardDefinition;
    private int currentIndex;
    private final boolean isGameScreenPopup;
    private Timer timer;
    private boolean isSwitchedAspect = false;

    // ctr
    public CardViewer() {
        this(false);
    }
    // ctr
    public CardViewer(final boolean isGameScreenPopup) {

        this.isGameScreenPopup = isGameScreenPopup;
        setCard(MagicCardDefinition.UNKNOWN);

        setDelayedVisibilityTimer();
        setTransformCardListener();

        setOpaque(false);
        this.setLayout(new BorderLayout());
        add(cardPanel,BorderLayout.CENTER);

        if (isGameScreenPopup) {
            addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {}
                @Override
                public void mouseMoved(MouseEvent e) {
                    hideDelayed();
                }
            });
        }

    }
    
    private void setDelayedVisibilityTimer() {
        timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setVisible(true);
            }
        });
        timer.setRepeats(false);        
    }

    private void setTransformCardListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentCardDefinition != null && !isGameScreenPopup) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            switchCardAspect();
                        }
                    });
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isGameScreenPopup && currentCardDefinition.hasMultipleAspects() && currentCardDefinition.isValid()) {
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
        if (currentCardDefinition.hasMultipleAspects() && currentCardDefinition.isValid()) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (currentCardDefinition.isDoubleFaced()) {
                setCard(currentCardDefinition.getTransformedDefinition());
            } else if (currentCardDefinition.isFlipCard()) {
                setCard(currentCardDefinition.getFlippedDefinition());
            }
            isSwitchedAspect = !isSwitchedAspect;
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    public void setCard(final MagicCardDefinition cardDefinition, final int index) {
        
        if (cardDefinition == null) {
            currentCardDefinition = MagicCardDefinition.UNKNOWN;
            setCardImage(IconImages.getMissingCardImage());

        } else if (cardDefinition != currentCardDefinition || index != currentIndex) {
            currentCardDefinition = cardDefinition;
            currentIndex = index;
            final BufferedImage cardImage;
            if (isGameScreenPopup && CONFIG.isHighQuality()) {
                final BufferedImage sourceImage = IMAGE_HELPER.getImage(cardDefinition,index,true);
                final int imageWidth=sourceImage.getWidth(this);
                final int imageHeight=sourceImage.getHeight(this);
                cardImage = sourceImage;
                setSize(imageWidth,imageHeight);
                revalidate();
            } else {
                cardImage = IMAGE_HELPER.getImage(cardDefinition,index,false);
                if (isGameScreenPopup) {
                    setSize(GraphicsUtils.getMaxCardImageSize());
                    revalidate();
                }
            }

            if (cardDefinition.isInvalid() && cardImage != IconImages.getMissingCardImage()) {
                setCardImage(GraphicsUtils.getGreyScaleImage(cardImage));
            } else {
                setCardImage(cardImage);
            }
        }
    }

    public final void setCard(final MagicCardDefinition cardDefinition) {
        setCard(cardDefinition, 0);
    }

    private void setCardImage(final Image newImage) {
        cardPanel.setImage(newImage);
        repaint();
    }


    public void showDelayed(final int delay) {
        timer.setInitialDelay(delay);
        timer.restart();
    }

    public void hideDelayed() {
        timer.stop();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(false);
            }
        });
    }

    @Override
    public void newCardSelected(final MagicCardDefinition card) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setCard(card);
            }
        });
    }
}
