package magic.ui.screen.deck.editor;

import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import magic.data.DeckType;
import magic.data.GeneralConfig;
import magic.data.MagicIcon;
import magic.data.MagicSetDefinitions;
import magic.exception.InvalidDeckException;
import magic.model.MagicDeck;
import magic.ui.MagicFileChoosers;
import magic.ui.MagicLogs;
import magic.ui.ScreenController;
import magic.ui.WikiPage;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.duel.decks.DuelDecksScreen;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.screen.widget.MenuButton;
import magic.ui.widget.deck.DeckStatusPanel;
import magic.utility.DeckUtils;

@SuppressWarnings("serial")
public class DeckEditorSplitScreen extends HeaderFooterScreen
    implements IDeckConsumer {

    private final DeckEditorSplitPanel screenContent;
    private final boolean isStandalone;
    private final DeckStatusPanel deckStatusPanel = new DeckStatusPanel();

    // CTR : opens Deck Editor ready to update passed in deck.
    public DeckEditorSplitScreen(final MagicDeck deck) {
        super("Deck Editor");
        isStandalone = (deck == null);
        this.screenContent = new DeckEditorSplitPanel(deck);
        setMainContent(this.screenContent);
        setHeaderContent(deckStatusPanel);
        setLeftFooter(getLeftAction());
        setRightFooter(getRightAction());
        addToFooter(
            MenuButton.build(this::loadDeck,
                MagicIcon.OPEN, "Select Deck", "Select an existing prebuilt or player deck."
            ),
            MenuButton.build(this::saveDeck,
                MagicIcon.SAVE, "Save Deck", "Save deck to file."
            ),
            MenuButton.build(this::showSampleHand,
                MagicIcon.HAND_ICON, "Sample Hand", "See what kind of Hand you might be dealt from this deck."
            ),
            MenuButton.build(this::showDeckImageView,
                MagicIcon.TILED, "Deck View", "Shows complete deck using tiled card images."
            )
        );
        setWikiPage(WikiPage.DECK_EDITOR);
    }

    // CTR : open Deck Editor in standalone mode starting with an empty deck.
    public DeckEditorSplitScreen() {
        this(null);
        loadMostRecentDeck();
    }

    private void showSampleHand() {
        if (screenContent.getDeck().size() >= 7) {
            ScreenController.showSampleHandScreen(screenContent.getDeck());
        } else {
            showInvalidActionMessage("A deck with a minimum of 7 cards is required first.");
        }
    }

    private void showDeckImageView() {
        if (screenContent.getDeck().size() > 0) {
            ScreenController.showDeckTiledCardsScreen(screenContent.getDeck());
        } else {
            showInvalidActionMessage("Deck is empty! Nothing to show.");
        }
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

    public MenuButton getLeftAction() {
        final String caption = (!screenContent.isStandaloneDeckEditor() ? "Cancel" : "Close");
        return MenuButton.getCloseScreenButton(caption);
    }

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

    private void showInvalidActionMessage(final String message) {
        ScreenController.showWarningMessage(message);
    }

    public void createNewEmptyDeck() {
        screenContent.setDeck(new MagicDeck());
    }

    public void loadDeck() {
        ScreenController.showDecksScreen(this);
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
                            ScreenController.getMainFrame(),
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
    public boolean isScreenReadyToClose(final Object nextScreen) {
        if (screenContent.isDeckEditor() && !screenContent.isStandaloneDeckEditor() && nextScreen instanceof DuelDecksScreen) {
            ((DuelDecksScreen)nextScreen).updateDecksAfterEdit();
        }
        MagicSetDefinitions.clearLoadedSets();
        MagicLogs.clearLoadedLogs();
        return true;
    }

    @Override
    public void setDeck(MagicDeck deck, Path deckPath) {
        screenContent.setDeck(deck);
        setMostRecentDeck(deckPath.toString());
        deckStatusPanel.setDeck(deck, false);
    }

    @Override
    public void setDeck(String deckName, DeckType deckType) { }
    
}
