package magic.ui.widget;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class KeysStripPanel extends TexturedPanel implements IThemeStyle {

    private final MigLayout layout = new MigLayout();

    public KeysStripPanel() {
        setPreferredSize(new Dimension(0, 20));
        setLayout(layout);
        refreshStyle();
        refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        layout.setLayoutConstraints("gap 14, insets 2 6 2 6, center");
        add(getLabel("F1: Help"));
        add(getLabel("F10: Screenshot"));
        add(getLabel("F11: Fullscreen"));
        add(getLabel("F12: Background"));
        add(getLabel("ESC: Options / Close"));
    }

    @Override
    public final void refreshStyle() {
        final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
        final Color thisBG = MagicStyle.getTranslucentColor(refBG, 220);
        setBackground(thisBG);
    }

    private JLabel getLabel(final String text) {
        final JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.LIGHT_GRAY.darker());
        return lbl;
    }

}
