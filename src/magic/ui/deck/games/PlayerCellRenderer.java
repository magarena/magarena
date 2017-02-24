package magic.ui.deck.games;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import magic.translate.MText;

@SuppressWarnings("serial")
class PlayerCellRenderer extends DefaultTableCellRenderer {

    // translatable UI text (prefix with _S).
    private static final String _S1 = "level:%d +life:%d";

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        PlayerInfo playerInfo = (PlayerInfo) value;
        String text = playerInfo.isHuman()
            ? String.format("<html><center>%s</center></html>",
                playerInfo.getHumanPlayerName()
            )
            : String.format(
                "<html><center>%s <small>%s</small><br><small>%s</small></center></html>",
                playerInfo.getAiType().name(),
                MText.get(_S1, playerInfo.getAiLevel(), playerInfo.getAiXtraLife()),
                playerInfo.getAiType().toString()
            );
        JLabel lbl = new JLabel(text);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setOpaque(true);
        return lbl;
    }
}
