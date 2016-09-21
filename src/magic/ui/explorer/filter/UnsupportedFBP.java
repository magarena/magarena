package magic.ui.explorer.filter;

import java.awt.Dimension;
import magic.model.MagicCardDefinition;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class UnsupportedFBP extends FilterButtonPanel {

    private String[] values;

    UnsupportedFBP(IFilterListener aListener) {
        super("Unsupported", aListener);
    }

    @Override
    protected boolean hideSearchOptionsAND() {
        return true;
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasStatus(values[i]);
    }

    @Override
    protected boolean hasActiveFilterValue() {
        return filterDialog.isFiltering();
    }

    @Override
    protected String getFilterTooltip() {
        return getFilterTooltip(values, filterDialog.getSelectedItemIndexes());
    }

    @Override
    protected Dimension getFilterDialogSize() {
        return new Dimension(300, 300);
    }

    @Override
    protected MigLayout getFilterDialogLayout() {
        return new MigLayout("flowy, gap 0, insets 0", "[fill, grow]", "[fill, grow][50!, fill]");
    }

    @Override
    protected String getSearchOperandText() {
        return filterDialog.getSearchOperandText();
    }

    @Override
    protected FilterDialog getFilterDialog() {
        values = MagicCardDefinition.getUnsupportedStatuses();
        return new CheckboxFilterDialog(this, values);
    }
}
