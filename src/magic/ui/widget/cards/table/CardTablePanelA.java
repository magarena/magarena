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
import magic.ui.FontsAndBorders;

@SuppressWarnings("serial")
public class CardTablePanelA extends CardsTablePanel {

    // fired when selection changes.
    public static final String CP_CARD_SELECTED = "7f9bfa20-a363-4ce4-8491-8bfb219a808d";
    // fired on mouse event.
    public static final String CP_CARD_LCLICKED = "fb5f3d15-c764-4436-a790-4aa349c24b73";
    public static final String CP_CARD_RCLICKED = "575ebbc6-c67b-45b5-9f3e-e03ae1d879be";
    public static final String CP_CARD_DCLICKED = "d3a081c1-a66c-402a-814e-819678257d3b";

    private boolean isAdjusting = false;
    private int lastSelectedRow = -1;

    public CardTablePanelA(final List<MagicCardDefinition> defs, final String title) {
        super(defs, title);

        // listener to change card image on selection
        table.getSelectionModel().addListSelectionListener(getTableListSelectionListener());

        // add table to scroll pane
        scrollpane.setViewportView(table);
        scrollpane.setBorder(FontsAndBorders.NO_BORDER);
        scrollpane.setOpaque(false);

        // Raise events on mouse clicks.
        table.addMouseListener(getTableMouseAdapter());

        setEmptyBackgroundColor();
    }

    public CardTablePanelA(final List<MagicCardDefinition> defs) {
        this(defs, "");
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
        return new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                isAdjusting = e.getValueIsAdjusting();
                if (!isAdjusting) {
                    firePropertyChange(CP_CARD_SELECTED, false, true);
                }
            }
        };
    }

    private MouseAdapter getTableMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isAdjusting) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (hasDoubleClickListeners() && e.getClickCount() == 2) {
                            firePropertyChange(CP_CARD_DCLICKED, false, true);
                        } else {
                            firePropertyChange(CP_CARD_LCLICKED, false, true);
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        final Point p = e.getPoint();
                        final int rowNumber = table.rowAtPoint(p);
                        final boolean isRowSelected = table.isRowSelected(rowNumber);
                        if (!isRowSelected) {
                            table.getSelectionModel().setSelectionInterval(rowNumber, rowNumber);
                        } else {
                            firePropertyChange(CP_CARD_RCLICKED, false, true);
                        }
                    }
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

    public void setCards(final List<MagicCardDefinition> defs) {
        final boolean isRowSelected = table.getSelectedRow() != -1;
        tableModel.setCards(defs);
        table.tableChanged(new TableModelEvent(tableModel));
        table.repaint();
        if (isRowSelected) {
            reselectLastCards();
        }
    }

    public void clearSelection() {
        table.clearSelection();
    }

    public JTable getDeckTable() {
        return table;
    }

    public void setDeckTable(CardsJTable aDeckTable) {
        this.table = aDeckTable;
        scrollpane.setViewportView(table);
    }

    public JTable getTable() {
        return table;
    }

    private boolean hasDoubleClickListeners() {
        return getPropertyChangeListeners(CP_CARD_DCLICKED).length > 0;
    }

    public void selectFirstRow() {
        if (table.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
            firePropertyChange(CP_CARD_SELECTED, false, true);
        }
    }

    public void setSelectedCard(MagicCardDefinition aCard) {
        final int index = tableModel.findCardIndex(aCard);
        if (index != -1 && getSelectedCards().contains(aCard) == false) {
            table.getSelectionModel().addSelectionInterval(index, index);
        } else if (tableModel.getRowCount() > 0) {
            table.getSelectionModel().addSelectionInterval(0, 0);
        }
    }

    public void showCardCount(final boolean b) {
        tableModel.showCardCount(b);
    }

}
