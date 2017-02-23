package magic.ui.deck.games;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
class GameCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        DeckGame info = (DeckGame) value;
        JLabel lbl = new JLabel(String.format(
            "<html><center>%s<br><small>%s</small></center></html>",
            info.getGamePeriod(), info.getConfigInfo())
        );
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setOpaque(true);
        return lbl;
    }
}
