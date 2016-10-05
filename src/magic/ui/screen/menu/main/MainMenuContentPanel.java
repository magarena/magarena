package magic.ui.screen.menu.main;

import magic.exception.InvalidDeckException;
import magic.ui.ScreenController;
import magic.ui.screen.menu.MenuScreenContentPanel;
import magic.ui.widget.alerter.AlertPanel;

@SuppressWarnings("serial")
class MainMenuContentPanel extends MenuScreenContentPanel {

    // translatable strings.
    private static final String _S1 = "Main Menu";
    private static final String _S2 = "New duel";
    private static final String _S3 = "Resume duel";
    private static final String _S4 = "Card explorer";
    private static final String _S5 = "Deck editor";
    private static final String _S6 = "Settings";
    private static final String _S7 = "Help";
    private static final String _S8 = "Quit to desktop";

    private static final AlertPanel alertPanel = new AlertPanel();

    MainMenuContentPanel() {
        super(_S1, false);
        addMenuItem(_S2, this::doNewDuel);
        addMenuItem(_S3, this::doResumeDuel);
        addMenuItem(_S4, this::showExplorerScreen);
        addMenuItem(_S5, this::showDeckEditor);
        addMenuItem(_S6, this::showSettingsMenu);
        addMenuItem(_S7, this::showHelpMenu);
        addMenuItem(_S8, this::doShutdown);
        refreshMenuLayout();

        add(alertPanel);
        refreshAlerts();
    }

    void refreshAlerts() {
        alertPanel.refreshAlerts();
    }

    private void doResumeDuel() {
        try {
            ScreenController.getMainFrame().loadDuel();
        } catch (InvalidDeckException ex) {
            ScreenController.showWarningMessage(ex.getMessage());
        }
    }

    private void doNewDuel() {
        ScreenController.showDuelPlayersScreen();
    }

    private void showExplorerScreen() {
        ScreenController.showCardExplorerScreen();
    }

    private void showDeckEditor() {
        ScreenController.showDeckEditor();        
    }

    private void showSettingsMenu() {
        ScreenController.showSettingsMenuScreen();
    }

    private void showHelpMenu() {
        ScreenController.showHelpMenuScreen();
    }

    private void doShutdown() {
        ScreenController.closeActiveScreen(false);
    }
}
