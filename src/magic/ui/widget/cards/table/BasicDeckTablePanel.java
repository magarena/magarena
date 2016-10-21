package magic.ui.widget.cards.table;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import magic.model.MagicCardDefinition;
import magic.ui.FontsAndBorders;
import magic.ui.widget.M.MScrollPane;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class BasicDeckTablePanel extends TexturedPanel {

    // fired when selection changes.
    public static final String CP_CARD_SELECTED = "745278ab-0ea0-42fd-abf1-2cb1ec677909";

    private final MigLayout migLayout = new MigLayout();
    private final MScrollPane scrollpane = new MScrollPane();
    private JTable table;
    private boolean isAdjusting = false;

    public BasicDeckTablePanel() {

        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.DARK_GRAY));
        setFocusable(true);

        scrollpane.setBorder(FontsAndBorders.NO_BORDER);
        scrollpane.setOpaque(false);

        setLayout(migLayout);

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

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0, gap 0");
        add(scrollpane.component(), "w 100%, h 100%");
    }

    public MagicCardDefinition getSelectedCard() {
        return table.getSelectedRow() == -1
                ? null
                : getTableModel().getCardDef(table.getSelectedRow());
    }

    public void clearSelection() {
        table.clearSelection();
    }

    public void setDeckTable(JTable aDeckTable) {

        this.table = aDeckTable;
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getTableHeader().setReorderingAllowed(false);
        table.getSelectionModel().addListSelectionListener(getTableListSelectionListener());
        firePropertyChange(CP_CARD_SELECTED, false, true);

        scrollpane.setViewportView(table);

        refreshLayout();

    }

    public JTable getTable() {
        return table;
    }

    public DeckTableModel getTableModel() {
        return (DeckTableModel) table.getModel();
    }

    public void setSelectedCard(MagicCardDefinition aCard) {
        final int index = getTableModel().findCardIndex(aCard);
        if (index != -1) {
            table.getSelectionModel().setSelectionInterval(index, index);
        }
    }

}
