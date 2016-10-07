package magic.ui.widget.cards.table;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckTablePanel extends TexturedPanel {

    // fired when selection changes.
    public static final String CP_CARD_SELECTED = "21b18a13-afbb-4d6a-9edc-3119653f4560";
    // fired on mouse event.
    public static final String CP_CARD_LCLICKED = "d1b4df60-feb9-4bfe-88e2-49cd823efeb0";
    public static final String CP_CARD_RCLICKED = "0c0fc5c6-3be3-40f4-9b79-ded9e304a96d";

    private final MigLayout migLayout = new MigLayout();
    private final JScrollPane scrollpane = new JScrollPane();
    private final DeckTableModel tableModel;
    private JTable table;

    private final TitleBar titleBar;
    private List<MagicCardDefinition> lastSelectedCards;
    private boolean isAdjusting = false;
    private int lastSelectedRow = -1;
    private final ListSelectionListener listSelListener;

    public DeckTablePanel(final List<MagicCardDefinition> defs, final String title) {

        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);

        this.tableModel = new DeckTableModel(defs);
        this.table = new DeckTable(tableModel, getForeground());

        this.lastSelectedCards = new ArrayList<>();

        // add table to scroll pane
        scrollpane.setViewportView(table);
        scrollpane.setBorder(FontsAndBorders.NO_BORDER);
        scrollpane.setOpaque(false);
        scrollpane.getViewport().setOpaque(false);

        // add title
        titleBar = new TitleBar(title);

        this.listSelListener = getTableListSelectionListener();
        table.getSelectionModel().addListSelectionListener(listSelListener);
        table.addMouseListener(getTableMouseAdapter());
        if (!GeneralConfig.getInstance().isPreviewCardOnSelect()) {
            table.addMouseMotionListener(new RowMouseOverListener());
        }

        setLayout(migLayout);
        refreshLayout();

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

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0, gap 0");
        add(titleBar, "w 100%, h 26!, hidemode 3");
        add(scrollpane, "w 100%, h 100%");
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

    public void setTitle(final String title) {
        titleBar.setText(title);
    }

    public void setHeaderVisible(boolean b) {
        titleBar.setVisible(b);
        refreshLayout();
    }

    public void clearSelection() {
        table.clearSelection();
    }

    public JTable getDeckTable() {
        return table;
    }

    public void setDeckTable(JTable aDeckTable) {
        this.table = aDeckTable;
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // ensures horizontal scrollbar is visible.
        scrollpane.setViewportView(table);
    }

    private class RowMouseOverListener extends MouseAdapter {
        @Override
        public void mouseMoved(final MouseEvent e) {
            final Point p = e.getPoint();
            final int row = table.rowAtPoint(p);
            if (row != lastSelectedRow) {
                lastSelectedRow = row;
            }
        }
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

    public TitleBar getTitleBar() {
        return titleBar;
    }

    public void showCardCount(final boolean b) {
        tableModel.showCardCount(b);
    }

}
