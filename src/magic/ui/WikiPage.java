package magic.ui;

public enum WikiPage {
    
    HOME("home"),
    CARDS_EXPLORER("UICardExplorer"),
    CARD_SCRIPTING("UICardScriptViewer"),
    DECK_EDITOR("UIDeckEditor"),
    DUEL_DECKS("UIDeckView"),
    MAIN_MENU("Main-Menu-Screen"),
    MULLIGAN("Mulligan-Screen"),
    NEW_DUEL("UINewDuel");

    private final String pageName;

    private WikiPage(String pageName) {
        this.pageName = pageName;
    }

    public String getUrl() {
        return URLUtils.URL_WIKI + pageName;
    }
}
