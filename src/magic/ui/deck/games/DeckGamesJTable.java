package magic.ui.deck.games;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import magic.ui.helpers.ColorHelper;
import magic.ui.screen.stats.TableColumnAdjuster;
import magic.ui.widget.cards.table.CardsTableStyle;

@SuppressWarnings("serial")
class DeckGamesJTable extends JTable {

    private static final int ROW_HEIGHT = 40; // pixels

    /*
    Default cell renderer
    */
    private static final DefaultTableCellRenderer defaultCellRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final JComponent c = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setBorder(noFocusBorder);
            return c;
        }
    };

    private static final Color GRID_COLOR = new Color(194, 197, 203);
    private Color DEFAULT_GRID_COLOR;
    private final TableColumnAdjuster tca;

    DeckGamesJTable(TableModel dm) {
        super(dm);
        setDefaultProperties();
        setDefaultColumnProperties();
        setColumnRenderers();
        tca = new TableColumnAdjuster(this);
        tca.adjustColumns();
    }

    private void setColumnRenderers() {
        getColumnModel().getColumn(0).setCellRenderer(new GameCellRenderer());
        getColumnModel().getColumn(1).setCellRenderer(new PlayerCellRenderer());
        getColumnModel().getColumn(2).setCellRenderer(new PlayerCellRenderer());
        getColumnModel().getColumn(3).setCellRenderer(new DeckCellRenderer());
    }

    private void setDefaultColumnProperties() {
        final TableColumnModel cm = getColumnModel();
        // set initial column widths.
        for (int i = 0; i < cm.getColumnCount(); i++) {
            cm.getColumn(i).setMinWidth(120);
            cm.getColumn(i).setPreferredWidth(120);
        }
        final JTableHeader header = getTableHeader();
        header.setReorderingAllowed(true);
        final DefaultTableCellRenderer renderer =
                (DefaultTableCellRenderer) header.getDefaultRenderer();
        // center the column header captions.
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setDefaultProperties() {
        DEFAULT_GRID_COLOR = getGridColor();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowSelectionAllowed(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // otherwise horizontal scrollbar won't work
        setRowHeight(ROW_HEIGHT);
        setOpaque(false);
        setStyleProperties();
        setDefaultRenderer(Object.class, defaultCellRenderer);
    }

    private void setStyleProperties() {
        setForeground(ColorHelper.getOppositeColor(getBackground()));
        CardsTableStyle style = CardsTableStyle.LIGHT;
        setShowGrid(style != CardsTableStyle.THEME);
        setGridColor(style == CardsTableStyle.LIGHT ? GRID_COLOR : DEFAULT_GRID_COLOR);
    }

    @Override
    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);
         setColumnRenderers();
        if (tca != null) {
            tca.adjustColumns();
        }
    }

}
