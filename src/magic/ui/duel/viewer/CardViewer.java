package magic.ui.duel.viewer;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import magic.model.MagicCardDefinition;
import magic.ui.CachedImagesProvider;
import magic.ui.MagicImages;
import magic.ui.cardtable.ICardSelectionListener;
import magic.ui.utility.GraphicsUtils;
import magic.ui.widget.TransparentImagePanel;

@SuppressWarnings("serial")
public class CardViewer extends JPanel implements ICardSelectionListener {

    private final TransparentImagePanel cardPanel = new TransparentImagePanel();
    private MagicCardDefinition currentCardDefinition;
    private Timer timer;
    private boolean isSwitchedAspect = false;

    // ctr
    public CardViewer() {

        setCard(MagicCardDefinition.UNKNOWN);

        setDelayedVisibilityTimer();
        setTransformCardListener();

        setOpaque(false);
        this.setLayout(new BorderLayout());
        add(cardPanel,BorderLayout.CENTER);
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
                if (currentCardDefinition != null) {
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
                if (currentCardDefinition.hasMultipleAspects() && currentCardDefinition.isValid()) {
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
        if (currentCardDefinition.hasMultipleAspects()) {
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

    public void setCard(final MagicCardDefinition cardDefinition) {

        if (cardDefinition == null) {
            currentCardDefinition = MagicCardDefinition.UNKNOWN;
            setCardImage(MagicImages.getMissingCardImage());

        } else if (cardDefinition != currentCardDefinition) {
            currentCardDefinition = cardDefinition;
            final BufferedImage cardImage;
            cardImage =  CachedImagesProvider.getInstance().getImage(cardDefinition, false);
            if (cardDefinition.isInvalid() && cardImage != MagicImages.getMissingCardImage()) {
                setCardImage(GraphicsUtils.getGreyScaleImage(cardImage));
            } else {
                setCardImage(cardImage);
            }
        }
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
