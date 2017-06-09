package magic.ui.screen.menu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
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

    /**
     * Calculates the vertical gap between menu items so as to keep the
     * layout more or less the same regardless of the font used.
     * <p>
     * Calculations are based on a font size of 32 having a {@code gapy}
     * of 8 and that if the font size is increased by 22% then {@code gapy}
     * should be reduced by 50%.
     */
    private long getGapY() {

        final double baseFontSize = 32.0D;
        final int baseGapY = 8;
        final double percentInc = 0.22D;
        final double percentHeight = 0.5D;

        Font f = NewMenuButton.getDisplayFont();
        FontMetrics fm = menuItems.get(0).getFontMetrics(f);
        double calc1 = (fm.getHeight() - baseFontSize) / baseFontSize;
        double calc2 = (calc1 / percentInc) * percentHeight;
        long gapy = baseGapY - Math.round(calc2 * baseGapY);
//        System.out.println("gapy = " + gapy + ", calc = " + calc2);

        return gapy;
    }

    private void setMenuPanelLayout() {
        removeAll();
        if (!menuItems.isEmpty()) {
            setLayout(new MigLayout(
                "insets 0 6 6 6, gapy " + getGapY() + ", flowy, ax center",
                "[center]")
            );
            for (NewMenuButton menuItem : menuItems) {
                add(menuItem);
            }
            revalidate();
        }
    }

    public void clearMenuItems() {
        menuItems.clear();
    }

}
