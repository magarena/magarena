package magic.ui.screen;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import magic.data.DuelConfig;
import magic.data.MagicIcon;
import magic.exception.InvalidDeckException;
import magic.model.DuelPlayerConfig;
import magic.model.MagicDeck;
import magic.model.MagicDeckConstructionRule;
import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.ui.DuelDecksPanel;
import magic.ui.MagicImages;
import magic.ui.MagicFrame;
import magic.ui.ScreenController;
import magic.ui.ScreenOptionsOverlay;
import magic.translate.UiString;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.interfaces.IWikiPage;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.DuelSettingsPanel;
import magic.ui.screen.widget.MenuButton;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.screen.widget.SampleHandActionButton;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
public class DuelDecksScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar, IOptionsMenu, IWikiPage {

    // translatable strings
    private static final String _S1 = "Duel Decks";
    private static final String _S2 = "Main Menu";
    private static final String _S3 = "Restart duel";
    private static final String _S4 = "Game %d";
    private static final String _S5 = "Deck Editor";
    private static final String _S6 = "Open the Deck Editor.";
    private static final String _S7 = "Swap Decks";
    private static final String _S8 = "Swap your deck with your opponent's.";
    private static final String _S9 = "%s wins the duel";
    private static final String _S10 = "Restart Duel";
    private static final String _S11 = "Same players, same decks, same duel settings. Same result...?";
    private static final String _S12 = "Deck View";
    private static final String _S13 = "Shows complete deck using tiled card images.";
    private static final String _S14 = "%s's deck is illegal.\n\n%s";

    private final DuelDecksPanel screenContent;

    public DuelDecksScreen(final MagicDuel duel) {
        this.screenContent = new DuelDecksPanel(duel);
        setContent(this.screenContent);
        if (duel.getGamesPlayed() > 0 && MagicSystem.isAiVersusAi() == false) {
            saveDuel();
        }
    }

    @Override
    public String getScreenCaption() {
        return UiString.get(_S1);
    }

    @Override
    public MenuButton getLeftAction() {
        if (screenContent.getDuel().getGamesPlayed() == 0) {
            return MenuButton.getCloseScreenButton(UiString.get(_S2));
        } else {
            return new MenuButton(UiString.get(_S2), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    ScreenController.showMainMenuScreen();
                }
            });
        }
    }

    @Override
    public void showOptionsMenuOverlay() {
        new ScreenOptions(getFrame());
    }

    private void startNextGame() {
        final DuelPlayerConfig[] players = screenContent.getDuel().getPlayers();
        if (isLegalDeckAndShowErrors(players[0]) && isLegalDeckAndShowErrors(players[1])) {
            saveDuel();
            ScreenController.showDuelGameScreen(screenContent.getDuel());
        }
    }

    @Override
    public MenuButton getRightAction() {
        if (!screenContent.getDuel().isFinished()) {
            return new MenuButton(getStartDuelCaption(), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    startNextGame();
                }
            });
        } else {
            return new MenuButton(UiString.get(_S3), new AbstractAction() {
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
        return UiString.get(_S4, screenContent.getDuel().getGamesPlayed() + 1);
    }

    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<>();
        if (screenContent.getDuel().getGamesPlayed() == 0) {
            buttons.add(new ActionBarButton(
                            MagicImages.getIcon(MagicIcon.DECK_ICON),
                            UiString.get(_S5), UiString.get(_S6),
                            new AbstractAction() {
                                @Override
                                public void actionPerformed(final ActionEvent e) {
                                    ScreenController.showDeckEditor(getActiveDeck());
                                }
                            })
                    );
            buttons.add(new ActionBarButton(
                            MagicImages.getIcon(MagicIcon.SWAP_ICON),
                            UiString.get(_S7), UiString.get(_S8),
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
            buttons.add(SampleHandActionButton.createInstance(getActiveDeck()));
        } else {
            if (screenContent.getDuel().isFinished()) {
                final MagicDuel duel = screenContent.getDuel();
                buttons.add(new MenuButton(UiString.get(_S9, duel.getWinningPlayerProfile().getPlayerName()), null));
            } else {
                buttons.add(new ActionBarButton(
                                MagicImages.getIcon(MagicIcon.REFRESH_ICON),
                                UiString.get(_S10), UiString.get(_S11),
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
            buttons.add(new ActionBarButton(
                            MagicImages.getIcon(MagicIcon.TILED_ICON),
                            UiString.get(_S12), UiString.get(_S13),
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

    public void swapDecks() {
        screenContent.getDuel().restart();
        final DuelPlayerConfig[] players = screenContent.getDuel().getPlayers();
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

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

    public int getGamesPlayed() {
        return screenContent.getDuel().getGamesPlayed();
    }

    private void saveDuel() {
        screenContent.getDuel().save(MagicDuel.getLatestDuelFile());
    }

    @Override
    public String getWikiPageName() {
        return "UIDeckView";
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

    private boolean isLegalDeckAndShowErrors(DuelPlayerConfig aPlayer) {
        final String brokenRulesText =
                MagicDeckConstructionRule.getRulesText(MagicDeckConstructionRule.checkDeck(aPlayer.getDeck()));

        if (brokenRulesText.length() > 0) {
            ScreenController.showWarningMessage(UiString.get(_S14, aPlayer.getName(), brokenRulesText));
            return false;
        }

        return true;
    }

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
