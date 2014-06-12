package magic.ui;

import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.ui.viewer.CardViewer;
import magic.ui.widget.CostPanel;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class CardTable extends TexturedPanel implements ListSelectionListener {

    private static final Color GRID_COLOR = new Color(194, 197, 203);
    private static final int ROW_HEIGHT = 20; //pixels

    private final MigLayout migLayout = new MigLayout();
    private final JScrollPane scrollpane = new JScrollPane();
    private final CardViewer cardViewer;
    private final CardTableModel tableModel;
    private final JTable table;
    private final ListSelectionModel selectionModel;

    private  TitleBar titleBar;
    private List<MagicCardDefinition> lastSelectedCards;

    public CardTable(final List<MagicCardDefinition> defs, final CardViewer cardViewer) {
        this(defs, cardViewer, "", false);
    }

    public CardTable(final List<MagicCardDefinition> defs, final CardViewer cardViewer, final String title, final boolean isDeck) {

        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);

        this.tableModel = new CardTableModel(defs, isDeck);

        this.table = new JTable(tableModel) {
            private final Color defaultForeColor = getForeground();
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                final MagicCardDefinition card = tableModel.getCardDef(row);
                final boolean isRowSelected = table.isRowSelected(row);
                if (isRowSelected) {
                    c.setForeground(table.getSelectionForeground());
                } else {
                    c.setForeground(card.isMissing() ? Color.GRAY : defaultForeColor);
                }
                return c;
            }
        };
        this.selectionModel = table.getSelectionModel();
        this.cardViewer = cardViewer;
        this.lastSelectedCards = new ArrayList<MagicCardDefinition>();

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

        // center the contents of the player rating column.
        final DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        // center the column header captions.
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // special renderer for mana symbols
        model.getColumn(CardTableModel.COST_COLUMN_INDEX).setCellRenderer(new ManaCostCellRenderer());

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

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0, gap 0");
        if (titleBar != null) {
            add(titleBar, "w 100%, h 26!");
        }
        add(scrollpane, "w 100%, h 100%");
    }

    private void setColumnWidths(final TableColumnModel model) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            model.getColumn(i).setMinWidth(CardTableModel.COLUMN_MIN_WIDTHS[i]);
            model.getColumn(i).setPreferredWidth(CardTableModel.COLUMN_MIN_WIDTHS[i]);
        }
    }

    public void setDeckEditorSelectionMode() {
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    public void addMouseListener(final MouseListener m) {
        table.addMouseListener(m);
    }

    public List<MagicCardDefinition> getSelectedCards() {
        final List<MagicCardDefinition> selectedCards = new ArrayList<MagicCardDefinition>();

        for (final int row : table.getSelectedRows()) {
            final MagicCardDefinition card = tableModel.getCardDef(row);
            if (card != null) {
                selectedCards.add(card);
            }
        }

        return selectedCards;
    }

    void reselectLastCards() {
        // select previous card if possible
        if (lastSelectedCards.size() > 0) {
            final List<MagicCardDefinition> newSelectedCards = new ArrayList<MagicCardDefinition>();

            for (final MagicCardDefinition card : lastSelectedCards) {
                final int index = tableModel.findCardIndex(card);
                if (index != -1) {
                    // previous card still in list
                    table.getSelectionModel().addSelectionInterval(index,index);
                    newSelectedCards.add(card);
                }
            }

            lastSelectedCards = newSelectedCards;
        }
    }

    public void setCards(final List<MagicCardDefinition> defs) {
        tableModel.setCards(defs);
        table.tableChanged(new TableModelEvent(tableModel));
        table.repaint();
        reselectLastCards();
    }

    public void setTitle(final String title) {
        if (titleBar != null) {
            titleBar.setText(title);
        } else {
            titleBar = new TitleBar(title);
        }
    }

    private void doPreviewCard(final MagicCardDefinition card) {
        if (card != null) {
            cardViewer.setCard(card,0);
        }
    }

    @Override
    public void valueChanged(final ListSelectionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // If cell selection is enabled, both row and column change events are fired
                if (e.getSource() == table.getSelectionModel() && table.getRowSelectionAllowed()) {
                    // Row selection changed
                    final MagicCardDefinition card = tableModel.getCardDef(selectionModel.getLeadSelectionIndex());
                    doPreviewCard(card);
                }
            }
        });
    }

    private class ColumnListener extends MouseAdapter {
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
        public void mouseMoved(final MouseEvent e) {
             final Point p = e.getPoint();
            if (p != null) {
                final int row = table.rowAtPoint(p);
                final MagicCardDefinition card = tableModel.getCardDef(row);
                doPreviewCard(card);
            }
        }
    }

    private static class ManaCostCellRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 113245L;

        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int col) {

            final MagicCardDefinition card = ((CardTableModel)table.getModel()).getCardDef(row);
            final CostPanel myRender = new CostPanel(card.isLand() ? null : (MagicManaCost)value);

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

}
