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
import magic.model.MagicDeckConstructionRule;
import magic.model.MagicPlayerDefinition;
import magic.ui.viewer.DeckDescriptionPreview;
import magic.ui.widget.MenuButton;

@SuppressWarnings("serial")
public class DeckEditorScreen
    extends MagScreen
    implements IMagStatusBar, IMagActionBar, IMagScreenOptionsMenu {

    private static ExplorerPanel content;
    private final MagicFrame frame;

    // CTR
    public DeckEditorScreen(final MagicFrame frame0, final int mode) {
        super(getScreenContent(frame0, mode), frame0);
        this.frame = frame0;
    }
    public DeckEditorScreen(final MagicFrame frame0, final int mode, final MagicPlayerDefinition player) {
        super(getScreenContent(frame0, mode, player), frame0);
        this.frame = frame0;
    }

    private static JPanel getScreenContent(MagicFrame frame, final int mode, final MagicPlayerDefinition player) {
        content = new ExplorerPanel(frame, mode, player);
        return content;
    }
    private static JPanel getScreenContent(MagicFrame frame, final int mode) {
        content = new ExplorerPanel(frame, mode, true);
        return content;
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getLeftAction()
     */
    @Override
    public MenuButton getLeftAction() {
        return new MenuButton("Close", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.closeActiveScreen(false);
            }
        });
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getRightAction()
     */
    @Override
    public MenuButton getRightAction() {
        return null;
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getMiddleActions()
     */
    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<MenuButton>();
        buttons.add(new MenuButton("Load deck", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDeck();
            }
        }, "Load deck from file"));
        if (content.getPlayer() == null) {
            buttons.add(new MenuButton("Save", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveDeck();
                }
            }, "Save deck to file"));
            buttons.add(new MenuButton("Clear", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
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
        if (content.getPlayer() != null) {
            return "Deck Editor : " + content.getPlayer().getName();
        } else {
            return "Deck Editor";
        }
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagScreenOptionsMenu#showOptionsMenuOverlay()
     */
    @Override
    public void showOptionsMenuOverlay() {
       new DeckEditorScreenOptions(frame, this);
    }

    public void createNewEmptyDeck() {
        final MagicPlayerDefinition player = content.getPlayer();
        if (player != null) {
            player.getDeck().clear();
            content.setDeck(player.getDeck());
        } else {
            content.setDeck(new MagicDeck());
        }
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
            final MagicPlayerDefinition player = content.getPlayer();
            if (player != null) {
              DeckUtils.loadDeck(filename, player);
              content.setDeck(player.getDeck());
            } else {
              content.setDeck(DeckUtils.loadDeckFromFile(filename));
            }
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
        if (content.getPlayer() != null) {
            return validateDeck(content.getPlayer().getDeck(), nextScreen);
        } else {
            return true;
        }
    }

    //
    // FIXME: Should be able to discard changes if rules are broken and close screen anyway.
    //        Currently cannot close screen if rules are broken. The problem is that any
    //        changes made in the Deck Editor are reflected in the player duel deck screen.
    //
    private boolean validateDeck(final MagicDeck deck, final MagScreen nextScreen) {
        final String brokenRules = getBrokenRules(deck);
        if (brokenRules.length() > 0) {
            notifyUser(brokenRules);
            return false;
        } else if (nextScreen instanceof DuelDecksScreen) {
            ((DuelDecksScreen)nextScreen).updateDecksAfterEdit();
        }
        return true;
    }

    private String getBrokenRules(final MagicDeck deck) {
        return MagicDeckConstructionRule.getRulesText(MagicDeckConstructionRule.checkDeck(deck));
    }

    private void notifyUser(final String brokenRules) {
        JOptionPane.showMessageDialog(
                this,
                "This deck is illegal.\n\n" + brokenRules,
                "Illegal Deck",
                JOptionPane.WARNING_MESSAGE);
    }

}
