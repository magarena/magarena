package magic.ui.widget;

import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.ui.ScreenController;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class TitleBar extends JPanel {

    private final JLabel label;

    public TitleBar(String text) {

        label = new JLabel(text);
        label.setForeground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_FOREGROUND));

        setPreferredSize(new Dimension(getPreferredSize().width, 22));
        setMinimumSize(getPreferredSize());
        setMaximumSize(new Dimension(ScreenController.getFrame().getSize().width, 22));

        setLayout(new MigLayout("insets 0 4 0 4", "[grow, fill][]", "[grow, fill]"));
        add(label);

        setBackground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND));
    }

    public TitleBar() {
        this("");
    }

    public void setText(final String text) {
        label.setText(text);
    }

    public JLabel getLabel() {
        return label;
    }

    public void setIcon(Icon icon) {
        label.setIcon(icon);
    }

}
