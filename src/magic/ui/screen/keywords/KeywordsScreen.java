package magic.ui.screen.keywords;

import java.awt.event.KeyEvent;
import magic.translate.UiString;
import magic.ui.ScreenController;
import magic.ui.helpers.KeyEventAction;
import magic.ui.screen.HeaderFooterScreen;

@SuppressWarnings("serial")
public class KeywordsScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "Keywords Glossary";

    public KeywordsScreen() {
        super(UiString.get(_S1));
        setDefaultProperties();
        setMainContent(new KeywordsContentPanel());
    }

    private void setDefaultProperties() {
        KeyEventAction.doAction(this, ()->ScreenController.closeActiveScreen())
            .on(0, KeyEvent.VK_K);
    }
}
