package magic.ui.deck.games;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
class PlayerCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String text = "";
        try {
        PlayerInfo playerInfo = (PlayerInfo) value;
        text = playerInfo.isHuman()
            ? String.format("<html><center>%s</center></html>",
                playerInfo.getHumanPlayerName()
            )
            : String.format(
                "<html><center>%s <small>level:%d +life:%d</small><br><small>%s</small></center></html>",
                playerInfo.getAiType().name(),
                playerInfo.getAiLevel(),
                playerInfo.getAiXtraLife(),
                playerInfo.getAiType().toString()
            );
        } catch (Exception ex) {
            text = "!!!";
        }
        JLabel lbl = new JLabel(text);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setOpaque(true);
        return lbl;
    }
}
