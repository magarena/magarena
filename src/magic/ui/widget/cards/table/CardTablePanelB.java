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
import magic.model.MagicCardDefinition;
import magic.model.MagicRandom;
import magic.ui.FontsAndBorders;

@SuppressWarnings("serial")
public class CardTablePanelB extends CardsTablePanel
        implements ListSelectionListener {

    private final List<ICardSelectionListener> cardSelectionListeners = new ArrayList<>();
    private final boolean isDeck;

    public CardTablePanelB(final List<MagicCardDefinition> defs, final String title, final boolean isDeck) {
        super(defs, title);

        this.isDeck = isDeck;

        // listener to change card image on selection
        table.getSelectionModel().addListSelectionListener(this);

        // listener to sort on column header click
        final JTableHeader header = table.getTableHeader();
        header.addMouseListener(new ColumnListener());

        // add table to scroll pane
        scrollpane.setViewportView(table);
        scrollpane.setBorder(FontsAndBorders.NO_BORDER);
        scrollpane.setOpaque(false);

        setEmptyBackgroundColor();
    }

    @Override
    protected MouseAdapter getRowMouseOverListener() {
        return new MouseAdapter() {
            @Override
            public void mouseMoved(final MouseEvent e) {
                final Point p = e.getPoint();
                final int row = table.rowAtPoint(p);
                final MagicCardDefinition card = tableModel.getCardDef(row);
                notifyCardSelectionListeners(card);
            }
        };
    }

    public CardTablePanelB(List<MagicCardDefinition> defs, boolean isDeck) {
        this(defs, "", isDeck);
    }

    public CardTablePanelB(List<MagicCardDefinition> defs) {
        this(defs, false);
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

    public void setCards(final List<MagicCardDefinition> defs) {
        tableModel.setCards(defs);
        table.tableChanged(new TableModelEvent(tableModel));
        table.repaint();
        if (!isDeck) {
            reselectLastCards();
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

    public void setStyle() {
        table.setStyle();
        setEmptyBackgroundColor();
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
