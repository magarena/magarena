package magic.ui.widget.cards.table;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.FontsAndBorders;

@SuppressWarnings("serial")
public class DeckTablePanel extends CardsTablePanel {

    // fired when selection changes.
    public static final String CP_CARD_SELECTED = "21b18a13-afbb-4d6a-9edc-3119653f4560";
    // fired on mouse event.
    public static final String CP_CARD_LCLICKED = "d1b4df60-feb9-4bfe-88e2-49cd823efeb0";
    public static final String CP_CARD_RCLICKED = "0c0fc5c6-3be3-40f4-9b79-ded9e304a96d";

    private boolean isAdjusting = false;
    private int lastSelectedRow = -1;
    private final ListSelectionListener listSelListener;

    public DeckTablePanel(final List<MagicCardDefinition> defs) {
        super(defs);

        // listener to change card image on selection
        this.listSelListener = getTableListSelectionListener();
        table.getSelectionModel().addListSelectionListener(listSelListener);

        // add table to scroll pane
        scrollpane.setViewportView(table);
        scrollpane.setBorder(FontsAndBorders.NO_BORDER);
        scrollpane.setOpaque(false);

        // Raise events on mouse clicks.
        table.addMouseListener(getTableMouseAdapter());

        setEmptyBackgroundColor();
    }

    @Override
    protected MouseAdapter getRowMouseOverListener() {
        return new MouseAdapter() {
            @Override
            public void mouseMoved(final MouseEvent e) {
                final Point p = e.getPoint();
                final int row = table.rowAtPoint(p);
                if (row != lastSelectedRow) {
                    lastSelectedRow = row;
                }
            }
        };
    }

    private void setEmptyBackgroundColor() {
        setBackground(CardsTableStyle.getStyle().getEmptyBackgroundColor());
    }

    private ListSelectionListener getTableListSelectionListener() {
        return (ListSelectionEvent e) -> {
            isAdjusting = e.getValueIsAdjusting();
            if (!isAdjusting) {
                firePropertyChange(CP_CARD_SELECTED, false, true);
            }
        };
    }

    private void doLeftClickAction() {
        table.getSelectionModel().removeListSelectionListener(listSelListener);
        firePropertyChange(CP_CARD_LCLICKED, false, true);

    }

    private void doRightClickAction() {
        firePropertyChange(CP_CARD_RCLICKED, false, true);
    }

    private boolean isMouseRowSelected(MouseEvent e) {
        int rowNumber = table.rowAtPoint(e.getPoint());
        return table.isRowSelected(rowNumber);
    }

    private void doMousePressedAction(MouseEvent e) {
        if (isMouseRowSelected(e)) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                doLeftClickAction();
            } else if (SwingUtilities.isRightMouseButton(e)) {
                doRightClickAction();
            }
        }
    }

    private MouseAdapter getTableMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isAdjusting) {
                    doMousePressedAction(e);
                }
            }
        };
    }

    public void setDeckEditorSelectionMode() {
        //table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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

    public void setDeck(MagicDeck aDeck) {
        tableModel.setCards(aDeck);
        table.tableChanged(new TableModelEvent(tableModel));
        table.repaint();
        reselectLastCards();
        table.getSelectionModel().addListSelectionListener(listSelListener);
    }

    public void setCards(final List<MagicCardDefinition> defs) {
        tableModel.setCards(defs);
        table.repaint();
        table.getSelectionModel().addListSelectionListener(listSelListener);
    }

    public void clearSelection() {
        table.clearSelection();
    }

    public CardsJTable getDeckTable() {
        return table;
    }

    public void setDeckTable(CardsJTable aDeckTable) {
        this.table = aDeckTable;
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // ensures horizontal scrollbar is visible.
        scrollpane.setViewportView(table);
    }

    public void selectFirstRow() {
        if (table.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }
    }

    public void setSelectedCard(MagicCardDefinition card) {
        final int index = tableModel.findCardIndex(card);
        if (index != -1) {
            table.getSelectionModel().addSelectionInterval(index, index);
        } else if (tableModel.getRowCount() > 0) {
            table.getSelectionModel().addSelectionInterval(0, 0);
        }
    }

    public void showCardCount(final boolean b) {
        tableModel.showCardCount(b);
    }

}
