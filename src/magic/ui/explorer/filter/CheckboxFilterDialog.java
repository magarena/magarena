package magic.ui.explorer.filter;

import java.util.List;
import javax.swing.JCheckBox;
import magic.model.MagicCardDefinition;

@SuppressWarnings("serial")
class CheckboxFilterDialog extends FilterDialog
    implements IFilterListener {

    private final FilterButtonPanel fbp;
    protected final ScrollableFilterPane filterPane;
    private final FilterOptionsPanel filterOptionsPanel;
    
    public CheckboxFilterDialog(final FilterButtonPanel fbp, Object[] filterValues) {

        this.fbp = fbp;
        this.filterPane = new ScrollableFilterPane(filterValues, this);
        this.filterOptionsPanel = new FilterOptionsPanel(fbp);

        setSize(fbp.getFilterDialogSize());

        setLayout(fbp.getFilterDialogLayout());
        add(filterPane);
        add(filterOptionsPanel);
    }

    @Override
    public void filterChanged() {
        fbp.filterChanged();
    }

    @Override
    protected boolean isFiltering() {
        return filterPane.hasSelectedCheckbox();
    }

    @Override
    boolean filterMatches(MagicCardDefinition aCard) {

        boolean somethingSelected = false;
        boolean resultOR = false;
        boolean resultAND = true;

        for (int i = 0; i < filterPane.getCheckboxes().length; i++) {
            if (filterPane.getCheckboxes()[i].isSelected()) {
                somethingSelected = true;
                if (fbp.isCardValid(aCard, i)) {
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

    @Override
    List<Integer> getSelectedItemIndexes() {
        return filterPane.getSelected();
    }

    @Override
    void reset() {
        for (JCheckBox checkbox : filterPane.getCheckboxes()) {
            checkbox.setSelected(false);
        }
        filterOptionsPanel.reset();
    }

    @Override
    String getSearchOperandText() {
        return filterOptionsPanel.getSearchOperandText();
    }

    @Override
    String getItemText(int i) {
        return filterPane.getCheckboxes()[i].getText();
    }

}
