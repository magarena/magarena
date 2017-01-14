package magic.ui.widget.cards.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import magic.model.MagicCardDefinition;

@SuppressWarnings("serial")
class DeckJTable extends JTable {

    private static final Color GRID_COLOR = new Color(194, 197, 203);
    private static final int ROW_HEIGHT = 20; // pixels

    // renderer that centers the contents of a column.
    private static final DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    static { centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); }

    private final DeckTableModel tableModel;
    private final Color defaultForeColor;

    DeckJTable(DeckTableModel aTableModel, Color aColor) {

        super(aTableModel);

        this.tableModel = aTableModel;
        this.defaultForeColor = aColor;

        setDefaultRenderer(Object.class, new HideCellFocusRenderer());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // otherwise horizontal scrollbar won't work
        setRowHeight(ROW_HEIGHT);
        setGridColor(GRID_COLOR);
        setFillsViewportHeight(true);

        final TableColumnModel colModel = getColumnModel();
        setColumnWidths(colModel);

        // center contents of columns.
        getColumn(CardTableColumn.Rating).setCellRenderer(centerRenderer);
        getColumn(CardTableColumn.Power).setCellRenderer(centerRenderer);
        getColumn(CardTableColumn.Toughness).setCellRenderer(centerRenderer);

        // center the column header captions.
        ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // special renderer for mana symbols
        colModel.getColumn(CardTableColumn.Cost.ordinal()).setCellRenderer(new ManaCostTableCellRenderer());


        // listener to sort on column header click
        final JTableHeader header = getTableHeader();
        header.addMouseListener(new ColumnListener());
        header.setReorderingAllowed(true);

    }

    private TableColumn getColumn(CardTableColumn col) {
        return getColumnModel().getColumn(col.ordinal());
    }

    private void setColumnWidths(final TableColumnModel model) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            model.getColumn(i).setMinWidth(CardTableColumn.getMinWidth(i));
            model.getColumn(i).setPreferredWidth(CardTableColumn.getMinWidth(i));
        }
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        final MagicCardDefinition card = tableModel.getCardDef(row);
        final boolean isRowSelected = isRowSelected(row);
        if (isRowSelected) {
            c.setForeground(getSelectionForeground());
        } else {
            c.setForeground(card.isInvalid() ? Color.GRAY : defaultForeColor);
        }
        return c;
    }

    private class HideCellFocusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(noFocusBorder);
            return this;
        }
    }

    private class ColumnListener extends MouseAdapter {
        @Override
        public void mouseClicked(final MouseEvent e) {
            final TableColumnModel colModel = getColumnModel();
            final int columnModelIndex = colModel.getColumnIndexAtX(e.getX());
            final int modelIndex = colModel.getColumn(columnModelIndex).getModelIndex();

            if (modelIndex < 0) {
                return;
            }

            // sort
            tableModel.sort(modelIndex);

            // redraw
            tableChanged(new TableModelEvent(tableModel));
            repaint();
        }
    }
}
