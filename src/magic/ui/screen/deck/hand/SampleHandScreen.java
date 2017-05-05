package magic.ui.screen.deck.hand;

import java.util.List;
import java.util.stream.Collectors;
import magic.data.MagicIcon;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.widget.MenuButton;
import magic.ui.widget.cards.canvas.CardsCanvas;
import magic.ui.widget.cards.canvas.CardsCanvas.LayoutMode;
import magic.ui.widget.deck.DeckStatusPanel;

@SuppressWarnings("serial")
public class SampleHandScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "Sample Hand";
    private static final String _S3 = "Refresh";
    private static final String _S4 = "Deal a new sample hand.";

    private CardsCanvas content;
    private final MagicDeck deck;
    private final DeckStatusPanel deckStatusPanel = new DeckStatusPanel();

    public SampleHandScreen(final MagicDeck aDeck) {
        super(MText.get(_S1));
        this.deck = aDeck;
        useLoadingScreen(this::initUI);
    }

    private void initUI() {
        content = new CardsCanvas();
        content.setAnimationDelay(50, 20);
        content.setLayoutMode(LayoutMode.SCALE_TO_FIT);
        content.refresh(getSampleHand(deck));
        setMainContent(content);
        deckStatusPanel.setDeck(deck, false);
        setHeaderContent(deckStatusPanel);
        addToFooter(MenuButton.build(this::dealSampleHand,
                MagicIcon.REFRESH, MText.get(_S3), MText.get(_S4))
        );
    }

    private void dealSampleHand() {
        if (!content.isBusy()) {
            content.refresh(getSampleHand(deck));
        }
    }

    private List<MagicCardDefinition> getSampleHand(MagicDeck deck) {
        return deck.getRandomCards(7).stream()
            .sorted(MagicCardDefinition.SORT_BY_NAME)
            .collect(Collectors.toList());
    }
}
