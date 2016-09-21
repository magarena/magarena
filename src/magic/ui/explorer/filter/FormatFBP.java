package magic.ui.explorer.filter;

import java.awt.Dimension;
import java.util.stream.Collectors;
import magic.data.MagicFormat;
import magic.data.MagicPredefinedFormat;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class FormatFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S7 = "Format";

    private String[] values;

    FormatFBP(IFilterListener aListener) {
        super(UiString.get(_S7), aListener);
    }

    private String[] getFilterValues() {
        return MagicPredefinedFormat.values().stream()
                .map(MagicFormat::getName)
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }

    @Override
    protected boolean isCardValid(final MagicCardDefinition card, final int i) {
        return MagicPredefinedFormat.values().get(i).isCardLegal(card);
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
