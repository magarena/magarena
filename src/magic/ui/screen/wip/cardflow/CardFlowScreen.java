package magic.ui.screen.wip.cardflow;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.swing.JPanel;
import magic.data.CardDefinitions;
import magic.data.MagicIcon;
import magic.model.MagicCardDefinition;
import magic.model.MagicRandom;
import magic.ui.MagicImages;
import magic.ui.helpers.ImageHelper;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.widget.MenuButton;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CardFlowScreen extends HeaderFooterScreen
    implements ICardFlowListener {

    private static final Color BACKGROUND_COLOR = new Color(18, 30, 49);

    private final CardFlowPanel cardFlowPanel;

    public CardFlowScreen() {

        super("Cardflow Test Screen");

        cardFlowPanel = new CardFlowPanel();
        cardFlowPanel.setBackground(BACKGROUND_COLOR);

        final JPanel panel = new JPanel(new MigLayout("insets 0, aligny center"));
        panel.setOpaque(false);
        final int h = CardFlowPanel.MAX_IMAGE_SIZE.height + 20;
        panel.add(cardFlowPanel, "w 100%, h " + h + "!, aligny center");

        setMainContent(panel);

        addToFooter(
            MenuButton.build(this::doScrollBack, MagicIcon.GO_BACK, "Scroll back", "Move back through cards. Can also use left arrow key."),
            MenuButton.build(this::doScrollForwards, MagicIcon.GO_NEXT, "Scroll forwards", "Move forward through cards. Can also use right arrow key.")
        );

        cardFlowPanel.addListener(this);
        cardFlowPanel.setImages(getImages());
    }

    private void doScrollForwards() {
        cardFlowPanel.doClickRight();
    }

    private void doScrollBack() {
        cardFlowPanel.doClickLeft();
    }

    private BufferedImage getTestImage(final Color aColor, final String text) {
        final BufferedImage image = ImageHelper.getCompatibleBufferedImage(CardFlowPanel.MAX_IMAGE_SIZE.width, CardFlowPanel.MAX_IMAGE_SIZE.height);
        final Graphics2D g2d = image.createGraphics();
        g2d.setColor(aColor);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.setColor(Color.WHITE);
        g2d.setFont(getFont().deriveFont(80f));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.drawString(text, 10, 80);
        g2d.drawString(text, image.getWidth() - 10 - 50, image.getHeight() - 20);
        g2d.dispose();
        return image;
    }

    private List<BufferedImage> getRandomListOfCardImages(int count) {
        List<MagicCardDefinition> cards = new ArrayList<>(CardDefinitions.getDefaultPlayableCardDefs());
        Collections.shuffle(cards, new Random(MagicRandom.nextRNGInt()));
        return cards.stream()
            .map(card -> MagicImages.getCardImage(card))
            .limit(count)
            .collect(Collectors.toList());
    }

    private List<BufferedImage> getImages() {

        final List<BufferedImage> images = new ArrayList<>();

        images.add(getTestImage(Color.ORANGE, "0"));
        images.add(getTestImage(Color.BLUE, "1"));
        images.add(getTestImage(Color.MAGENTA, "2"));
        images.addAll(getRandomListOfCardImages(5));
        images.add(getTestImage(Color.ORANGE, Integer.toString(images.size())));
        images.add(getTestImage(Color.BLUE, Integer.toString(images.size())));

        return images;
    }

    @Override
    public void setNewActiveImage(int activeImageIndex) {
        System.out.printf("setNewActiveImage = %d of %d\n", activeImageIndex, cardFlowPanel.getImagesCount() - 1);
    }
}
