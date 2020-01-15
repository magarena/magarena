package magic.ui.screen.widget;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ScreenFooterPanel extends TexturedPanel
        implements IThemeStyle {

    private static class ContentPanel extends JPanel {
        public ContentPanel() {
            setLayout(new MigLayout("insets 0, gap 0"));
            setOpaque(false);
        }
    }

    public final static int PANEL_HEIGHT = 50;

    private final JPanel leftPanel;
    private final JPanel middlePanel;
    private final JPanel rightPanel;

    // True if button being added is the first.
    private boolean isFirstFooter = true;

    public ScreenFooterPanel() {

        this.leftPanel = new ContentPanel();
        this.middlePanel = new ContentPanel();
        this.rightPanel = new ContentPanel();

        setMinimumSize(new Dimension(getPreferredSize().width, PANEL_HEIGHT));

        // add close button as default.
        setLeftButton(PlainMenuButton.getCloseScreenButton());

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

    public void addMiddleButtons(PlainMenuButton[] buttons) {
        if (buttons != null) {
            for (PlainMenuButton btn : buttons) {
                btn.setEnabled(btn.isRunnable());
                btn.setBorderPainted(true);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                isFirstFooter ? 1 : 0,
                                0,
                                btn.hasSeparator() ? 1 : 0,
                                Color.LIGHT_GRAY
                        ),
                        BorderFactory.createEmptyBorder(0, 10, 0, 10))
                );
                middlePanel.add(btn, "w 60, h 40");
                isFirstFooter = false;
            }
        }
        middlePanel.revalidate();
    }

    public void setLeftButton(PlainMenuButton btn) {
        leftPanel.removeAll();
        if (btn != null) {
            btn.setEnabled(btn.isRunnable());
            btn.setIcon(MagicImages.getIcon(MagicIcon.GO_BACK));
            btn.setHorizontalTextPosition(SwingConstants.RIGHT);
            leftPanel.add(btn);
        }
        leftPanel.revalidate();
    }

    public void setRightButton(PlainMenuButton btn) {
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

    public void clearFooterButtons() {
        middlePanel.removeAll();
        revalidate();
        isFirstFooter = true;
    }

    public void addFooterGroup(PlainMenuButton[] btns) {
        for (PlainMenuButton btn : btns) {
            final boolean isGroupFirst = btn == btns[0];
            final boolean isGroupLast = btn == btns[btns.length - 1];
            btn.setBorderPainted(true);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(
                            0,
                            isFirstFooter ? 1 : 0,
                            0,
                            isGroupLast ? 1 : 0,
                            Color.LIGHT_GRAY
                    ),
                    BorderFactory.createEmptyBorder(
                            0,
                            isGroupFirst ? 18 : 10,
                            0,
                            isGroupLast ? 18 : 10
                    )
            ));
            middlePanel.add(btn, String.format("w %d, h 40",
                    isGroupFirst || isGroupLast ? 60 : 50)
            );
            isFirstFooter = false;
        }
        middlePanel.revalidate();
    }

    public void setFooterContent(JComponent obj) {
        removeAll();
        add(this.leftPanel);
        add(obj);
        add(this.rightPanel);
        revalidate();
    }

}
