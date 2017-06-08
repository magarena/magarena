package magic.ui.screen.menu.help;

import magic.translate.MText;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.widget.KeysStripPanel;

@SuppressWarnings("serial")
public class HelpMenuScreen extends HeaderFooterScreen {

    // translatable strings
    private static final String _S1 = "Help";

    public HelpMenuScreen() {
        super(MText.get(_S1));
        setLeftFooter(null);
        setFooterContent(new KeysStripPanel());
        setMainContent(new HelpMenuContentPanel());
    }
}
