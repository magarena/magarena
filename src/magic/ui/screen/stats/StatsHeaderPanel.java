package magic.ui.screen.stats;

import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.translate.MText;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class StatsHeaderPanel extends JPanel {

    // translatable strings
    private static final String _S1 = "%s games";

    private final JLabel totals1;
    private final JLabel totals2;

    StatsHeaderPanel(String schemaVersion) {
        setOpaque(false);
        totals1 = createLabel(16);
        totals1.setText(schemaVersion);
        totals2 = createLabel(14);
        setLayout(new MigLayout(
                "insets 0, gap 2, flowy, aligny center",
                "[fill, grow]")
        );
        add(totals1);
        add(totals2);
        refreshTotals(0);
    }

    private JLabel createLabel(int fontSize) {
        final JLabel lbl = new JLabel();
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Dialog", Font.PLAIN, fontSize));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    void refreshTotals(int games) {
        totals2.setText(MText.get(_S1, NumberFormat.getInstance().format(games)));
    }
}
