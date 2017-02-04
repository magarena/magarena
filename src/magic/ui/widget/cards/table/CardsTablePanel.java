package magic.ui.widget.cards.table;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.ui.FontsAndBorders;
import magic.ui.widget.M.MScrollPane;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
abstract class CardsTablePanel extends TexturedPanel {

    protected final MScrollPane scrollpane = new MScrollPane();
    private final TitleBar titleBar;

    protected CardsJTable table;
    protected final MigLayout migLayout = new MigLayout();
    protected final CardTableModel tableModel;
    protected List<MagicCardDefinition> lastSelectedCards = new ArrayList<>();

    protected abstract MouseAdapter getRowMouseOverListener();

    public CardsTablePanel(List<MagicCardDefinition> defs, String title) {

        tableModel = new CardTableModel(defs);
        table = new CardsJTable(tableModel);

        titleBar = new TitleBar(title);
        setTitle(title);

        if (!GeneralConfig.getInstance().isPreviewCardOnSelect()) {
            table.addMouseMotionListener(getRowMouseOverListener());
        }

        // listener to sort on column header click
        final JTableHeader header = table.getTableHeader();
        header.addMouseListener(new ColumnListener());

        // add table to scroll pane
        scrollpane.setViewportView(table);
        scrollpane.setBorder(FontsAndBorders.NO_BORDER);
        scrollpane.setOpaque(false);

        setLayout(migLayout);
        refreshLayout();

        setEmptyBackgroundColor();
    }

    public CardsTablePanel(List<MagicCardDefinition> defs) {
        this(defs, "");
    }

    protected void setEmptyBackgroundColor() {
        setBackground(CardsTableStyle.getStyle().getEmptyBackgroundColor());
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0, gap 0");
        add(titleBar, "w 100%, h 26!, hidemode 3");
        add(scrollpane.component(), "w 100%, h 100%");
        revalidate();
    }

    public void setTitle(final String title) {
        titleBar.setText(title);
        titleBar.setVisible(!title.isEmpty());
        refreshLayout();
    }

    private void setSelectedRow() {
        if (table.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }
    }

    protected void reselectLastCards() {
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
}
