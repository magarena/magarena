package magic.ui.viewer;

import magic.data.CardImagesProvider;
import magic.data.GeneralConfig;
import magic.data.HighQualityCardImagesProvider;
import magic.model.MagicCardDefinition;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.TitleBar;
import magic.ui.widget.TransparentImagePanel;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Class responsible for showing the card pic popup
 */
public class CardViewer extends JPanel {

    private static final long serialVersionUID = 1L;

    private final TransparentImagePanel cardPanel;
    private MagicCardDefinition currentCardDefinition;
    private int currentIndex;
    private final boolean image;
    private final boolean opaque;
    private final Timer timer;

    public CardViewer(final boolean image,final boolean opaque) {
        this("", image, opaque);
    }

    public CardViewer(final String title,final boolean image,final boolean opaque) {
        this.image=image;
        this.opaque=opaque;

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
    }

    public void setCard(final MagicCardDefinition cardDefinition,final int index) {
        if (cardDefinition!=currentCardDefinition||index!=currentIndex) {
            currentCardDefinition=cardDefinition;
            currentIndex=index;
            final BufferedImage cardImage;
            float opacity=1.0f;
            if (image&&GeneralConfig.getInstance().isHighQuality()) {
                if (!opaque) {
                    opacity=ThemeFactory.getInstance().getCurrentTheme().getValue(Theme.VALUE_POPUP_OPACITY)/100.0f;
                }
                final BufferedImage sourceImage =
                    HighQualityCardImagesProvider.getInstance().getImage(cardDefinition,index,true);
                final int imageWidth=sourceImage.getWidth(this);
                final int imageHeight=sourceImage.getHeight(this);
                cardImage = sourceImage;
                cardPanel.setOpacity(opacity);
                setSize(imageWidth,imageHeight);
                revalidate();
            } else {
                cardImage=HighQualityCardImagesProvider.getInstance().getImage(cardDefinition,index,false);
                if (image) {
                    setSize(CardImagesProvider.CARD_WIDTH,CardImagesProvider.CARD_HEIGHT);
                    revalidate();
                }
            }

            cardPanel.setOpacity(opacity);
            cardPanel.setImage(cardImage);
            repaint();
        }
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
