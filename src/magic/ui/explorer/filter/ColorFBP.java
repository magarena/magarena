package magic.ui.explorer.filter;

import java.awt.Dimension;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.translate.UiString;

@SuppressWarnings("serial")
class ColorFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S11 = "Color";

    ColorFBP(IFilterListener aListener) {
        super(UiString.get(_S11), aListener);
    }

    @Override
    protected Dimension getFilterDialogSize() {
        return new Dimension(280, 88);
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasColor(MagicColor.values()[i]);
    }

    @Override
    protected boolean hasActiveFilterValue() {
        return filterDialog.isFiltering();
    }

    @Override
    protected String getFilterTooltip() {
        return getFilterTooltip(
                MagicColor.values(),
                filterDialog.getSelectedItemIndexes()
        );
    }

    @Override
    protected String getSearchOperandText() {
        return filterDialog.getSearchOperandText();
    }

    @Override
    protected FilterDialog getFilterDialog() {
        return new ColorFilterDialog(this, MagicColor.values());
    }

}
