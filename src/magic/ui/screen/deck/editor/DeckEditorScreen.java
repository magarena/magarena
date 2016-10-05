package magic.ui.screen.deck.editor;

import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import magic.data.DeckType;
import magic.utility.DeckUtils;
import magic.data.GeneralConfig;
import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.data.MagicSetDefinitions;
import magic.exception.InvalidDeckException;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.MagicFileChoosers;
import magic.ui.MagicFrame;
import magic.ui.ScreenController;
import magic.ui.ScreenOptionsOverlay;
import magic.translate.UiString;
import magic.ui.MagicLogs;
import magic.ui.screen.deck.editor.DeckEditorScreenPanel;
import magic.ui.screen.deck.editor.IDeckEditorListener;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.interfaces.IWikiPage;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.deck.widget.DeckStatusPanel;
import magic.ui.screen.AbstractScreen;
import magic.ui.screen.duel.decks.DuelDecksScreen;
import magic.utility.MagicFileSystem;
import magic.utility.WikiPage;

@SuppressWarnings("serial")
public class DeckEditorScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar, IOptionsMenu, IWikiPage, IDeckConsumer, IDeckEditorListener {

    // translatable strings
    private static final String _S1 = "Cancel";
    private static final String _S2 = "Close";
    private static final String _S3 = "Use this deck";
    private static final String _S4 = "Select Deck";
    private static final String _S5 = "Select an existing prebuilt or player deck.";
    private static final String _S6 = "Save Deck";
    private static final String _S7 = "Save deck to file.";
    private static final String _S8 = "Sample Hand";
    private static final String _S9 = "See what kind of Hand you might be dealt from this deck.";
    private static final String _S10 = "A deck with a minimum of 7 cards is required first.";
    private static final String _S11 = "Deck View";
    private static final String _S12 = "Shows complete deck using tiled card images.";
    private static final String _S13 = "Deck is empty! Nothing to show.";
    private static final String _S14 = "Deck Editor";
    private static final String _S15 = "Deck is empty! Nothing to save.";
    private static final String _S16 = "This directory is reserved for prebuilt decks.\nPlease choose a different directory.";
    private static final String _S17 = "Overwrite existing deck file?";
    private static final String _S18 = "Overwrite file";
    private static final String _S19 = "Save deck";
    private static final String _S20 = "There was a problem saving the deck file!";

    private DeckEditorScreenPanel screenContent;
    private final boolean isStandalone;
    private final DeckStatusPanel deckStatusPanel = new DeckStatusPanel();

    // CTR : opens Deck Editor ready to update passed in deck.
    public DeckEditorScreen(final MagicDeck deck) {
        isStandalone = false;
        setScreenContent(deck);
    }
    // CTR : open Deck Editor in standalone mode starting with an empty deck.
    public DeckEditorScreen() {
        isStandalone = true;
        setScreenContent(getMostRecentEditedDeck());
    }

    private void setScreenContent(final MagicDeck deck) {
        screenContent = new DeckEditorScreenPanel(deck, this);
        screenContent.setIsStandalone(isStandalone);
        setDeck(deck == null ? new MagicDeck() : deck);
        setContent(screenContent);
    }

    private static MagicDeck getMostRecentEditedDeck() {
        final Path deckFilePath = GeneralConfig.getInstance().getMostRecentDeckFilePath();
        if (deckFilePath != null) {
            final MagicDeck deck = loadDeck(deckFilePath);
            if (deck != null && deck.isValid()) {
                return deck;
            }
        }
        return null;
    }

    private static MagicDeck loadDeck(final Path deckFilePath) {
        try {
            return DeckUtils.loadDeckFromFile(deckFilePath);
        } catch (InvalidDeckException ex) {
            // if the most recent deck is invalid for some reason then I think it suffices
            // to log the error to console and open the deck editor with an empty deck.
            System.err.println(ex);
            return null;
        }
    }

    @Override
    public MenuButton getLeftAction() {
        final String caption = (!isStandalone ? UiString.get(_S1) : UiString.get(_S2));
        return MenuButton.getCloseScreenButton(caption);
    }

    @Override
    public MenuButton getRightAction() {
        if (!isStandalone) {
            return new MenuButton(UiString.get(_S3), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (screenContent.validateDeck(true) && screenContent.applyDeckUpdates()) {
                        ScreenController.closeActiveScreen(false);
                    }
                }
            });
        } else {
            return null;
        }
    }

    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<>();
        buttons.add(new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.OPEN_ICON),
                        UiString.get(_S4), UiString.get(_S5),
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                loadDeck();
                            }
                        })
                );
        buttons.add(new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.SAVE_ICON),
                        UiString.get(_S6), UiString.get(_S7),
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                saveDeck();
                            }
                        })
                );
        buttons.add(new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.HAND_ICON),
                        UiString.get(_S8), UiString.get(_S9),
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                if (screenContent.getDeck().size() >= 7) {
                                    ScreenController.showSampleHandScreen(screenContent.getDeck());
                                } else {
                                    showInvalidActionMessage(UiString.get(_S10));
                                }
                            }
                        })
                );
        buttons.add(new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.TILED_ICON),
                        UiString.get(_S11), UiString.get(_S12),
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                if (screenContent.getDeck().size() > 0) {
                                    ScreenController.showDeckView(screenContent.getDeck());
                                } else {
                                    showInvalidActionMessage(UiString.get(_S13));
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
        return UiString.get(_S14);
    }

    @Override
    public void showOptionsMenuOverlay() {
        new ScreenOptions(getFrame());
    }

    public void createNewEmptyDeck() {
        setDeck(new MagicDeck());
    }

    public void loadDeck() {
        ScreenController.showDeckChooserScreen(this);
    }

    private boolean isReservedDeckFolder(final Path saveFolder) {
        return MagicFileSystem.isSamePath(saveFolder, DeckUtils.getPrebuiltDecksFolder())
            || MagicFileSystem.isSamePath(saveFolder, DeckUtils.getFiremindDecksFolder());
    }

    public void saveDeck() {

        if (screenContent.getDeck().size() == 0) {
            showInvalidActionMessage(UiString.get(_S15));
            return;
        }

        final JFileChooser fileChooser = new JFileChooser(DeckUtils.getDeckFolder()) {
            @Override
            public void approveSelection() {
                // first ensure filename has "dec" extension
                String filename = getSelectedFile().getAbsolutePath();
                if (!filename.endsWith(DeckUtils.DECK_EXTENSION)) {
                    setSelectedFile(new File(filename + DeckUtils.DECK_EXTENSION));
                }
                if (isReservedDeckFolder(getSelectedFile().toPath().getParent())) {
                    ScreenController.showWarningMessage(UiString.get(_S16));
                } else if (Files.exists(getSelectedFile().toPath())) {
                    int response = JOptionPane.showConfirmDialog(
                            getFrame(),
                            UiString.get(_S17),
                            UiString.get(_S18),
                            JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        super.approveSelection();
                    }
                } else {
                    super.approveSelection();
                }
            }
        };
        final MagicDeck deck = screenContent.getDeck();
        fileChooser.setDialogTitle(UiString.get(_S19));
        fileChooser.setFileFilter(MagicFileChoosers.DECK_FILEFILTER);
        fileChooser.setAcceptAllFileFilterUsed(false);
        if (deck != null) {
            fileChooser.setSelectedFile(new File(deck.getFilename()));
        }
        final int action = fileChooser.showSaveDialog(this);
        if (action == JFileChooser.APPROVE_OPTION) {
            final String filename = fileChooser.getSelectedFile().getAbsolutePath();
            if (DeckUtils.saveDeck(filename, screenContent.getDeck())) {
                final String shortFilename = fileChooser.getSelectedFile().getName();
                screenContent.getDeck().setFilename(shortFilename);
                setDeck(screenContent.getDeck());
                setMostRecentDeck(filename);
            } else {
                ScreenController.showWarningMessage(UiString.get(_S20));
            }
        }
    }

    private void setMostRecentDeck(final String filename) {
        if (isStandalone) {
            GeneralConfig.getInstance().setMostRecentDeckFilename(filename);
            GeneralConfig.getInstance().save();
        }
    }

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        if (!screenContent.isStandaloneDeckEditor() && nextScreen instanceof DuelDecksScreen) {
            ((DuelDecksScreen)nextScreen).updateDecksAfterEdit();
        }
        MagicSetDefinitions.clearLoadedSets();
        MagicLogs.clearLoadedLogs();
        return true;
    }

    @Override
    public String getWikiPageName() {
        return WikiPage.DECK_EDITOR;
    }

    @Override
    public void setDeck(final MagicDeck deck) {
        screenContent.setDeck(deck);
        deckStatusPanel.setDeck(deck, false);
    }

    @Override
    public void setDeck(MagicDeck deck, Path deckPath) {
        setDeck(deck);
        setMostRecentDeck(deckPath.toString());
    }

    @Override
    public void setDeck(String deckName, DeckType deckType) { }

    @Override
    public void deckUpdated(MagicDeck deck) {
        deckStatusPanel.setDeck(deck, false);
    }

    @Override
    public void cardSelected(MagicCardDefinition card) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addCardToRecall(MagicCardDefinition card) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class ScreenOptions extends ScreenOptionsOverlay {

        public ScreenOptions(final MagicFrame frame) {
            super(frame);
        }

        @Override
        protected MenuPanel getScreenMenu() {
            return null;
        }

        @Override
        protected boolean showPreferencesOption() {
            return false;
        }

    }

    @Override
    public JPanel getStatusPanel() {
        return deckStatusPanel;
    }

}
