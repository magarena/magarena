package magic.ui.screen;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import magic.data.DuelConfig;
import magic.data.MagicIcon;
import magic.exception.InvalidDeckException;
import magic.model.DuelPlayerConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.model.MagicDeckConstructionRule;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.ui.DuelDecksPanel;
import magic.ui.MagicImages;
import magic.ui.MagicFrame;
import magic.ui.ScreenController;
import magic.ui.ScreenOptionsOverlay;
import magic.translate.UiString;
import magic.ui.cardBuilder.renderers.CardBuilder;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.interfaces.IWikiPage;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.DuelSettingsPanel;
import magic.ui.screen.widget.MenuButton;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.screen.widget.SampleHandActionButton;
import magic.ui.widget.StartGameButton;
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
    private static final String _S9 = "%s wins the duel";
    private static final String _S10 = "Restart Duel";
    private static final String _S11 = "Same players, same decks, same duel settings. Same result...?";
    private static final String _S12 = "Deck View";
    private static final String _S13 = "Shows complete deck using tiled card images.";
    private static final String _S14 = "%s's deck is illegal.\n\n%s";

    private final DuelDecksPanel screenContent;
    private MagicGame nextGame = null;
    private final StartGameButton nextGameButton;
    private StartupWorker worker;

    public DuelDecksScreen(final MagicDuel duel) {

        screenContent = new DuelDecksPanel(duel);
        nextGameButton = new StartGameButton(getStartDuelCaption(), getPlayGameAction());

        if (duel.getGamesPlayed() > 0 && MagicSystem.isAiVersusAi() == false) {
            saveDuel();
        }

        MagicImages.clearCache();

        setContent(screenContent);

        if (MagicSystem.isAiVersusAi() == false) {
            doGameSetupInBackground(duel);
            screenContent.addPropertyChangeListener(
                DuelDecksPanel.CP_DECK_CHANGED,
                (e) -> { doGameSetupInBackground(duel); }
            );
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
            ScreenController.showDuelGameScreen(nextGame);
        }
    }

    private AbstractAction getPlayGameAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                startNextGame();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        };
    }

    @Override
    public MenuButton getRightAction() {
        if (!screenContent.getDuel().isFinished()) {
            return nextGameButton;
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

    private boolean isNewDuel() {
        return screenContent.getDuel().getGamesPlayed() == 0;
    }

    private boolean isDuelFinished() {
        return screenContent.getDuel().isFinished();
    }

    private ActionBarButton getDeckEditorButton() {
        return new ActionBarButton(
            MagicImages.getIcon(MagicIcon.DECK_ICON),
            UiString.get(_S5), UiString.get(_S6),
            new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    ScreenController.showDeckEditor(getActiveDeck());
                }
            }
        );
    }

    private ActionBarButton getRestartButton() {
        return new ActionBarButton(
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
            }
        );
    }

    private MenuButton getWinnerButton() {
        String winner = screenContent.getDuel().getWinningPlayerProfile().getPlayerName();
        return new MenuButton(UiString.get(_S9, winner), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // do nothing
            }
        });
    }

    private ActionBarButton getTiledDeckButton() {
        return new ActionBarButton(
            MagicImages.getIcon(MagicIcon.TILED_ICON),
            UiString.get(_S12), UiString.get(_S13),
            new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    ScreenController.showDeckView(getActiveDeck());
                }
            }
        );
    }

    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<>();
        if (isNewDuel()) {
            buttons.add(getDeckEditorButton());
            buttons.add(getTiledDeckButton());
            buttons.add(SampleHandActionButton.createInstance(getActiveDeck()));
            buttons.addAll(screenContent.getActionBarButtons());
        } else if (isDuelFinished()) {
            buttons.add(getWinnerButton());
        } else { // duel in progress
            buttons.add(getTiledDeckButton());
            buttons.add(SampleHandActionButton.createInstance(getActiveDeck()));
            buttons.add(getRestartButton());
        }
        return buttons;
    }

    public void updateDecksAfterEdit() {
        screenContent.updateDecksAfterEdit();
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
        final DuelConfig config = screenContent.getDuel().getConfiguration();
        final DuelSettingsPanel panel = new DuelSettingsPanel(getFrame(), config);
        panel.setEnabled(false);
        return panel;
    }

    private MagicDeck getActiveDeck() {
        return screenContent.getSelectedPlayer().getDeck();
    }

    private void doGameSetupInBackground(final MagicDuel duel) {
        nextGameButton.setBusy(true);
        if (worker != null && worker.isDone() == false) {
            worker.cancel(true);
        }
        worker = new StartupWorker(duel);
        worker.execute();
    }

    private final class StartupWorker extends SwingWorker<MagicGame, Void> {

        private final MagicDuel duel;

        public StartupWorker(final MagicDuel aDuel) {
            this.duel = aDuel;
        }

        private Optional<MagicCardDefinition> findFirstProxyCard(MagicDeck aDeck) {
            return aDeck.stream()
                .filter(card -> MagicImages.isProxyImage(card.getCardDefinition()))
                .findFirst();
        }

        private Optional<MagicCardDefinition> findFirstProxyCardInDecks() {
            Optional<MagicCardDefinition> proxy = findFirstProxyCard(duel.getPlayer(0).getDeck());
            return proxy.isPresent() ? proxy : findFirstProxyCard(duel.getPlayer(1).getDeck());
        }

        private void loadCardBuilderIfRequired() {
            if (!CardBuilder.IS_LOADED) {
                Optional<MagicCardDefinition> proxy = findFirstProxyCardInDecks();
                if (proxy.isPresent()) {
                    CardBuilder.getCardBuilderImage(proxy.get());
                }
            }
        }

        @Override
        protected MagicGame doInBackground() throws Exception {
            loadCardBuilderIfRequired();
            return duel.nextGame();
        }

        @Override
        protected void done() {
            nextGame = getNextGame();
            nextGameButton.setBusy(nextGame == null);
        }

        private MagicGame getNextGame() {
            try {
                return get();
            } catch (ExecutionException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                System.err.println(ex);
                return null;
            } catch (CancellationException ex) {
                System.err.println("Worker cancelled : " + MagicSystem.getHeapUtilizationStats().replace("\n", ", "));
                return null;
            }
        }

    }

}
