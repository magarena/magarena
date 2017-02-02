package magic.ui.widget.duel.player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.ui.helpers.ImageHelper;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PlayerCounterPanel extends JPanel {

    private static final Font COUNTER_FONT = new Font("Dialog", Font.PLAIN, 12);
    private static final Color OFF_COLOR = MagicStyle.getTranslucentColor(Color.BLACK, 100);

    private final ImageIcon onIcon;
    private final ImageIcon offIcon;
    private final JLabel iconLabel;
    private final JLabel valueLabel;

    PlayerCounterPanel(MagicIcon icon, String tooltip) {
        setOpaque(false);
        onIcon = MagicImages.getIcon(icon);
        offIcon = new ImageIcon(ImageHelper.getTranslucentImage(onIcon.getImage(), 0.4f));
        iconLabel = getIconLabel(tooltip);
        valueLabel = getValueLabel(tooltip);
        setLayout(new MigLayout("insets 0, alignx right, gapx 1"));
        add(valueLabel, "aligny top");
        add(iconLabel);
    }

    private JLabel getIconLabel(String tooltip) {
        JLabel lbl = new JLabel();
        lbl.setToolTipText(tooltip);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setMinimumSize(new Dimension(16, 16));
        lbl.setPreferredSize(lbl.getMinimumSize());
        lbl.setMaximumSize(lbl.getMinimumSize());
        return lbl;
    }

    private JLabel getValueLabel(String tooltip) {
        JLabel lbl = new JLabel();
        lbl.setToolTipText(tooltip);
        lbl.setFont(COUNTER_FONT);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl.setMaximumSize(new Dimension(36, 14));
        return lbl;
    }

    void update(int newValue) {
        iconLabel.setIcon(newValue > 0 ? onIcon : offIcon);
        valueLabel.setForeground(newValue > 0 ? Color.BLACK : OFF_COLOR);
        valueLabel.setText(String.valueOf(newValue));
    }

}
