package magic.ui.explorer.filter;

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

    SetsFBP(IFilterListener aListener) {
        super(UiString.get(_S1));
        this.filterListener = aListener;
        this.filterPane = new ScrollableFilterPane(MagicSetDefinitions.getFilterValues(), this);
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
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return  MagicSetDefinitions.isCardInSet(card, MagicSets.values()[i]);
    }

    @Override
    protected boolean hasActiveFilterValue() {
        return filterPane.hasSelectedCheckbox();
    }

}
