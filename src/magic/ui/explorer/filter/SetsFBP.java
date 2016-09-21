package magic.ui.explorer.filter;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import magic.data.MagicSetDefinitions;
import magic.data.MagicSets;
import magic.model.MagicCardDefinition;
import magic.translate.StringContext;
import magic.translate.UiString;

@SuppressWarnings("serial")
class SetsFBP extends FilterButtonPanel {

    // translatable strings
    @StringContext(eg = "Set filter in Cards Explorer")
    private static final String _S1 = "Set";

    private final ScrollableFilterPane filterPane;
    private final String[] values;

    SetsFBP(IFilterListener aListener) {
        super(UiString.get(_S1));
        this.values = getFilterValues();
        this.filterListener = aListener;
        this.filterPane = new ScrollableFilterPane(values, this);
        setPopupContent();
    }

    private String[] getFilterValues() {
        final List<String> values = new ArrayList<>();
        for (MagicSets magicSet : MagicSets.values()) {
            values.add(magicSet.toString().replace("_", "") + " " + magicSet.getSetName());
        }
        return values.toArray(new String[0]);
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
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return  MagicSetDefinitions.isCardInSet(card, MagicSets.values()[i]);
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
