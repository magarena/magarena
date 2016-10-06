package magic.ui.screen.keywords;

import magic.translate.UiString;
import magic.ui.screen.HeaderFooterScreen;

@SuppressWarnings("serial")
public class KeywordsScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "Keywords Glossary";

    public KeywordsScreen() {
        super(UiString.get(_S1));
        setMainContent(new KeywordsContentPanel());
    }
}
