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
import magic.ui.widget.TitleBar;
import magic.ui.widget.TransparentImagePanel;

/**
 * Class responsible for showing the card pic popup
 */
public class CardViewer extends JPanel {

    private static final long serialVersionUID = 1L;

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

        setCard(MagicCardDefinition.UNKNOWN,0);

        timer = new Timer(0, new ActionListener() {
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
                                setCard(currentCardDefinition.getTransformedDefinition(), 0);
                            } else if (currentCardDefinition.isFlipCard()) {
                                setCard(currentCardDefinition.getFlippedDefinition(), 0);
                            }
                            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        }
                    });
                }
            }            
        });
    }

    public void setCard(final MagicCardDefinition cardDefinition, final int index) {
        assert cardDefinition != null;
        if (cardDefinition!=currentCardDefinition||index!=currentIndex) {
            currentCardDefinition=cardDefinition;
            currentIndex=index;
            final BufferedImage cardImage;
            if (image&&GeneralConfig.getInstance().isHighQuality()) {
                final BufferedImage sourceImage =
                    HighQualityCardImagesProvider.getInstance().getImage(cardDefinition,index,true);
                final int imageWidth=sourceImage.getWidth(this);
                final int imageHeight=sourceImage.getHeight(this);
                cardImage = sourceImage;
                setSize(imageWidth,imageHeight);
                revalidate();
            } else {
                cardImage=HighQualityCardImagesProvider.getInstance().getImage(cardDefinition,index,false);
                if (image) {
                    setSize(CardImagesProvider.CARD_WIDTH,CardImagesProvider.CARD_HEIGHT);
                    revalidate();
                }
            }

            if (cardDefinition.isMissing() && cardImage != IconImages.MISSING_CARD) {
                cardPanel.setImage(getGreyScaleImage(cardImage));
            } else {
                cardPanel.setImage(cardImage);
            }
            repaint();
        }
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
            public void run() {
                setVisible(false);
            }
        });
    }
}
