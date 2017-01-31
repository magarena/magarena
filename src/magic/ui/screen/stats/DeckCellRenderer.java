package magic.ui.screen.stats;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
class DeckCellRenderer extends DefaultTableCellRenderer {

    private static final Font withUnderline;
    static {
        Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
        fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        JLabel lbl = new JLabel();
        withUnderline = lbl.getFont().deriveFont(Font.BOLD).deriveFont(fontAttributes);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        String deckName = (String) value;
        JLabel lbl = new JLabel(deckName);
        lbl.setOpaque(true);

        if (isSelected) {
            lbl.setForeground(table.getSelectionForeground());
            lbl.setBackground(table.getSelectionBackground());
        } else {
            lbl.setForeground(table.getForeground());
            lbl.setBackground(table.getBackground());
        }

        Point mp = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mp, table);
        int mRow = table.rowAtPoint(mp);
        int mCol = table.columnAtPoint(mp);
        if (row == mRow && column == mCol) {
            lbl.setForeground(Color.blue);
            lbl.setFont(withUnderline);
        }

        return lbl;
    }
}
