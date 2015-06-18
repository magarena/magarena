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
import magic.ui.IconImages;
import magic.data.MagicSetDefinitions;
import magic.exception.InvalidDeckException;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.MagicFileChoosers;
import magic.ui.MagicFrame;
import magic.ui.ScreenController;
import magic.ui.ScreenOptionsOverlay;
import magic.ui.dialog.DownloadImagesDialog;
import magic.ui.deck.editor.DeckEditorScreenPanel;
import magic.ui.deck.editor.IDeckEditorListener;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.interfaces.IWikiPage;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.widget.deck.DeckStatusPanel;

@SuppressWarnings("serial")
public class DeckEditorScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar, IOptionsMenu, IWikiPage, IDeckConsumer, IDeckEditorListener {

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
        setDeck(deck);
        setContent(screenContent);
    }

    private static MagicDeck getMostRecentEditedDeck() {
        final Path deckFilePath = GeneralConfig.getInstance().getMostRecentDeckFilePath();
        if (deckFilePath != null) {
             return loadDeck(deckFilePath);
        } else {
            return null;
        }
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
        final String caption = (!isStandalone ? "Cancel" : "Close");
        return MenuButton.getCloseScreenButton(caption);
    }

    @Override
    public MenuButton getRightAction() {
        if (!isStandalone) {
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

    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<>();
        buttons.add(
                new ActionBarButton(
                        IconImages.getIcon(MagicIcon.OPEN_ICON),
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
                        IconImages.getIcon(MagicIcon.SAVE_ICON),
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
                        IconImages.getIcon(MagicIcon.HAND_ICON),
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
        buttons.add(
                new ActionBarButton(
                        IconImages.getIcon(MagicIcon.TILED_ICON),
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

    @Override
    public String getScreenCaption() {
        return "Deck Editor";
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
                setDeck(screenContent.getDeck());
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

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        if (!screenContent.isStandaloneDeckEditor() && nextScreen instanceof DuelDecksScreen) {
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

    }

    @Override
    public JPanel getStatusPanel() {
        return deckStatusPanel;
    }

}
