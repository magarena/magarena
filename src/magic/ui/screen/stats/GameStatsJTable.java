package magic.ui.screen.stats;

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
import magic.model.player.PlayerProfiles;
import magic.ui.MagicSound;
import magic.ui.ScreenController;
import magic.ui.helpers.ColorHelper;
import magic.ui.helpers.MouseHelper;
import magic.ui.widget.cards.table.CardsTableStyle;
import magic.utility.DeckUtils;

@SuppressWarnings("serial")
public class GameStatsJTable extends JTable {

    private static final int ROW_HEIGHT = 20; // pixels

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

    public GameStatsJTable(TableModel dm) {
        super(dm);
        setDefaultProperties();
        setDefaultColumnProperties();
        setColumnRenderers();
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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
                if (col == 2 || col == 11) { // P1_ID, P2_ID
                    String guid = (String)getModel().getValueAt(row, col);
                    if (PlayerProfiles.getPlayerProfiles().get(guid) != null) {
                        MouseHelper.showBusyCursor(GameStatsJTable.this);
                        ScreenController.showPlayerScreen(guid);
                        MouseHelper.showDefaultCursor();
                    }
                } else if (col == 6 || col == 15) { // P1_DECK, P2_DECK
                    final TableModel dm = getModel();
                    String deckName = (String) dm.getValueAt(row, col);
                    long expectedChecksum = Long.parseLong((String) dm.getValueAt(row, col + 1));
                    DeckType deckType = DeckType.valueOf((String) dm.getValueAt(row, col + 2));
                    if (deckType != DeckType.Random) {
                        long fileChecksum = DeckUtils.getDeckFileChecksum(deckName, deckType);
                        if (fileChecksum == expectedChecksum) {
                            MouseHelper.showBusyCursor(GameStatsJTable.this);
                            ScreenController.showDeckEditor(DeckUtils.loadDeckFromFile(deckName, deckType));
                            MouseHelper.showDefaultCursor();
                        }
                    } else {
                        MagicSound.BEEP.play();
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
                if (mCol == 2 || mCol == 11 || mCol == 6 || mCol == 15) {
                    aTable.repaint();
                    MouseHelper.showHandCursor(aTable);
                } else if (lastCol == 2 || lastCol == 11 || lastCol == 6 || lastCol == 15) {
                    aTable.repaint();
                    MouseHelper.showDefaultCursor(aTable);
                }
                lastCol = mCol;
            }
        });
    }

    private void setColumnRenderers() {
//        setColCentered(CardTableColumn.Rating);
//        setColCentered(CardTableColumn.Power);
//        setColCentered(CardTableColumn.Toughness);

        // TIME_START
        getColumnModel().getColumn(0).setCellRenderer(new StartTimeCellRenderer());
        // P1_ID
        getColumnModel().getColumn(2).setCellRenderer(new PlayerCellRenderer());
        // P2_ID
        getColumnModel().getColumn(11).setCellRenderer(new PlayerCellRenderer());
        // WINNER
        getColumnModel().getColumn(20).setCellRenderer(new PlayerCellRenderer());
        // P1_DECK
        getColumnModel().getColumn(6).setCellRenderer(new DeckCellRenderer());
        getColumnModel().getColumn(15).setCellRenderer(new DeckCellRenderer());
    }

    private void setDefaultColumnProperties() {
        final TableColumnModel cm = getColumnModel();
        // set initial column widths.
        for (int i = 0; i < cm.getColumnCount(); i++) {
            cm.getColumn(i).setMinWidth(50);
            cm.getColumn(i).setPreferredWidth(50);
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
