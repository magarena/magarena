package magic.ui.explorer.filter;

import java.util.stream.Collectors;
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
    private final String[] values;

    FormatFBP(IFilterListener aListener) {
        super(UiString.get(_S7));
        this.values = getFilterValues();
        this.filterListener = aListener;
        this.filterPane = new ScrollableFilterPane(values, this);
        setPopupContent();
    }


    private String[] getFilterValues() {
        return MagicPredefinedFormat.values().stream()
                .map(MagicFormat::getName)
                .collect(Collectors.toList())
                .toArray(new String[0]);
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
        return MagicPredefinedFormat.values().get(i).isCardLegal(card);
    }

    @Override
    protected boolean hasActiveFilterValue() {
        return filterPane.hasSelectedCheckbox();
    }

    @Override
    protected String getFilterTooltip() {
        return getFilterTooltip(values, filterPane.getSelected());
    }

}
