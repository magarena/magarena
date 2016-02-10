package magic.ui.widget.deck;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.CardStatistics;
import magic.data.MagicIcon;
import magic.translate.UiString;
import magic.ui.MagicImages;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ManaCurvePanel extends JPanel {

    private static final String _S1 = "Mana curve";

    private final ManaCurveUnitPanel[] manaPanels;

    ManaCurvePanel() {
        setOpaque(false);
        manaPanels = new ManaCurveUnitPanel[CardStatistics.MANA_CURVE_SIZE];
        doLayoutManaCurveGrid();
    }

    private void doLayoutManaCurveGrid() {

        setLayout(new MigLayout("insets 0, gap 0"));

        add(DeckStatisticsViewer.getCaptionLabel(UiString.get(_S1)), "w 100%, wrap, span");

        for (int i = 0; i < CardStatistics.MANA_CURVE_SIZE; i++) {

            final MagicIcon manaSymbol = CardStatistics.MANA_CURVE_ICONS.get(i);
            final JLabel iconLabel = new JLabel(MagicImages.getIcon(manaSymbol));
            iconLabel.setHorizontalAlignment(JLabel.CENTER);

            manaPanels[i] = new ManaCurveUnitPanel(CardStatistics.MANA_CURVE_ICONS.get(i));
            manaPanels[i].setBorder(BorderFactory.createMatteBorder(1, i==0?1:0, 1, 1, Color.GRAY));
            add(manaPanels[i], "w 27!");
        }
    }

    void setStats(CardStatistics stats) {
        ManaCurveUnitPanel.MAX_VALUE = stats.getMaxManaCurve();
        for (int i = 0; i < CardStatistics.MANA_CURVE_SIZE; i++) {
            manaPanels[i].setValue(stats.manaCurve[i]);
        }
    }

}
