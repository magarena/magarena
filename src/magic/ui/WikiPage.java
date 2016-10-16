package magic.ui;

import magic.ui.helpers.UrlHelper;

public enum WikiPage {
    
    HOME("home"),
    CARDS_EXPLORER("UICardExplorer"),
    CARD_SCRIPTING("UICardScriptViewer"),
    DECK_EDITOR("UIDeckEditor"),
    DUEL_DECKS("UIDeckView"),
    MAIN_MENU("Main-Menu-Screen"),
    MULLIGAN("Mulligan-Screen"),
    NEW_DUEL("UINewDuel"),
    IMPORT_SETTINGS("Upgrading-to-a-new-release");

    private final String pageName;

    private WikiPage(String pageName) {
        this.pageName = pageName;
    }

    public String getUrl() {
        return UrlHelper.URL_WIKI + pageName;
    }
}
