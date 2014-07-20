package magic.ui.screen;

import magic.data.DeckUtils;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.data.MagicSetDefinitions;
import magic.model.MagicDeck;
import magic.ui.DownloadImagesDialog;
import magic.ui.ExplorerPanel;
import magic.ui.MagicFrame;
import magic.ui.ScreenOptionsOverlay;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.widget.deck.DeckStatusPanel;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import magic.data.DeckType;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.screen.interfaces.IWikiPage;

@SuppressWarnings("serial")
public class DeckEditorScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar, IOptionsMenu, IWikiPage, IDeckConsumer {

    private final ExplorerPanel screenContent;
    private final boolean isStandalone;
    private final DeckStatusPanel deckStatusPanel = new DeckStatusPanel();

    // CTR : opens Deck Editor ready to update passed in deck.
    public DeckEditorScreen(final MagicDeck deck) {
        isStandalone = (deck == null);
        this.screenContent = new ExplorerPanel(deck);
        setContent(this.screenContent);
    }
    // CTR : open Deck Editor in standalone mode starting with an empty deck.
    public DeckEditorScreen() {
        this(null);
        loadMostRecentDeck();
    }

    private void loadMostRecentDeck() {
        final String deckFilename = GeneralConfig.getInstance().getMostRecentDeckFilename();
        if (!deckFilename.trim().isEmpty()) {
            final MagicDeck recentDeck = DeckUtils.loadDeckFromFile(deckFilename);
            if (recentDeck != null) {
                this.screenContent.setDeck(recentDeck);
                deckStatusPanel.setDeck(recentDeck, false);
            }
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
                        getFrame().closeActiveScreen(false);
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
        buttons.add(
                new ActionBarButton(
                        IconImages.OPEN_ICON,
                        "Select Deck", "Select an existing prebuilt or player deck.",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                loadDeck();
                            }
                        })
                );
        buttons.add(
                new ActionBarButton(
                        IconImages.SAVE_ICON,
                        "Save Deck", "Save deck to file.",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                saveDeck();
                            }
                        })
                );
        buttons.add(
                new ActionBarButton(
                        IconImages.HAND_ICON,
                        "Sample Hand", "See what kind of Hand you might be dealt from this deck.",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                if (screenContent.getDeck().size() >= 7) {
                                    getFrame().showSampleHandGenerator(screenContent.getDeck());
                                } else {
                                    showInvalidActionMessage("A deck with a minimum of 7 cards is required first.");
                                }
                            }
                        })
                );
        buttons.add(
                new ActionBarButton(
                        IconImages.TILED_ICON,
                        "Deck View", "Shows complete deck using tiled card images.",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                if (screenContent.getDeck().size() > 0) {
                                    getFrame().showDeckView(screenContent.getDeck());
                                } else {
                                    showInvalidActionMessage("Deck is empty! Nothing to show.");
                                }
                            }
                        })
                );

        return buttons;
    }

    private void showInvalidActionMessage(final String message) {
        JOptionPane.showMessageDialog(this, message, "Invalid Action", JOptionPane.INFORMATION_MESSAGE);
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
        getFrame().showDeckChooserScreen(this);
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
                    JOptionPane.showMessageDialog(
                            getFrame(),
                            "This directory is reserved for prebuilt decks.\nPlease choose a different directory.",
                            "Invalid directory",
                            JOptionPane.WARNING_MESSAGE);
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
        fileChooser.setFileFilter(DeckUtils.DECK_FILEFILTER);
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
                JOptionPane.showMessageDialog(
                        getFrame(),
                        "There was a problem saving the deck file!",
                        "Deck not saved",
                        JOptionPane.ERROR_MESSAGE);
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
        DownloadImagesDialog.clearLoadedLogs();
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

        /* (non-Javadoc)
         * @see magic.ui.ScreenOptionsOverlay#getScreenMenu()
         */
        @Override
        protected MenuPanel getScreenMenu() {
            return null;
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
