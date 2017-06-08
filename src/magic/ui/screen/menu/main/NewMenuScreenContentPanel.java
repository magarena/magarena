package magic.ui.screen.menu.main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import magic.ui.FontsAndBorders;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.KeysStripPanel;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class NewMenuScreenContentPanel extends TexturedPanel
    implements IThemeStyle {

    protected final NewMenuPanel mp;

    public NewMenuScreenContentPanel(boolean showKeyStripPanel) {

        mp = new NewMenuPanel();

        final MigLayout layout = new MigLayout();
        layout.setLayoutConstraints("insets 0, gap 0, flowy");
        layout.setRowConstraints("[30!][100%, center][30!, bottom]");
        layout.setColumnConstraints("[center, fill, grow]");
        setLayout(layout);

        add(mp, "cell 0 1");
        if (showKeyStripPanel) {
            add(new KeysStripPanel());
        }

        refreshStyle();
        setOpaque(false);
    }

    protected void addMenuItem(String name, int fontSize, Runnable action) {
        mp.addMenuItem(name, fontSize, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    protected void addMenuItem(String name, String tooltip, Runnable action) {
        mp.addMenuItem(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        }, tooltip);
    }

    protected void addMenuItem(String name, Runnable action) {
        mp.addMenuItem(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    protected void addSpace() {
        mp.addBlankItem();
    }

    protected void refreshMenuLayout() {
        mp.refreshLayout();
        repaint();
    }

    protected void clearMenuItems() {
        mp.clearMenuItems();
    }

    @Override
    public void refreshStyle() {
        final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
        final Color thisBG = MagicStyle.getTranslucentColor(refBG, 160);
        setBorder(FontsAndBorders.BLACK_BORDER);
        setBackground(thisBG);
    }

}
