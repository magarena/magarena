package magic.ui.explorer.filter;

import java.awt.Dimension;
import java.util.EnumSet;
import magic.model.MagicCardDefinition;
import magic.model.MagicSubType;
import magic.translate.UiString;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class SubtypeFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S13 = "Subtype";

    private String[] values;

    SubtypeFBP(IFilterListener aListener) {
        super(UiString.get(_S13), aListener);
    }

    private String[] getSubTypesArray() {
        return EnumSet.allOf(MagicSubType.class).stream()
                .map(s -> s.name().replace('_', ' '))
                .toArray(size -> new String[size]);
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasSubType(MagicSubType.values()[i]);
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
        this.values = getSubTypesArray();
        return new CheckboxFilterDialog(this, values);
    }
}
