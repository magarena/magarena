package magic.utility;

import magic.ui.URLUtils;

public final class WikiPage {

    public static final String HOME = "home";
    public static final String CARDS_EXPLORER = "UICardExplorer";
    public static final String CARD_SCRIPTING = "UICardScriptViewer";
    public static final String DECK_EDITOR = "UIDeckEditor";
    public static final String DUEL_DECKS = "UIDeckView";
    public static final String MAIN_MENU = "Main-Menu-Screen";
    public static final String MULLIGAN = "Mulligan-Screen";
    public static final String NEW_DUEL = "UINewDuel";

    public static String getUrl(String pageName) {
        return URLUtils.URL_WIKI + pageName;
    }

    private WikiPage() { }
}
