package magic.ui.screen.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import magic.ui.FontsAndBorders;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.about.AboutPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class MenuPanel extends TexturedPanel implements IThemeStyle {

    private static final Dimension PREFERRED_SIZE = new Dimension(320, 405);

    private final String title;
    private final List<PlainMenuButton> menuItems = new ArrayList<>();

    public MenuPanel() {
        this(null);
    }

    public MenuPanel(final String title0) {

        this.title = title0;

        setPreferredSize(PREFERRED_SIZE);
        setMaximumSize(PREFERRED_SIZE);

        refreshStyle();

        setMenuPanelLayout();

    }

    public void addMenuItem(final PlainMenuButton button) {
        menuItems.add(button);
    }

    public void addMenuItem(final String caption, final AbstractAction action, final String tooltip) {
        addMenuItem(new PlainMenuButton(caption, action, tooltip));
    }

    public void addMenuItem(final String caption, int fontSize, final AbstractAction action) {
        final PlainMenuButton btn = new PlainMenuButton(caption, action, null);
        btn.setFont(btn.getFont().deriveFont((float)fontSize));
        addMenuItem(btn);
    }

    public void addMenuItem(final String caption, final AbstractAction action) {
        addMenuItem(caption, action, null);
    }

    public void addBlankItem() {
        final PlainMenuButton emptyButton = new PlainMenuButton("", null);
        emptyButton.setMinimumSize(new Dimension(0, 10));
        menuItems.add(emptyButton);
    }

    public void refreshLayout() {
        setMenuPanelLayout();
    }

    private void setMenuPanelLayout() {
        removeAll();
        setLayout(new MigLayout("insets 0 6 6 6, gap 0, flowy"));
        if (this.title != null) {
            add(getMenuTitlePanel(), "w 100%, pad 0 0 10 0, gapbottom 20");
        }
        for (PlainMenuButton menuItem : menuItems) {
            add(menuItem, "w 100%");
        }
        revalidate();
    }

    private AboutPanel getMenuTitlePanel() {
        final AboutPanel p = new AboutPanel(this.title);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        return p;
    }

    @Override
    public void refreshStyle() {
        final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
        final Color thisBG = MagicStyle.getTranslucentColor(refBG, 200);
        setBorder(FontsAndBorders.BLACK_BORDER);
        setBackground(thisBG);
    }

    public void clearMenuItems() {
        menuItems.clear();
    }

}
