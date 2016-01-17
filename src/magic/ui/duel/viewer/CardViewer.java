package magic.ui.duel.viewer;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.model.MagicCardDefinition;
import magic.ui.CachedImagesProvider;
import magic.ui.MagicImages;
import magic.ui.cardtable.ICardSelectionListener;
import magic.ui.utility.GraphicsUtils;
import magic.ui.widget.TransparentImagePanel;

@SuppressWarnings("serial")
public class CardViewer extends JPanel implements ICardSelectionListener {

    private final TransparentImagePanel cardPanel = new TransparentImagePanel();
    private MagicCardDefinition thisCard;
    private boolean isSwitchedAspect = false;

    public CardViewer() {
        setCard(MagicCardDefinition.UNKNOWN);
        setTransformCardListener();
        setOpaque(false);
        this.setLayout(new BorderLayout());
        add(cardPanel,BorderLayout.CENTER);
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

        if (aCard == null) {
            thisCard = MagicCardDefinition.UNKNOWN;
            setCardImage(MagicImages.getMissingCardImage());

        } else if (aCard != thisCard) {
            thisCard = aCard;
            final BufferedImage cardImage;
            cardImage =  CachedImagesProvider.getInstance().getImage(aCard, false);
            if (aCard.isInvalid() && cardImage != MagicImages.getMissingCardImage()) {
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


    @Override
    public void newCardSelected(final MagicCardDefinition card) {
        SwingUtilities.invokeLater(() -> {
            setCard(card);
        });
    }
}
