package magic.ui.screen.deck.hand;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import magic.data.MagicIcon;
import magic.model.IRenderableCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.screen.HandCanvasLayeredPane;
import magic.ui.screen.HandCanvasOptionsPanel;
import magic.ui.screen.HandCanvasScreen;
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

    private final MagicDeck deck;
    private final DeckStatusPanel deckStatusPanel = new DeckStatusPanel();
    private List<? extends IRenderableCard> renderableCards = new ArrayList<>();
    private int startImageIndex = 0;

    public SampleHandScreen(final MagicDeck aDeck) {
        super(MText.get(_S1));
        this.deck = aDeck;
        useCardsLoadingScreen(this::initUI);
    }

    private void initUI() {

        cardsCanvas = new CardsCanvas();
        cardsCanvas.setListener(this);
        setCardsLayout();
        cardsCanvas.setAnimationDelay(50, 20);
        cardsCanvas.setLayoutMode(LayoutMode.SCALE_TO_FIT);
        cardsCanvas.refresh(getSampleHand(deck));

        layeredPane = new HandCanvasLayeredPane(cardsCanvas, flashOverlay);
        setMainContent(layeredPane);

        deckStatusPanel.setDeck(deck, false);
        optionsPanel = new HandCanvasOptionsPanel(this);
        setHeaderContent(deckStatusPanel);
        setHeaderOptions(optionsPanel);
        addToFooter(PlainMenuButton.build(this::dealSampleHand,
                MagicIcon.REFRESH, MText.get(_S3), MText.get(_S4))
        );
    }

    private void dealSampleHand() {
        if (!cardsCanvas.isBusy()) {
            cardsCanvas.refresh(getSampleHand(deck));
        }
    }

    private List<MagicCardDefinition> getSampleHand(MagicDeck deck) {
        List<MagicCardDefinition> cards = deck.getRandomCards(7).stream()
            .sorted(MagicCardDefinition.SORT_BY_NAME)
            .collect(Collectors.toList());
        renderableCards = cards;
        return cards;
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
}
