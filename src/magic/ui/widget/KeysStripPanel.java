package magic.ui.widget;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import magic.translate.MText;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class KeysStripPanel extends TexturedPanel implements IThemeStyle {

    // translatable strings
    private static final String _S1 = "F1: Help";
    private static final String _S2 = "F10: Screenshot";
    private static final String _S3 = "F11: Fullscreen";
    private static final String _S4 = "F12: Background";
    private static final String _S5 = "ESC: Options / Close";

    private final MigLayout layout = new MigLayout();

    public KeysStripPanel() {
        setPreferredSize(new Dimension(0, 22));
        setLayout(layout);
        refreshStyle();
        refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        layout.setLayoutConstraints("gap 14, insets 0, center, center");
        add(getLabel(MText.get(_S1)));
        add(getLabel(MText.get(_S2)));
        add(getLabel(MText.get(_S3)));
        add(getLabel(MText.get(_S4)));
        add(getLabel(MText.get(_S5)));
    }

    @Override
    public final void refreshStyle() {
        final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
        final Color thisBG = MagicStyle.getTranslucentColor(refBG, 240);
        setBackground(thisBG);
    }

    private JLabel getLabel(final String text) {
        final JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.LIGHT_GRAY);
        return lbl;
    }

}
