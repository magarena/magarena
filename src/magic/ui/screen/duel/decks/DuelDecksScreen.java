package magic.ui.screen.duel.decks;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import magic.data.DuelConfig;
import magic.data.MagicIcon;
import magic.exception.InvalidDeckException;
import magic.model.DuelPlayerConfig;
import magic.model.MagicDeck;
import magic.model.MagicDeckConstructionRule;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.ui.DuelDecksPanel;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.translate.UiString;
import magic.ui.screen.widget.DuelSettingsPanel;
import magic.ui.screen.widget.MenuButton;
import magic.ui.screen.widget.SampleHandActionButton;
import magic.utility.MagicSystem;
import magic.ui.WikiPage;
import magic.ui.screen.HeaderFooterScreen;

@SuppressWarnings("serial")
public class DuelDecksScreen extends HeaderFooterScreen { // IOptionsMenu

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
    private NewGameWorker worker;

    public DuelDecksScreen(final MagicDuel duel) {
        super(UiString.get(_S1));

        screenContent = new DuelDecksPanel(duel);
        nextGameButton = new StartGameButton(getStartDuelCaption(), getPlayGameAction());

        if (duel.getGamesPlayed() > 0 && MagicSystem.isAiVersusAi() == false) {
            saveDuel();
        }

        MagicImages.clearCache();

        setMainContent(screenContent);

        if (MagicSystem.isAiVersusAi() == false) {
            doGameSetupInBackground(duel);
            screenContent.addPropertyChangeListener(
                DuelDecksPanel.CP_DECK_CHANGED,
                (e) -> { doGameSetupInBackground(duel); }
            );
        }

        setHeaderContent(getHeaderPanel());
        setFooterButtons();
        setWikiPage(WikiPage.DUEL_DECKS);
    }

    private void setFooterButtons() {

        setLeftFooter(screenContent.getDuel().getGamesPlayed() == 0
                ? MenuButton.getCloseScreenButton()
                : MenuButton.build(this::doShowMainMenu, UiString.get(_S2))
        );

        setRightFooter(screenContent.getDuel().isFinished()
                ? MenuButton.build(this::doRestartDuel, UiString.get(_S3))
                : nextGameButton
        );

        // middle actions
        if (isNewDuel()) {
            addToFooter(
                    MenuButton.build(this::showDeckEditor,
                            MagicIcon.DECK, UiString.get(_S5), UiString.get(_S6)
                    ),
                    getTiledDeckCardImagesButton(),
                    SampleHandActionButton.createInstance(getActiveDeck())
            );
            addToFooter(screenContent.getActionBarButtons());
        
        } else if (isDuelFinished()) {
            addToFooter(getWinnerButton());
        
        } else { // duel in progress
            addToFooter(
                    getTiledDeckCardImagesButton(),
                    SampleHandActionButton.createInstance(getActiveDeck()),
                    MenuButton.build(this::doRestartDuel,
                            MagicIcon.REFRESH, UiString.get(_S10), UiString.get(_S11)
                    )
            );
        }        
    }

    private MenuButton getTiledDeckCardImagesButton() {
        return MenuButton.build(this::showTiledDeckCardImages,
                MagicIcon.TILED, UiString.get(_S12), UiString.get(_S13));
    }

    /**
     * Return to main menu without closing this screen.
     */
    private void doShowMainMenu() {
        ScreenController.showMainMenuScreen();
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

    private void doRestartDuel() {
        try {
            ScreenController.getFrame().restartDuel();
        } catch (InvalidDeckException ex) {
            ScreenController.showWarningMessage(ex.getMessage());
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

    private void showDeckEditor() {
        ScreenController.showDeckEditor(getActiveDeck());
    }

    private void showTiledDeckCardImages() {
        ScreenController.showDeckTiledCardsScreen(getActiveDeck());
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

    public void updateDecksAfterEdit() {
        screenContent.updateDecksAfterEdit();
    }

    public int getGamesPlayed() {
        return screenContent.getDuel().getGamesPlayed();
    }

    private void saveDuel() {
        screenContent.getDuel().save(MagicDuel.getLatestDuelFile());
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

    private JPanel getHeaderPanel() {
        final DuelConfig config = screenContent.getDuel().getConfiguration();
        final DuelSettingsPanel panel = new DuelSettingsPanel(config);
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
        worker = new NewGameWorker(duel, this);
        worker.execute();
    }

    void setNextGame(MagicGame aGame) {
        nextGame = aGame;
        nextGameButton.setBusy(nextGame == null);
    }

}
