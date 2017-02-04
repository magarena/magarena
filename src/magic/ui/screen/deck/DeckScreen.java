package magic.ui.screen.deck;

import java.nio.file.Path;
import magic.data.DeckType;
import magic.data.MagicIcon;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.ui.ScreenController;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.screen.widget.MenuButton;
import magic.ui.widget.deck.DeckStatusPanel;

@SuppressWarnings("serial")
public class DeckScreen extends HeaderFooterScreen
    implements IDeckConsumer {

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

    public DeckScreen(final MagicDeck deck, final MagicCardDefinition selectedCard) {
        super(MText.get(_S8));
        screenContent = new DeckScreenPanel(deck, selectedCard);
        setMainContent(screenContent);
        setHeaderContent(deckStatusPanel);
        addToFooter(MenuButton.build(this::showSampleHand,
                        MagicIcon.HAND_ICON, MText.get(_S2), MText.get(_S3)),
                MenuButton.build(this::showDeckImageView,
                        MagicIcon.TILED, MText.get(_S5), MText.get(_S6))
        );
        setDeck(deck);
    }

    public DeckScreen(final MagicDeck deck) {
        this(deck, null);
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

    @Override
    public void setDeck(MagicDeck deck) {
        screenContent.setDeck(deck);
        deckStatusPanel.setDeck(deck, false);
    }

    @Override
    public boolean setDeck(MagicDeck deck, Path deckPath) {
        setDeck(deck);
        return true;
    }

    @Override
    public void setDeck(String deckName, DeckType deckType) { }

}
