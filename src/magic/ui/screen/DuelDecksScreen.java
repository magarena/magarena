package magic.ui.screen;

import magic.data.DuelConfig;
import magic.ui.IconImages;
import magic.model.MagicDeck;
import magic.model.MagicDeckConstructionRule;
import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicPlayerDefinition;
import magic.ui.DuelDecksPanel;
import magic.ui.MagicFrame;
import magic.ui.ScreenOptionsOverlay;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.DuelSettingsPanel;
import magic.ui.screen.widget.MenuButton;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.screen.widget.SampleHandActionButton;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import magic.data.MagicIcon;
import magic.exception.InvalidDeckException;
import magic.ui.ScreenController;
import magic.ui.screen.interfaces.IWikiPage;

@SuppressWarnings("serial")
public class DuelDecksScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar, IOptionsMenu, IWikiPage {

    private final DuelDecksPanel screenContent;

    public DuelDecksScreen(final MagicDuel duel) {
        this.screenContent = new DuelDecksPanel(duel);
        setContent(this.screenContent);
        if (duel.getGamesPlayed() > 0) {
            saveDuel(false);
        }
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagStatusBar#getScreenCaption()
     */
    @Override
    public String getScreenCaption() {
        return "Duel Decks";
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getLeftAction()
     */
    @Override
    public MenuButton getLeftAction() {
        if (screenContent.getDuel().getGamesPlayed() == 0) {
            return MenuButton.getCloseScreenButton("Main menu");
        } else {
            return new MenuButton("Main Menu", new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    ScreenController.showMainMenuScreen();
                }
            });
        }
    }

    /* (non-Javadoc)
     * @see magic.ui.widget.IMagScreenOptionsProvider#showScreenOptionsOverlay()
     */
    @Override
    public void showOptionsMenuOverlay() {
        new ScreenOptions(getFrame());
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getRightAction()
     */
    @Override
    public MenuButton getRightAction() {
        if (!screenContent.getDuel().isFinished()) {
            return new MenuButton(getStartDuelCaption(), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    final MagicPlayerDefinition[] players = screenContent.getDuel().getPlayers();

                    if (isLegalDeckAndShowErrors(players[0].getDeck(), players[0].getName()) &&
                       isLegalDeckAndShowErrors(players[1].getDeck(), players[1].getName())) {
                        saveDuel(false);
                        getFrame().nextGame();
                    }
                }
            });
        } else {
            return new MenuButton("Restart duel", new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    try {
                        getFrame().restartDuel();
                    } catch (InvalidDeckException ex) {
                        ScreenController.showWarningMessage(ex.getMessage());
                    }
                }
            });
        }
    }

    private String getStartDuelCaption() {
        return "Game " + (screenContent.getDuel().getGamesPlayed() + 1);
    }

    /* (non-Javadoc)
     * @see magic.ui.IMagActionBar#getMiddleActions()
     */
    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<MenuButton>();
        if (screenContent.getDuel().getGamesPlayed() == 0) {
            buttons.add(
                    new ActionBarButton(
                            IconImages.getIcon(MagicIcon.DECK_ICON),
                            "Deck Editor", "Open the Deck Editor.",
                            new AbstractAction() {
                                @Override
                                public void actionPerformed(final ActionEvent e) {
                                    ScreenController.showDeckEditor(getActiveDeck());
                                }
                            })
                    );
            buttons.add(
                    new ActionBarButton(
                            IconImages.getIcon(MagicIcon.SWAP_ICON),
                            "Swap Decks", "Swap your deck with your opponent's.",
                            new AbstractAction() {
                                @Override
                                public void actionPerformed(final ActionEvent e) {
                                    try {
                                        swapDecks();
                                    } catch (InvalidDeckException ex) {
                                        ScreenController.showWarningMessage(ex.getMessage());
                                    }
                                }
                            })
                    );
            buttons.add(SampleHandActionButton.createInstance(getActiveDeck(), getFrame()));
        } else {
            if (screenContent.getDuel().isFinished()) {
                final MagicDuel duel = screenContent.getDuel();
                buttons.add(new MenuButton(duel.getWinningPlayerProfile().getPlayerName() + " wins the duel", null));
            } else {
                buttons.add(
                        new ActionBarButton(
                                IconImages.getIcon(MagicIcon.REFRESH_ICON),
                                "Restart Duel", "Same players, same decks, same duel settings. Same result...?",
                                new AbstractAction() {
                                    @Override
                                    public void actionPerformed(final ActionEvent e) {
                                        try {
                                            getFrame().restartDuel();
                                        } catch (InvalidDeckException ex) {
                                            ScreenController.showWarningMessage(ex.getMessage());
                                        }
                                    }
                                })
                        );
            }
        }

        if (!screenContent.getDuel().isFinished()) {
            buttons.add(
                    new ActionBarButton(
                            IconImages.getIcon(MagicIcon.TILED_ICON),
                            "Deck View", "Shows complete deck using tiled card images.",
                            new AbstractAction() {
                                @Override
                                public void actionPerformed(final ActionEvent e) {
                                    ScreenController.showDeckView(getActiveDeck());
                                }
                            })
                    );
        }

        return buttons;
    }

    public void updateDecksAfterEdit() {
        screenContent.updateDecksAfterEdit();
    }

    /**
     *
     */
    public void swapDecks() throws InvalidDeckException {
        screenContent.getDuel().restart();
        final MagicPlayerDefinition[] players = screenContent.getDuel().getPlayers();
        final MagicDeckProfile deckProfile1 = players[0].getDeckProfile();
        final MagicDeckProfile deckProfile2 = players[1].getDeckProfile();
        final MagicDeck deck1 = new MagicDeck(players[0].getDeck());
        final MagicDeck deck2 = new MagicDeck(players[1].getDeck());
        players[0].setDeckProfile(deckProfile2);
        players[0].setDeck(deck2);
        players[1].setDeckProfile(deckProfile1);
        players[1].setDeck(deck1);
        ScreenController.closeActiveScreen(false);
        getFrame().showDuel();
    }

    /* (non-Javadoc)
     * @see magic.ui.MagScreen#isScreenReadyToClose(magic.ui.MagScreen)
     */
    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        screenContent.haltStrengthViewer();
        return true;
    }

    public int getGamesPlayed() {
        return screenContent.getDuel().getGamesPlayed();
    }

    public void saveDuel(final boolean confirmSave) {
        screenContent.getDuel().save(MagicDuel.getDuelFile());
        if (confirmSave) {
            ScreenController.showInfoMessage("<html><b>Duel saved.</b><br><br>Please use Resume Duel option in Main Menu to restore.");
        }
    }

    @Override
    public String getWikiPageName() {
        return "UIDeckView";
    }

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

    private boolean isLegalDeckAndShowErrors(final MagicDeck deck, final String playerName) {
        final String brokenRulesText =
                MagicDeckConstructionRule.getRulesText(MagicDeckConstructionRule.checkDeck(deck));

        if (brokenRulesText.length() > 0) {
            ScreenController.showWarningMessage(playerName + "'s deck is illegal.\n\n" + brokenRulesText);
            return false;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see magic.ui.interfaces.IStatusBar#getStatusPanel()
     */
    @Override
    public JPanel getStatusPanel() {
        final DuelConfig config =  screenContent.getDuel().getConfiguration();
        final DuelSettingsPanel panel = new DuelSettingsPanel(getFrame(), config);
        panel.setEnabled(false);
        return panel;
    }

    private MagicDeck getActiveDeck() {
        return screenContent.getSelectedPlayer().getDeck();
    }

}
