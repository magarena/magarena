package magic.ui.explorer.filter;

import java.awt.Dimension;
import magic.model.MagicCardDefinition;
import magic.model.MagicRarity;
import magic.translate.UiString;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class RarityFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S14 = "Rarity";

    private String[] values;

    RarityFBP(IFilterListener aListener) {
        super(UiString.get(_S14), aListener);
    }

    @Override
    protected boolean hideSearchOptionsAND() {
        return true;
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return card.isRarity(MagicRarity.values()[i]);
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
        return new Dimension(260, 200);
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
        this.values = MagicRarity.getDisplayNames();
        return new CheckboxFilterDialog(this, values);
    }
}
