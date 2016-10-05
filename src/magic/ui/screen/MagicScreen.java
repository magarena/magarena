package magic.ui.screen;

import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import magic.ui.URLUtils;
import magic.ui.ScreenController;
import magic.ui.WikiPage;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class MagicScreen extends JPanel {

    private static final JPanel TEMP_PANEL = new JPanel() {
        @Override
        public boolean isVisible() {
            return false;
        }
    };

    private JComponent contentPanel = TEMP_PANEL;
    private WikiPage wikiPage = WikiPage.HOME;

    public MagicScreen() {
        setOpaque(false);
        setDefaultKeyboardActions();
        setLayout(new MigLayout("insets 0, gap 0", "[fill, grow]", "[fill, grow]"));
    }

    private void setDefaultKeyboardActions() {
        ScreenHelper.setKeyboardAction(this, KeyEvent.VK_ESCAPE, this::doEscapeKeyAction);
        ScreenHelper.setKeyboardAction(this, KeyEvent.VK_F1, this::doF1KeyAction);
    }

    protected void refreshLayout() {
        removeAll();
        add(contentPanel);
        revalidate();   
    }

    protected void setMainContent(final JComponent aPanel) {
        this.contentPanel = aPanel;
        refreshLayout();
    }

    protected void doEscapeKeyAction() {
        ScreenController.closeActiveScreen(true);
    }

    protected void doF1KeyAction() {
        showWikiHelpPage();
    }

    public void showWikiHelpPage() {
        URLUtils.openURL(wikiPage.getUrl());
    }

    public boolean isScreenReadyToClose(final Object aScreen) {
        return true;
    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        if (isVisible) {
            refreshLayout();
        }
    }

    protected JComponent getContentPanel() {
        return contentPanel;
    }

    public void setWikiPage(WikiPage wikiPage) {
        this.wikiPage = wikiPage;
    }

}
