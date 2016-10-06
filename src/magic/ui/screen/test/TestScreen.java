package magic.ui.screen.test;

import magic.data.MagicIcon;
import magic.ui.ScreenController;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.card.explorer.ExplorerHeaderPanel;
import magic.ui.screen.widget.MenuButton;

@SuppressWarnings("serial")
public class TestScreen extends HeaderFooterScreen {

    public TestScreen() {

        // mandatory screen title, everything else is optional.
        super("Test Screen");

        // main JPanel where everything happens.
        setMainContent(new TestContentPanel());

        // display a JPanel of stuff in central header.
        setHeaderContent(new ExplorerHeaderPanel());

        // adds a default "Close" button if not specified.
        setLeftFooter(MenuButton.getTestButton());

        // Optional or one button allowed.
        setRightFooter(MenuButton.getTestButton());

        // adds a variable number of MenuButtons to central footer.
        addToFooter(
                MenuButton.getTestButton(), 
                MenuButton.build(this::showTestMessage,
                        MagicIcon.STATS,
                        "Testing", "Click to test...")
            );
    }

    private void showTestMessage() {
        ScreenController.showInfoMessage("Testing...");
    }
}
