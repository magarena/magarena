package magic.ui.widget.card.filter;

import java.util.List;

public interface IMultiSelectFilter {
    boolean hasSelectedItem();
    int getItemsCount();
    boolean isItemSelected(int i);
    List<Integer> getSelectedItemIndexes();
    void reset();
    String getItemText(int i);
}
