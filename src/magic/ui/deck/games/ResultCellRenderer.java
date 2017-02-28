package magic.ui.deck.games;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import magic.data.MagicIcon;
import magic.ui.FontsAndBorders;
import magic.ui.MagicImages;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ResultCellRenderer extends DefaultTableCellRenderer {

    private static final ImageIcon WON_ICON = MagicImages.getIcon(MagicIcon.LEGAL);
    private static final ImageIcon LOST_ICON = MagicImages.getIcon(MagicIcon.BANNED);
    private static final MigLayout layout = new MigLayout(
        "flowy, insets 0, gap 0", "[fill, grow, center]", "7[][]"
    );

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        DeckGame info = (DeckGame) value;

        JLabel iconLabel = new JLabel(info.isWinner() ? WON_ICON : LOST_ICON);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel turnLabel = new JLabel(String.valueOf(info.getTurns()));
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        turnLabel.setFont(FontsAndBorders.FONT0);

        JPanel panel = new JPanel(layout);
        panel.add(iconLabel);
        panel.add(turnLabel);

        return panel;
    }
}
