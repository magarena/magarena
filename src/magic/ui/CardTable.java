package magic.ui;

import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.ui.viewer.CardViewer;
import magic.ui.widget.CostPanel;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TitleBar;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class CardTable extends JPanel implements ListSelectionListener {

    private static final long serialVersionUID = 113243L;

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
        this.tableModel = new CardTableModel(defs, isDeck);
        this.table = new JTable(tableModel);
        this.selectionModel = table.getSelectionModel();
        this.cardViewer = cardViewer;
        this.lastSelectedCards = new ArrayList<MagicCardDefinition>();

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // otherwise horizontal scrollbar won't work
        table.setRowHeight(20);
        table.setGridColor(new Color(194, 197, 203));
        table.addMouseMotionListener(new RowMouseOverListener());

        // set column widths
        final TableColumnModel model = table.getColumnModel();
        for (int i = 0; i < model.getColumnCount(); i++) {
            model.getColumn(i).setMinWidth(CardTableModel.COLUMN_MIN_WIDTHS[i]);
            model.getColumn(i).setPreferredWidth(CardTableModel.COLUMN_MIN_WIDTHS[i]);
        }

        // special renderer for mana symbols
        model.getColumn(CardTableModel.COST_COLUMN_INDEX).setCellRenderer(new ManaCostCellRenderer());

        // listener to change card image on selection
        table.getSelectionModel().addListSelectionListener(this);
        model.getSelectionModel().addListSelectionListener(this);

        // listener to sort on column header click
        final JTableHeader header = table.getTableHeader();
        header.setUpdateTableInRealTime(true);
        header.addMouseListener(new ColumnListener());
        header.setReorderingAllowed(true);

        // add table to scroll pane
        final JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setBorder(FontsAndBorders.NO_BORDER);
        scrollpane.setOpaque(false);
        scrollpane.getViewport().setOpaque(false);

        // add title
        setLayout(new BorderLayout());
        add(scrollpane, BorderLayout.CENTER);
        titleBar = null;
        if (title.length() > 0) {
            setTitle(title);
        }
    }

    public void addMouseListener(final MouseListener m) {
        table.addMouseListener(m);
    }

    public List<MagicCardDefinition> getSelectedCards() {
        final List<MagicCardDefinition> selectedCards = new ArrayList<MagicCardDefinition>();

        for(final int row : table.getSelectedRows()) {
            final MagicCardDefinition card = tableModel.getCardDef(row);
            if(card != null) {
                selectedCards.add(card);
            }
        }

        return selectedCards;
    }

    void reselectLastCards() {
        // select previous card if possible
        if (lastSelectedCards.size() > 0) {
            final List<MagicCardDefinition> newSelectedCards = new ArrayList<MagicCardDefinition>();

            for(final MagicCardDefinition card : lastSelectedCards) {
                final int index = tableModel.findCardIndex(card);
                if(index != -1) {
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
            add(titleBar, BorderLayout.PAGE_START);
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
                    if (card != null) {
                        cardViewer.setCard(card,0);
                    }
                } /* else if (e.getSource() == table.getColumnModel().getSelectionModel() && table.getColumnSelectionAllowed() ){
                    // Column selection changed
                    int first = e.getFirstIndex();
                    int last = e.getLastIndex();
                } */

                /* if (e.getValueIsAdjusting()) {
                    // The mouse button has not yet been released
                } */
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
                if (card != null) {
                    cardViewer.setCard(card,0);
                }
            }
        }
    }

    private static class ManaCostCellRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 113245L;

        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int col) {
            final CostPanel myRender = new CostPanel((MagicManaCost) value);

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
            return myRender;
        }
    }
}
