package magic.ui.explorer.filter.dialogs;

import java.util.List;
import magic.model.MagicCardDefinition;
import magic.ui.explorer.filter.FilterOptionsPanel;
import magic.ui.explorer.filter.IMultiSelectFilter;
import magic.ui.explorer.filter.SearchOperand;

@SuppressWarnings("serial")
abstract class MultiSelectFilterDialog extends FilterDialog {

    protected FilterOptionsPanel filterOptionsPanel;
    protected IMultiSelectFilter filter;

    protected abstract boolean isCardValid(final MagicCardDefinition card, final int i);

    public String getSearchOperandText() {
        return filterOptionsPanel.getSearchOperandText();
    }

    @Override
    public boolean isFiltering() {
        return filter.hasSelectedItem();
    }

    public List<Integer> getSelectedItemIndexes() {
        return filter.getSelectedItemIndexes();
    }

    @Override
    public void reset() {
        filter.reset();
        filterOptionsPanel.reset();
    }

    public boolean filterMatches(MagicCardDefinition aCard) {

        boolean somethingSelected = false;
        boolean resultOR = false;
        boolean resultAND = true;

        for (int i = 0; i < filter.getItemsCount(); i++) {
            if (filter.isItemSelected(i)) {
                somethingSelected = true;
                if (isCardValid(aCard, i)) {
                    resultOR = true;
                } else {
                    resultAND = false;
                }
            }
        }
        if (filterOptionsPanel.isSelected(SearchOperand.EXCLUDE)) {
            // exclude selected
            return !resultOR;
        }
        // otherwise return OR or AND result
        return !somethingSelected
                || ((filterOptionsPanel.isSelected(SearchOperand.MATCH_ANY) && resultOR)
                || (filterOptionsPanel.isSelected(SearchOperand.MATCH_ALL) && resultAND));
    }
}
