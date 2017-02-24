package magic.ui.deck.games;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import magic.data.DeckType;
import magic.model.MagicDeck;
import magic.ui.MagicSound;
import magic.ui.ScreenController;
import magic.ui.helpers.ColorHelper;
import magic.ui.helpers.MouseHelper;
import magic.ui.screen.stats.TableColumnAdjuster;
import magic.ui.widget.cards.table.CardsTableStyle;
import magic.utility.DeckUtils;

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
    private final DeckCellRenderer deckCellRenderer = new DeckCellRenderer();
    private MouseHoverListener mouseHoverListener;

    DeckGamesJTable(TableModel dm, boolean hasDeckLinks) {
        super(dm);
        setDefaultProperties();
        setDefaultColumnProperties();
        setColumnRenderers();
        tca = new TableColumnAdjuster(this);
        tca.adjustColumns();
        deckCellRenderer.setEnabled(hasDeckLinks);
        if (hasDeckLinks) {
            mouseHoverListener = new MouseHoverListener();
            addMouseMotionListener(mouseHoverListener);
            setOnMouseClickAction();
        }
    }

    private void setOnMouseClickAction() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent ev) {
                doOnMousePressed(ev.getPoint());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                doOnMouseExited();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                doOnMouseEntered();
            }

        });
    }

    private void doOnMousePressed(Point mousePosition) {
        int col = columnAtPoint(mousePosition);
        if (col == 4) {
            final TableModel dm = getModel();
            int row = rowAtPoint(mousePosition);
            DeckInfo deckInfo = (DeckInfo) dm.getValueAt(row, col);
            if (deckInfo.deckType != DeckType.Random) {
                long fileChecksum = DeckUtils.getDeckFileChecksum(deckInfo.deckName, deckInfo.deckType);
                if (fileChecksum == deckInfo.checksum) {
                    MouseHelper.showBusyCursor(DeckGamesJTable.this);
                    MagicDeck deck = DeckUtils.loadDeckFromFile(deckInfo.deckName, deckInfo.deckType);
                    ScreenController.showDeckScreen(deck);
                    mouseHoverListener.clear();
                    MouseHelper.showDefaultCursor();
                }
            } else {
                MagicSound.BEEP.play();
            }
        }
    }

    private void doOnMouseEntered() {
        deckCellRenderer.setEnabled(true);
    }

    private void doOnMouseExited() {
        deckCellRenderer.setEnabled(false);
        mouseHoverListener.clear();
        repaint();
    }

    private class MouseHoverListener extends MouseMotionAdapter {
        private int lastMCol = -1;
        private int lastMRow = -1;
        @Override
        public void mouseMoved(MouseEvent e) {
            JTable aTable = (JTable) e.getSource();
            int mCol = aTable.columnAtPoint(e.getPoint());
            int mRow = aTable.rowAtPoint(e.getPoint());
            if (mCol != lastMCol || mRow != lastMRow) {
                lastMCol = mCol;
                lastMRow = mRow;
                if (mCol == 4) {
                    aTable.repaint();
                    MouseHelper.showHandCursor(aTable);
                } else {
                    aTable.repaint();
                    MouseHelper.showDefaultCursor(aTable);
                }
            }
        }
        private void clear() {
            lastMCol = -1;
            lastMRow = -1;
        }
    }

    private void setColumnRenderers() {
        getColumnModel().getColumn(0).setCellRenderer(new ResultCellRenderer());
        getColumnModel().getColumn(1).setCellRenderer(new GameCellRenderer());
        getColumnModel().getColumn(2).setCellRenderer(new PlayerCellRenderer());
        getColumnModel().getColumn(3).setCellRenderer(new PlayerCellRenderer());
        getColumnModel().getColumn(4).setCellRenderer(deckCellRenderer);
    }

    private void setDefaultColumnProperties() {
        final TableColumnModel cm = getColumnModel();
        // set initial column widths.
        cm.getColumn(0).setMinWidth(30);
        cm.getColumn(1).setMinWidth(130);
        cm.getColumn(2).setMinWidth(170);
        cm.getColumn(3).setMinWidth(170);
        cm.getColumn(4).setMinWidth(180);
        final JTableHeader header = getTableHeader();
        header.setEnabled(false);
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
