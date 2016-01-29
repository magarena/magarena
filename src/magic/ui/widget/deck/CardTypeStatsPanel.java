package magic.ui.widget.deck;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.CardStatistics;
import magic.ui.MagicImages;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class CardTypeStatsPanel extends JPanel {

    CardTypeStatsPanel() {
        setLayout(new MigLayout("insets 0, gap 0, center"));
        setOpaque(false);
    }

    void setStats(CardStatistics statistics) {
        
        removeAll();

        add(DeckStatisticsViewer.getCaptionLabel("Card type"), "w 100%, wrap, span");
        
        for (int i = 0; i < CardStatistics.NR_OF_TYPES; i++) {

            final int total = statistics.totalTypes[i];

            // card count
            final JLabel totalLabel = new JLabel(Integer.toString(total));
            totalLabel.setIcon(MagicImages.getIcon(CardStatistics.TYPE_ICONS.get(i)));
            totalLabel.setToolTipText(CardStatistics.TYPE_NAMES.get(i));
            totalLabel.setIconTextGap(4);

            // card percentage
            final int percentage = (int) Math.round(((double) total / statistics.totalCards) * 100);
            final JLabel percentLabel = new JLabel(Integer.toString(percentage) + "%");
            percentLabel.setFont(FontsAndBorders.FONT0);
            
            final JPanel panel = new JPanel(new MigLayout("flowy, gap 0, insets 4"));
            panel.setBorder(BorderFactory.createMatteBorder(1, i==0?1:0, 1, 1, Color.GRAY));
            panel.setOpaque(false);
            panel.add(totalLabel, "w 35!");
            panel.add(percentLabel, "h 12!, center, top");

            add(panel);
        }

        revalidate();
    }

}
