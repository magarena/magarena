package magic.ui.explorer.filter;

import java.awt.Dimension;
import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.translate.UiString;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ManaCostFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S12 = "Cost";

    private static final String[] COST_VALUES = new String[MagicManaCost.MAXIMUM_MANA_COST + 1];
    static {
        for (int i = 0; i <= MagicManaCost.MAXIMUM_MANA_COST; i++) {
            COST_VALUES[i] = Integer.toString(i);
        }
    }

    ManaCostFBP(IFilterListener aListener) {
        super(UiString.get(_S12), aListener);
    }

    @Override
    protected Dimension getFilterDialogSize() {
        return new Dimension(290, 140);
    }

    @Override
    protected boolean hideSearchOptionsAND() {
        return true;
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasConvertedCost(Integer.parseInt(COST_VALUES[i]));
    }

    @Override
    protected boolean hasActiveFilterValue() {
        return filterDialog.isFiltering();
    }

    @Override
    protected String getFilterTooltip() {
        return getFilterTooltip(COST_VALUES, filterDialog.getSelectedItemIndexes());
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
        return new CostFilterDialog(this, COST_VALUES);
    }
}
