package magic.ui.widget.card.filter;

import java.awt.Component;
import java.util.List;
import javax.swing.JScrollPane;
import magic.ui.widget.card.filter.button.FilterPanel;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

/**
 * Presents filter values as a scrollable list of checkboxes.
 */
@SuppressWarnings("serial")
public class ScrollableFilterPane extends JScrollPane
    implements IMultiSelectFilter {

    private final CheckboxValuesPanel filterPanel;

    public ScrollableFilterPane(Object[] values, FilterPanel fbp) {
        this.filterPanel = new CheckboxValuesPanel(values, fbp);
        setViewportView(filterPanel);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBorder(FontsAndBorders.DOWN_BORDER);
        setOpaque(false);
        getViewport().setOpaque(false);
        getVerticalScrollBar().setUnitIncrement(18);
    }

    public void setMigLayout(MigLayout miglayout) {
        filterPanel.setLayout(miglayout);
    }

    @Override
    public boolean hasSelectedItem() {
        return filterPanel.hasSelectedItem();
    }

    @Override
    public List<Integer> getSelectedItemIndexes() {
        return filterPanel.getSelectedItemIndexes();
    }

    @Override
    public int getItemsCount() {
        return filterPanel.getItemsCount();
    }

    @Override
    public boolean isItemSelected(int i) {
        return filterPanel.isItemSelected(i);
    }

    @Override
    public void reset() {
        filterPanel.reset();
    }

    @Override
    public String getItemText(int i) {
        return filterPanel.getItemText(i);
    }
}
