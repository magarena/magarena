package magic.ui.screen.duel.decks;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import magic.data.DuelConfig;
import magic.data.MagicIcon;
import magic.exception.InvalidDeckException;
import magic.model.DuelPlayerConfig;
import magic.model.MagicDeck;
import magic.model.MagicDeckConstructionRule;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.IDeckProvider;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.WikiPage;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.deck.editor.IDeckEditorClient;
import magic.ui.screen.widget.DuelSettingsPanel;
import magic.ui.screen.widget.MenuButton;
import magic.ui.screen.widget.SampleHandActionButton;
import magic.ui.widget.cards.table.CardsTableStyle;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
public class DuelDecksScreen extends HeaderFooterScreen
    implements IDeckEditorClient, IDeckProvider {

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

    private final DuelSettingsPanel settingsPanel;
    private final DuelDecksPanel screenContent;
    private MagicGame nextGame = null;
    private final StartGameButton nextGameButton;
    private NewGameWorker worker;
    private final OptionsPanel optionsPanel;

    public DuelDecksScreen(final MagicDuel duel) {
        super(MText.get(_S1));

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

        final DuelConfig config = duel.getConfiguration();

        settingsPanel = new DuelSettingsPanel(config);
        settingsPanel.setEnabled(duel.getGamesPlayed() == 0);
        settingsPanel.setBorder(null);
        settingsPanel.setBackground(FontsAndBorders.TEXTAREA_TRANSPARENT_COLOR_HACK);
        settingsPanel.addPropertyChangeListener(
            DuelSettingsPanel.CP_CONFIG_UPDATED, evt -> onConfigUpdate()
        );


        setHeaderContent(settingsPanel);

        optionsPanel = new OptionsPanel(this);
        setHeaderOptions(optionsPanel);

        setFooterButtons();
        setWikiPage(WikiPage.DUEL_DECKS);
    }

    private void onConfigUpdate() {
        doGameSetupInBackground(screenContent.getDuel());
    }

    private void setFooterButtons() {

        setLeftFooter(screenContent.getDuel().getGamesPlayed() == 0
                ? MenuButton.getCloseScreenButton()
                : MenuButton.build(this::doShowMainMenu, MText.get(_S2))
        );

        setRightFooter(screenContent.getDuel().isFinished()
                ? MenuButton.build(this::doRestartDuel, MText.get(_S3))
                : nextGameButton
        );

        // middle actions
        if (isNewDuel()) {
            addToFooter(MenuButton.build(this::showDeckEditor,
                            MagicIcon.DECK, MText.get(_S5), MText.get(_S6)
                    ),
                    getTiledDeckCardImagesButton(),
                    SampleHandActionButton.createInstance(this)
            );
            addToFooter(screenContent.getActionBarButtons());

        } else if (isDuelFinished()) {
            addToFooter(getWinnerButton());

        } else { // duel in progress
            addToFooter(getTiledDeckCardImagesButton(),
                    SampleHandActionButton.createInstance(this),
                    MenuButton.build(this::doRestartDuel,
                            MagicIcon.REFRESH, MText.get(_S10), MText.get(_S11)
                    )
            );
        }
    }

    private MenuButton getTiledDeckCardImagesButton() {
        return MenuButton.build(this::showTiledDeckCardImages,
                MagicIcon.TILED, MText.get(_S12), MText.get(_S13));
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

    private void updateDuelConfig() {
        final DuelConfig config = screenContent.getDuel().getConfiguration();
        config.setStartLife(settingsPanel.getStartLife());
        config.setHandSize(settingsPanel.getHandSize());
        config.setNrOfGames(settingsPanel.getNrOfGames());
        config.setCube(settingsPanel.getCube());
    }

    private AbstractAction getPlayGameAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                updateDuelConfig();
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
        return MText.get(_S4, screenContent.getDuel().getGamesPlayed() + 1);
    }

    private boolean isNewDuel() {
        return screenContent.getDuel().getGamesPlayed() == 0;
    }

    private boolean isDuelFinished() {
        return screenContent.getDuel().isFinished();
    }

    private void showDeckEditor() {
        ScreenController.showDeckEditor(this);
    }

    private void showTiledDeckCardImages() {
        ScreenController.showDeckTiledCardsScreen(getActiveDeck());
    }

    private MenuButton getWinnerButton() {
        String winner = screenContent.getDuel().getWinningPlayerProfile().getPlayerName();
        return new MenuButton(MText.get(_S9, winner), new AbstractAction() {
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
            ScreenController.showWarningMessage(MText.get(_S14, aPlayer.getName(), brokenRulesText));
            return false;
        }

        return true;
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

    @Override
    public MagicDeck getDeck() {
        return getActiveDeck();
    }

    @Override
    public boolean setDeck(MagicDeck newDeck) {
        MagicDeck oldDeck = getActiveDeck();
        if (!newDeck.equals(oldDeck)) {
            if (newDeck.isSameDeckFile(oldDeck)) {
                // cards list has changed.
                newDeck.setUnsavedStatus();
            }
            screenContent.setDeck(newDeck);
            return true;
        }
        return true;
    }

    void setCardsTableStyle(int dialPosition) {
        CardsTableStyle.setStyle(dialPosition);
        screenContent.setCardsTableStyle();
    }
}
