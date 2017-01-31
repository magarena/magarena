package magic.ui.screen.deck.editor.stats;

import java.awt.Component;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
class StartTimeCellRenderer extends DefaultTableCellRenderer {

    private LocalDateTime getLocalTimeFromEpoch(Long epochMilli) {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(epochMilli),
            ZoneId.systemDefault()
        );
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int col) {
        final LocalDateTime gameStart = getLocalTimeFromEpoch(Long.parseLong((String) value));
        final JLabel lbl = new JLabel(gameStart.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
        lbl.setOpaque(true);
        if (isSelected) {
            lbl.setForeground(table.getSelectionForeground());
            lbl.setBackground(table.getSelectionBackground());
        } else {
            lbl.setForeground(table.getForeground());
            lbl.setBackground(table.getBackground());
        }
//            lbl.setBorder(noFocusBorder);
        return lbl;
    }

}
