package magic.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import magic.MagicMain;
import magic.data.DeckUtils;
import magic.model.MagicDeck;
import magic.ui.viewer.DeckDescriptionPreview;
import magic.ui.widget.MenuButton;

@SuppressWarnings("serial")
public class DeckEditorScreen
    extends MagScreen
    implements IMagStatusBar, IMagActionBar, IMagScreenOptionsMenu {

    private static ExplorerPanel content;
    private final MagicFrame frame;

    // CTR : opens Deck Editor ready to update passed in deck.
    public DeckEditorScreen(final MagicFrame frame0, final MagicDeck deck) {
        super(getScreenContent(deck), frame0);
        this.frame = frame0;
    }
    // CTR : open Deck Editor in standalone mode starting with an empty deck.
    public DeckEditorScreen(final MagicFrame frame0) {
        super(getScreenContent(), frame0);
        this.frame = frame0;
    }

    private static JPanel getScreenContent(final MagicDeck deck) {
        content = new ExplorerPanel(deck);
        return content;
    }
    private static JPanel getScreenContent() {
        return getScreenContent(null);
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getLeftAction()
     */
    @Override
    public MenuButton getLeftAction() {
        if (!content.isStandaloneDeckEditor()) {
            return new MenuButton("Cancel", new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    frame.closeActiveScreen(false);
                }
            });
        } else {
            return new MenuButton("Close", new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    frame.closeActiveScreen(false);
                }
            });
        }
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getRightAction()
     */
    @Override
    public MenuButton getRightAction() {
        if (!content.isStandaloneDeckEditor()) {
            return new MenuButton("Use this deck", new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (content.validateDeck(true) && content.applyDeckUpdates()) {
                        frame.closeActiveScreen(false);
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
        final List<MenuButton> buttons = new ArrayList<MenuButton>();
        buttons.add(new MenuButton("Load deck", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                loadDeck();
            }
        }, "Load deck from file"));
        if (content.isStandaloneDeckEditor()) {
            buttons.add(new MenuButton("Save", new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    saveDeck();
                }
            }, "Save deck to file"));
            buttons.add(new MenuButton("Clear", new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (JOptionPane.showConfirmDialog(
                            MagicMain.rootFrame,
                            "Remove all cards from the deck?") == JOptionPane.YES_OPTION) {
                        createNewEmptyDeck();
                    }
                }
            }, "Clear all cards from deck"));
        }
        return buttons;
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
       new DeckEditorScreenOptions(frame, this);
    }

    public void createNewEmptyDeck() {
        content.setDeck(new MagicDeck());
    }

    public void loadDeck() {
        final JFileChooser fileChooser=new JFileChooser(DeckUtils.getDeckFolder());
        fileChooser.setDialogTitle("Load deck");
        fileChooser.setFileFilter(DeckUtils.DECK_FILEFILTER);
        fileChooser.setAcceptAllFileFilterUsed(false);
        // Add the description preview pane
        fileChooser.setAccessory(new DeckDescriptionPreview(fileChooser));
        final int action=fileChooser.showOpenDialog(this);
        if (action==JFileChooser.APPROVE_OPTION) {
            final String filename=fileChooser.getSelectedFile().getAbsolutePath();
            content.setDeck(DeckUtils.loadDeckFromFile(filename));
        }
    }

    public void saveDeck() {
        final JFileChooser fileChooser = new JFileChooser(DeckUtils.getDeckFolder());
        fileChooser.setDialogTitle("Save deck");
        fileChooser.setFileFilter(DeckUtils.DECK_FILEFILTER);
        fileChooser.setAcceptAllFileFilterUsed(false);
        final int action = fileChooser.showSaveDialog(this);
        if (action == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filename.endsWith(DeckUtils.DECK_EXTENSION)) {
                filename += DeckUtils.DECK_EXTENSION;
            }
            if (DeckUtils.saveDeck(filename, content.getDeck())) {
                String shortFilename = fileChooser.getSelectedFile().getName();
                if (shortFilename.indexOf(".dec") == -1) {
                    shortFilename += ".dec";
                }
                content.getDeck().setName(shortFilename);
                content.setDeck(content.getDeck());
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see magic.ui.MagScreen#canScreenClose()
     */
    @Override
    public boolean isScreenReadyToClose(final MagScreen nextScreen) {
        if (content.isDeckEditor() && !content.isStandaloneDeckEditor() && nextScreen instanceof DuelDecksScreen) {
            ((DuelDecksScreen)nextScreen).updateDecksAfterEdit();
        }
        return true;
    }

}
