package magic.ui.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import magic.data.DeckType;
import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.model.MagicDeck;
import magic.ui.ScreenController;
import magic.translate.UiString;
import magic.ui.cardtable.CardTable;
import magic.ui.dialog.DecksFilterDialog;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.duel.viewer.CardViewer;
import magic.ui.deck.widget.DeckDescriptionViewer;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.deck.widget.DeckPicker;
import magic.ui.deck.widget.DeckStatusPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DecksScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar {

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
    private static final String _S14 = "%s (%d cards)";
    private static final String _S15 = "NO DECK";

    private final ScreenContent screenContent;
    private final IDeckConsumer deckConsumer;
    private final DeckStatusPanel deckStatusPanel;

    public DecksScreen(final IDeckConsumer deckConsumer) {
        this.deckConsumer = deckConsumer;
        deckStatusPanel = new DeckStatusPanel();
        screenContent = new ScreenContent();
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
                });
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
                        })
                );
        buttons.add(new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.TILED_ICON),
                        UiString.get(_S10), UiString.get(_S11),
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                if (screenContent.getDeck() == null || screenContent.getDeck().size() == 0) {
                                    showInvalidActionMessage(UiString.get(_S12));
                                } else if (screenContent.getDeck().isValid() == false) {
                                    showInvalidActionMessage(UiString.get(_S13));
                                } else {
                                    ScreenController.showDeckView(screenContent.getDeck());
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
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        DecksFilterDialog.resetFilterHistory();
        return true;
    }

    @Override
    public JPanel getStatusPanel() {
        return deckStatusPanel;
    }

    private class ScreenContent extends JPanel implements IDeckConsumer {

        private MagicDeck selectedDeck = null;
        private Path deckFilePath = null;
        private final DeckDescriptionViewer descViewer = new DeckDescriptionViewer();
        private final CardTable deckTable;
        private final CardViewer cardViewer = new CardViewer();
        private DeckPicker deckPicker;
        private final JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        private ScreenContent() {

            descViewer.setDeckChooserLayout();
            setOpaque(false);

            selectedDeck = new MagicDeck();
            deckTable = new CardTable(selectedDeck, "{deckName}", true);
            deckTable.addCardSelectionListener(cardViewer);
            deckTable.setHeaderVisible(false);
            deckTable.showCardCount(true);

            setLayout(new MigLayout("insets 0, gap 0"));
            add(getDeckNamesPanel(), "w 300!, h 100%");
            add(getDeckDetailsPane(), "w 100%, h 100%");

        }

        public MagicDeck getDeck() {
            return selectedDeck;
        }

        public Path getDeckPath() {
            return deckFilePath;
        }

        private JSplitPane getDeckDetailsPane() {
            splitter.setOneTouchExpandable(false);
            splitter.setLeftComponent(deckTable);
            splitter.setRightComponent(getCardDetailsPanel());
            splitter.setDividerSize(14);
            splitter.setBorder(null);
            splitter.setOpaque(false);
            splitter.getRightComponent().setMinimumSize(new Dimension());
            splitter.setResizeWeight(1.0);
            return splitter;
        }

        private JPanel getCardDetailsPanel() {
            final JPanel panel = new JPanel();
            panel.setMinimumSize(new Dimension());
            panel.setOpaque(false);
            panel.setLayout(new MigLayout("insets 0"));
            panel.add(cardViewer, "w 100%, h 0:100%");
            return panel;
        }

        private JPanel getDeckNamesPanel() {

            deckPicker = new DeckPicker();
            deckPicker.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
            deckPicker.addListener(this);

            descViewer.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));

            // main container panel
            final JPanel container = new TexturedPanel();
            container.setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
            container.setLayout(new MigLayout("insets 0, gap 0, flowy"));
            container.add(deckPicker, "w 100%, h 100%");
            container.add(descViewer, "w 100%, h 28%:28%:200px");
            return container;
        }

        @Override
        public void setDeck(String deckName, DeckType deckType) {
            System.out.println(deckName + ", " + deckType);
        }

        @Override
        public void setDeck(MagicDeck deck, Path deckPath) {
            if (deckPath != null) {
                selectedDeck = deck;
                deckFilePath = deckPath;
                descViewer.setDeckDescription(deck);
                deckTable.setCards(deck);
                deckTable.setTitle(UiString.get(_S14, deck.getName(), deck.size()));
                deckStatusPanel.setDeck(deck, deck.isValid() || deck.size() > 0);
                splitter.setVisible(deck.isValid() || deck.size() > 0);
            } else {
                selectedDeck = null;
                deckFilePath = null;
                descViewer.setDeckDescription(selectedDeck);
                deckTable.setCards(deck);
                deckTable.setTitle(UiString.get(_S15));
                deckStatusPanel.setDeck(null, false);
                splitter.setVisible(false);
            }
        }

    }

}
