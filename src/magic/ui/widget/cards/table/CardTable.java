package magic.ui.widget.cards.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.model.MagicRandom;
import magic.ui.widget.CostPanel;
import magic.ui.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CardTable extends TexturedPanel implements ListSelectionListener {

    // renderer that centers the contents of a column.
    static final DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    static { centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); }

    private static final Color GRID_COLOR = new Color(194, 197, 203);
    private static final int ROW_HEIGHT = 20; //pixels

    private final MigLayout migLayout = new MigLayout();
    private final JScrollPane scrollpane = new JScrollPane();
    private final CardTableModel tableModel;
    private final JTable table;
    private final ListSelectionModel selectionModel;

    private TitleBar titleBar;
    private List<MagicCardDefinition> lastSelectedCards;
    private final List<ICardSelectionListener> cardSelectionListeners = new ArrayList<>();
    private final boolean isDeck;

    public CardTable(final List<MagicCardDefinition> defs) {
        this(defs, "", false);
    }

    public CardTable(final List<MagicCardDefinition> defs, final String title, final boolean isDeck) {

        this.isDeck = isDeck;

        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);

        this.tableModel = new CardTableModel(defs);

        this.table = new JTable(tableModel) {
            private final Color defaultForeColor = getForeground();
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                final MagicCardDefinition card = tableModel.getCardDef(row);
                final boolean isRowSelected = table.isRowSelected(row);
                if (isRowSelected) {
                    c.setForeground(card.isInvalid() ? Color.LIGHT_GRAY : table.getSelectionForeground());
                } else {
                    c.setForeground(card.isInvalid() ? Color.GRAY : defaultForeColor);
                }
                return c;
            }
        };
        this.selectionModel = table.getSelectionModel();
        this.lastSelectedCards = new ArrayList<>();

        table.setDefaultRenderer(Object.class, new HideCellFocusRenderer());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // otherwise horizontal scrollbar won't work
        table.setRowHeight(ROW_HEIGHT);
        table.setGridColor(GRID_COLOR);
        if (!GeneralConfig.getInstance().isPreviewCardOnSelect()) {
            table.addMouseMotionListener(new RowMouseOverListener());
        }

        final TableColumnModel model = table.getColumnModel();
        setColumnWidths(model);

        // center contents of columns.
        getColumn(CardTableColumn.Rating).setCellRenderer(centerRenderer);
        getColumn(CardTableColumn.Power).setCellRenderer(centerRenderer);
        getColumn(CardTableColumn.Toughness).setCellRenderer(centerRenderer);

        // center the column header captions.
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // special renderer for mana symbols
        model.getColumn(CardTableColumn.Cost.ordinal()).setCellRenderer(new ManaCostCellRenderer());

        // listener to change card image on selection
        table.getSelectionModel().addListSelectionListener(this);

        // listener to sort on column header click
        final JTableHeader header = table.getTableHeader();
        header.addMouseListener(new ColumnListener());
        header.setReorderingAllowed(true);

        // add table to scroll pane
        scrollpane.setViewportView(table);
        scrollpane.setBorder(FontsAndBorders.NO_BORDER);
        scrollpane.setOpaque(false);
        scrollpane.getViewport().setOpaque(false);

        // add title
        titleBar = null;
        if (title.length() > 0) {
            setTitle(title);
        }

        setLayout(migLayout);
        refreshLayout();

    }

    private TableColumn getColumn(CardTableColumn col) {
        return table.getColumnModel().getColumn(col.ordinal());
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0, gap 0");
        if (titleBar != null && titleBar.isVisible()) {
            add(titleBar, "w 100%, h 26!");
        }
        add(scrollpane, "w 100%, h 100%");
    }

    private void setColumnWidths(final TableColumnModel model) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            model.getColumn(i).setMinWidth(CardTableColumn.getMinWidth(i));
            model.getColumn(i).setPreferredWidth(CardTableColumn.getMinWidth(i));
        }
    }

    public void setDeckEditorSelectionMode() {
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    @Override
    public void addMouseListener(final MouseListener m) {
        table.addMouseListener(m);
    }

    public List<MagicCardDefinition> getSelectedCards() {
        final List<MagicCardDefinition> selectedCards = new ArrayList<>();

        for (final int row : table.getSelectedRows()) {
            final MagicCardDefinition card = tableModel.getCardDef(row);
            if (card != null) {
                selectedCards.add(card);
            }
        }

        return selectedCards;
    }

    private void reselectLastCards() {
        // select previous card if possible
        if (lastSelectedCards.size() > 0) {
            final List<MagicCardDefinition> newSelectedCards = new ArrayList<>();
            for (final MagicCardDefinition card : lastSelectedCards) {
                final int index = tableModel.findCardIndex(card);
                if (index != -1) {
                    // previous card still in list
                    table.getSelectionModel().addSelectionInterval(index,index);
                    newSelectedCards.add(card);
                }
            }
            lastSelectedCards = newSelectedCards;
        } else {
            setSelectedRow();
        }
    }

    private void setSelectedRow() {
        if (table.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }
    }

    public void setCards(final List<MagicCardDefinition> defs) {
        tableModel.setCards(defs);
        table.tableChanged(new TableModelEvent(tableModel));
        table.repaint();
        if (!isDeck) {
            reselectLastCards();
        }
    }

    public void setTitle(final String title) {
        if (titleBar != null) {
            titleBar.setText(title);
        } else {
            titleBar = new TitleBar(title);
        }
    }

    @Override
    public void valueChanged(final ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            // If cell selection is enabled, both row and column change events are fired
            if (e.getSource() == table.getSelectionModel() && table.getRowSelectionAllowed()) {
                final MagicCardDefinition card = tableModel.getCardDef(selectionModel.getLeadSelectionIndex());
                notifyCardSelectionListeners(card);
            }
        }
    }

    public void setHeaderVisible(boolean b) {
        if (titleBar != null) {
            titleBar.setVisible(b);
            refreshLayout();
        }
    }

    public void selectRandomCard() {
        if (tableModel.getRowCount() > 0) {
            int row = MagicRandom.nextRNGInt(tableModel.getRowCount());
            table.setRowSelectionInterval(row, row);
            scrollRowToViewportCenter(row);
        }
    }

    private void scrollRowToViewportCenter(int row) {

        JViewport viewport = (JViewport) table.getParent();
        Rectangle viewRect = viewport.getViewRect();
        Rectangle rect = table.getCellRect(row, 0, true);

        int y = rect.y < viewRect.y || rect.y > (viewRect.y + viewRect.height)
            ? rect.y - (viewRect.height / 2) + rect.height
            : viewRect.y;

        viewport.setViewPosition(new Point(viewRect.x, y));
    }

    private class ColumnListener extends MouseAdapter {
        @Override
        public void mouseClicked(final MouseEvent e) {
            final TableColumnModel colModel = table.getColumnModel();
            final int columnModelIndex = colModel.getColumnIndexAtX(e.getX());
            final int modelIndex = colModel.getColumn(columnModelIndex).getModelIndex();

            if (modelIndex < 0) {
                return;
            }

            // sort
            tableModel.sort(modelIndex);

            // redraw
            table.tableChanged(new TableModelEvent(tableModel));
            table.repaint();

            reselectLastCards();
        }
    }

    private class RowMouseOverListener extends MouseAdapter {
        @Override
        public void mouseMoved(final MouseEvent e) {
            final Point p = e.getPoint();
            final int row = table.rowAtPoint(p);
            final MagicCardDefinition card = tableModel.getCardDef(row);
            notifyCardSelectionListeners(card);
        }
    }

    @SuppressWarnings("serial")
    private static class ManaCostCellRenderer extends DefaultTableCellRenderer {

        private MagicManaCost getManaCost(MagicCardDefinition card, Object value) {
            return card.hasCost() ? (MagicManaCost)value : null;
        }

        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int col) {

            final MagicCardDefinition card = ((CardTableModel)table.getModel()).getCardDef(row);
            final CostPanel myRender = new CostPanel(getManaCost(card, value));

            // match border and background formatting with default
            final JComponent defaultRender = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            myRender.setOpaque(defaultRender.isOpaque());
            myRender.setBorder(defaultRender.getBorder());

            if (isSelected) {
                myRender.setForeground(table.getSelectionForeground());
                myRender.setBackground(table.getSelectionBackground());
            } else {
                myRender.setForeground(getForeground());
                // We have to create a new color object because Nimbus returns
                // a color of type DerivedColor, which behaves strange, not sure
                // why.
                myRender.setBackground(new Color(getBackground().getRed(), getBackground().getGreen(), getBackground().getBlue()));

            }
            myRender.setBorder(noFocusBorder);
            return myRender;
        }
    }

    private class HideCellFocusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(noFocusBorder);
            return this;
        }
    }

    public void addCardSelectionListener(final ICardSelectionListener listener) {
        cardSelectionListeners.add(listener);
    }

    private void notifyCardSelectionListeners(final MagicCardDefinition card) {
        if (card != null) {
            for (final ICardSelectionListener listener : cardSelectionListeners) {
                listener.newCardSelected(card);
            }
        }
    }

    public void showCardCount(final boolean b) {
        tableModel.showCardCount(b);
    }

}
