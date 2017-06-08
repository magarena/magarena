package magic.ui.screen.menu.help;

import magic.translate.MText;
import magic.ui.ScreenController;
import magic.ui.helpers.UrlHelper;
import magic.ui.screen.menu.main.NewMenuScreenContentPanel;

@SuppressWarnings("serial")
class HelpMenuContentPanel extends NewMenuScreenContentPanel {

    // translatable strings
    private static final String _S2 = "ReadMe";
    private static final String _S3 = "Online help";
    private static final String _S4 = "Keywords glossary";
    private static final String _S5 = "About Magarena";
    private static final String _S6 = "Main menu";

    HelpMenuContentPanel() {
        super(false);
        addMenuItem(MText.get(_S2), this::onReadMeMenu);
        addMenuItem(MText.get(_S3), this::onOnlineHelpMenu);
        addMenuItem(MText.get(_S4), this::onKeywordsMenu);
        addMenuItem(MText.get(_S5), this::onAboutMenu);
        addSpace();
        addMenuItem(MText.get(_S6), this::doCloseMenu);
        refreshMenuLayout();
    }

    void onReadMeMenu() {
        ScreenController.showReadMeScreen();
    }

    void onOnlineHelpMenu() {
        UrlHelper.openURL(UrlHelper.URL_USERGUIDE);
    }

    void onKeywordsMenu() {
        ScreenController.showKeywordsScreen();
    }

    void onAboutMenu() {
        ScreenController.showAboutScreen();
    }

    private void doCloseMenu() {
        ScreenController.closeActiveScreen(false);
    }
}
