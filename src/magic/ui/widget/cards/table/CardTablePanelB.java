package magic.ui.widget.cards.table;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicRandom;
import magic.ui.FontsAndBorders;
import magic.ui.widget.M.MScrollPane;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CardTablePanelB extends TexturedPanel
        implements ListSelectionListener {

    private final MigLayout migLayout = new MigLayout();
    private final MScrollPane scrollpane = new MScrollPane();
    private final CardTableModel tableModel;
    private final CardsJTable table;
    private TitleBar titleBar;
    private List<MagicCardDefinition> lastSelectedCards;
    private final List<ICardSelectionListener> cardSelectionListeners = new ArrayList<>();
    private final boolean isDeck;

    public CardTablePanelB(final List<MagicCardDefinition> defs) {
        this(defs, "", false);
    }

    public CardTablePanelB(final List<MagicCardDefinition> defs, final String title, final boolean isDeck) {

        this.isDeck = isDeck;
        this.lastSelectedCards = new ArrayList<>();

        this.tableModel = new CardTableModel(defs);
        this.table = new CardsJTable(tableModel);

        if (!GeneralConfig.getInstance().isPreviewCardOnSelect()) {
            table.addMouseMotionListener(new RowMouseOverListener());
        }

        // listener to change card image on selection
        table.getSelectionModel().addListSelectionListener(this);

        // listener to sort on column header click
        final JTableHeader header = table.getTableHeader();
        header.addMouseListener(new ColumnListener());

        // add table to scroll pane
        scrollpane.setViewportView(table);
        scrollpane.setBorder(FontsAndBorders.NO_BORDER);
        scrollpane.setOpaque(false);

        // add title
        titleBar = null;
        if (title.length() > 0) {
            setTitle(title);
        }

        setLayout(migLayout);
        refreshLayout();
        setEmptyBackgroundColor();
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0, gap 0");
        if (titleBar != null && titleBar.isVisible()) {
            add(titleBar, "w 100%, h 26!");
        }
        add(scrollpane.component(), "w 100%, h 100%");
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
                final MagicCardDefinition card = tableModel.getCardDef(table.getSelectionModel().getLeadSelectionIndex());
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

    private void setEmptyBackgroundColor() {
        setBackground(CardsTableStyle.getStyle().getEmptyBackgroundColor());
    }

    public void doSwitchStyle() {
        table.doSwitchStyle();
        setEmptyBackgroundColor();
    }

    public void setStyle(CardsTableStyle newStyle) {
        table.setStyle(newStyle);
        setEmptyBackgroundColor();
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
