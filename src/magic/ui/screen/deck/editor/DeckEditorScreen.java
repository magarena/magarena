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
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.MText;
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
import magic.utility.MagicFileSystem;

@SuppressWarnings("serial")
public class DeckEditorScreen extends HeaderFooterScreen
    implements IDeckConsumer, IDeckEditorListener {

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

    private ContentPanel contentPanel;
    private final boolean isStandalone;
    private final DeckStatusPanel deckStatusPanel = new DeckStatusPanel();
    private final MagicDeck startDeck;

    // CTR : opens Deck Editor ready to update passed in deck.
    public DeckEditorScreen(final MagicDeck deck) {
        super(MText.get(_S14));
        isStandalone = false;
        this.startDeck = deck;
        useLoadingScreen(this::initUI);
    }

    // CTR : open Deck Editor in standalone mode starting with an empty deck.
    public DeckEditorScreen() {
        super(MText.get(_S14));
        isStandalone = true;
        this.startDeck = getMostRecentEditedDeck();
        useLoadingScreen(this::initUI);
    }

    @Override
    protected boolean isCardDataRequired() {
        return true;
    }

    private void initUI() {
        contentPanel = new ContentPanel(startDeck, this);
        contentPanel.setIsStandalone(isStandalone);
        setDeck(startDeck);
        setMainContent(contentPanel);
        setHeaderContent(deckStatusPanel);
        setLeftFooter(getLeftAction());
        setRightFooter(getRightAction());
        setFooterButtons();
        setWikiPage(WikiPage.DECK_EDITOR);
    }

    private void showSampleHand() {
        if (contentPanel.getDeck().size() >= 7) {
            ScreenController.showSampleHandScreen(contentPanel.getDeck());
        } else {
            showInvalidActionMessage(MText.get(_S10));
        }
    }

    private void showTiledImagesView() {
        if (contentPanel.getDeck().size() > 0) {
            ScreenController.showDeckTiledCardsScreen(contentPanel.getDeck());
        } else {
            showInvalidActionMessage(MText.get(_S13));
        }
    }

    private void setFooterButtons() {
        addToFooter(MenuButton.build(this::loadDeck,
                        MagicIcon.OPEN, MText.get(_S4), MText.get(_S5)
                ),
                MenuButton.build(this::saveDeck,
                        MagicIcon.SAVE, MText.get(_S6), MText.get(_S7)
                ),
                MenuButton.build(this::showSampleHand,
                        MagicIcon.HAND_ICON, MText.get(_S8), MText.get(_S9)
                ),
                MenuButton.build(this::showTiledImagesView,
                        MagicIcon.TILED, MText.get(_S11), MText.get(_S12)
                )
        );
    }

    private static MagicDeck getMostRecentEditedDeck() {
        final Path deckFilePath = GeneralConfig.getInstance().getMostRecentDeckFilePath();
        if (deckFilePath != null) {
            final MagicDeck deck = loadDeck(deckFilePath);
            if (deck != null && deck.isValid()) {
                return deck;
            }
        }
        return new MagicDeck();
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

    public MenuButton getLeftAction() {
        final String caption = (!isStandalone ? MText.get(_S1) : MText.get(_S2));
        return MenuButton.getCloseScreenButton(caption);
    }

    public MenuButton getRightAction() {
        if (!isStandalone) {
            return new MenuButton(MText.get(_S3), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (contentPanel.validateDeck(true) && contentPanel.applyDeckUpdates()) {
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
        setDeck(new MagicDeck());
    }

    public void loadDeck() {
        ScreenController.showDecksScreen(this);
    }

    private boolean isReservedDeckFolder(final Path saveFolder) {
        return MagicFileSystem.isSamePath(saveFolder, DeckUtils.getPrebuiltDecksFolder())
            || MagicFileSystem.isSamePath(saveFolder, DeckUtils.getFiremindDecksFolder());
    }

    public void saveDeck() {

        if (contentPanel.getDeck().size() == 0) {
            showInvalidActionMessage(MText.get(_S15));
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
                    ScreenController.showWarningMessage(MText.get(_S16));
                } else if (Files.exists(getSelectedFile().toPath())) {
                    int response = JOptionPane.showConfirmDialog(ScreenController.getFrame(),
                            MText.get(_S17),
                            MText.get(_S18),
                            JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        super.approveSelection();
                    }
                } else {
                    super.approveSelection();
                }
            }
        };
        final MagicDeck deck = contentPanel.getDeck();
        fileChooser.setDialogTitle(MText.get(_S19));
        fileChooser.setFileFilter(MagicFileChoosers.DECK_FILEFILTER);
        fileChooser.setAcceptAllFileFilterUsed(false);
        if (deck != null) {
            fileChooser.setSelectedFile(new File(deck.getFilename()));
        }
        final int action = fileChooser.showSaveDialog(ScreenController.getFrame());
        if (action == JFileChooser.APPROVE_OPTION) {
            final String filename = fileChooser.getSelectedFile().getAbsolutePath();
            if (DeckUtils.saveDeck(filename, contentPanel.getDeck())) {
                final String shortFilename = fileChooser.getSelectedFile().getName();
                contentPanel.getDeck().setFilename(shortFilename);
                setDeck(contentPanel.getDeck());
                setMostRecentDeck(filename);
            } else {
                ScreenController.showWarningMessage(MText.get(_S20));
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
    public boolean isScreenReadyToClose(final Object nextScreen) {
        if (super.isScreenReadyToClose(nextScreen)) {
            if (contentPanel == null) {
                return true;
            } else if (!contentPanel.isStandaloneDeckEditor() && nextScreen instanceof DuelDecksScreen) {
                ((DuelDecksScreen)nextScreen).updateDecksAfterEdit();
            }
            MagicSetDefinitions.clearLoadedSets();
            MagicLogs.clearLoadedLogs();
            return true;
        }
        return false;
    }

    @Override
    public void setDeck(final MagicDeck deck) {
        contentPanel.setDeck(deck);
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

}
