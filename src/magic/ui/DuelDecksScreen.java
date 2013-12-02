package magic.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import magic.data.CubeDefinitions;
import magic.model.MagicCubeDefinition;
import magic.model.MagicDeck;
import magic.model.MagicDuel;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.ui.widget.MenuButton;

@SuppressWarnings("serial")
public class DuelDecksScreen
    extends MagScreen
    implements IMagStatusBar, IMagActionBar, IMagScreenOptionsMenu {

    private final MagicFrame frame;
    private static DuelPanel screenContent;

    public DuelDecksScreen(final MagicFrame frame0, final MagicDuel duel0) {
        super(getScreenContent(frame0, duel0), frame0);
        this.frame = frame0;
    }

    private static JPanel getScreenContent(final MagicFrame frame, final MagicDuel duel) {
        screenContent = new DuelPanel(frame, duel);
        return screenContent;
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagStatusBar#getScreenCaption()
     */
    @Override
    public String getScreenCaption() {
        return "Deck Settings";
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getLeftAction()
     */
    @Override
    public MenuButton getLeftAction() {
        if (screenContent.getDuel().getGamesPlayed() == 0) {
            return new MenuButton("< Main menu", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.closeActiveScreen(false);
                }
            });
        } else {
            return new MenuButton("Restart duel", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.restartDuel();
                }
            });
        }
    }

    /* (non-Javadoc)
     * @see magic.ui.widget.IMagScreenOptionsProvider#showScreenOptionsOverlay()
     */
    @Override
    public void showOptionsMenuOverlay() {
        new DuelDecksScreenOptions(frame, this);
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getRightAction()
     */
    @Override
    public MenuButton getRightAction() {
        if (!screenContent.getDuel().isFinished()) {
            return new MenuButton(getStartDuelCaption() + " >", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.nextGame();
                }
            });
        } else {
            return new MenuButton("New Duel", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.showNewDuelDialog();
                }
            });
        }
    }

    private String getStartDuelCaption() {
        if (screenContent.getDuel().getGamesPlayed() == 0) {
            return "Start duel";
        } else {
            return "Game " + (screenContent.getDuel().getGamesPlayed() + 1);
        }
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getMiddleActions()
     */
    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<MenuButton>();
        if (screenContent.getDuel().getGamesPlayed() == 0) {
            buttons.add(new MenuButton("Deck Editor", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    final MagicCubeDefinition cubeDefinition = CubeDefinitions.getCubeDefinition(screenContent.getDuel().getConfiguration().getCube());
                    frame.showDeckEditor(screenContent.getSelectedPlayer(), cubeDefinition);
                }
            }));
            buttons.add(new MenuButton("Swap Decks", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    swapDecks();
                }
            }));
        }
        return buttons;
    }

    public void updateDecksAfterEdit() {
        screenContent.updateDecksAfterEdit();
    }

    /**
     *
     */
    public void swapDecks() {
        screenContent.getDuel().restart();
        final MagicPlayerDefinition[] players = screenContent.getDuel().getPlayers();
        final MagicPlayerProfile profile1 = players[0].getProfile();
        final MagicPlayerProfile profile2 = players[1].getProfile();
        final MagicDeck deck1 = new MagicDeck(players[0].getDeck());
        final MagicDeck deck2 = new MagicDeck(players[1].getDeck());
        players[0].setProfile(profile2);
        players[0].setDeck(deck2);
        players[1].setProfile(profile1);
        players[1].setDeck(deck1);
        frame.closeActiveScreen(false);
        frame.showDuel();
    }

    /* (non-Javadoc)
     * @see magic.ui.MagScreen#isScreenReadyToClose(magic.ui.MagScreen)
     */
    @Override
    public boolean isScreenReadyToClose(MagScreen nextScreen) {
        screenContent.haltStrengthViewer();
        return true;
    }

    public int getGamesPlayed() {
        return screenContent.getDuel().getGamesPlayed();
    }

    public void saveDuel() {
        screenContent.getDuel().save(MagicDuel.getDuelFile());
        JOptionPane.showMessageDialog(this, "Duel saved. Use Load Duel option in Main Menu to restore.", "Save Duel", JOptionPane.INFORMATION_MESSAGE);
    }

}
