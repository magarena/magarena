package magic.ui.screen.widget;

import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class MenuPanel extends TexturedPanel {

    private final String title;
    private final List<MenuButton> menuItems = new ArrayList<MenuButton>();

    public MenuPanel() {
        this(null);
    }

    public MenuPanel(final String title0) {

        this.title = title0;

        setPreferredSize(new Dimension(300, 380));
        setMaximumSize(new Dimension(300, 380));

        setBorder(FontsAndBorders.BLACK_BORDER);
        setBackground(FontsAndBorders.MENUPANEL_COLOR);

        setMenuPanelLayout();

    }

    public void addMenuItem(final MenuButton button) {
        menuItems.add(button);
    }
    public void addMenuItem(final String caption, final AbstractAction action, final String tooltip) {
        addMenuItem(new MenuButton(caption, action, tooltip));
    }
    public void addMenuItem(final String caption, final AbstractAction action) {
        addMenuItem(caption, action, null);
    }

    public void addBlankItem() {
        final MenuButton emptyButton = new MenuButton("", null);
        emptyButton.setMinimumSize(new Dimension(0, 10));
        menuItems.add(emptyButton);
    }

    public void refreshLayout() {
        setMenuPanelLayout();
    }

    private void setMenuPanelLayout() {
        removeAll();
        setLayout(new MigLayout("insets 6, gap 0, flowy"));
        if (this.title != null) {
            add(getMenuTitlePanel(), "w 100%, pad 0 0 10 0, gapbottom 15");
        }
        for (MenuButton menuItem : menuItems) {
            add(menuItem, "w 100%");
        }
    }

    private CaptionPanel getMenuTitlePanel() {
        final CaptionPanel p = new CaptionPanel(this.title);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        return p;
    }

}
