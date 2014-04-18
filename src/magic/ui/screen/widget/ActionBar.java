package magic.ui.screen.widget;

import magic.data.IconImages;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

@SuppressWarnings("serial")
public class ActionBar extends TexturedPanel {

    private IActionBar actionProvider;

    public ActionBar(final IActionBar provider0) {
        actionProvider = provider0;
        setMinimumSize(new Dimension(getPreferredSize().width, 50));
        setBackground(FontsAndBorders.MAGSCREEN_BAR_COLOR);
        setMagActionBarLayout();
    }

    /**
     *
     */
    private void setMagActionBarLayout() {
        removeAll();
        setLayout(new MigLayout("insets 0, gap 10, flowx, aligny 50%", "[200][center,grow][200,right]"));
        addLeftAction();
        addMiddleActions();
        addRightAction();

    }
    public void refreshLayout() {
        setMagActionBarLayout();
        validate();
        repaint();
    }

    private void addRightAction() {
        MenuButton action = actionProvider.getRightAction();
        if (action != null) {
            action.setEnabled(action.isRunnable());
            if (action.getIcon() == null) {
                action.setIcon(IconImages.NEXT_ICON);
            }
            action.setHorizontalTextPosition(SwingConstants.LEFT);
            add(action);
        }
    }

    private void addMiddleActions() {
        final JPanel panel = new JPanel(new MigLayout("insets 0, gap 0, flowx"));
        panel.setOpaque(false);
        final List<MenuButton> buttons = actionProvider.getMiddleActions();
        if (buttons != null) {
            boolean isFirstButton = true;
            for (MenuButton btn : buttons) {
                panel.add(btn, "w 60, h 40");
                btn.setEnabled(btn.isRunnable());
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, isFirstButton ? 1 : 0, 0, btn.showSeparator() ? 1 : 0, Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(0, 10, 0, 10)));
                btn.setBorderPainted(true);
                isFirstButton = false;
            }
        }
        add(panel);
    }

    private void addLeftAction() {
        MenuButton action = actionProvider.getLeftAction();
        if (action != null) {
            action.setEnabled(action.isRunnable());
            action.setIcon(IconImages.BACK_ICON);
            action.setHorizontalTextPosition(SwingConstants.RIGHT);
            add(action);
        } else {
            JLabel lbl = new JLabel();
            lbl.setMinimumSize(new Dimension(120, 50));
            add(lbl);
        }
    }

}
