package magic.ui.screen.deck.hand;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import magic.data.GeneralConfig;
import magic.data.MagicIcon;
import magic.model.IRenderableCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.screen.HandCanvasScreen;
import magic.ui.screen.HandZoneLayout;
import magic.ui.screen.MScreen;
import magic.ui.screen.cardflow.FlashTextOverlay;
import magic.ui.screen.cardflow.ICardFlowProvider;
import magic.ui.screen.widget.PlainMenuButton;
import magic.ui.widget.cards.canvas.CardsCanvas;
import magic.ui.widget.cards.canvas.CardsCanvas.LayoutMode;
import magic.ui.widget.cards.canvas.ICardsCanvasListener;
import magic.ui.widget.deck.DeckStatusPanel;

@SuppressWarnings("serial")
public class SampleHandScreen extends HandCanvasScreen
    implements ICardsCanvasListener, ICardFlowProvider {

    // translatable strings
    private static final String _S1 = "Sample Hand";
    private static final String _S3 = "Refresh";
    private static final String _S4 = "Deal a new sample hand.";

    private SampleHandLayeredPane layeredPane;
    private CardsCanvas content;
    private final MagicDeck deck;
    private final DeckStatusPanel deckStatusPanel = new DeckStatusPanel();
    private OptionsPanel optionsPanel;
    private List<? extends IRenderableCard> renderableCards = new ArrayList<>();
    private int startImageIndex = 0;
    private final FlashTextOverlay flashOverlay;

    public SampleHandScreen(final MagicDeck aDeck) {
        super(MText.get(_S1));
        this.deck = aDeck;
        flashOverlay = new FlashTextOverlay(600, 60);
        useLoadingScreen(this::initUI);
    }

    private void initUI() {

        content = new CardsCanvas();
        content.setListener(this);
        setCardsLayout();
        content.setAnimationDelay(50, 20);
        content.setLayoutMode(LayoutMode.SCALE_TO_FIT);
        content.refresh(getSampleHand(deck));

        layeredPane = new SampleHandLayeredPane(content, flashOverlay);
        setMainContent(layeredPane);

        deckStatusPanel.setDeck(deck, false);
        optionsPanel = new OptionsPanel(this);
        setHeaderContent(deckStatusPanel);
        setHeaderOptions(optionsPanel);
        addToFooter(PlainMenuButton.build(this::dealSampleHand,
                MagicIcon.REFRESH, MText.get(_S3), MText.get(_S4))
        );
    }

    private void dealSampleHand() {
        if (!content.isBusy()) {
            content.refresh(getSampleHand(deck));
        }
    }

    private List<MagicCardDefinition> getSampleHand(MagicDeck deck) {
        List<MagicCardDefinition> cards = deck.getRandomCards(7).stream()
            .sorted(MagicCardDefinition.SORT_BY_NAME)
            .collect(Collectors.toList());
        renderableCards = cards;
        return cards;
    }

    private void setCardsLayout() {
        switch (HandZoneLayout.getLayout()) {
        case STACKED_DUPLICATES:
            content.setStackDuplicateCards(true);
            break;
        case NO_STACKING:
            content.setStackDuplicateCards(false);
            break;
        default:
            throw new IndexOutOfBoundsException();
        }
    }

    private void doSaveSettings() {
        HandZoneLayout.save();
        GeneralConfig.getInstance().save();
    }

    @Override
    public boolean isScreenReadyToClose(MScreen nextScreen) {
        if (super.isScreenReadyToClose(nextScreen)) {
            doSaveSettings();
            return true;
        }
        return false;
    }

    @Override
    public void cardSelected(MagicCardDefinition aCard) {
        // not applicable
    }

    @Override
    public void cardClicked(int index, MagicCardDefinition card) {
        for (int i = index; i < renderableCards.size(); i++) {
            if (renderableCards.get(i) == card) {
                startImageIndex = i;
                ScreenController.showCardFlowScreen(this, MText.get(_S1));
                break;
            }
        }
    }

    @Override
    public BufferedImage getImage(int index) {
        return MagicImages.getCardImage(renderableCards.get(index));
    }

    @Override
    public int getImagesCount() {
        return renderableCards.size();
    }

    @Override
    public int getStartImageIndex() {
        return startImageIndex;
    }

    void flashLayoutSetting() {
        flashOverlay.flashText(HandZoneLayout.getLayout().getDisplayName());
    }

    void setCardsLayout(int ordinal) {
        HandZoneLayout.setLayout(ordinal);
        setCardsLayout();
        flashLayoutSetting();
    }

}
