package magic.ui.deck.games;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import magic.data.DeckType;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class DeckCellRenderer extends DefaultTableCellRenderer {

    private static final int BORDER_WIDTH = 2;
    private static final BasicStroke BORDER_STROKE = new BasicStroke(BORDER_WIDTH);
    private static final Color BORDER_COLOR = MagicStyle.getRolloverColor();
    private static final MigLayout layout = new MigLayout("flowy, gap 2, insets 2 2 2 2", "[grow, fill]");

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        final DeckInfo info = (DeckInfo) value;

        JLabel deckLabel = new JLabel(info.deckName);
        DeckColorLabel colorLabel = new DeckColorLabel(info.deckColor);

        final JPanel panel = new JPanel(layout) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (showCellHighlight(table, row, col, info)) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setPaint(BORDER_COLOR);
                    g2d.setStroke(BORDER_STROKE);
                    g.drawRect(1, 1, getWidth()-3, getHeight()-3);
                }
            }
        };

        panel.add(deckLabel);
        panel.add(colorLabel);
        return panel;
    }

    private boolean showCellHighlight(JTable table, int row, int col, DeckInfo info) {
        return isEnabled()
            && info.deckType != DeckType.Random
            && isMouseOverCell(table, row, col);
    }

    private boolean isMouseOverCell(JTable table, int row, int col) {
        Point mp = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mp, table);
        int mRow = table.rowAtPoint(mp);
        int mCol = table.columnAtPoint(mp);
        return row == mRow && col == mCol;
    }
}
