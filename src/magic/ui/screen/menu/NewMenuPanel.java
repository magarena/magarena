package magic.ui.screen.menu;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class NewMenuPanel extends JPanel {

    private final List<NewMenuButton> menuItems = new ArrayList<>();

    NewMenuPanel() {
        setMenuPanelLayout();
        setOpaque(false);
    }

    public void addMenuItem(final NewMenuButton button) {
        menuItems.add(button);
    }

    public void addMenuItem(final String caption, final AbstractAction action, final String tooltip) {
        addMenuItem(new NewMenuButton(caption, action, tooltip));
    }

    public void addMenuItem(final String caption, int fontSize, final AbstractAction action) {
        final NewMenuButton btn = new NewMenuButton(caption, action, null);
        btn.setFont(btn.getFont().deriveFont((float)fontSize));
        addMenuItem(btn);
    }

    public void addMenuItem(final String caption, final AbstractAction action) {
        addMenuItem(caption, action, null);
    }

    public void addBlankItem() {
        final NewMenuButton emptyButton = new NewMenuButton("", null);
        emptyButton.setMinimumSize(new Dimension(0, 10));
        menuItems.add(emptyButton);
    }

    public void refreshLayout() {
        setMenuPanelLayout();
    }

    private void setMenuPanelLayout() {
        removeAll();
        setLayout(new MigLayout("insets 0 6 6 6, gapy 4, flowy, ax center", "[center]"));
        for (NewMenuButton menuItem : menuItems) {
            add(menuItem);
        }
        revalidate();
    }

    public void clearMenuItems() {
        menuItems.clear();
    }

}
