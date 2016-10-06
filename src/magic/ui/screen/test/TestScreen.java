package magic.ui.screen.test;

import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.widget.MenuButton;

@SuppressWarnings("serial")
public class TestScreen extends HeaderFooterScreen {

//    private final ExplorerHeaderPanel headerPanel;

    public TestScreen() {
        super("Test Screen");
        setLeftFooter(MenuButton.getTestButton());
        setRightFooter(MenuButton.getTestButton());
        addToFooter(MenuButton.getTestButton(), MenuButton.getTestButton());
        setMainContent(new TestContentPanel());
//        this.headerPanel = new ExplorerHeaderPanel();
//        setHeaderContent(this.headerPanel);
//        addToFooter(MenuButton.build(
//                () -> System.out.println("TESTING"),
//                MagicIcon.SAVE_ICON,
//                "Testing...", "Testing")
//        );
    }
}
