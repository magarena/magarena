package magic.ui.explorer.filter;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import magic.data.MagicFormat;
import magic.data.MagicPredefinedFormat;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;

@SuppressWarnings("serial")
class FormatFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S7 = "Format";

    private final ScrollableFilterPane filterPane;

    FormatFBP(IFilterListener aListener) {
        super(UiString.get(_S7));
        this.filterListener = aListener;
        this.filterPane = new ScrollableFilterPane(MagicPredefinedFormat.getFilterValues(), this);
        setPopupContent();
    }

    @Override
    protected IFilterListener getSearchOptionsListener() {
        return filterListener;
    }

    @Override
    protected JCheckBox[] getCheckboxes() {
        return filterPane.getCheckboxes();
    }

    @Override
    protected JComponent getFilterValuesComponent() {
        return filterPane;
    }

    @Override
    protected boolean isCardValid(final MagicCardDefinition card, final int i) {
        final MagicFormat fmt = MagicPredefinedFormat.values().get(i);
        return fmt.isCardLegal(card);
    }

    @Override
    protected boolean hasActiveFilterValue() {
        return filterPane.hasSelectedCheckbox();
    }

}
