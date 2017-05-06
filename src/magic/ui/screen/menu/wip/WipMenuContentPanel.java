package magic.ui.screen.menu.wip;

import java.awt.Color;
import javax.swing.BorderFactory;
import magic.ui.ScreenController;
import magic.ui.screen.menu.MenuScreenContentPanel;

@SuppressWarnings("serial")
class WipMenuContentPanel extends MenuScreenContentPanel {

    WipMenuContentPanel() {
        super("WIP Menu", true);
        addMenuItem("Test screen", this::showTestScreen);
        addMenuItem("Cardflow", this::showCardFlowScreen);
        addSpace();
        addSpace();
        addMenuItem("Close menu", this::onCloseMenu);
        refreshMenuLayout();
        mp.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
    }

    private void showCardFlowScreen() {
        ScreenController.showCardFlowScreen();
    }

    private void showTestScreen() {
        ScreenController.showTestScreen();
    }

    private void onCloseMenu() {
        ScreenController.closeActiveScreen(false);
    }
}
