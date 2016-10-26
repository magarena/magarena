package magic.ui.widget.deck.stats;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.CardStatistics;
import magic.model.MagicColor;
import magic.translate.MText;
import magic.ui.MagicImages;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class CardColorStatsPanel extends JPanel {

    // translatable strings
    private static final String _S3 = "Mono: %d  •  Multi: %d  •  Colorless: %d";
    private static final String _S4 = "%d  •  Mono: %d  •  Lands: %d";
    private static final String _S1 = "Color distribution";

    private final List<JLabel> lines = new ArrayList<>();

    CardColorStatsPanel() {
        setOpaque(false);
        setLayout(new MigLayout("flowy, insets 0 2 2 2, gap 0"));
    }

    void setStats(CardStatistics stats) {

        lines.clear();
        for (int i = 0; i < stats.colorCount.length; i++) {
            if (stats.colorCount[i] > 0) {
                final MagicColor color = MagicColor.values()[i];
                final JLabel label = new JLabel(MagicImages.getIcon(color.getManaType()));
                label.setHorizontalAlignment(JLabel.LEFT);
                label.setIconTextGap(5);
                label.setText(MText.get(_S4,
                    stats.colorCount[i],
                    stats.colorMono[i],
                    stats.colorLands[i])
                );
                lines.add(label);
            }
        }

        final JLabel allLabel = new JLabel(MText.get(_S3,
            stats.monoColor,
            stats.multiColor,
            stats.colorless)
        );
        allLabel.setFont(allLabel.getFont().deriveFont(Font.ITALIC));
        lines.add(allLabel);

        final JPanel panel = new JPanel(new MigLayout("flowy, insets 4"));
        panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
        panel.setOpaque(false);
        for (final JLabel line : lines) {
            panel.add(line, "w 100%");
        }

        removeAll();
        add(DeckStatisticsViewer.getCaptionLabel(MText.get(_S1)), "w 100%");
        add(panel, "w 100%");
        revalidate();
    }

}
