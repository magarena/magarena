package magic.ui.screen.wip.cardflow;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.swing.JPanel;
import magic.data.CardDefinitions;
import magic.data.MagicIcon;
import magic.model.IRenderableCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicRandom;
import magic.ui.MagicImages;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.widget.MenuButton;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CardFlowScreen extends HeaderFooterScreen
    implements ICardFlowListener, ICardFlowProvider {

    private static final Color BACKGROUND_COLOR = new Color(18, 30, 49);

    private final CardFlowPanel cardFlowPanel;
    private List<IRenderableCard> cards;
    private final MenuButton activeImageButton = new MenuButton("", null);
    private final ICardFlowProvider provider;

     public CardFlowScreen(ICardFlowProvider provider, String screenTitle) {
        super(screenTitle);
        this.provider = provider;
        cardFlowPanel = new CardFlowPanel(provider);
        initialize();
     }

    public CardFlowScreen() {
        super("Cardflow Test Screen");
        this.provider = this;
        cardFlowPanel = new CardFlowPanel(this);
        initialize();
    }

    private void initialize() {

        cardFlowPanel.setBackground(BACKGROUND_COLOR);
        cardFlowPanel.addListener(this);

        final JPanel panel = new JPanel(new MigLayout("insets 0, aligny center"));
        panel.setOpaque(false);
        final int h = CardFlowPanel.MAX_IMAGE_SIZE.height + 20;
        panel.add(cardFlowPanel, "w 100%, h " + h + "!, aligny center");

        setMainContent(panel);

        MenuButton[] btns = new MenuButton[3];
        btns[0] = getScrollBackButton();
        btns[1] = activeImageButton;
        btns[2] = getScrollForwardsButton();
        addFooterGroup(btns);

        activeImageButton.setText(String.format("%d of %d",
            provider.getStartImageIndex() + 1, provider.getImagesCount())
        );

    }

    private MenuButton getScrollForwardsButton() {
        MenuButton btn = MenuButton.build(this::doScrollForwards,
            MagicIcon.GO_NEXT,
            "Scroll forwards",
            "You can also use the right arrow key or by moving the mouse-wheel back."
        );
        return btn;
    }

    private MenuButton getScrollBackButton() {
        MenuButton btn = MenuButton.build(this::doScrollBack,
            MagicIcon.GO_BACK,
            "Scroll back",
            "You can also use the left arrow key or by moving the mouse-wheel forwards."
        );
        return btn;
    }

    private void doScrollForwards() {
        cardFlowPanel.doClickRight();
    }

    private void doScrollBack() {
        cardFlowPanel.doClickLeft();
    }

    private List<IRenderableCard> getRandomListOfRenderableCards(int count) {
        final List<MagicCardDefinition> cards = new ArrayList<>(CardDefinitions.getDefaultPlayableCardDefs());
        Collections.shuffle(cards, new Random(MagicRandom.nextRNGInt()));
        return cards.stream()
            .limit(count)
            .collect(Collectors.toList());
    }

    @Override
    public void setNewActiveImage(int activeImageIndex) {
        System.out.printf("setNewActiveImage = %d of %d\n", activeImageIndex, cardFlowPanel.getImagesCount() - 1);
        activeImageButton.setText(String.format("%d of %d",
            activeImageIndex + 1, provider.getImagesCount())
        );
    }

    private List<IRenderableCard> getCards() {
        if (cards == null) {
            cards = getRandomListOfRenderableCards(200);
        }
        return cards;
    }

    @Override
    public BufferedImage getImage(int index) {
        return MagicImages.getCardImage(getCards().get(index));
    }

    @Override
    public int getImagesCount() {
        return getCards().size();
    }
}
