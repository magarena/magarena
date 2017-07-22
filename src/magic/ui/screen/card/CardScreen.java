package magic.ui.screen.card;

import magic.data.MagicIcon;
import magic.model.MagicCardDefinition;
import magic.translate.MText;
import magic.ui.WikiPage;
import magic.ui.helpers.MouseHelper;
import magic.ui.helpers.UrlHelper;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.widget.PlainMenuButton;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
public class CardScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "Card Script";
    private static final String _S3 = "Reload";
    private static final String _S4 = "Reload script/groovy files.";
    private static final String _S5 = "Firemind";
    private static final String _S6 = "Opens the Project Firemind scrips submission page in your browser.";

    private final ContentPanel content;

    public CardScreen(final MagicCardDefinition card) {
        super(MText.get(_S1));
        content = new ContentPanel(card);
        setMainContent(content);
        setFooterButtons();
        setWikiPage(WikiPage.CARD_SCRIPTING);
    }

    private void setFooterButtons() {
        addToFooter(PlainMenuButton.build(this::openFiremindWebpage,
                MagicIcon.FIREMIND, MText.get(_S5), MText.get(_S6))
        );
        if (MagicSystem.isDevMode()) {
            addToFooter(PlainMenuButton.build(this::doReloadScript,
                    MagicIcon.REFRESH, MText.get(_S3), MText.get(_S4))
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
