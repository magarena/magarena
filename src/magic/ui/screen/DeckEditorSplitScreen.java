package magic.ui.screen;

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
import magic.model.MagicDeck;
import magic.ui.MagicFileChoosers;
import magic.ui.MagicFrame;
import magic.ui.MagicLogs;
import magic.ui.ScreenController;
import magic.ui.ScreenOptionsOverlay;
import magic.ui.deck.editor.DeckEditorSplitPanel;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.interfaces.IWikiPage;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.deck.widget.DeckStatusPanel;

@SuppressWarnings("serial")
public class DeckEditorSplitScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar, IOptionsMenu, IWikiPage, IDeckConsumer {

    private final DeckEditorSplitPanel screenContent;
    private final boolean isStandalone;
    private final DeckStatusPanel deckStatusPanel = new DeckStatusPanel();

    // CTR : opens Deck Editor ready to update passed in deck.
    public DeckEditorSplitScreen(final MagicDeck deck) {
        isStandalone = (deck == null);
        this.screenContent = new DeckEditorSplitPanel(deck);
        setContent(this.screenContent);
    }
    // CTR : open Deck Editor in standalone mode starting with an empty deck.
    public DeckEditorSplitScreen() {
        this(null);
        loadMostRecentDeck();
    }

    private void loadMostRecentDeck() {
        final Path deckFilePath = GeneralConfig.getInstance().getMostRecentDeckFilePath();
        if (deckFilePath != null) {
            final MagicDeck recentDeck = loadDeck(deckFilePath);
            if (recentDeck != null && recentDeck.isValid()) {
                this.screenContent.setDeck(recentDeck);
                deckStatusPanel.setDeck(recentDeck, false);
            }
        }
    }

    private MagicDeck loadDeck(final Path deckFilePath) {
        try {
            return DeckUtils.loadDeckFromFile(deckFilePath);
        } catch (InvalidDeckException ex) {
            // if the most recent deck is invalid for some reason then I think it suffices
            // to log the error to console and open the deck editor with an empty deck.
            System.err.println(ex);
            return null;
        }
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getLeftAction()
     */
    @Override
    public MenuButton getLeftAction() {
        final String caption = (!screenContent.isStandaloneDeckEditor() ? "Cancel" : "Close");
        return MenuButton.getCloseScreenButton(caption);
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getRightAction()
     */
    @Override
    public MenuButton getRightAction() {
        if (!screenContent.isStandaloneDeckEditor()) {
            return new MenuButton("Use this deck", new AbstractAction() {
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

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getMiddleActions()
     */
    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<>();
        buttons.add(new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.OPEN_ICON),
                        "Select Deck", "Select an existing prebuilt or player deck.",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                loadDeck();
                            }
                        })
                );
        buttons.add(new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.SAVE_ICON),
                        "Save Deck", "Save deck to file.",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                saveDeck();
                            }
                        })
                );
        buttons.add(new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.HAND_ICON),
                        "Sample Hand", "See what kind of Hand you might be dealt from this deck.",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                if (screenContent.getDeck().size() >= 7) {
                                    ScreenController.showSampleHandScreen(screenContent.getDeck());
                                } else {
                                    showInvalidActionMessage("A deck with a minimum of 7 cards is required first.");
                                }
                            }
                        })
                );
        buttons.add(new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.TILED_ICON),
                        "Deck View", "Shows complete deck using tiled card images.",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                if (screenContent.getDeck().size() > 0) {
                                    ScreenController.showDeckView(screenContent.getDeck());
                                } else {
                                    showInvalidActionMessage("Deck is empty! Nothing to show.");
                                }
                            }
                        })
                );

        return buttons;
    }

    private void showInvalidActionMessage(final String message) {
        ScreenController.showWarningMessage(message);
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagStatusBar#getScreenCaption()
     */
    @Override
    public String getScreenCaption() {
        return "Deck Editor";
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagScreenOptionsMenu#showOptionsMenuOverlay()
     */
    @Override
    public void showOptionsMenuOverlay() {
        new ScreenOptions(getFrame());
    }

    public void createNewEmptyDeck() {
        screenContent.setDeck(new MagicDeck());
    }

    public void loadDeck() {
        ScreenController.showDeckChooserScreen(this);
    }

    public void saveDeck() {

        if (screenContent.getDeck().size() == 0) {
            showInvalidActionMessage("Deck is empty! Nothing to save.");
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
                final Path prebuiltDecksFolder = DeckUtils.getPrebuiltDecksFolder();
                final Path saveFolder = getSelectedFile().toPath().getParent();
                if (saveFolder.equals(prebuiltDecksFolder)) {
                    ScreenController.showWarningMessage("This directory is reserved for prebuilt decks.\nPlease choose a different directory.");
                } else if (Files.exists(getSelectedFile().toPath())) {
                    int response = JOptionPane.showConfirmDialog(
                            getFrame(),
                            "Overwrite existing deck file?",
                            "Overwrite file",
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
        fileChooser.setDialogTitle("Save deck");
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
                screenContent.setDeck(screenContent.getDeck());
                setMostRecentDeck(filename);
            } else {
                ScreenController.showWarningMessage("There was a problem saving the deck file!");
            }
        }
    }

    private void setMostRecentDeck(final String filename) {
        if (isStandalone) {
            GeneralConfig.getInstance().setMostRecentDeckFilename(filename);
            GeneralConfig.getInstance().save();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see magic.ui.MagScreen#canScreenClose()
     */
    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        if (screenContent.isDeckEditor() && !screenContent.isStandaloneDeckEditor() && nextScreen instanceof DuelDecksScreen) {
            ((DuelDecksScreen)nextScreen).updateDecksAfterEdit();
        }
        MagicSetDefinitions.clearLoadedSets();
        MagicLogs.clearLoadedLogs();
        return true;
    }

    @Override
    public String getWikiPageName() {
        return "UIDeckEditor";
    }

    @Override
    public void setDeck(MagicDeck deck, Path deckPath) {
        screenContent.setDeck(deck);
        setMostRecentDeck(deckPath.toString());
        deckStatusPanel.setDeck(deck, false);
    }

    @Override
    public void setDeck(String deckName, DeckType deckType) { }

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

    /* (non-Javadoc)
     * @see magic.ui.screen.interfaces.IStatusBar#getStatusPanel()
     */
    @Override
    public JPanel getStatusPanel() {
        return deckStatusPanel;
    }

}
