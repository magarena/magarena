package magic.ui.screen.menu.main;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import magic.data.GeneralConfig;
import magic.translate.MText;
import magic.ui.ScreenController;
import magic.ui.WikiPage;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.MScreen;
import magic.ui.screen.widget.MenuButton;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
public class MainMenuScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "Main menu";
    private static final String _S2 = "Quit";

    private static final KeyboardFocusManager KBM = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    private final MainMenuContentPanel contentPanel;

    static boolean isCtrlKeyPressed = false;

    private final KeyEventDispatcher keyEventDispatcher = (KeyEvent ev) -> {
        if (!ScreenController.isActive(this)) {
            return false;
        }
        synchronized (MainMenuScreen.class) {
            switch (ev.getID()) {
            case KeyEvent.KEY_PRESSED:
                if (ev.isControlDown() && !isCtrlKeyPressed) {
                    isCtrlKeyPressed = true;
                    showDevModeMenuItem();
                    return true;
                }
                break;
            case KeyEvent.KEY_RELEASED:
                if (isCtrlKeyPressed) {
                    isCtrlKeyPressed = false;
                    hideDevModeMenuItem();
                    return true;
                }
                break;
            }
            return false;
        }
    };

    public MainMenuScreen() {
        super(MText.get(_S1));
        MagicSystem.setIsTestGame(false);
        contentPanel = new MainMenuContentPanel();
        setMainContent(contentPanel);
        setLeftFooter(null);
        addToFooter(MenuButton.build(this::doCloseScreen, MText.get(_S2)));
        setWikiPage(WikiPage.MAIN_MENU);
        if (!MagicSystem.isDevMode()) {
            KBM.addKeyEventDispatcher(keyEventDispatcher);
        }
    }

    private void doCloseScreen() {
        ScreenController.closeActiveScreen();
    }

    private void showDevModeMenuItem() {
        contentPanel.showDevModeMenuItem();
    }

    private void hideDevModeMenuItem() {
        contentPanel.hideDevModeMenuItem();
    }

    public void updateMissingImagesNotification() {
        if (GeneralConfig.getInstance().isMissingFiles()) {
            GeneralConfig.getInstance().setIsMissingFiles(false);
            contentPanel.refreshAlerts();
        }
    }

    @Override
    public boolean isScreenReadyToClose(MScreen aScreen) {
        if (!MagicSystem.isDevMode()) {
            KBM.removeKeyEventDispatcher(keyEventDispatcher);
        }
        return super.isScreenReadyToClose(aScreen);
    }

}
