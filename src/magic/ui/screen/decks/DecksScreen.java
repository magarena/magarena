package magic.ui.screen.decks;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import magic.data.MagicIcon;
import magic.translate.UiString;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.widget.deck.DeckStatusPanel;
import magic.ui.dialog.DecksFilterDialog;
import magic.ui.screen.AbstractScreen;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;

@SuppressWarnings("serial")
public class DecksScreen extends AbstractScreen implements IStatusBar, IActionBar {

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
        this.deckConsumer = deckConsumer;
        deckStatusPanel = new DeckStatusPanel();
        screenContent = new ScreenPanel(deckStatusPanel);
        setContent(screenContent);
    }

    @Override
    public String getScreenCaption() {
        return UiString.get(_S1);
    }

    @Override
    public MenuButton getLeftAction() {
        return MenuButton.getCloseScreenButton(UiString.get(_S2));
    }

    @Override
    public MenuButton getRightAction() {
        return new ActionBarButton(
            UiString.get(_S3),
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (screenContent.getDeck() == null) {
                        showInvalidActionMessage(UiString.get(_S4));
                    } else if (screenContent.getDeck().isValid() == false) {
                        showInvalidActionMessage(UiString.get(_S5));
                    } else {
                        deckConsumer.setDeck(screenContent.getDeck(), screenContent.getDeckPath());
                        ScreenController.closeActiveScreen(false);
                    }
                }
            }
        );
    }

    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<>();
        buttons.add(new ActionBarButton(
            MagicImages.getIcon(MagicIcon.HAND_ICON),
            UiString.get(_S6), UiString.get(_S7),
            new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (screenContent.getDeck() == null || screenContent.getDeck().size() < 7) {
                        showInvalidActionMessage(UiString.get(_S8));
                    } else if (screenContent.getDeck().isValid() == false) {
                        showInvalidActionMessage(UiString.get(_S9));
                    } else {
                        ScreenController.showSampleHandScreen(screenContent.getDeck());
                    }
                }
            }
        ));
        buttons.add(new ActionBarButton(
            MagicImages.getIcon(MagicIcon.TILED_ICON),
            UiString.get(_S10), UiString.get(_S11),
            new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (screenContent.getDeck() == null || screenContent.getDeck().isEmpty()) {
                        showInvalidActionMessage(UiString.get(_S12));
                    } else if (screenContent.getDeck().isValid() == false) {
                        showInvalidActionMessage(UiString.get(_S13));
                    } else {
                        ScreenController.showDeckView(screenContent.getDeck());
                    }
                }
            }
        ));
        return buttons;
    }

    private void showInvalidActionMessage(final String message) {
        ScreenController.showWarningMessage(message);
    }

    @Override
    public boolean isScreenReadyToClose(final Object nextScreen) {
        DecksFilterDialog.resetFilterHistory();
        return true;
    }

    @Override
    public JPanel getStatusPanel() {
        return deckStatusPanel;
    }

}
