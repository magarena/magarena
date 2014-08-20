package magic.ui.viewer;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import magic.data.CardImagesProvider;
import magic.data.GeneralConfig;
import magic.data.HighQualityCardImagesProvider;
import magic.data.IconImages;
import magic.model.MagicCardDefinition;
import magic.ui.ICardSelectionListener;
import magic.ui.widget.TitleBar;
import magic.ui.widget.TransparentImagePanel;

/**
 * Class responsible for showing the card pic popup
 */
@SuppressWarnings("serial")
public class CardViewer extends JPanel implements ICardSelectionListener {

    private static CardImagesProvider IMAGE_HELPER = HighQualityCardImagesProvider.getInstance();

    private final TransparentImagePanel cardPanel;
    private MagicCardDefinition currentCardDefinition;
    private int currentIndex;
    private final boolean image;
    private final Timer timer;

    public CardViewer(final boolean image) {
        this("", image);
    }

    public CardViewer(final String title, final boolean image) {
        this.image=image;

        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        if (!title.isEmpty()) {
            add(new TitleBar(title),BorderLayout.NORTH);
        }

        cardPanel=new TransparentImagePanel();
        add(cardPanel,BorderLayout.CENTER);

        setCard(MagicCardDefinition.UNKNOWN);

        timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setVisible(true);
            }
        });
        timer.setRepeats(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentCardDefinition != null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                            if (currentCardDefinition.isDoubleFaced()) {
                                setCard(currentCardDefinition.getTransformedDefinition());
                            } else if (currentCardDefinition.isFlipCard()) {
                                setCard(currentCardDefinition.getFlippedDefinition());
                            }
                            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        }
                    });
                }
            }            
        });
    }

    public void setCard(final MagicCardDefinition cardDefinition, final int index) {
        if (cardDefinition == null) {
            currentCardDefinition = MagicCardDefinition.UNKNOWN;
            setCardImage(IconImages.MISSING_CARD);
        } else if (cardDefinition != currentCardDefinition || index != currentIndex) {
            currentCardDefinition=cardDefinition;
            currentIndex=index;
            final BufferedImage cardImage;
            if (image&&GeneralConfig.getInstance().isHighQuality()) {
                final BufferedImage sourceImage = IMAGE_HELPER.getImage(cardDefinition,index,true);
                final int imageWidth=sourceImage.getWidth(this);
                final int imageHeight=sourceImage.getHeight(this);
                cardImage = sourceImage;
                setSize(imageWidth,imageHeight);
                revalidate();
            } else {
                cardImage = IMAGE_HELPER.getImage(cardDefinition,index,false);
                if (image) {
                    setSize(CardImagesProvider.CARD_WIDTH,CardImagesProvider.CARD_HEIGHT);
                    revalidate();
                }
            }

            if (cardDefinition.isMissing() && cardImage != IconImages.MISSING_CARD) {
                setCardImage(getGreyScaleImage(cardImage));
            } else {
                setCardImage(cardImage);
            }
        }
    }

    private void setCardImage(final BufferedImage newImage) {
        cardPanel.setImage(newImage);
        repaint();
    }

    public void setCard(final MagicCardDefinition cardDefinition) {
        setCard(cardDefinition, 0);
    }

    private BufferedImage getGreyScaleImage(final BufferedImage image) {
        final BufferedImage gsImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        final Graphics gsg = gsImage.getGraphics();
        gsg.drawImage(image, 0, 0, this);
        gsg.dispose();
        return gsImage;
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
