package magic.ui.screen.decks;

import magic.data.MagicIcon;
import magic.translate.MText;
import magic.ui.ScreenController;
import magic.ui.widget.deck.DeckStatusPanel;
import magic.ui.dialog.DecksFilterDialog;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.screen.widget.MenuButton;

@SuppressWarnings("serial")
public class DecksScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "Decks";
    private static final String _S2 = "Cancel";
    private static final String _S3 = "Select";
    private static final String _S4 = "No deck specified.";
    private static final String _S5 = "This deck is invalid.";
    private static final String _S6 = "Sample Hand";
    private static final String _S7 = "See what kind of Hand you might be dealt from this deck.";
    private static final String _S8 = "A deck with a minimum of 7 cards is required first.";
    private static final String _S9 = "This deck is invalid.";
    private static final String _S10 = "Deck View";
    private static final String _S11 = "Shows complete deck using tiled card images.";
    private static final String _S12 = "Deck is empty! Nothing to show.";
    private static final String _S13 = "This deck is invalid.";

    private final ScreenPanel screenContent;
    private final IDeckConsumer deckConsumer;
    private final DeckStatusPanel deckStatusPanel;

    public DecksScreen(final IDeckConsumer deckConsumer) {
        super(MText.get(_S1));
        this.deckConsumer = deckConsumer;
        deckStatusPanel = new DeckStatusPanel();
        screenContent = new ScreenPanel(deckStatusPanel);
        setMainContent(screenContent);
        setHeaderContent(deckStatusPanel);
        setLeftFooter(MenuButton.getCloseScreenButton(MText.get(_S2)));
        setRightFooter(MenuButton.build(this::doSelectDeck, MText.get(_S3)));
        addToFooter(MenuButton.build(this::showSampleHand,
                        MagicIcon.HAND_ICON, MText.get(_S6), MText.get(_S7)),
                MenuButton.build(this::showDeckImageView,
                        MagicIcon.TILED, MText.get(_S10), MText.get(_S11))
        );
    }

    private void doSelectDeck() {
        if (screenContent.getDeck() == null) {
            showInvalidActionMessage(MText.get(_S4));
        } else if (screenContent.getDeck().isValid() == false) {
            showInvalidActionMessage(MText.get(_S5));
        } else {
            deckConsumer.setDeck(screenContent.getDeck(), screenContent.getDeckPath());
            ScreenController.closeActiveScreen(false);
        }
    }

    private void showSampleHand() {
        if (screenContent.getDeck() == null || screenContent.getDeck().size() < 7) {
            showInvalidActionMessage(MText.get(_S8));
        } else if (screenContent.getDeck().isValid() == false) {
            showInvalidActionMessage(MText.get(_S9));
        } else {
            ScreenController.showSampleHandScreen(screenContent.getDeck());
        }
    }

    private void showDeckImageView() {
        if (screenContent.getDeck() == null || screenContent.getDeck().isEmpty()) {
            showInvalidActionMessage(MText.get(_S12));
        } else if (screenContent.getDeck().isValid() == false) {
            showInvalidActionMessage(MText.get(_S13));
        } else {
            ScreenController.showDeckTiledCardsScreen(screenContent.getDeck());
        }
    }
    
    private void showInvalidActionMessage(final String message) {
        ScreenController.showWarningMessage(message);
    }

    @Override
    public boolean isScreenReadyToClose(final Object nextScreen) {
        DecksFilterDialog.resetFilterHistory();
        return true;
    }
}
