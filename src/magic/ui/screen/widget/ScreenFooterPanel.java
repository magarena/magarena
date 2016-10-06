package magic.ui.screen.widget;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.theme.Theme;
import magic.ui.widget.TexturedPanel;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ScreenFooterPanel extends TexturedPanel
        implements IThemeStyle {

    private class ContentPanel extends JPanel {
        public ContentPanel() {
            setLayout(new MigLayout("insets 0, gap 0"));
            setOpaque(false);
        }
    }

    public final static int PANEL_HEIGHT = 50;
    
    private final JPanel leftPanel;
    private final JPanel middlePanel;
    private final JPanel rightPanel;

    public ScreenFooterPanel() {

        this.leftPanel = new ContentPanel();
        this.middlePanel = new ContentPanel();
        this.rightPanel = new ContentPanel();

        setMinimumSize(new Dimension(getPreferredSize().width, PANEL_HEIGHT));
               
        // add close button as default.
        setLeftButton(MenuButton.getCloseScreenButton());

        setLayout(new MigLayout(
                "insets 0, gap 10, flowx, aligny 50%",
                "[200][center,grow][200,right]")
        );
        add(this.leftPanel);
        add(this.middlePanel);
        add(this.rightPanel);

        refreshStyle();
    }

    public void refreshLayout() {
        validate();
        repaint();
    }

    @Override
    public final void refreshStyle() {
        final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
        final Color thisBG = MagicStyle.getTranslucentColor(refBG, 220);
        setBackground(thisBG);
    }

    private boolean isFirstButton = true;

    public void addMiddleButtons(MenuButton[] buttons) {
        if (buttons != null) {
            for (MenuButton btn : buttons) {
                middlePanel.add(btn, "w 60, h 40");
                btn.setEnabled(btn.isRunnable());
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, isFirstButton ? 1 : 0, 0, btn.hasSeparator() ? 1 : 0, Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(0, 10, 0, 10)));
                btn.setBorderPainted(true);
                isFirstButton = false;
            }
        }
        middlePanel.revalidate();
    }

    public void setLeftButton(MenuButton btn) {
        leftPanel.removeAll();
        if (btn != null) {
            btn.setEnabled(btn.isRunnable());
            btn.setIcon(MagicImages.getIcon(MagicIcon.GO_BACK));
            btn.setHorizontalTextPosition(SwingConstants.RIGHT);
            leftPanel.add(btn);
        }
        leftPanel.revalidate();
    }

    public void setRightButton(MenuButton btn) {
        rightPanel.removeAll();
        if (btn != null) {
            btn.setEnabled(btn.isRunnable());
            if (btn.getIcon() == null) {
                btn.setIcon(MagicImages.getIcon(MagicIcon.GO_NEXT));
            }
            btn.setHorizontalTextPosition(SwingConstants.LEFT);
            rightPanel.add(btn);
        }
        rightPanel.revalidate();
    }

}
