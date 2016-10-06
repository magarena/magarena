package magic.ui.screen.card.explorer;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.translate.StringContext;
import magic.translate.UiString;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ExplorerHeaderPanel extends JPanel {

    // translatable strings
    @StringContext(eg = "Total number of cards displayed in explorer.")
    private static final String _S9 = "%s cards";
    private static final String _S10 = "Playable: %s   •   Unimplemented: %s";

    private final JLabel totals1;
    private final JLabel totals2;

    public ExplorerHeaderPanel() {
        setOpaque(false);
        totals1 = createLabel(16);
        totals2 = createLabel(14);
        setLayout(new MigLayout(
                "insets 0, gap 2, flowy, aligny center",
                "[fill, grow]")
        );
        add(totals1);
        add(totals2);
        refreshTotals(0, 0, 0);
    }

    private JLabel createLabel(int fontSize) {
        final JLabel lbl = new JLabel();
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Dialog", Font.PLAIN, fontSize));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    private String getCountCaption(final int total, final int value) {
        final double percent = value / (double) total * 100;
        DecimalFormat df = new DecimalFormat("0.0");
        return NumberFormat.getInstance().format(value) + " (" + (!Double.isNaN(percent) ? df.format(percent) : "0.0") + "%)";
    }

    public void refreshTotals(int total, int playable, int missing) {
        totals1.setText(UiString.get(_S9,
                NumberFormat.getInstance().format(total))
        );
        totals2.setText(UiString.get(_S10,
                getCountCaption(total, playable),
                getCountCaption(total, missing))
        );
    }
}
