package magic.ui.widget.card.filter;

import java.util.List;

public interface IMultiSelectFilter {

    public boolean hasSelectedItem();

    public int getItemsCount();

    public boolean isItemSelected(int i);

    public List<Integer> getSelectedItemIndexes();

    public void reset();

    public String getItemText(int i);

}
