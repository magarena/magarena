package magic.ui.screen.deck.editor.stats;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import magic.data.DeckType;
import magic.ui.ScreenController;
import magic.ui.helpers.ColorHelper;
import magic.ui.helpers.MouseHelper;
import magic.ui.screen.stats.TableColumnAdjuster;
import magic.ui.widget.cards.table.CardsTableStyle;
import magic.utility.DeckUtils;

@SuppressWarnings("serial")
class GameStatsJTable extends JTable {

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

    GameStatsJTable(TableModel dm) {
        super(dm);
        setDefaultProperties();
        setDefaultColumnProperties();
        setColumnRenderers();
        tca = new TableColumnAdjuster(this);
        tca.adjustColumns();
        setMouseListeners();
    }

    private void setMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent ev) {
                int row = rowAtPoint(ev.getPoint());
                int col = columnAtPoint(ev.getPoint());
                if (col == 1 || col == 10) { // P1_ID, P2_ID
                    final TableModel dm = getModel();
                    MouseHelper.showBusyCursor(GameStatsJTable.this);
                    ScreenController.showPlayerScreen((String)dm.getValueAt(row, col));
                    MouseHelper.showDefaultCursor();

                } else if (col == 5 || col == 14) { // P1_DECK, P2_DECK
                    final TableModel dm = getModel();
                    String deckName = (String) dm.getValueAt(row, col);
                    long expectedChecksum = Long.parseLong((String) dm.getValueAt(row, col + 1));
                    DeckType deckType = DeckType.valueOf((String) dm.getValueAt(row, col + 2));
                    long fileChecksum = DeckUtils.getDeckFileChecksum(deckName, deckType);
                    if (fileChecksum == expectedChecksum) {
                        MouseHelper.showBusyCursor(GameStatsJTable.this);
                        ScreenController.showDeckEditor(DeckUtils.loadDeckFromFile(deckName, deckType));
                        MouseHelper.showDefaultCursor();
                    }
                }
            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            private int lastCol = -1;
            @Override
            public void mouseDragged(MouseEvent e) { }
            @Override
            public void mouseMoved(MouseEvent e) {
                JTable aTable = (JTable) e.getSource();
                int mCol = aTable.columnAtPoint(e.getPoint());
                if (mCol == 1 || mCol == 10 || mCol == 5 || mCol == 14) {
                    aTable.repaint();
                    MouseHelper.showHandCursor(aTable);
                } else if (lastCol == 1 || lastCol == 10 || lastCol == 5 || lastCol == 14) {
                    aTable.repaint();
                    MouseHelper.showDefaultCursor(aTable);
                }
                lastCol = mCol;
            }
        });
    }

    private void setColumnRenderers() {
        getColumnModel().getColumn(4).setCellRenderer(new DeckCellRenderer());
//        // P1_ID
//        getColumnModel().getColumn(1).setCellRenderer(new PlayerCellRenderer());
//        // P2_ID
//        getColumnModel().getColumn(10).setCellRenderer(new PlayerCellRenderer());
//        // WINNER
//        getColumnModel().getColumn(19).setCellRenderer(new PlayerCellRenderer());
//        // P1_DECK
//        getColumnModel().getColumn(5).setCellRenderer(new DeckCellRenderer());
//        getColumnModel().getColumn(14).setCellRenderer(new DeckCellRenderer());
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
