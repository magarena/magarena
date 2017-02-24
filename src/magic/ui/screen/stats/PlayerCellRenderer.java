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
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;

@SuppressWarnings("serial")
public class PlayerCellRenderer extends DefaultTableCellRenderer {

    private static final Font withUnderline;
    static {
        Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
        fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        JLabel lbl = new JLabel();
        withUnderline = lbl.getFont().deriveFont(Font.BOLD).deriveFont(fontAttributes);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String profileGUID = (String)value;
        PlayerProfile player = PlayerProfiles.getPlayerProfiles().get(profileGUID);
        boolean isTempPlayer = player == null;
        final JLabel lbl = new JLabel(!isTempPlayer ? player.getPlayerName() : "");
        lbl.setOpaque(true);
        if (isSelected) {
            lbl.setForeground(table.getSelectionForeground());
            lbl.setBackground(table.getSelectionBackground());
        } else {
            lbl.setForeground(table.getForeground());
            lbl.setBackground(table.getBackground());
        }
        if (isTempPlayer) {
            lbl.setForeground(Color.GRAY);
        } else {
            Point mp = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(mp, table);
            int mRow = table.rowAtPoint(mp);
            int mCol = table.columnAtPoint(mp);
            if (row == mRow && column == mCol) {
                lbl.setForeground(Color.blue);
                lbl.setFont(withUnderline);
            }
        }
        return lbl;
    }
}