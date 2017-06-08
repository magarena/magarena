package magic.ui.widget;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.translate.MText;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class KeysStripPanel extends JPanel {

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
        refreshLayout();
        setOpaque(false);
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

    private JLabel getLabel(final String text) {
        final JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.LIGHT_GRAY);
        return lbl;
    }

}
