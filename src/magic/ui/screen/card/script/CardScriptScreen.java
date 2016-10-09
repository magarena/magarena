package magic.ui.screen.card.script;

import magic.data.MagicIcon;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import magic.ui.helpers.UrlHelper;
import magic.ui.screen.widget.MenuButton;
import magic.utility.MagicSystem;
import magic.ui.WikiPage;
import magic.ui.helpers.MouseHelper;
import magic.ui.screen.HeaderFooterScreen;

@SuppressWarnings("serial")
public class CardScriptScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "Card Script";
    private static final String _S3 = "Reload";
    private static final String _S4 = "Reload script/groovy files.";
    private static final String _S5 = "Firemind";
    private static final String _S6 = "Opens the Project Firemind scrips submission page in your browser.";

    private final ScriptContentPanel content;

    public CardScriptScreen(final MagicCardDefinition card) {
        super(UiString.get(_S1));
        content = new ScriptContentPanel(card);
        setMainContent(content);
        setFooterButtons();
        setWikiPage(WikiPage.CARD_SCRIPTING);
    }

    private void setFooterButtons() {
        addToFooter(MenuButton.build(this::openFiremindWebpage, 
                MagicIcon.FIREMIND, _S5, _S6)
        );
        if (MagicSystem.isDevMode()) {
            addToFooter(MenuButton.build(this::doReloadScript,
                    MagicIcon.REFRESH, _S3, _S4)
            );
        }
    }

    private void doReloadScript() {
        MouseHelper.showBusyCursor();
        content.refreshContent();
        MouseHelper.showDefaultCursor();
    }

    private void openFiremindWebpage() {
        UrlHelper.openURL(UrlHelper.URL_FIREMIND_SCRIPTS);
    }

}
