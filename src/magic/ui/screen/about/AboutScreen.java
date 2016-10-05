package magic.ui.screen.about;

import magic.translate.UiString;
import magic.ui.screen.HeaderFooterScreen;

@SuppressWarnings("serial")
public class AboutScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "About...";

    public AboutScreen() {
        super(UiString.get(_S1));
        setHeaderContent(new AboutHeaderPanel());
        setMainContent(new AboutContentPanel());
        addToFooter(new LicenseButton());
    }

}
