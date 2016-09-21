package magic.ui.explorer.filter;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import magic.data.MagicSetDefinitions;
import magic.data.MagicSets;
import magic.model.MagicCardDefinition;
import magic.translate.StringContext;
import magic.translate.UiString;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class SetsFBP extends FilterButtonPanel {

    // translatable strings
    @StringContext(eg = "Set filter in Cards Explorer")
    private static final String _S1 = "Set";

    private String[] values;

    SetsFBP(IFilterListener aListener) {
        super(UiString.get(_S1), aListener);
    }

    private String[] getFilterValues() {
        final List<String> sets = new ArrayList<>();
        for (MagicSets magicSet : MagicSets.values()) {
            sets.add(magicSet.toString().replace("_", "") + " " + magicSet.getSetName());
        }
        return sets.toArray(new String[0]);
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return MagicSetDefinitions.isCardInSet(card, MagicSets.values()[i]);
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
        return new Dimension(260, 300);
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
        this.values = getFilterValues();
        return new CheckboxFilterDialog(this, values);
    }
}
