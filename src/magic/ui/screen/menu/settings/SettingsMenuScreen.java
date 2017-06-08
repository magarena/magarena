package magic.ui.screen.menu.settings;

import magic.translate.MText;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.widget.KeysStripPanel;

@SuppressWarnings("serial")
public class SettingsMenuScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "Settings";

    public SettingsMenuScreen() {
        super(MText.get(_S1));
        setLeftFooter(null);
        setFooterContent(new KeysStripPanel());
        setMainContent(new SettingsMenuContentPanel());
    }
}
