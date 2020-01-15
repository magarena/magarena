package magic.ui.screen;

import javax.swing.JComponent;
import javax.swing.JPanel;

import magic.ui.screen.widget.PlainMenuButton;
import magic.ui.screen.widget.ScreenFooterPanel;
import magic.ui.screen.widget.ScreenHeaderPanel;
import net.miginfocom.swing.MigLayout;

/**
 * Basic template for a screen with a header and footer.
 */
@SuppressWarnings("serial")
public abstract class HeaderFooterScreen extends MScreen {

    private final ScreenHeaderPanel headerPanel;
    private final ScreenFooterPanel footerPanel;

    public HeaderFooterScreen(String title) {
        // default "empty" layout with minimum functionality for a new subclass.
        this.headerPanel = new ScreenHeaderPanel(title);
        this.footerPanel = new ScreenFooterPanel();
        setMigLayout();
    }

    private void setMigLayout() {
        final MigLayout layout = new MigLayout();
        layout.setLayoutConstraints("insets 0, gap 0, flowy");
        layout.setRowConstraints("[50!][100%, fill, grow][]");
        layout.setColumnConstraints("[center, fill, grow]");
        setLayout(layout);
    }

    protected final void refreshFooter() {
        footerPanel.refreshLayout();
    }

    @Override
    protected void refreshLayout() {
        removeAll();
        add(headerPanel);
        add(getContentPanel());
        add(footerPanel);
        revalidate();
    }

    protected final void setHeaderContent(JComponent aPanel) {
        headerPanel.setContent(aPanel);
    }

    protected final void addToFooter(PlainMenuButton... btns) {
        footerPanel.addMiddleButtons(btns);
    }

    protected void setFooterContent(JPanel panel) {
        footerPanel.setFooterContent(panel);
    }

    protected final void setLeftFooter(PlainMenuButton btn) {
        footerPanel.setLeftButton(btn);
    }

    protected final void setRightFooter(PlainMenuButton btn) {
        footerPanel.setRightButton(btn);
    }

    protected void clearFooterButtons() {
        footerPanel.clearFooterButtons();
    }

    protected final void addFooterGroup(PlainMenuButton... btns) {
        footerPanel.addFooterGroup(btns);
    }

    public void setHeaderOptions(JComponent c) {
        headerPanel.setOptions(c);
    }

}
