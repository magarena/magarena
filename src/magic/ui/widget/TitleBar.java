package magic.ui.widget;

import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class TitleBar extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JLabel label;

    public TitleBar(final String title) {

        final Theme theme=ThemeFactory.getInstance().getCurrentTheme();
        setBackground(theme.getColor(Theme.COLOR_TITLE_BACKGROUND));
        setLayout(new BorderLayout());

        label=new JLabel(title);
        label.setIconTextGap(4);
        label.setOpaque(false);
        label.setForeground(theme.getColor(Theme.COLOR_TITLE_FOREGROUND));
        label.setPreferredSize(new Dimension(0,20));
        label.setBorder(FontsAndBorders.NO_TARGET_BORDER);
        add(label,BorderLayout.CENTER);
    }

    public void setText(final String text) {

        label.setText(text);
    }

    public void setIcon(final ImageIcon icon) {

        label.setIcon(icon);
    }

    public void setHorizontalAlignment(final int alignment) {

        label.setHorizontalAlignment(alignment);
    }
}
