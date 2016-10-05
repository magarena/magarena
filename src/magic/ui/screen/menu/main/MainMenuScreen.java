package magic.ui.screen.menu.main;

import magic.data.GeneralConfig;
import magic.ui.WikiPage;
import magic.ui.screen.MagicScreen;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
public class MainMenuScreen extends MagicScreen {

    private final MainMenuContentPanel contentPanel;

    public MainMenuScreen() {
        MagicSystem.setIsTestGame(false);
        contentPanel = new MainMenuContentPanel();
        setMainContent(contentPanel);
        setWikiPage(WikiPage.MAIN_MENU);
    }

    public void updateMissingImagesNotification() {
        if (GeneralConfig.getInstance().isMissingFiles()) {
            GeneralConfig.getInstance().setIsMissingFiles(false);
            contentPanel.refreshAlerts();
        }
    }
}
