package magic.ui.explorer.filter;

import java.awt.Dimension;
import magic.data.MagicFormat;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class CubeFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S6 = "Cube";

    CubeFBP(IFilterListener aListener) {
        super(UiString.get(_S6), aListener);
    }

    @Override
    protected FilterDialog getFilterDialog() {
        return new CheckboxFilterDialog(this, MagicFormat.getCubeFilterLabels());
    }

    @Override
    protected boolean isCardValid(final MagicCardDefinition card, final int i) {
        final MagicFormat fmt = MagicFormat.getCubeFilterFormats().get(i);
        return fmt.isCardLegal(card);
    }

    @Override
    protected boolean hasActiveFilterValue() {
        return filterDialog.isFiltering();
    }

    @Override
    protected String getFilterTooltip() {
        return getFilterTooltip(
                MagicFormat.getCubeFilterLabels(),
                filterDialog.getSelectedItemIndexes()
        );
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
}
