package magic.ui.screen.menu.dev;

import magic.ui.screen.HeaderFooterScreen;

@SuppressWarnings("serial")
public class DevMenuScreen extends HeaderFooterScreen {

    public DevMenuScreen() {
        super("DevMode menu");
        setMainContent(new DevMenuContentPanel());
    }
}
