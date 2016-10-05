package magic.ui.screen.about;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import magic.translate.UiString;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class AboutHeaderPanel extends JPanel {

    // translatable strings
    private static final String _S3 = "Memory (MB)";
    private static final String _S5 = "Used: %.1f";
    private static final String _S6 = "Free: %.1f";
    private static final String _S7 = "Total: %.1f";
    private static final String _S8 = "Max: %.1f";

    private final JLabel memoryLabel = new JLabel();

    AboutHeaderPanel() {

        setOpaque(false);

        JLabel lbl = new JLabel(UiString.get(_S3));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Monospaced", Font.PLAIN, 12));

        memoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        memoryLabel.setForeground(Color.WHITE);
        memoryLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));

        setLayout(new MigLayout(
                "insets 0, gapy 2, flowy, aligny center",
                "[fill, grow]")
        );
        add(lbl);
        add(memoryLabel);

        new Timer(1000, (e) -> { refreshMemoryLabel(); }).start();
        refreshMemoryLabel();
    }

    private String getHeapUtilizationStats() {
        final float mb = 1024 * 1024;
        final Runtime runtime = Runtime.getRuntime();
        String s1 = UiString.get(_S5, (runtime.totalMemory() - runtime.freeMemory()) / mb);
        String s2 = UiString.get(_S6, runtime.freeMemory() / mb);
        String s3 = UiString.get(_S7, runtime.totalMemory() / mb);
        String s4 = UiString.get(_S8, runtime.maxMemory() / mb);
        return s1 + " • " + s2 + " • " + s3 + " • " + s4;
    }

    private void refreshMemoryLabel() {
        memoryLabel.setText(getHeapUtilizationStats());
    }

}
