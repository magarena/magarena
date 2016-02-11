package magic.ui.deck.widget;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.data.CardStatistics;
import magic.translate.UiString;
import magic.ui.MagicImages;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class CardTypeStatsPanel extends JPanel {

    // translatable strings    
    private static final String _S1 = "Card type";

    CardTypeStatsPanel() {
        setLayout(new MigLayout("insets 0, gap 0, center"));
        setOpaque(false);
    }

    void setStats(CardStatistics statistics) {

        removeAll();

        add(DeckStatisticsViewer.getCaptionLabel(UiString.get(_S1)), "w 100%, wrap, span");

        for (int i = 0; i < CardStatistics.NR_OF_TYPES; i++) {

            final int total = statistics.totalTypes[i];

            // type icon
            final JLabel iconLabel = new JLabel();
            iconLabel.setIcon(MagicImages.getIcon(CardStatistics.TYPE_ICONS.get(i)));
            iconLabel.setToolTipText(CardStatistics.TYPE_NAMES.get(i));
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // type total
            final JLabel totalLabel = new JLabel(Integer.toString(total));
            totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
            totalLabel.setToolTipText(CardStatistics.TYPE_NAMES.get(i));

            // type percentage
            final int percentage = (int) Math.round(((double) total / statistics.totalCards) * 100);
            final JLabel percentLabel = new JLabel(Integer.toString(percentage) + "%");
            percentLabel.setFont(FontsAndBorders.FONT0);

            final JPanel panel = new JPanel(new MigLayout("flowx, gapy 2, gapx 0, insets 4 4 2 4"));
            panel.setBorder(BorderFactory.createMatteBorder(1, i==0?1:0, 1, 1, Color.GRAY));
            panel.setOpaque(false);
            panel.add(iconLabel, "w 16!, h 16!");
            panel.add(totalLabel, "w 100%, wrap");
            panel.add(percentLabel, "center, spanx 2");

            add(panel, "w 44!");
        }

        revalidate();
    }

}
