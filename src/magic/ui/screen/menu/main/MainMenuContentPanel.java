package magic.ui.screen.menu.main;

import magic.exception.InvalidDeckException;
import magic.translate.MText;
import magic.ui.ScreenController;
import magic.ui.screen.menu.MenuScreenContentPanel;
import magic.ui.widget.alerter.AlertPanel;
import magic.utility.MagicSystem;

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
        super(MText.get(_S1), false);
        setMenuItems(MagicSystem.isDevMode());
        add(alertPanel);
        refreshAlerts();
    }

    private void setMenuItems(boolean showDevMenuItem) {
        clearMenuItems();
        addMenuItem(MText.get(_S2), this::doNewDuel);
        addMenuItem(MText.get(_S3), this::doResumeDuel);
        addMenuItem(MText.get(_S4), this::showExplorerScreen);
        addMenuItem(MText.get(_S5), this::showDeckEditor);
        addMenuItem(MText.get(_S6), this::showSettingsMenu);
        addMenuItem(MText.get(_S7), this::showHelpMenu);
        addSpace();
        addMenuItem(MText.get(_S8), this::doShutdown);
        if (showDevMenuItem) {
            addSpace();
            addSpace();
            addSpace();
            addMenuItem("DevMode", 16, this::showDevMenu);
        }
        refreshMenuLayout();
    }

    void refreshAlerts() {
        alertPanel.refreshAlerts();
    }

    private void doResumeDuel() {
        try {
            ScreenController.getFrame().loadDuel();
        } catch (InvalidDeckException ex) {
            ScreenController.showWarningMessage(ex.getMessage());
        }
    }

    private void doNewDuel() {
        ScreenController.showNewDuelSettingsScreen();
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

    private void showDevMenu() {
        ScreenController.showDevMenuScreen();
        if (!MagicSystem.isDevMode()) {
            hideDevModeMenuItem();
        }
    }

    void showDevModeMenuItem() {
        setMenuItems(true);
    }

    void hideDevModeMenuItem() {
        setMenuItems(false);
        MainMenuScreen.isCtrlKeyPressed = false;
    }
}
