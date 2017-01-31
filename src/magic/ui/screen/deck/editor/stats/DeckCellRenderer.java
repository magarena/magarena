package magic.ui.screen.deck.editor.stats;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class DeckCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        final DeckInfo info = (DeckInfo) value;

        JLabel lbl = new JLabel(info.deckName);
        DeckColorLabel cp = new DeckColorLabel(info.deckColor);

        final JPanel panel = new JPanel(new MigLayout("flowy, gap 2, insets 2 2 2 2", "[grow, fill]"));
        panel.add(lbl);
        panel.add(cp);
        return panel;
    }
}
