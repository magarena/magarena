package magic.ui.screen;

import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import magic.data.DeckType;
import magic.data.MagicIcon;
import magic.model.MagicCardDefinition;
import magic.ui.MagicImages;
import magic.model.MagicDeck;
import magic.ui.ScreenController;
import magic.translate.UiString;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.deck.widget.DeckStatusPanel;

@SuppressWarnings("serial")
public class DeckViewScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar, IDeckConsumer {

    // translatable strings
    private static final String _S1 = "Close";
    private static final String _S2 = "Sample Hand";
    private static final String _S3 = "See what kind of Hand you might be dealt from this deck.";
    private static final String _S4 = "A deck with a minimum of 7 cards is required first.";
    private static final String _S5 = "Deck View";
    private static final String _S6 = "Shows complete deck using tiled card images.";
    private static final String _S7 = "Deck is empty! Nothing to show.";
    private static final String _S8 = "Deck";

    private DeckViewPanel screenContent;
    private final DeckStatusPanel deckStatusPanel = new DeckStatusPanel();

    public DeckViewScreen(final MagicDeck deck, final MagicCardDefinition selectedCard) {
        setupScreen(deck, selectedCard);
    }

    public DeckViewScreen(final MagicDeck deck) {
        setupScreen(deck, null);
    }

    private void setupScreen(final MagicDeck deck, final MagicCardDefinition selectedCard) {
        this.screenContent = new DeckViewPanel(deck, selectedCard);
        setDeck(deck);
        setContent(this.screenContent);
    }

    @Override
    public MenuButton getLeftAction() {
        return MenuButton.getCloseScreenButton(UiString.get(_S1));
    }

    @Override
    public MenuButton getRightAction() {
        return null;
    }

    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<>();
        buttons.add(new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.HAND_ICON),
                        UiString.get(_S2), UiString.get(_S3),
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                if (screenContent.getDeck().size() >= 7) {
                                    ScreenController.showSampleHandScreen(screenContent.getDeck());
                                } else {
                                    showInvalidActionMessage(UiString.get(_S4));
                                }
                            }
                        })
        );
        buttons.add(new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.TILED_ICON),
                        UiString.get(_S5), UiString.get(_S6),
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                if (screenContent.getDeck().size() > 0) {
                                    ScreenController.showDeckView(screenContent.getDeck());
                                } else {
                                    showInvalidActionMessage(UiString.get(_S7));
                                }
                            }
                        })
        );
        return buttons;
    }

    private void showInvalidActionMessage(final String message) {
        ScreenController.showWarningMessage(message);
    }

    @Override
    public String getScreenCaption() {
        return UiString.get(_S8);
    }

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

    private void setDeck(final MagicDeck deck) {
        screenContent.setDeck(deck);
        deckStatusPanel.setDeck(deck, false);
    }

    @Override
    public void setDeck(MagicDeck deck, Path deckPath) {
        setDeck(deck);
    }

    @Override
    public void setDeck(String deckName, DeckType deckType) { }

    @Override
    public JPanel getStatusPanel() {
        return deckStatusPanel;
    }

}
