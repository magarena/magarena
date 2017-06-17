package magic.ui.screen.deck;

import magic.data.MagicIcon;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.ui.ScreenController;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.widget.PlainMenuButton;
import magic.ui.widget.deck.DeckStatusPanel;

@SuppressWarnings("serial")
public class DeckScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S2 = "Sample Hand";
    private static final String _S3 = "See what kind of Hand you might be dealt from this deck.";
    private static final String _S4 = "A deck with a minimum of 7 cards is required first.";
    private static final String _S5 = "Deck View";
    private static final String _S6 = "Shows complete deck using tiled card images.";
    private static final String _S7 = "Deck is empty! Nothing to show.";
    private static final String _S8 = "Deck";

    private DeckScreenPanel screenContent;
    private final DeckStatusPanel deckStatusPanel = new DeckStatusPanel();

    private DeckScreen(MagicDeck deck, MagicCardDefinition selectedCard, String title) {
        super(title);
        screenContent = new DeckScreenPanel(deck, selectedCard);
        setMainContent(screenContent);
        setHeaderContent(deckStatusPanel);
        addToFooter(PlainMenuButton.build(this::showSampleHand, MagicIcon.HAND_ICON, MText.get(_S2), MText.get(_S3)),
            PlainMenuButton.build(this::showDeckImageView,MagicIcon.TILED, MText.get(_S5), MText.get(_S6))
        );
        deckStatusPanel.setDeck(deck, false);
    }

    public DeckScreen(final MagicDeck deck, final MagicCardDefinition selectedCard) {
        this(deck, selectedCard, MText.get(_S8));
    }

    public DeckScreen(MagicDeck deck, String title) {
        this(deck, null, title);
    }

    private void showSampleHand() {
        if (screenContent.getDeck().size() >= 7) {
            ScreenController.showSampleHandScreen(screenContent.getDeck());
        } else {
            showInvalidActionMessage(MText.get(_S4));
        }
    }

    private void showDeckImageView() {
        if (screenContent.getDeck().size() > 0) {
            ScreenController.showDeckTiledCardsScreen(screenContent.getDeck());
        } else {
            showInvalidActionMessage(MText.get(_S7));
        }
    }

    private void showInvalidActionMessage(final String message) {
        ScreenController.showWarningMessage(message);
    }
}
