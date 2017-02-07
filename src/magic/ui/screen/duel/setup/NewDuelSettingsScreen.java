package magic.ui.screen.duel.setup;

import magic.data.DuelConfig;
import magic.exception.InvalidDeckException;
import magic.translate.MText;
import magic.ui.ScreenController;
import magic.ui.WikiPage;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.widget.MenuButton;

@SuppressWarnings("serial")
public class NewDuelSettingsScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "New Duel Settings";
    private static final String _S2 = "Cancel";
    private static final String _S3 = "Next";
    private static final String _S4 = "Invalid Deck\n%s";
    private static final String _S5 = "%s's deck is invalid.";
    private static final String _S6 = "The following player decks are invalid :-\n\n";

    private static final int PLAYERS_COUNT = 2;
    private static final DuelConfig duelConfig = DuelConfig.getInstance();

    private final ScreenContentPanel contentPanel;

    public NewDuelSettingsScreen() {
        super(MText.get(_S1));
        duelConfig.load();
        contentPanel = new ScreenContentPanel(duelConfig);
        setMainContent(contentPanel);
        setLeftFooter(MenuButton.getCloseScreenButton(MText.get(_S2)));
        setRightFooter(MenuButton.build(this::doNextAction, MText.get(_S3)));
        setWikiPage(WikiPage.NEW_DUEL);
    }

    private void doNextAction() {
        if (isEachPlayerDeckValid(true)) {
            updateDuelConfig();
            ScreenController.closeActiveScreen(false);
            try {
                ScreenController.getFrame().newDuel(duelConfig);
            } catch (InvalidDeckException ex) {
                ScreenController.showWarningMessage(MText.get(_S4, ex.getMessage()));
            }
        }
    }

    private boolean isEachPlayerDeckValid(final boolean showErrorDialog) {
        boolean isEachDeckValid = true;
        final StringBuffer sb = new StringBuffer();
        if (!contentPanel.isDeckValid(0)) {
            sb.append(MText.get(_S5, contentPanel.getPlayerProfile(0).getPlayerName())).append("\n");
            isEachDeckValid = false;
        }
        if (!contentPanel.isDeckValid(1)) {
            sb.append(MText.get(_S5, contentPanel.getPlayerProfile(1).getPlayerName()));
            isEachDeckValid = false;
        }
        if (!isEachDeckValid && showErrorDialog) {
            sb.insert(0, MText.get(_S6));
            ScreenController.showWarningMessage(sb.toString());
        }
        return isEachDeckValid;
    }

    private void updateDuelConfig() {
        duelConfig.setStartLife(contentPanel.getStartLife());
        duelConfig.setHandSize(contentPanel.getHandSize());
        duelConfig.setNrOfGames(contentPanel.getNrOfGames());
        duelConfig.setCube(contentPanel.getCube());
        for (int i = 0; i < PLAYERS_COUNT; i++) {
            duelConfig.setPlayerProfile(i, contentPanel.getPlayerProfile(i));
            duelConfig.setPlayerDeckProfile(i, contentPanel.getDeckType(i), contentPanel.getDeckValue(i));
        }
    }

    @Override
    public boolean isScreenReadyToClose(final Object nextScreen) {
        if (isEachPlayerDeckValid(false)) {
            updateDuelConfig();
        }
        return true;
    }
}
